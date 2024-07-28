package com.glassyzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.glassyzone.entity.Category;

@Repository
@EnableJpaRepositories
public interface CategoryDAO extends JpaRepository<Category, Integer> {
}
