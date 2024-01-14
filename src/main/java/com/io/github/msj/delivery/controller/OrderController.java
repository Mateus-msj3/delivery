package com.io.github.msj.delivery.controller;

import com.io.github.msj.delivery.dto.request.OrderRequestDTO;
import com.io.github.msj.delivery.dto.response.OrderResponseDTO;
import com.io.github.msj.delivery.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/")
    public ResponseEntity<OrderResponseDTO> create(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(orderRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> edit(@Valid @RequestBody OrderRequestDTO orderRequestDTO,
                                                    @PathVariable Long id) {
        return ResponseEntity.ok(orderService.edit(id, orderRequestDTO));
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderResponseDTO>> findAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<OrderResponseDTO>> findOrdersByCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOrdersByCustomer(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
