package com.io.github.msj.delivery.security.user.dto;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "O email deve ser informado")
    @Email(message = "Por favor forneca um email válido")
    private String email;

    @NotBlank(message = "A senha deve ser informada")
    private String password;

}
