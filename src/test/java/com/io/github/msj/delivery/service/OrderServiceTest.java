package com.io.github.msj.delivery.service;

import com.io.github.msj.delivery.domain.entities.Customer;
import com.io.github.msj.delivery.domain.entities.OrderEntity;
import com.io.github.msj.delivery.domain.enums.OrderStatus;
import com.io.github.msj.delivery.dto.CustomerDTO;
import com.io.github.msj.delivery.dto.request.OrderRequestDTO;
import com.io.github.msj.delivery.dto.response.CustomerResponseDTO;
import com.io.github.msj.delivery.dto.response.OrderResponseDTO;
import com.io.github.msj.delivery.exception.NotFoundException;
import com.io.github.msj.delivery.repository.OrderRepository;
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
class OrderServiceTest {

    private final Long ID = 1L;

    private OrderEntity order;

    private Customer customer;

    private OrderRequestDTO orderRequestDTO;

    private OrderResponseDTO orderResponseDTO;

    private CustomerResponseDTO customerResponseDTO;

    @Captor
    private ArgumentCaptor<OrderEntity> orderEntityArgumentCaptor;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(ID)
                .name("New Customer")
                .email("nc@email.com")
                .phone("0011223344")
                .orders(Collections.singletonList(order))
                .build();

        customerResponseDTO = CustomerResponseDTO.builder()
                .id(ID)
                .name("New Customer")
                .email("nc@email.com")
                .phone("(00)1122-3344")
                .createdBy("Anonymous")
                .createdOn(LocalDateTime.now())
                .updatedBy("Anonymous")
                .updatedOn(LocalDateTime.now())
                .build();

        orderRequestDTO = OrderRequestDTO.builder()
                .customer(new CustomerDTO(1L, null, null, null))
                .orderDate(LocalDate.now())
                .status(OrderStatus.WAITING_PAYMENT)
                .totalAmount(BigDecimal.valueOf(100.00))
                .build();

        order = OrderEntity.builder()
                .id(ID)
                .customer(customer)
                .totalAmount(orderRequestDTO.getTotalAmount())
                .orderDate(orderRequestDTO.getOrderDate())
                .status(OrderStatus.WAITING_PAYMENT)
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
    }


    @Nested
    class CreateOrder {

        @Test
        @DisplayName("Deve criar um pedido com sucesso")
        void shouldCreateOrderSuccessfully() {
            when(customerService.findById(ID)).thenReturn(customerResponseDTO);
            when(modelMapper.map(customerResponseDTO, Customer.class)).thenReturn(customer);
            when(modelMapper.map(orderRequestDTO, OrderEntity.class)).thenReturn(order);
            when(orderRepository.save(orderEntityArgumentCaptor.capture())).thenReturn(order);
            when(modelMapper.map(order, OrderResponseDTO.class)).thenReturn(orderResponseDTO);

            OrderResponseDTO response = orderService.create(orderRequestDTO);

            assertNotNull(response);

            OrderEntity orderEntityCaptured = orderEntityArgumentCaptor.getValue();

            assertEquals(orderRequestDTO.getTotalAmount(), orderEntityCaptured.getTotalAmount());
            assertEquals(orderRequestDTO.getOrderDate(), orderEntityCaptured.getOrderDate());
            assertEquals(orderRequestDTO.getStatus(), orderEntityCaptured.getStatus());

            verify(customerService, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(customerResponseDTO, Customer.class);
            verify(modelMapper, times(1)).map(orderRequestDTO, OrderEntity.class);
            verify(orderRepository, times(1)).save(orderEntityArgumentCaptor.getValue());
            verify(modelMapper, times(1)).map(order, OrderResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um erro ao salvar um pedido")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(customerService.findById(ID)).thenReturn(customerResponseDTO);
            when(modelMapper.map(customerResponseDTO, Customer.class)).thenReturn(customer);
            when(modelMapper.map(orderRequestDTO, OrderEntity.class)).thenReturn(order);
            when(orderRepository.save(orderEntityArgumentCaptor.capture())).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> orderService.create(orderRequestDTO));

            verify(customerService, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(customerResponseDTO, Customer.class);
            verify(modelMapper, times(1)).map(orderRequestDTO, OrderEntity.class);
            verify(orderRepository, times(1)).save(orderEntityArgumentCaptor.getValue());
        }

    }

    @Nested
    class EditOrder {

        @Test
        @DisplayName("Deve editar um pedido com sucesso")
        void shouldEditOrderSuccessfully() {
            order.setTotalAmount(BigDecimal.valueOf(200.00));
            when(orderRepository.findById(ID)).thenReturn(Optional.of(order));
            when(customerService.findById(ID)).thenReturn(customerResponseDTO);
            when(modelMapper.map(customerResponseDTO, Customer.class)).thenReturn(customer);
            when(orderRepository.save(orderEntityArgumentCaptor.capture())).thenReturn(order);
            when(modelMapper.map(order, OrderResponseDTO.class)).thenReturn(orderResponseDTO);

            OrderResponseDTO response = orderService.edit(ID, orderRequestDTO);

            assertNotNull(response);
            assertNotNull(response.getCustomer());

            OrderEntity orderEntityCaptured = orderEntityArgumentCaptor.getValue();

            assertEquals(orderRequestDTO.getTotalAmount(), orderEntityCaptured.getTotalAmount());
            assertEquals(orderRequestDTO.getOrderDate(), orderEntityCaptured.getOrderDate());
            assertEquals(orderRequestDTO.getStatus(), orderEntityCaptured.getStatus());

            verify(orderRepository, times(1)).findById(ID);
            verify(customerService, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(customerResponseDTO, Customer.class);
            verify(orderRepository, times(1)).save(orderEntityArgumentCaptor.getValue());
            verify(modelMapper, times(1)).map(order, OrderResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um erro ao editar um cliente")
        void shouldThrowExceptionWhenErrorOccurs() {
            order.setTotalAmount(BigDecimal.valueOf(200.00));
            when(orderRepository.findById(ID)).thenReturn(Optional.of(order));
            when(customerService.findById(ID)).thenReturn(customerResponseDTO);
            when(modelMapper.map(customerResponseDTO, Customer.class)).thenReturn(customer);
            when(orderRepository.save(orderEntityArgumentCaptor.capture())).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> orderService.edit(ID, orderRequestDTO));

            verify(orderRepository, times(1)).findById(ID);
            verify(customerService, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(customerResponseDTO, Customer.class);
            verify(orderRepository, times(1)).save(orderEntityArgumentCaptor.getValue());
        }

    }

    @Nested
    class FindAllOrder {

        @Test
        @DisplayName("Deve listar todos os pedidos com sucesso")
        void shouldListOrdersSuccessfully() {
            List<OrderEntity> orderEntities = Collections.singletonList(order);
            when(orderRepository.findAll()).thenReturn(orderEntities);
            when(modelMapper.map(order, OrderResponseDTO.class)).thenReturn(orderResponseDTO);

            List<OrderResponseDTO> response = orderService.findAll();

            assertNotNull(response);
            assertEquals(orderEntities.size(), response.size());

            verify(orderRepository, times(1)).findAll();
            verify(modelMapper, times(1)).map(order, OrderResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um erro ao listar todos os pedidos")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(orderRepository.findAll()).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> orderService.findAll());

            verify(orderRepository, times(1)).findAll();
        }

    }

    @Nested
    class FindByIdOrder {

        @Test
        @DisplayName("Deve buscar um pedido por id com sucesso")
        void shouldFindByIdClientSuccessfully() {
            when(orderRepository.findById(ID)).thenReturn(Optional.of(order));
            when(modelMapper.map(order, OrderResponseDTO.class)).thenReturn(orderResponseDTO);

            OrderResponseDTO response = orderService.findById(ID);

            assertNotNull(response);
            assertNotNull(response.getId());
            assertNotNull(response.getCustomer());
            assertNotNull(response.getTotalAmount());
            assertNotNull(response.getOrderDate());

            verify(orderRepository, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(order, OrderResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um erro ao listar um pedido por id")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(orderRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> orderService.findById(ID));

            verify(orderRepository, times(1)).findById(ID);
        }

    }

    @Nested
    class FindOrdersByCustomer {

        @Test
        @DisplayName("Deve buscar todos os pedidos de um erro cliente por id com sucesso")
        void shouldFindOrdersByCustomerSuccessfully() {
            List<OrderEntity> orderEntities = Collections.singletonList(order);
            when(orderRepository.findOrderEntitiesByCustomer_Id(ID)).thenReturn(orderEntities);
            when(modelMapper.map(order, OrderResponseDTO.class)).thenReturn(orderResponseDTO);

            List<OrderResponseDTO> response = orderService.findOrdersByCustomer(ID);

            assertNotNull(response);
            assertEquals(orderEntities.size(), response.size());

            verify(orderRepository, times(1)).findOrderEntitiesByCustomer_Id(ID);
            verify(modelMapper, times(1)).map(order, OrderResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um erro ao listar todos os pedidos de um cliente")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(orderRepository.findOrderEntitiesByCustomer_Id(ID)).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> orderService.findOrdersByCustomer(ID));

            verify(orderRepository, times(1)).findOrderEntitiesByCustomer_Id(ID);
        }

    }

    @Nested
    class DeleteOrder {

        @Test
        @DisplayName("Deve deletar um pedido com sucesso")
        void shouldDeleteOrderSuccessfully() {
            when(orderRepository.findById(ID)).thenReturn(Optional.of(order));
            doNothing().when(orderRepository).delete(order);

            orderService.delete(ID);

            verify(orderRepository, times(1)).findById(ID);
            verify(orderRepository, times(1)).delete(order);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um erro ao deletar um pedido")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(orderRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> orderService.findById(ID));

            verify(orderRepository, times(1)).findById(ID);
        }

    }

}