package com.io.github.msj.delivery.service;


import com.io.github.msj.delivery.domain.entities.Customer;
import com.io.github.msj.delivery.dto.request.CustomerRequestDTO;
import com.io.github.msj.delivery.dto.response.CustomerResponseDTO;
import com.io.github.msj.delivery.exception.NotFoundException;
import com.io.github.msj.delivery.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private static final String CUSTOMER_NOT_FOUND_MESSAGE = "Não foi encontrado CLIENTE com ID: ";

    private final ModelMapper modelMapper;

    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerResponseDTO create(@Valid CustomerRequestDTO customerRequestDTO) {
        Customer customer = customerRepository.save(modelMapper.map(customerRequestDTO, Customer.class));
        LOGGER.info("Cliente salvo com sucesso: {}", customer);
        return buildReturn(customer);
    }

    @Transactional
    public CustomerResponseDTO edit(Long id, @Valid CustomerRequestDTO customerRequestDTO) {
        Customer customerFound = findCustomerById(id);
        BeanUtils.copyProperties(customerRequestDTO, customerFound);
        customerFound.setId(id);
        Customer customer = customerRepository.save(customerFound);
        LOGGER.info("Cliente editado com sucesso: {}", customer);
        return buildReturn(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> findAll() {
        List<Customer> customers = customerRepository.findAll();
        LOGGER.info("Listando todos os clientes. Total de clientes encontrados: {}", customers.size());
        return customers.stream().map(this::buildReturn).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerResponseDTO findById(Long id) {
        Customer customerFound = findCustomerById(id);
        LOGGER.info("Cliente encontrado por ID {}: {}", id, customerFound);
        return buildReturn(customerFound);
    }

    @Transactional
    public void delete(Long id) {
        Customer customerFound = findCustomerById(id);
        customerRepository.delete(customerFound);
        LOGGER.info("Cliente deletado com sucesso. ID: {}", id);
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error(CUSTOMER_NOT_FOUND_MESSAGE + id);
                    return new NotFoundException(CUSTOMER_NOT_FOUND_MESSAGE + id);
                });
    }

    private CustomerResponseDTO buildReturn(Customer customer) {
        return modelMapper.map(customer, CustomerResponseDTO.class);
    }

}

