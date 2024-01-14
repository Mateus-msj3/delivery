package com.io.github.msj.delivery.controller;

import com.io.github.msj.delivery.dto.request.DeliveryRequestDTO;
import com.io.github.msj.delivery.dto.request.OrderRequestDTO;
import com.io.github.msj.delivery.dto.response.DeliveryResponseDTO;
import com.io.github.msj.delivery.dto.response.OrderResponseDTO;
import com.io.github.msj.delivery.service.DeliveryService;
import com.io.github.msj.delivery.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/")
    public ResponseEntity<DeliveryResponseDTO> create(@Valid @RequestBody DeliveryRequestDTO deliveryRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.create(deliveryRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryResponseDTO> edit(@Valid @RequestBody DeliveryRequestDTO deliveryRequestDTO,
                                                    @PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.edit(id, deliveryRequestDTO));
    }

    @GetMapping("/")
    public ResponseEntity<List<DeliveryResponseDTO>> findAll() {
        return ResponseEntity.ok(deliveryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
