package com.glassyzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.glassyzone.entity.OrderDetail;

@Repository
@EnableJpaRepositories
public interface OrderDetailDAO extends JpaRepository<OrderDetail, Integer> {

}
