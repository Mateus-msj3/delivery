package com.io.github.msj.delivery.repository;

import com.io.github.msj.delivery.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}