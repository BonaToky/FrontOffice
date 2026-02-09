package com.projet.frontoffice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hotel {

    private Long idHotel;
    private String nom;
    private String lieu;

    public Hotel() {}

    public Long getIdHotel() { return idHotel; }
    public void setIdHotel(Long idHotel) { this.idHotel = idHotel; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
}