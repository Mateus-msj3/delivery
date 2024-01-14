package com.io.github.msj.delivery.dto.response;

import com.io.github.msj.delivery.domain.enums.OrderStatus;
import com.io.github.msj.delivery.dto.CustomerDTO;
import com.io.github.msj.delivery.dto.DeliveryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponseDTO {

    private Long id;

    private LocalDate orderDate;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private String createdBy;

    private LocalDateTime createdOn;

    private String updatedBy;

    private LocalDateTime updatedOn;

    private CustomerDTO customer;

    private DeliveryDTO delivery;

}
