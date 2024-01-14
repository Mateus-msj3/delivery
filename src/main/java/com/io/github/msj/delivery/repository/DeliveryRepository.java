package com.io.github.msj.delivery.repository;

import com.io.github.msj.delivery.domain.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}