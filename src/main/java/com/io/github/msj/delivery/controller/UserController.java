package com.io.github.msj.delivery.controller;


import com.io.github.msj.delivery.dto.request.UserRequestDTO;
import com.io.github.msj.delivery.dto.response.UserResponseDTO;
import com.io.github.msj.delivery.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    @PostMapping("/")
    public UserResponseDTO create(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return userService.create(userRequestDTO);
    }

}
