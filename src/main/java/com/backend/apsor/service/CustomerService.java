package com.backend.apsor.service;

import com.backend.apsor.payloads.dtos.CustomerDTO;
import com.backend.apsor.payloads.requests.CustomerReq;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface CustomerService {
    CustomerDTO createNewCustomer(Jwt jwt, @Valid CustomerReq req);

    List<CustomerDTO> getAllCustomer();

    CustomerDTO getCustomerById(Long id);

    CustomerDTO updateCustomerById(Long id, @Valid CustomerReq req);

    String deleteCustomerById(Long id);

    CustomerDTO getCustomerByJwt(Jwt jwt);

    CustomerDTO updateCustomerByJwt(Jwt jwt, @Valid CustomerReq req);
}
