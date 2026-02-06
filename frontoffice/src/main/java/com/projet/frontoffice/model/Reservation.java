package com.projet.frontoffice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    private Long idReservation;
    private Long idClient;
    private Integer nombre;
    private LocalDateTime date_reservation;
    private Hotel hotel;
}
