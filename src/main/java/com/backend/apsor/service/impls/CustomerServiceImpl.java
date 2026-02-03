package com.backend.apsor.service.impls;

import com.backend.apsor.entities.Customer;
import com.backend.apsor.entities.Users;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.CustomerMapper;
import com.backend.apsor.payloads.dtos.CustomerDTO;
import com.backend.apsor.payloads.requests.CustomerReq;
import com.backend.apsor.repositories.CustomerRepo;
import com.backend.apsor.repositories.UserRepo;
import com.backend.apsor.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final UserServiceImpl userServiceImpl;
    private final CustomerMapper customerMapper;
    private final CustomerRepo customerRepo;

    @Override
    public CustomerDTO createNewCustomer(Jwt jwt, CustomerReq req) {

        Users user = userServiceImpl.loadUserByJwt(jwt);

        if (customerRepo.existsByUser_Id(user.getId())) {
            throw ApiException.conflict(ApiErrorCode.CUSTOMER_ALREADY_EXISTS,
                    "Customer profile already exists for this user id: ",
                    user.getId());
        }

        Customer customer = customerMapper.toEntity(req);
        customer.setUser(user);

        return customerMapper.toDto(customerRepo.save(customer));
    }

    @Override
    public List<CustomerDTO> getAllCustomer() {
        return customerMapper.toListDto(customerRepo.findAll());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepo.findById(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Customer with id " + id + " not found."
                ));
    }
    @Override
    public CustomerDTO getCustomerByJwt(Jwt jwt) {
        Users user = userServiceImpl.loadUserByJwt(jwt);

        return customerRepo.findById(user.getId())
                .map(customerMapper::toDto)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Customer with id " + user.getId() + " not found."
                ));
    }


    @Override
    public CustomerDTO updateCustomerById(Long id, CustomerReq req) {
        return customerRepo.findById(id)
                .map(customer -> {
                     customerMapper.updateEntity(req,customer);
                     return customerMapper.toDto(customerRepo.save(customer));
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Customer with id " + id + " not found."
                ));
    }

    @Override
    public String deleteCustomerById(Long id) {
        return customerRepo.findById(id)
                .map(customer -> {
                    customerRepo.delete(customer);
                    return "Customer with id " + id + " has been deleted.";
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Customer with id " + id + " not found."
                ));
    }


}
