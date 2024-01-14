package com.io.github.msj.delivery.dto.request;

import com.io.github.msj.delivery.domain.enums.OrderStatus;
import com.io.github.msj.delivery.dto.CustomerDTO;
import com.io.github.msj.delivery.dto.DeliveryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderRequestDTO {

    private LocalDate orderDate;

    private BigDecimal totalAmount;

    @NotNull(message = "É necessário informar o status.")
    private OrderStatus status;

    @NotNull(message = "É necessário informar o cliente.")
    private CustomerDTO customer;

    private DeliveryDTO delivery;

}
