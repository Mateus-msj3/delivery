package com.io.github.msj.delivery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRequestDTO {

    @NotEmpty(message = "É necessário informar o nome.")
    private String name;

    @NotEmpty(message = "É necessário informar o email.")
    @Email(message = "Informe um email válido.")
    private String email;

    @NotEmpty(message = "É necessário informar a senha.")
    private String password;

}
