package com.io.github.msj.delivery.service;

import com.io.github.msj.delivery.domain.entities.User;
import com.io.github.msj.delivery.dto.request.UserRequestDTO;
import com.io.github.msj.delivery.dto.response.UserResponseDTO;
import com.io.github.msj.delivery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final Long ID = 1L;

    private User user;

    private UserRequestDTO userRequestDTO;

    private UserResponseDTO userResponseDTO;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRequestDTO = UserRequestDTO.builder()
                .email("user@email.com")
                .name("User")
                .password("user123")
                .build();

        user = User.builder()
                .id(ID)
                .name(userRequestDTO.getName())
                .email(userRequestDTO.getEmail())
                .password(userRequestDTO.getPassword())
                .build();

        userResponseDTO = UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }


    @Nested
    class CreateUser {

        @Test
        @DisplayName("Deve criar um usuário com sucesso")
        void shouldCreateUserSuccessfully() {
            when(modelMapper.map(userRequestDTO, User.class)).thenReturn(user);
            when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("*85sh$3sedda");
            when(userRepository.save(userArgumentCaptor.capture())).thenReturn(user);
            when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);

            UserResponseDTO response = userService.create(userRequestDTO);

            assertNotNull(response);

            User userCaptured = userArgumentCaptor.getValue();

            assertEquals(userRequestDTO.getName(), userCaptured.getName());
            assertEquals(userRequestDTO.getEmail(), userCaptured.getEmail());
            assertNotEquals(userRequestDTO.getPassword(), userCaptured.getPassword());

            verify(modelMapper, times(1)).map(userRequestDTO, User.class);
            verify(passwordEncoder, times(1)).encode(userRequestDTO.getPassword());
            verify(userRepository, times(1)).save(userArgumentCaptor.getValue());
            verify(modelMapper, times(1)).map(user, UserResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um ao salvar um usuário")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(modelMapper.map(userRequestDTO, User.class)).thenReturn(user);
            when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("*85sh$3sedda");
            when(userRepository.save(userArgumentCaptor.capture())).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> userService.create(userRequestDTO));

            verify(modelMapper, times(1)).map(userRequestDTO, User.class);
            verify(passwordEncoder, times(1)).encode(userRequestDTO.getPassword());
            verify(userRepository, times(1)).save(userArgumentCaptor.getValue());
        }

    }


}