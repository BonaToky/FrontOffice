package com.projet.frontoffice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet.frontoffice.model.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${backoffice.api.url:http://localhost:8080/reservation-core/api/reservations}")
    private String apiUrl;

    public ReservationService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Reservation> getAllReservations() {
        try {
            String json = restTemplate.getForObject(apiUrl, String.class);
            System.out.println("=== JSON brut ===");
            System.out.println(json);

            JsonNode root = objectMapper.readTree(json);
            JsonNode reservationsNode = root.get("reservations");

            List<Reservation> reservations = objectMapper.convertValue(
                    reservationsNode, new TypeReference<List<Reservation>>() {});

            for (Reservation r : reservations) {
                System.out.println("ID: " + r.getIdReservation()
                        + " | Client: " + r.getIdClient()
                        + " | Date: " + r.getDateReservation()
                        + " | Hotel: " + (r.getHotel() != null ? r.getHotel().getNom() : "null"));
            }

            return reservations;
        } catch (Exception e) {
            System.err.println("Error calling BackOffice API: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Reservation> getFilteredReservations(LocalDate date) {
        List<Reservation> all = getAllReservations();
        if (date == null) {
            return all;
        }
        return all.stream()
                .filter(r -> r.getDateReservation() != null && r.getDateReservation().toLocalDate().isEqual(date))
                .collect(Collectors.toList());
    }
}