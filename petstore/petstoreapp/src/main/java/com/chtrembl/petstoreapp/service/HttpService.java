package com.chtrembl.petstoreapp.service;

import com.chtrembl.petstoreapp.model.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
public class HttpService {

    @Value("${petstore.functionurl}")
    private String functionUrl;

    private WebClient webClient;

    @PostConstruct
    public void initialize() {
        this.webClient = WebClient.builder()
                .baseUrl(functionUrl)
                .build();
    }

    public String callFunction(String sessionId, Order order) {

       String updatedOrder;
        try {
            updatedOrder = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                     .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                     .configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false).writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String response = null;

        try {
            response = this.webClient.post().uri("?sessionId="+ sessionId)
                    .body(BodyInserters.fromPublisher(Mono.just(updatedOrder), String.class))
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("Cache-Control", "no-cache")
                    .header("x-functions-key", System.getenv("FUNCTION_KEY"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (RestClientException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
