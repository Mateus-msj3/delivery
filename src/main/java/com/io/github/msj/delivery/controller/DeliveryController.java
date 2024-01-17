package com.io.github.msj.delivery.controller;

import com.io.github.msj.delivery.dto.request.DeliveryRequestDTO;
import com.io.github.msj.delivery.dto.response.DeliveryResponseDTO;
import com.io.github.msj.delivery.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@AllArgsConstructor
@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "Criar uma nova entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entrega criada com sucesso",
                    content = @Content(schema = @Schema(implementation = DeliveryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida - Dados de entrega ausentes ou inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/")
    public ResponseEntity<DeliveryResponseDTO> create(@Valid @RequestBody DeliveryRequestDTO deliveryRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.create(deliveryRequestDTO));
    }

    @Operation(summary = "Editar uma entrega existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrega editada com sucesso",
                    content = @Content(schema = @Schema(implementation = DeliveryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida - Dados de entrega ausentes ou inválidos"),
            @ApiResponse(responseCode = "404", description = "Entrega não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DeliveryResponseDTO> edit(@Valid @RequestBody DeliveryRequestDTO deliveryRequestDTO,
                                                    @PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.edit(id, deliveryRequestDTO));
    }

    @Operation(summary = "Buscar todas as entregas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de entregas recuperada com sucesso",
                    content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/")
    public ResponseEntity<List<DeliveryResponseDTO>> findAll() {
        return ResponseEntity.ok(deliveryService.findAll());
    }

    @Operation(summary = "Buscar uma entrega pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrega recuperada com sucesso",
                    content = @Content(schema = @Schema(implementation = DeliveryResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Entrega não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.findById(id));
    }

    @Operation(summary = "Buscar uma entrega pelo ID do pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrega recuperada com sucesso",
                    content = @Content(schema = @Schema(implementation = DeliveryResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Entrega não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/order/{id}")
    public ResponseEntity<DeliveryResponseDTO> findDeliveryByOrder(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.findDeliveryByOrder(id));
    }

    @Operation(summary = "Excluir uma entrega pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entrega excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Entrega não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
