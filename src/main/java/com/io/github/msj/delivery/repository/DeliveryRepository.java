package com.io.github.msj.delivery.repository;

import com.io.github.msj.delivery.domain.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findDeliveryByOrder_Id(Long id);
}