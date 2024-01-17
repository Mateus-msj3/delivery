package com.io.github.msj.delivery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DeliveryRequestDTO {

    private LocalDate deliveryDate;

    @NotEmpty(message = "É necessário informar o endereço.")
    private String address;

    @NotNull(message = "É necessário informar o ID do pedido.")
    private Long orderId;

}
