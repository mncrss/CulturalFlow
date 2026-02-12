/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.controller;

import com.culturalflow.model.Evento;
import com.culturalflow.model.EventoPopUp;
import java.util.Date;
/**
 *
 * @author monic
 */
public class EventoPopUpConcreteBuilder implements EventoBuilder{
    private EventoPopUp evento;

    @Override
    public void createNuovoEvento(String nome, String luogo, Date data, float prezzobase, int disponibilita) {
        this.evento = new EventoPopUp();
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
    public void buildScadenza(Date scadenzaPriorita) {
        evento.setScadenzaPriorita(scadenzaPriorita);
    }

    @Override
    public void buildTarget(String target) {
        evento.setTarget(target);
    }

    @Override
    public void buildCodiceAccesso(String codiceAccesso) {
        evento.setCodiceAccesso(codiceAccesso);
    }
    
    @Override
    public void buildDisponibilita(int disponibilita) {
        evento.setDisponibilita(disponibilita); 
    }

    @Override public void buildTipologia(String tipologia) {}
    @Override public void buildServizio(boolean servizio) {}
    @Override public void buildDettagli(String dettagli) {}
    @Override public void buildPrezzoServizio(float prezzoServizio) {}

    @Override
    public Evento getEvento() {
        return this.evento;
    }
}
