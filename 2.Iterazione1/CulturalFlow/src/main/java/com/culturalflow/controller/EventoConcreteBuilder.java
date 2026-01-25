/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.controller;

import com.culturalflow.model.Evento;
import java.util.Date;

/**
 *
 * @author monic
 */
public class EventoConcreteBuilder implements EventoBuilder{
    private Evento evento;
    
    @Override
    public void createNuovoEvento(String nome, String luogo, Date data, float prezzobase){
        this.evento = new Evento();
        this.evento.setNome(nome);
        this.evento.setLuogo(luogo);
        this.evento.setData(data);
        this.evento.setPrezzoBase(prezzobase);
    }

    @Override
    public void buildIdEvento(int idEvento){ 
        evento.setIdEvento(idEvento); 
    }

    @Override
    public void buildTipologia(String tipologia){ 
        evento.setTipologia(tipologia); 
    }

    @Override
    public void buildServizio(boolean servizio){ 
        evento.setServizio(servizio); 
    }

    @Override
    public void buildDettagli(String dettagli){ 
        evento.setDettagli(dettagli); 
    }
    
    @Override
    public Evento getEvento(){ 
        return this.evento; 
    }
}
