package com.io.github.msj.delivery.service;

import com.io.github.msj.delivery.domain.entities.Customer;
import com.io.github.msj.delivery.domain.entities.Delivery;
import com.io.github.msj.delivery.domain.entities.OrderEntity;
import com.io.github.msj.delivery.domain.enums.OrderStatus;
import com.io.github.msj.delivery.dto.CustomerDTO;
import com.io.github.msj.delivery.dto.OrderDTO;
import com.io.github.msj.delivery.dto.request.DeliveryRequestDTO;
import com.io.github.msj.delivery.dto.response.DeliveryResponseDTO;
import com.io.github.msj.delivery.dto.response.OrderResponseDTO;
import com.io.github.msj.delivery.exception.NotFoundException;
import com.io.github.msj.delivery.repository.DeliveryRepository;
import org.hibernate.service.spi.ServiceException;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    private final Long ID = 1L;

    private Delivery delivery;

    private OrderEntity order;

    private Customer customer;

    private OrderDTO orderDTO;

    private DeliveryRequestDTO deliveryRequestDTO;

    private DeliveryResponseDTO deliveryResponseDTO;

    private OrderResponseDTO orderResponseDTO;

    @Captor
    private ArgumentCaptor<Delivery> deliveryArgumentCaptor;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private DeliveryService deliveryService;

    @BeforeEach
    void setUp() {

        customer = Customer.builder()
                .id(ID)
                .name("New Customer")
                .email("nc@email.com")
                .phone("0011223344")
                .orders(Collections.singletonList(order))
                .build();

        order = OrderEntity.builder()
                .id(ID)
                .customer(customer)
                .totalAmount(BigDecimal.valueOf(100.00))
                .orderDate(LocalDate.now())
                .status(OrderStatus.WAITING_PAYMENT)
                .build();

        orderDTO = OrderDTO.builder()
                .id(ID)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .build();

        orderResponseDTO = OrderResponseDTO.builder()
                .id(ID)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderDate(LocalDate.now())
                .customer(new CustomerDTO(ID, customer.getName(), customer.getEmail(), customer.getPhone()))
                .createdBy("Anonymous")
                .createdOn(LocalDateTime.now())
                .updatedBy("Anonymous")
                .updatedOn(LocalDateTime.now())
                .build();

        deliveryRequestDTO = DeliveryRequestDTO.builder()
                .orderId(ID)
                .address("Rua A, 123")
                .deliveryDate(LocalDate.now())
                .build();

        delivery = Delivery.builder()
                .id(ID)
                .address(deliveryRequestDTO.getAddress())
                .deliveryDate(deliveryRequestDTO.getDeliveryDate())
                .order(order)
                .build();

        deliveryResponseDTO = DeliveryResponseDTO.builder()
                .id(delivery.getId())
                .address(delivery.getAddress())
                .order(orderDTO)
                .deliveryDate(delivery.getDeliveryDate())
                .createdBy("Anonymous")
                .createdOn(LocalDateTime.now())
                .updatedBy("Anonymous")
                .updatedOn(LocalDateTime.now())
                .build();
    }


    @Nested
    class CreateDelivery {

        @Test
        @DisplayName("Deve criar uma entrega com sucesso")
        void shouldCreateDeliverySuccessfully() {
            when(deliveryRepository.existsDeliveryByOrderId(ID)).thenReturn(false);
            when(orderService.findById(ID)).thenReturn(orderResponseDTO);
            when(modelMapper.map(orderResponseDTO, OrderEntity.class)).thenReturn(order);
            when(modelMapper.map(deliveryRequestDTO, Delivery.class)).thenReturn(delivery);
            when(deliveryRepository.save(deliveryArgumentCaptor.capture())).thenReturn(delivery);
            when(modelMapper.map(delivery, DeliveryResponseDTO.class)).thenReturn(deliveryResponseDTO);

            DeliveryResponseDTO response = deliveryService.create(deliveryRequestDTO);

            assertNotNull(response);

            Delivery deliveryArgumentCaptorValue = deliveryArgumentCaptor.getValue();

            assertEquals(deliveryRequestDTO.getDeliveryDate(), deliveryArgumentCaptorValue.getDeliveryDate());
            assertEquals(deliveryRequestDTO.getAddress(), deliveryArgumentCaptorValue.getAddress());

            verify(deliveryRepository, times(1)).existsDeliveryByOrderId(ID);
            verify(orderService, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(orderResponseDTO, OrderEntity.class);
            verify(modelMapper, times(1)).map(deliveryRequestDTO, Delivery.class);
            verify(deliveryRepository, times(1)).save(deliveryArgumentCaptor.getValue());
            verify(modelMapper, times(1)).map(delivery, DeliveryResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando já existir entrega cadastrada para um pedido")
        void shouldThrowExceptionWhenThereIsRegisteredDeliveryForTheOrder() {
            when(deliveryRepository.existsDeliveryByOrderId(ID)).thenReturn(true);

            assertThrows(ServiceException.class, () -> deliveryService.create(deliveryRequestDTO));

            verify(deliveryRepository, times(1)).existsDeliveryByOrderId(ID);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um ao salvar uma entrega")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(orderService.findById(ID)).thenReturn(orderResponseDTO);
            when(modelMapper.map(orderResponseDTO, OrderEntity.class)).thenReturn(order);
            when(modelMapper.map(deliveryRequestDTO, Delivery.class)).thenReturn(delivery);
            when(deliveryRepository.save(deliveryArgumentCaptor.capture())).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> deliveryService.create(deliveryRequestDTO));

            verify(orderService, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(orderResponseDTO, OrderEntity.class);
            verify(modelMapper, times(1)).map(deliveryRequestDTO, Delivery.class);
            verify(deliveryRepository, times(1)).save(deliveryArgumentCaptor.getValue());
        }

    }

    @Nested
    class EditDelivery {

        @Test
        @DisplayName("Deve editar uma entrega com sucesso")
        void shouldEditDeliverySuccessfully() {
            delivery.setAddress("Rua B, 321");
            when(deliveryRepository.findById(ID)).thenReturn(Optional.of(delivery));
            when(orderService.findById(ID)).thenReturn(orderResponseDTO);
            when(modelMapper.map(orderResponseDTO, OrderEntity.class)).thenReturn(order);
            when(deliveryRepository.save(deliveryArgumentCaptor.capture())).thenReturn(delivery);
            when(modelMapper.map(delivery, DeliveryResponseDTO.class)).thenReturn(deliveryResponseDTO);

            DeliveryResponseDTO response = deliveryService.edit(ID, deliveryRequestDTO);

            assertNotNull(response);
            assertNotNull(response.getOrder());

            Delivery deliveryCaptured = deliveryArgumentCaptor.getValue();

            assertEquals(deliveryRequestDTO.getAddress(), deliveryCaptured.getAddress());
            assertEquals(deliveryRequestDTO.getDeliveryDate(), deliveryCaptured.getDeliveryDate());

            verify(deliveryRepository, times(1)).findById(ID);
            verify(orderService, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(orderResponseDTO, OrderEntity.class);
            verify(deliveryRepository, times(1)).save(deliveryArgumentCaptor.getValue());
            verify(modelMapper, times(1)).map(delivery, DeliveryResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando já existir entrega cadastrada para um pedido")
        void shouldThrowExceptionWhenThereIsRegisteredDeliveryForTheOrder() {
            deliveryRequestDTO.setOrderId(2L);
            when(deliveryRepository.findById(ID)).thenReturn(Optional.of(delivery));
            when(deliveryRepository.existsDeliveryByOrderId(2L)).thenReturn(true);

            assertThrows(ServiceException.class, () -> deliveryService.edit(ID, deliveryRequestDTO));

            verify(deliveryRepository, times(1)).findById(ID);
            verify(deliveryRepository, times(1)).existsDeliveryByOrderId(2L);
        }


        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um erro ao editar uma entrega")
        void shouldThrowExceptionWhenErrorOccurs() {
            delivery.setAddress("Rua B, 321");
            when(deliveryRepository.findById(ID)).thenReturn(Optional.of(delivery));
            when(orderService.findById(ID)).thenReturn(orderResponseDTO);
            when(modelMapper.map(orderResponseDTO, OrderEntity.class)).thenReturn(order);
            when(deliveryRepository.save(deliveryArgumentCaptor.capture())).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> deliveryService.edit(ID, deliveryRequestDTO));

            verify(deliveryRepository, times(1)).findById(ID);
            verify(orderService, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(orderResponseDTO, OrderEntity.class);
            verify(deliveryRepository, times(1)).save(deliveryArgumentCaptor.getValue());
        }

    }

    @Nested
    class FindAllDelivery {

        @Test
        @DisplayName("Deve listar todos as entregas com sucesso")
        void shouldListDeliveriesSuccessfully() {
            List<Delivery> deliveries = Collections.singletonList(delivery);
            when(deliveryRepository.findAll()).thenReturn(deliveries);
            when(modelMapper.map(delivery, DeliveryResponseDTO.class)).thenReturn(deliveryResponseDTO);

            List<DeliveryResponseDTO> response = deliveryService.findAll();

            assertNotNull(response);
            assertEquals(deliveries.size(), response.size());

            verify(deliveryRepository, times(1)).findAll();
            verify(modelMapper, times(1)).map(delivery, DeliveryResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um erro ao listar todas as entrgas")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(deliveryRepository.findAll()).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> deliveryService.findAll());

            verify(deliveryRepository, times(1)).findAll();
        }

    }

    @Nested
    class FindByIdDelivery {

        @Test
        @DisplayName("Deve buscar uma entrega por id com sucesso")
        void shouldFindByIdDeliverySuccessfully() {
            when(deliveryRepository.findById(ID)).thenReturn(Optional.of(delivery));
            when(modelMapper.map(delivery, DeliveryResponseDTO.class)).thenReturn(deliveryResponseDTO);

            DeliveryResponseDTO response = deliveryService.findById(ID);

            assertNotNull(response);
            assertNotNull(response.getId());
            assertNotNull(response.getOrder());
            assertNotNull(response.getAddress());
            assertNotNull(response.getDeliveryDate());

            verify(deliveryRepository, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(delivery, DeliveryResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um erro ao listar uma entrega por id")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(deliveryRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> deliveryService.findById(ID));

            verify(deliveryRepository, times(1)).findById(ID);
        }

    }

    @Nested
    class FindDeliveryByOrder {

        @Test
        @DisplayName("Deve buscar uma entrega por id com sucesso")
        void shouldFindDeliveryByOrderSuccessfully() {
            when(deliveryRepository.findDeliveryByOrder_Id(ID)).thenReturn(Optional.of(delivery));
            when(modelMapper.map(delivery, DeliveryResponseDTO.class)).thenReturn(deliveryResponseDTO);

            DeliveryResponseDTO response = deliveryService.findDeliveryByOrder(ID);

            assertNotNull(response);
            assertNotNull(response.getId());
            assertNotNull(response.getOrder());
            assertNotNull(response.getAddress());
            assertNotNull(response.getDeliveryDate());

            verify(deliveryRepository, times(1)).findDeliveryByOrder_Id(ID);
            verify(modelMapper, times(1)).map(delivery, DeliveryResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um erro ao listar uma entrega de um pedido por id")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(deliveryRepository.findDeliveryByOrder_Id(ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> deliveryService.findDeliveryByOrder(ID));

            verify(deliveryRepository, times(1)).findDeliveryByOrder_Id(ID);
        }

    }

    @Nested
    class DeleteDelivery {

        @Test
        @DisplayName("Deve deletar uma entrega com sucesso")
        void shouldDeleteOrderSuccessfully() {
            delivery.setOrder(null);
            when(deliveryRepository.findById(ID)).thenReturn(Optional.of(delivery));
            doNothing().when(deliveryRepository).delete(delivery);

            deliveryService.delete(ID);

            verify(deliveryRepository, times(1)).findById(ID);
            verify(deliveryRepository, times(1)).delete(delivery);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando tentar deletar uma entrega vinculada a um pedido")
        void shouldThrowExceptionWhenErrorTryingToDeleteDeliveryLinkedToOrder() {
            when(deliveryRepository.findById(ID)).thenReturn(Optional.of(delivery));

            assertThrows(IllegalStateException.class, () -> deliveryService.delete(ID));

            verify(deliveryRepository, times(1)).findById(ID);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um erro ao deletar uma entrega")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(deliveryRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> deliveryService.delete(ID));

            verify(deliveryRepository, times(1)).findById(ID);
        }

    }

}