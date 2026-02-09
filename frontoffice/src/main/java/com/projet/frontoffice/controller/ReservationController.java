package com.projet.frontoffice.controller;

import com.projet.frontoffice.service.ReservationService;
import com.projet.frontoffice.model.Reservation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String listReservations(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        List<Reservation> reservations = reservationService.getFilteredReservations(date);
        model.addAttribute("reservations", reservations);
        model.addAttribute("selectedDate", date != null ? date.toString() : "");
        return "reservation-list";
    }
    
}
