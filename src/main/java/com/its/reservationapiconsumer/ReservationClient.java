package com.its.reservationapiconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class ReservationClient {
    private final WebClient webClient;

    public ReservationClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Reservation> getAllReservations() {
        return webClient
                .get()
                .uri("http://localhost:8080/reservations")
                .retrieve()
                .bodyToFlux(Reservation.class);
    }
}
