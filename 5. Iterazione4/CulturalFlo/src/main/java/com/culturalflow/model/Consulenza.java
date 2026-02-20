/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

/**
 *
 * @author monic
 */
public class Consulenza {
    private int idConsulenza;
    private String oggetto;
    private String descrizione;
    private String stato;
    private String emailCliente;
    private String soluzione; 
    
    public Consulenza(int id, String oggetto, String descrizione, String emailCliente) {
        this.idConsulenza = id;
        this.oggetto = oggetto;
        this.descrizione = descrizione;
        this.emailCliente = emailCliente;
        this.stato = "Aperta";
    }

    public String getSoluzione(){
        return soluzione;
    }
    
    public String getStato(){
        return stato;
    }
    
    public void setSoluzione(String soluzione) {
        this.soluzione = soluzione;
        this.stato = "Chiusa";
    }

    public int getIdConsulenza() { 
        return idConsulenza; 
    }
    
    public String getOggetto() { 
        return oggetto; 
    }
    
    public String getDescrizione() { 
        return descrizione; 
    }
    
    public String getEmailCliente() { 
        return emailCliente; 
    }
}
