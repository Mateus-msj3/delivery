package com.io.github.msj.delivery.controller;

import com.io.github.msj.delivery.dto.request.CustomerRequestDTO;
import com.io.github.msj.delivery.dto.response.CustomerResponseDTO;
import com.io.github.msj.delivery.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/")
    public ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(customerRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> edit(@Valid @RequestBody CustomerRequestDTO customerRequestDTO,
                                                    @PathVariable Long id) {
        return ResponseEntity.ok(customerService.edit(id, customerRequestDTO));
    }

    @GetMapping("/")
    public ResponseEntity<List<CustomerResponseDTO>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
