package com.io.github.msj.delivery.controller;


import com.io.github.msj.delivery.security.user.dto.JWTResponseDTO;
import com.io.github.msj.delivery.security.user.dto.LoginRequestDTO;
import com.io.github.msj.delivery.service.AuthenticationService;
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

    @PostMapping("/login")
    public JWTResponseDTO authenticate(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return authenticationService.authenticate(loginRequestDTO);
    }

}
