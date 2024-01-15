package com.paymentservice.paymentservice.service;

import com.paymentservice.paymentservice.dto.request.CardRequest;
import com.paymentservice.paymentservice.dto.request.ChargeRequest;
import com.paymentservice.paymentservice.dto.request.CustomerChargeRequest;
import com.paymentservice.paymentservice.dto.request.CustomerRequest;
import com.paymentservice.paymentservice.dto.response.*;
import com.paymentservice.paymentservice.exception.AlreadyExistsException;
import com.paymentservice.paymentservice.exception.NotFoundException;
import com.paymentservice.paymentservice.exception.PaymentException;
import com.paymentservice.paymentservice.model.User;
import com.paymentservice.paymentservice.repository.CustomerRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.paymentservice.paymentservice.util.Messages.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl {

    @Value("${stripe.key.secret}")
    private String SECRET_KEY;
    @Value("${stripe.key.public}")
    private String PUBLIC_KEY;
    private final CustomerRepository customerRepository;

    public StringResponse charge(ChargeRequest request) {
        Stripe.apiKey = SECRET_KEY;
        Charge charge;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", Math.round(request.getAmount() * 100));
            params.put("currency", request.getCurrency());
            params.put("source", request.getCardToken());
            charge = Charge.create(params);
        }catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
        String message = "Payment successful. ID: " + charge.getId();
        return StringResponse.builder().message(message).build();
    }
    public TokenResponse createToken(CardRequest request) {
        Stripe.apiKey = PUBLIC_KEY;
        Token token;
        try {
            Map<String, Object> card = new HashMap<>();
            card.put("number", request.getCardNumber());
            card.put("exp_month", request.getExpMonth());
            card.put("exp_year", request.getExpYear());
            card.put("cvc", request.getCvc());
            Map<String, Object> params = new HashMap<>();
            params.put("card", card);
            token = Token.create(params);
        }catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
        return TokenResponse.builder().token(token.getId()).build();
    }
    public CustomerResponse createCustomer(CustomerRequest request) {
        Stripe.apiKey = PUBLIC_KEY;
        if (customerRepository.existsById(request.getPassengerId()))
            throw new AlreadyExistsException(String.format(ALREADY_EXISTS_MESSAGE, request.getPassengerId()));
        CustomerCreateParams params =
                CustomerCreateParams.builder()
                        .setName(request.getName())
                        .setEmail(request.getEmail())
                        .setPhone(request.getPhone())
                        .setBalance(Math.round(request.getBalance() * 100))
                        .build();
        Stripe.apiKey = SECRET_KEY;
        return createUser(params, request.getPassengerId());
    }

    private CustomerResponse createUser(CustomerCreateParams params, long id) {
        Stripe.apiKey = SECRET_KEY;
        Customer customer = checkCustomerParams(params);
        createPaymentMethod(customer.getId());
        User user = User
                .builder().customerId(customer.getId())
                .customerId(customer.getId())
                .passengerId(id)
                .build();
        customerRepository.save(user);
        return CustomerResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .name(customer.getName())
                .build();
    }
    private void createPaymentMethod(String customerId) {
        Stripe.apiKey = SECRET_KEY;
        Map<String, Object> paymentMethodParams = new HashMap<>();
        paymentMethodParams.put("type", "card");
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put("token", "tok_visa");
        paymentMethodParams.put("card", cardParams);
        try {
            PaymentMethod paymentMethod = PaymentMethod.create(paymentMethodParams);
            Map<String, Object> attachParams = new HashMap<>();
            attachParams.put("customer", customerId);
            paymentMethod.attach(attachParams);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }
    public CustomerResponse retrieveCustomer(Long id) {
        Stripe.apiKey = SECRET_KEY;
        User user = getUserEntityById(id);
        Customer customer;
        try {
            customer = Customer.retrieve(user.getCustomerId());
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
        return CustomerResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .name(customer.getName())
                .build();
    }
    public ResponseEntity<HttpStatus> deleteCustomer(Long id) {
        Stripe.apiKey = SECRET_KEY;
        User user = getUserEntityById(id);
        customerRepository.delete(user);
        Customer customer;
        try {
            customer = Customer.retrieve(user.getCustomerId());
            customer.delete();
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
        return ResponseEntity.noContent().build();
    }
    private User getUserEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
    }
    private Customer checkCustomerParams(CustomerCreateParams params) {
        try {
            return Customer.create(params);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }

    public BalanceResponse getBalance() {
        Stripe.apiKey = SECRET_KEY;
        Balance balance;
        try {
            balance = Balance.retrieve();
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
        return BalanceResponse
                .builder()
                .amount(balance.getPending().get(0).getAmount()/100.0)
                .currency(balance.getPending().get(0).getCurrency())
                .build();

    }
    public ChargeResponse chargeFromCustomer(CustomerChargeRequest request) {
        Stripe.apiKey = SECRET_KEY;
        User user = getUserEntityById(request.getPassengerId());
        String customerId = user.getCustomerId();
        checkBalance(customerId, Math.round(request.getAmount() * 100));
        updateBalance(customerId, Math.round(request.getAmount() * 100));
        Map<String, Object> paymentIntentParams = new HashMap<>();
        paymentIntentParams.put("amount", Math.round(request.getAmount() * 100));
        paymentIntentParams.put("currency", request.getCurrency());
        paymentIntentParams.put("customer", customerId);
        PaymentIntent intent;
        try {
            intent = PaymentIntent.create(paymentIntentParams);
            intent.setPaymentMethod(customerId);
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(Math.round(request.getAmount() * 100))
                    .setCurrency(request.getCurrency())
                    .setCustomer(customerId)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .setAllowRedirects(
                                            PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER
                                    )
                                    .build()
                    )
                    .build();
            intent = PaymentIntent.create(params);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
        return ChargeResponse.builder()
                .id(intent.getId())
                .amount(intent.getAmount()/100.0)
                .currency(intent.getCurrency()).build();
    }
    private void updateBalance(String customerId, long amount) {
        Customer customer;
        try {
            customer = Customer.retrieve(customerId);
        }catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
        CustomerUpdateParams params =
                CustomerUpdateParams.builder()
                        .setBalance(customer.getBalance() - amount)
                        .build();
        Stripe.apiKey = SECRET_KEY;
        try {
            customer.update(params);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }
    private void checkBalance(String customerId, long amount) {
        Customer customer;
        try {
            customer = Customer.retrieve(customerId);
        }catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
        Long balance = customer.getBalance();
        if (balance < amount) {
            throw new PaymentException(NOT_ENOUGH_MONEY_MESSAGE);
        }
    }
}