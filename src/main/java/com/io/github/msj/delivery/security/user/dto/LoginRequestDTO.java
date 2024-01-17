package com.io.github.msj.delivery.security.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginRequestDTO {

    @NotBlank(message = "O email deve ser informado")
    @Email(message = "Por favor forneca um email válido")
    private String email;

    @NotBlank(message = "A senha deve ser informada")
    private String password;

}
