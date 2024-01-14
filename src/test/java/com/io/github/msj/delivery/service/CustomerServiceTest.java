package com.io.github.msj.delivery.service;

import com.io.github.msj.delivery.domain.entities.Customer;
import com.io.github.msj.delivery.dto.request.CustomerRequestDTO;
import com.io.github.msj.delivery.dto.response.CustomerResponseDTO;
import com.io.github.msj.delivery.exception.NotFoundException;
import com.io.github.msj.delivery.repository.CustomerRepository;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private final Long ID = 1L;

    private Customer customer;

    private CustomerRequestDTO customerRequestDTO;

    private CustomerResponseDTO customerResponseDTO;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRequestDTO = CustomerRequestDTO.builder()
                .name("New Customer")
                .email("nc@email.com")
                .phone("(00)1122-3344")
                .build();

        customer = Customer.builder()
                .id(ID)
                .name(customerRequestDTO.getName())
                .email(customerRequestDTO.getEmail())
                .phone(customerRequestDTO.getPhone())
                .build();

        customerResponseDTO = CustomerResponseDTO.builder()
                .id(ID)
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .createdBy("Anonymous")
                .createdOn(LocalDateTime.now())
                .updatedBy("Anonymous")
                .updatedOn(LocalDateTime.now())
                .build();
    }


    @Nested
    class CreateCustomer {

        @Test
        @DisplayName("Deve criar um cliente com sucesso")
        void shouldCreateClientSuccessfully() {
            when(modelMapper.map(customerRequestDTO, Customer.class)).thenReturn(customer);
            when(customerRepository.save(customerArgumentCaptor.capture())).thenReturn(customer);
            when(modelMapper.map(customer, CustomerResponseDTO.class)).thenReturn(customerResponseDTO);

            CustomerResponseDTO response = customerService.create(customerRequestDTO);

            assertNotNull(response);

            Customer customerCaptured = customerArgumentCaptor.getValue();

            assertEquals(customerRequestDTO.getName(), customerCaptured.getName());
            assertEquals(customerRequestDTO.getEmail(), customerCaptured.getEmail());
            assertEquals(customerRequestDTO.getPhone(), customerCaptured.getPhone());

            verify(modelMapper, times(1)).map(customerRequestDTO, Customer.class);
            verify(customerRepository, times(1)).save(customerArgumentCaptor.getValue());
            verify(modelMapper, times(1)).map(customer, CustomerResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um ao salvar um cliente")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(modelMapper.map(customerRequestDTO, Customer.class)).thenReturn(customer);
            when(customerRepository.save(customerArgumentCaptor.capture())).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> customerService.create(customerRequestDTO));

            verify(modelMapper, times(1)).map(customerRequestDTO, Customer.class);
            verify(customerRepository, times(1)).save(customerArgumentCaptor.getValue());
        }

    }

    @Nested
    class EditCustomer {

        @Test
        @DisplayName("Deve editar um cliente com sucesso")
        void shouldEditClientSuccessfully() {
            customerRequestDTO.setName("Edit New Customer");
            when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));
            when(customerRepository.save(customerArgumentCaptor.capture())).thenReturn(customer);
            when(modelMapper.map(customer, CustomerResponseDTO.class)).thenReturn(customerResponseDTO);

            CustomerResponseDTO response = customerService.edit(ID, customerRequestDTO);

            assertNotNull(response);

            Customer customerCaptured = customerArgumentCaptor.getValue();

            assertEquals(customerRequestDTO.getName(), customerCaptured.getName());
            assertEquals(customerRequestDTO.getEmail(), customerCaptured.getEmail());
            assertEquals(customerRequestDTO.getPhone(), customerCaptured.getPhone());

            verify(customerRepository, times(1)).findById(ID);
            verify(customerRepository, times(1)).save(customerArgumentCaptor.getValue());
            verify(modelMapper, times(1)).map(customer, CustomerResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um ao editar um cliente")
        void shouldThrowExceptionWhenErrorOccurs() {
            customerRequestDTO.setName("Edit New Customer");
            when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));
            when(customerRepository.save(customerArgumentCaptor.capture())).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> customerService.edit(ID, customerRequestDTO));

            verify(customerRepository, times(1)).findById(ID);
            verify(customerRepository, times(1)).save(customerArgumentCaptor.getValue());
        }

    }

    @Nested
    class FindAllCustomer {

        @Test
        @DisplayName("Deve listar todos os clientes com sucesso")
        void shouldListClientSuccessfully() {
            List<Customer> customers = Collections.singletonList(customer);
            when(customerRepository.findAll()).thenReturn(customers);
            when(modelMapper.map(customer, CustomerResponseDTO.class)).thenReturn(customerResponseDTO);

            List<CustomerResponseDTO> response = customerService.findAll();

            assertNotNull(response);
            assertEquals(customers.size(), response.size());

            verify(customerRepository, times(1)).findAll();
            verify(modelMapper, times(1)).map(customer, CustomerResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um ao listar todos os clientes")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(customerRepository.findAll()).thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> customerService.findAll());

            verify(customerRepository, times(1)).findAll();
        }

    }

    @Nested
    class FindByIdCustomer {

        @Test
        @DisplayName("Deve buscar um cliente por id com sucesso")
        void shouldFindByIdClientSuccessfully() {
            when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));
            when(modelMapper.map(customer, CustomerResponseDTO.class)).thenReturn(customerResponseDTO);

            CustomerResponseDTO response = customerService.findById(ID);

            assertNotNull(response);
            assertNotNull(response.getId());
            assertNotNull(response.getName());
            assertNotNull(response.getEmail());
            assertNotNull(response.getPhone());

            verify(customerRepository, times(1)).findById(ID);
            verify(modelMapper, times(1)).map(customer, CustomerResponseDTO.class);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um ao listar um cliente por id")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(customerRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> customerService.findById(ID));

            verify(customerRepository, times(1)).findById(ID);
        }

    }

    @Nested
    class DeleteCustomer {

        @Test
        @DisplayName("Deve deletar um cliente com sucesso")
        void shouldDeleteClientSuccessfully() {
            when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));
            doNothing().when(customerRepository).delete(customer);

            customerService.delete(ID);

            verify(customerRepository, times(1)).findById(ID);
            verify(customerRepository, times(1)).delete(customer);
        }

        @Test
        @DisplayName("Deve lançar uma execeção quando ocorrer um ao deletar um cliente")
        void shouldThrowExceptionWhenErrorOccurs() {
            when(customerRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> customerService.findById(ID));

            verify(customerRepository, times(1)).findById(ID);
        }

    }

}