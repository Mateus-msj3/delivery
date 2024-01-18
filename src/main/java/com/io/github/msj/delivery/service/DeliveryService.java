package com.io.github.msj.delivery.service;


import com.io.github.msj.delivery.domain.entities.Delivery;
import com.io.github.msj.delivery.domain.entities.OrderEntity;
import com.io.github.msj.delivery.dto.request.DeliveryRequestDTO;
import com.io.github.msj.delivery.dto.response.DeliveryResponseDTO;
import com.io.github.msj.delivery.exception.NotFoundException;
import com.io.github.msj.delivery.repository.DeliveryRepository;
import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class DeliveryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryService.class);
    private static final String DELIVERY_NOT_FOUND_MESSAGE = "Não foi encontrado Entrega com ID: ";

    private final ModelMapper modelMapper;

    private final OrderService orderService;

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public DeliveryResponseDTO create(@Valid DeliveryRequestDTO deliveryRequestDTO) {
        checkExistingDeliveryForOrder(deliveryRequestDTO);
        OrderEntity order = modelMapper.map(orderService.findById(deliveryRequestDTO.getOrderId()), OrderEntity.class);
        Delivery delivery = modelMapper.map(deliveryRequestDTO, Delivery.class);
        delivery.setOrder(order);
        Delivery deliverySaved = deliveryRepository.save(delivery);
        LOGGER.info("Entrega salva com sucesso: {}", delivery);
        return buildReturn(deliverySaved);
    }

    @Transactional
    public DeliveryResponseDTO edit(Long id, @Valid DeliveryRequestDTO deliveryRequestDTO) {
        Delivery deliveryFound = findDeliveryById(id);
        if (!deliveryFound.getOrder().getId().equals(deliveryRequestDTO.getOrderId())) {
            checkExistingDeliveryForOrder(deliveryRequestDTO);
        }
        update(id, deliveryRequestDTO, deliveryFound);
        Delivery delivery = deliveryRepository.save(deliveryFound);
        LOGGER.info("Entrega editada com sucesso: {}", delivery);
        return buildReturn(delivery);
    }

    @Transactional(readOnly = true)
    public List<DeliveryResponseDTO> findAll() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        LOGGER.info("Listando todos as entregas. Total de entregas encontrados: {}", deliveries.size());
        return deliveries.stream().map(this::buildReturn).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeliveryResponseDTO findById(Long id) {
        Delivery delivery = findDeliveryById(id);
        LOGGER.info("Entrega encontrada por ID {}: {}", id, delivery);
        return buildReturn(delivery);
    }

    @Transactional(readOnly = true)
    public DeliveryResponseDTO findDeliveryByOrder(Long id) {
        Delivery delivery = deliveryRepository.findDeliveryByOrder_Id(id)
                .orElseThrow(() -> new NotFoundException(DELIVERY_NOT_FOUND_MESSAGE + id));
        LOGGER.info("Entrega encontrada por ID {}: {}", id, delivery);
        return buildReturn(delivery);
    }

    @Transactional
    public void delete(Long id) {
        Delivery delivery = findDeliveryById(id);
        if (Objects.nonNull(delivery.getOrder())) {
            throw new IllegalStateException("Não é possível excluir uma entrega vinculada a um pedido.");
        }
        deliveryRepository.delete(delivery);
        LOGGER.info("Entrega deletada com sucesso. ID: {}", id);
    }

    private void checkExistingDeliveryForOrder(DeliveryRequestDTO deliveryRequestDTO) {
        if (deliveryRepository.existsDeliveryByOrderId(deliveryRequestDTO.getOrderId())) {
            throw new ServiceException("Já existe entrega cadastrada para este pedido.");
        }
    }

    private void update(Long id, DeliveryRequestDTO deliveryRequestDTO, Delivery delivery) {
        BeanUtils.copyProperties(deliveryRequestDTO, delivery);
        delivery.setId(id);
        delivery.setOrder(modelMapper.map(orderService
                .findById(deliveryRequestDTO.getOrderId()), OrderEntity.class));
    }

    private Delivery findDeliveryById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error(DELIVERY_NOT_FOUND_MESSAGE + id);
                    return new NotFoundException(DELIVERY_NOT_FOUND_MESSAGE + id);
                });
    }

    private DeliveryResponseDTO buildReturn(Delivery delivery) {
        return modelMapper.map(delivery, DeliveryResponseDTO.class);
    }

}

