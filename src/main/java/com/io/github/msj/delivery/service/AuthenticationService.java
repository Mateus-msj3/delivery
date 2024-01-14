package com.io.github.msj.delivery.service;


import com.io.github.msj.delivery.security.jwt.JwtService;
import com.io.github.msj.delivery.security.user.dto.JWTResponseDTO;
import com.io.github.msj.delivery.security.user.dto.LoginRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public JWTResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );
        if(authentication.isAuthenticated()){
            return JWTResponseDTO.builder()
                    .accessToken(jwtService.generateToken(loginRequestDTO.getEmail()))
                    .build();

        } else {
            throw new UsernameNotFoundException("Solicitação de login inválida..!!");
        }


    }

}
