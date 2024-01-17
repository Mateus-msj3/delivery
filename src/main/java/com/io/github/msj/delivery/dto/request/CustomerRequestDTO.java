package com.io.github.msj.delivery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerRequestDTO {

    @NotEmpty(message = "É necessário informar o nome.")
    private String name;

    @NotEmpty(message = "É necessário informar o email.")
    private String email;

    @NotEmpty(message = "É necessário informar o numero do seu telefone/celular.")
    private String phone;

}
