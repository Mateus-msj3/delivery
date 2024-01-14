package com.io.github.msj.delivery.service;


import com.io.github.msj.delivery.domain.entities.Customer;
import com.io.github.msj.delivery.domain.entities.OrderEntity;
import com.io.github.msj.delivery.dto.request.OrderRequestDTO;
import com.io.github.msj.delivery.dto.response.OrderResponseDTO;
import com.io.github.msj.delivery.exception.NotFoundException;
import com.io.github.msj.delivery.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private static final String ORDER_NOT_FOUND_MESSAGE = "Não foi encontrado Pedido com ID: ";

    private final ModelMapper modelMapper;

    private final CustomerService customerService;

    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponseDTO create(@Valid OrderRequestDTO orderRequestDTO) {
        Customer customer = modelMapper.map(customerService.findById(orderRequestDTO.getCustomer().getId()), Customer.class);
        OrderEntity order = modelMapper.map(orderRequestDTO, OrderEntity.class);
        order.setCustomer(customer);
        orderRepository.save(order);
        LOGGER.info("Pedido salvo com sucesso: {}", order);
        return buildReturn(order);
    }

    @Transactional
    public OrderResponseDTO edit(Long id, @Valid OrderRequestDTO orderRequestDTO) {
        OrderEntity orderFound = findOrderById(id);
        modelMapper.map(orderRequestDTO, orderFound);
        orderFound.setId(id);
        OrderEntity order = orderRepository.save(orderFound);
        LOGGER.info("Cliente editado com sucesso: {}", order);
        return buildReturn(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> findAll() {
        List<OrderEntity> orderEntities = orderRepository.findAll();
        LOGGER.info("Listando todos os pedidos. Total de pedidos encontrados: {}", orderEntities.size());
        return builReturnList(orderEntities);
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO findById(Long id) {
        OrderEntity order = findOrderById(id);
        LOGGER.info("Pedido encontrado por ID {}: {}", id, order);
        return buildReturn(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> findOrdersByCustomer(Long id) {
        List<OrderEntity> orderEntities = orderRepository.findOrderEntitiesByCustomer_Id(id);
        LOGGER.info("Listando todos os pedidos do cliente. Total de pedidos encontrados: {}", orderEntities.size());
        return builReturnList(orderEntities);
    }

    @Transactional
    public void delete(Long id) {
        OrderEntity order = findOrderById(id);
        orderRepository.delete(order);
        LOGGER.info("Pedido deletado com sucesso. ID: {}", id);
    }

    private OrderEntity findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error(ORDER_NOT_FOUND_MESSAGE + id);
                    return new NotFoundException(ORDER_NOT_FOUND_MESSAGE + id);
                });
    }

    private List<OrderResponseDTO> builReturnList(List<OrderEntity> orderEntities) {
        return orderEntities.stream().map(this::buildReturn).collect(Collectors.toList());
    }
    private OrderResponseDTO buildReturn(OrderEntity order) {
        return modelMapper.map(order, OrderResponseDTO.class);
    }

}

