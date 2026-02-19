package com.backend.apsor.service.impls;

import com.backend.apsor.entities.*;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.OrderStatus;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.OrderMapper;
import com.backend.apsor.payloads.dtos.OrderDTO;
import com.backend.apsor.payloads.requests.OrderCreateReq;
import com.backend.apsor.payloads.requests.OrderStatusReq;
import com.backend.apsor.repositories.*;
import com.backend.apsor.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final UserServiceImpl userServiceImpl;
    private final CustomerRepo customerRepo;
    private final ServiceRepo serviceRepo;
    private final ServicePriceRepo servicePriceRepo;
    private final OrderMapper orderMapper;
    private final ProviderRepo providerRepo;

    @Override
    public OrderDTO placeNewOrder(Jwt jwt, OrderCreateReq req) {

        Users user = userServiceImpl.loadUserByJwt(jwt);

        customerRepo.findByUser(user)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CUSTOMER_NOT_FOUND,
                        "Customer not found with id: " + user.getId()
                ));

        Services services = serviceRepo.findById(req.getServiceId())
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_NOT_FOUND,
                        "Service or prices not found with id: " + req.getServiceId()
                ));

        ServicePrice servicePrice = servicePriceRepo.findById(req.getServicePriceId())
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SERVICE_PRICE_NOT_FOUND,
                        "Service price not found with id: " + req.getServicePriceId()
                ));

        if (!servicePrice.getService().getId().equals(services.getId())) {
            throw ApiException.badRequest(ApiErrorCode.INVALID_SERVICE_PRICE,
                    "ServicePrice " + servicePrice.getId() + " does not belong to service " + services.getId());
        }

        Integer units = req.getUnits();
        Double discount = req.getDiscount();
        Double subTotal = req.getSubtotal();

        Double total = (subTotal * units) - discount;

        Order order = Order.builder()
                .orderNo(generateOrderNumber())
                .currency(servicePrice.getCurrency())
                .subtotal(subTotal)
                .discount(discount)
                .units(req.getUnits())
                .total(total)
                .note(req.getNote())
                .status(OrderStatus.IN_PROGRESS)
                .service(services)
                .servicePrice(servicePrice)
                .build();

        orderRepo.save(order);
        
        return orderMapper.toDTO(order);
    }

    @Override
    public List<OrderDTO> getAllOrders() {

        return orderMapper.toDTOs(orderRepo.findAll()) ;

    }

    @Override
    public OrderDTO updateOrderStatus(Jwt jwt, Long orderId, OrderStatusReq req) {

        Users user = userServiceImpl.loadUserByJwt(jwt);

        providerRepo.findByUser(user)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CUSTOMER_NOT_FOUND,
                        "Customer not found with id: " + user.getId()
                ));

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.ORDER_NOT_FOUND,
                        "Order not found with id: " + orderId
                ));
        order.setStatus(req.getStatus());

        return orderMapper.toDTO(orderRepo.save(order));
    }

    @Override
    public String cancelOrder(Jwt jwt, Long orderId) {
        Users user = userServiceImpl.loadUserByJwt(jwt);

        customerRepo.findByUser(user)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CUSTOMER_NOT_FOUND,
                        "Customer not found with id: " + user.getId()
                ));

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.ORDER_NOT_FOUND,
                        "Order not found with id: " + orderId
                ));

        order.setStatus(OrderStatus.CANCELED);
        orderRepo.save(order);
        return "Successfully cancelled order";
    }

    private String generateOrderNumber() {
        String date = LocalDate.now().toString().replace("-", "");
        String rand = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "ORD-" + date + "-" + rand;
    }
}
