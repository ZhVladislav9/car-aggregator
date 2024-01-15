package com.paymentservice.paymentservice.controller;


import com.paymentservice.paymentservice.dto.request.CardRequest;
import com.paymentservice.paymentservice.dto.request.ChargeRequest;
import com.paymentservice.paymentservice.dto.request.CustomerChargeRequest;
import com.paymentservice.paymentservice.dto.request.CustomerRequest;
import com.paymentservice.paymentservice.dto.response.*;
import com.paymentservice.paymentservice.service.PaymentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentServiceImpl;

    @PostMapping("/charge")
    public StringResponse chargeCard(@RequestBody @Valid ChargeRequest chargeRequest) {
        return paymentServiceImpl.charge(chargeRequest);
    }

    @PostMapping("/token")
    public TokenResponse createToken(@RequestBody @Valid CardRequest request) {
        return paymentServiceImpl.createToken(request);
    }

    @PostMapping("/customers")
    public CustomerResponse createCustomer(@RequestBody @Valid CustomerRequest request) {
        return paymentServiceImpl.createCustomer(request);
    }
    @GetMapping("/customers/{id}")
    public CustomerResponse findCustomer(@PathVariable long id) {
        return paymentServiceImpl.retrieveCustomer(id);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable long id) {
        return paymentServiceImpl.deleteCustomer(id);
    }

    @GetMapping("/balance")
    public BalanceResponse balance() {
        return paymentServiceImpl.getBalance();
    }

    @PostMapping("/customers/charge")
    public ChargeResponse chargeFromCustomer(@RequestBody @Valid CustomerChargeRequest request) {
        return paymentServiceImpl.chargeFromCustomer(request);
    }
}
