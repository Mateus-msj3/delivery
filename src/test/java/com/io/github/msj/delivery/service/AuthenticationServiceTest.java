package com.io.github.msj.delivery.service;

import com.io.github.msj.delivery.security.jwt.JwtService;
import com.io.github.msj.delivery.security.user.dto.JWTResponseDTO;
import com.io.github.msj.delivery.security.user.dto.LoginRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Deve autenticar um usuário com sucesso")
    void shouldAuthenticateSuccessfully() {
        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder().email("valid@example.com").password("validPassword").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        Authentication authenticatedUser = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),
                null, AuthorityUtils.createAuthorityList("ROLE_ADMIN"));

        when(authenticationManager.authenticate(authentication)).thenReturn(authenticatedUser);
        when(jwtService.generateToken(loginRequestDTO.getEmail())).thenReturn("generatedToken");

        JWTResponseDTO response = authenticationService.authenticate(loginRequestDTO);

        assertNotNull(response);
        assertEquals("generatedToken", response.getAccessToken());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateToken(loginRequestDTO.getEmail());
    }


    @Test
    @DisplayName("Deve lançar uma execeção ao auntenticar um usuário")
    void shouldThrowUsernameNotFoundExceptionForInvalidLogin() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("invalid@example.com", "invalidPassword");

        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(loginRequestDTO));

        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Deve lançar uma execeção quando não conseguir auntenticar um usuário")
    void shouldThrowUsernameNotFoundExceptionForUserNotAutheticated() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("invalid@example.com", "invalidPassword");
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        when(authenticationManager.authenticate(authentication)).thenReturn(authentication);

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(loginRequestDTO));

        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, never()).generateToken(any());
    }






}