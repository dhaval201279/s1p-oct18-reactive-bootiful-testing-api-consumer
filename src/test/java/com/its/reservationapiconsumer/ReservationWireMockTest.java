package com.its.reservationapiconsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.util.Arrays;


@RunWith(SpringRunner.class)
@AutoConfigureWireMock(port = 8080)
@Import({ReservationApiConsumerApplication.class, ReservationClient.class})
@AutoConfigureJsonTesters
public class ReservationWireMockTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ReservationClient reservationClient;

    @Before
    public void setup() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(Arrays.asList(
           new Reservation("1", "Yat")
        ));

        WireMock.stubFor(
            WireMock.get("/reservations")
                .willReturn(
                    WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(json)
                )
        );
    }

    @Test
    public void testShouldClientReturn() {
        StepVerifier.create(reservationClient.getAllReservations())
                .expectNextMatches(r -> r.getId() != null && r.getName().equalsIgnoreCase("Yat"))
                .expectComplete()
                .log()
                .verify();
    }
}
