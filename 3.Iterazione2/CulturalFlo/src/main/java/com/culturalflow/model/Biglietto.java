/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;
import java.util.Date;

/**
 *
 * @author monic
 */
public class Biglietto {
    private int idBiglietto;
    private Date dataAcquisto;
    private float prezzoFinale;
    private boolean servizio;
    private String dettagli;
    private Evento evento;
    private ScontoStrategy scontoStrategy;
    
    public Biglietto(Evento evento, boolean servizio, String dettagli, ScontoStrategy strategy) {
        this.evento = evento;
        this.dataAcquisto = new Date();
        this.servizio = servizio;
        this.dettagli = dettagli;
        this.scontoStrategy = strategy;
    }
        
    public float calcolaPrezzoScontato(float pb, float ps){        
        this.prezzoFinale = scontoStrategy.applicaSconto(pb, ps);
        return this.prezzoFinale;
    }
    
    public int getIdBiglietto() {
        return idBiglietto;
    }

    public void setIdBiglietto(int idBiglietto) {
        this.idBiglietto = idBiglietto;
    }

    public float getPrezzoFinale() {
        return prezzoFinale;
    }

    public Date getDataAcquisto() {
        return dataAcquisto;
    }

    public boolean getServizio() {
        return servizio;
    }

    public String getDettagli() {
        return dettagli;
    }
}
