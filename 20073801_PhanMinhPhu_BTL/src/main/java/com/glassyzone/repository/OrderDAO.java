package com.glassyzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.glassyzone.entity.Order;

@Repository
@EnableJpaRepositories
public interface OrderDAO extends JpaRepository<Order, Integer> {
}
