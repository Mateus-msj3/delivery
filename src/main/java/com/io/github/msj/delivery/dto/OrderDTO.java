package com.io.github.msj.delivery.dto;

import com.io.github.msj.delivery.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDTO {

    private Long id;

    private LocalDate orderDate;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private CustomerDTO customer;
}
