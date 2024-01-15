package com.paymentservice.paymentservice.service.interfaces;

import com.paymentservice.paymentservice.dto.request.CardRequest;
import com.paymentservice.paymentservice.dto.request.ChargeRequest;
import com.paymentservice.paymentservice.dto.request.CustomerChargeRequest;
import com.paymentservice.paymentservice.dto.request.CustomerRequest;
import com.paymentservice.paymentservice.dto.response.BalanceResponse;
import com.paymentservice.paymentservice.dto.response.ChargeResponse;
import com.paymentservice.paymentservice.dto.response.CustomerResponse;
import com.paymentservice.paymentservice.dto.response.StringResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {
    StringResponse charge(ChargeRequest request) throws StripeException;

    StringResponse createToken(CardRequest request) throws StripeException;

    CustomerResponse createCustomer(CustomerRequest request) throws StripeException;

    CustomerResponse retrieveCustomer(Long id) throws StripeException;

    BalanceResponse getBalance() throws StripeException;

    ChargeResponse chargeFromCustomer(CustomerChargeRequest request) throws StripeException;
}
