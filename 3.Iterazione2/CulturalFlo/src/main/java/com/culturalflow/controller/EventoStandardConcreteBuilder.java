/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.controller;

import com.culturalflow.model.Evento;
import com.culturalflow.model.EventoStandard;
import java.util.Date;

/**
 *
 * @author monic
 */
public class EventoStandardConcreteBuilder implements EventoBuilder {
    private EventoStandard evento;

    @Override
    public void createNuovoEvento(String nome, String luogo, Date data, float prezzobase, int disponibilita) {
        this.evento = new EventoStandard();
        this.evento.setNome(nome);
        this.evento.setLuogo(luogo);
        this.evento.setData(data);
        this.evento.setPrezzoBase(prezzobase);
        this.evento.setDisponibilita(disponibilita);
    }
    
    @Override
    public void buildIdEvento(int idEvento) {
        evento.setIdEvento(idEvento);
    }

    @Override
    public void buildTipologia(String tipologia) {
        evento.setTipologia(tipologia);
    }

    @Override
    public void buildServizio(boolean servizio) {
        evento.setServizio(servizio);
    }

    @Override
    public void buildDettagli(String dettagli) {
        evento.setDettagli(dettagli);
    }

    @Override
    public void buildPrezzoServizio(float prezzoServizio) {
        evento.setPrezzoServizio(prezzoServizio); 
    }
    
    @Override
    public void buildDisponibilita(int disponibilita) {
        evento.setDisponibilita(disponibilita); 
    }

    @Override public void buildScadenza(Date scadenza) {}
    @Override public void buildTarget(String target) {}
    @Override public void buildCodiceAccesso(String codice) {}

    @Override
    public Evento getEvento() {
        return this.evento;
    }
}
