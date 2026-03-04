package com.projet.frontoffice.model;

import lombok.Data;
import java.util.List;

@Data
public class ReservationResponse {
    private List<Reservation> reservations;
}
