package com.leonovalexprog.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class EwmClient {
    private final WebClient webClient;

    public EwmClient(WebClient.Builder webClientBuilder, @Value("${ewm.server.url}") String ewmServerUrl) {
        this.webClient = webClientBuilder.baseUrl(ewmServerUrl).build();
    }

    public Mono<Object> someRestCall(String uri) {
        return this.webClient.get().uri(uri)
                .retrieve().bodyToMono(Object.class);
    }
}
