package com.glassyzone;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.glassyzone.repository.UserDAO;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDAO userDAO;
	@Autowired
	BCryptPasswordEncoder pe;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(username -> {
			try {
				com.glassyzone.entity.User user = userDAO.findByUsername(username);
				System.out.println(user);
				String password = pe.encode(user.getPassword());
				String role = user.getRole();
				System.out.println("Found user: " + username + ", role: " + role);
				return User.withUsername(username).password(password).roles(role).build();
			} catch (NoSuchElementException e) {
				throw new UsernameNotFoundException(username + " not found");
			}
		});
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().disable().csrf().disable();

		http.authorizeRequests().antMatchers("/admin/**").hasAnyRole("admin")
		.anyRequest().permitAll();

		 http.formLogin()
         .loginPage("/login")
         .loginProcessingUrl("/login")
         .successHandler(savedRequestAwareAuthenticationSuccessHandler())
         .usernameParameter("username").passwordParameter("password");

		http.rememberMe().tokenValiditySeconds(86400);

		http.exceptionHandling().accessDeniedPage("/unauthorized");

		http.logout().logoutSuccessUrl("/login");
	}

	
	@Bean
    public SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler successHandler =
            new SavedRequestAwareAuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(
                    HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws ServletException, IOException {
                    boolean isAdmin = authentication.getAuthorities()
                        .stream()
                        .anyMatch(auth -> auth.getAuthority().equals("admin"));

                    if (isAdmin) {
                        setDefaultTargetUrl("/admin/product");
                    } else {
                        setDefaultTargetUrl("/home");
                    }
                    super.onAuthenticationSuccess(request, response, authentication);
                }
            };
        return successHandler;
    }
}
