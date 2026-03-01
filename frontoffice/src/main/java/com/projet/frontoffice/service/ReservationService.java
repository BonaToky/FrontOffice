package com.projet.frontoffice.service;

import com.projet.frontoffice.model.Hotel;
import com.projet.frontoffice.model.Reservation;
import com.projet.frontoffice.model.ReservationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Service
public class ReservationService {

    private final RestTemplate restTemplate;

    @Value("${backoffice.api.url:http://localhost:8081/api/reservations}")
    private String apiUrl;

    public ReservationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Reservation> getAllReservations(String token) {
        try {
            String url = apiUrl;
            if (token != null && !token.isEmpty()) {
                url += (apiUrl.contains("?") ? "&" : "?") + "token=" + token;
            }
            ReservationResponse response = restTemplate.getForObject(url, ReservationResponse.class);
            return response != null ? response.getReservations() : List.of();
        } catch (Exception e) {
            // Log error and return mock data for now since BackOffice is not ready
            System.err.println("Error calling BackOffice API: " + e.getMessage());
            return getMockReservations(); 
        }
    }

    private List<Reservation> getMockReservations() {
        Hotel h1 = new Hotel(1L, "Hotel Sunshine", "Paris");
        Hotel h2 = new Hotel(2L, "Hotel Moonlight", "Lyon");

        return Arrays.asList(
            new Reservation(1L, 101L, 2, java.time.LocalDateTime.now(), h1),
            new Reservation(2L, 102L, 4, java.time.LocalDateTime.now().plusDays(1), h2),
            new Reservation(3L, 103L, 1, java.time.LocalDateTime.now().minusDays(1), h1)
        );
    }

    public List<Reservation> getFilteredReservations(LocalDate date, String token) {
        List<Reservation> all = getAllReservations(token);
        if (date == null) {
            return all;
        }
        return all.stream()
                .filter(r -> r.getDate_reservation().toLocalDate().isEqual(date))
                .collect(Collectors.toList());
    }
}
