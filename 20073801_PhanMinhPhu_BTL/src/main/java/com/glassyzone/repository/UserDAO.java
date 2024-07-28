package com.glassyzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.glassyzone.entity.User;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
	@Query("SELECT u FROM User u WHERE u.username = :username")
	User findByUsername(String username);
}
