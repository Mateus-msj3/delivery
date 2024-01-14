package com.io.github.msj.delivery.dto.response;

import com.io.github.msj.delivery.dto.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DeliveryResponseDTO {

    private Long id;

    private LocalDate deliveryDate;

    private String address;

    private String createdBy;

    private LocalDateTime createdOn;

    private String updatedBy;

    private LocalDateTime updatedOn;

    private OrderDTO order;

}
