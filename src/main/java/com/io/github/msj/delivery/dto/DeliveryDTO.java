package com.io.github.msj.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DeliveryDTO {

    private Long id;

    private LocalDate deliveryDate;

    private String address;

}
