package com.io.github.msj.delivery.service;


import com.io.github.msj.delivery.domain.entities.User;
import com.io.github.msj.delivery.dto.request.UserRequestDTO;
import com.io.github.msj.delivery.dto.response.UserResponseDTO;
import com.io.github.msj.delivery.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO create(@Valid UserRequestDTO userRequestDTO) {
        User user = modelMapper.map(userRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        User userSaved = userRepository.save(user);
        return modelMapper.map(userSaved, UserResponseDTO.class);
    }

}
