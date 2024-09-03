package com.shopme.checkout.paypal;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PayPalApiTests {
    private static final String BASE_URL = "https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "ATwnvWKyxzIv5QJhUk8jusAsUmxkvxKjBRKPFlaL1IPFmKXyvghBq37PIu0-4Gbn-Pk83q1ERvuw2JJ7";
    private static final String CLIENT_SECRET = "EPo2WHXp7km7vWpBShL9NXj8xJ_zl8osXEA2gtf1OtOKbZkkBdokqIqMA-kB33nC6sWQv9vRVDtiPepJ";

    @Test
    public void testGetOrderDetails() {
        String orderId = "4A027975W0474063L";
        String requestURL = BASE_URL + GET_ORDER_API + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(
                requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
        PayPalOrderResponse orderResponse = response.getBody();

        System.out.println("Order ID: " + orderResponse.getId());
        System.out.println("Validated: " + orderResponse.validate(orderId));

    }
}