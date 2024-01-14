package com.io.github.msj.delivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerResponseDTO {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String createdBy;

    private LocalDateTime createdOn;

    private String updatedBy;

    private LocalDateTime updatedOn;

}
