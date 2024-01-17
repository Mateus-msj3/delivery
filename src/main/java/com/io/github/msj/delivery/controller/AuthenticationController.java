package com.io.github.msj.delivery.controller;


import com.io.github.msj.delivery.security.user.dto.JWTResponseDTO;
import com.io.github.msj.delivery.security.user.dto.LoginRequestDTO;
import com.io.github.msj.delivery.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @Operation(summary = "Autenticar um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida",
                    content = @Content(schema = @Schema(implementation = JWTResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida - Dados de login ausentes ou inválidos"),
            @ApiResponse(responseCode = "401", description = "Falha na autenticação - Credenciais inválidas"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/login")
    public JWTResponseDTO authenticate(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return authenticationService.authenticate(loginRequestDTO);
    }

}
