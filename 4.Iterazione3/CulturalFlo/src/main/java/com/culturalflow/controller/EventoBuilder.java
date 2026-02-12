/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.culturalflow.controller;

import com.culturalflow.model.Evento;
import java.util.Date;

/**
 *
 * @author monic
 */
public interface EventoBuilder {
    void buildIdEvento(int idEvento);
    void buildTipologia(String tipologia);
    void buildServizio(boolean servizio);
    void buildDettagli(String dettagli);
    void createNuovoEvento(String nome, String luogo, Date data, float prezzobase, int disponibilita);
    
    void buildPrezzoServizio(float prezzoServizio);
    void buildScadenza(Date scadenzaPriorita); 
    void buildTarget(String target);
    void buildCodiceAccesso(String codiceAccesso);
    
    void buildDisponibilita(int disponibilita);
    
    Evento getEvento();
}
