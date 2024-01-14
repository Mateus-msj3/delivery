package com.io.github.msj.delivery.repository;

import com.io.github.msj.delivery.domain.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findOrderEntitiesByCustomer_Id(Long id);

}