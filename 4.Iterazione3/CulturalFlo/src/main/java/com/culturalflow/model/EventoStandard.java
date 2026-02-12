/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

/**
 *
 * @author monic
 */
public class EventoStandard extends Evento{
    private String tipologia;
    private boolean servizio;
    private String dettagli;
    private float prezzoServizio;
    
    public EventoStandard() { 
        super(); 
    }
    
    @Override
    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }
    
    public boolean getServizio() {
        return servizio;
    }

    public void setServizio(boolean servizio) {
        this.servizio = servizio;
    }

    public String getDettagli() {
        return dettagli;
    }

    public void setDettagli(String dettagli) {
        this.dettagli = dettagli;
    }

    public float getPrezzoServizio() {
        return prezzoServizio;
    }

    public void setPrezzoServizio(float prezzoServizio) {
        this.prezzoServizio = prezzoServizio;
    }
    
    @Override
    public boolean isAcquistabile(String codiceInserito) {
        return super.isAcquistabile(codiceInserito);
    }
}
