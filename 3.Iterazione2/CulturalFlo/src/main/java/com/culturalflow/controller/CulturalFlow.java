/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.controller;

import com.culturalflow.model.Cliente;
import com.culturalflow.model.Evento;
import com.culturalflow.model.Biglietto;
import com.culturalflow.model.EventoPopUp;
import com.culturalflow.model.EventoStandard;
import com.culturalflow.model.Sconto25;
import com.culturalflow.model.ScontoSenior;
import com.culturalflow.model.ScontoStandard;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author monic
 */
public class CulturalFlow {
    private static CulturalFlow culturalFlow;
    
    private final Map<String, Cliente> elencoClienti;
    private final Map<Integer, Evento> elencoEventi;
    
    private Cliente clienteCorrente;
    private Evento eventoCorrente;
    
    private EventoBuilder eventoBuilder;
    
    private List<Biglietto> biglietti;
    
    private CulturalFlow(){
        this.elencoClienti = new HashMap<>();
        this.elencoEventi = new HashMap<>();
    }
    
    public static CulturalFlow getInstance() {
        if (culturalFlow == null) {
            culturalFlow = new CulturalFlow();
        }
        return culturalFlow;
    }
    
    public void inserisciDati(String nome, String cognome, String email, String password, Date dataNascita, String categoria)
        throws ClienteGiaRegistratoException, Exception {
            
            if(!verificaEta(dataNascita)){
                throw new Exception("Devi essere maggiorenne per registrarti.");
            }
            if(password == null || password.length()<8){
                throw new Exception("Password troppo debole, deve avere almeno 8 caratteri.");
            }
            if(elencoClienti.containsKey(email)){
                throw new ClienteGiaRegistratoException("Cliente già registrato con l'email: " + email);
            }
            this.clienteCorrente = new Cliente(nome, cognome, email, password, dataNascita, categoria);
    }
    
    private boolean verificaEta(Date dataNascita){
        Calendar oggi = Calendar.getInstance();
        Calendar nascita = Calendar.getInstance();
        nascita.setTime(dataNascita);
        
        int eta = oggi.get(Calendar.YEAR) - nascita.get(Calendar.YEAR);
        if (oggi.get(Calendar.DAY_OF_YEAR) < nascita.get(Calendar.DAY_OF_YEAR)){
            eta--;
        }
       
        return eta >= 18;
    }
    
    public void confermaRegistrazione(){
        String email = clienteCorrente.getEmail();
        elencoClienti.put(email, clienteCorrente);
    }
    
    public List<Evento> cercaEvento(String nome, String tipologia, String luogo){
        List<Evento> elencoEventiDisponibili = new ArrayList<>();
        
        for(Evento e : elencoEventi.values()){
            if((nome == null || e.getNome().toLowerCase().contains(nome.toLowerCase())) &&
                    (tipologia == null || e.getTipologia().toLowerCase().equals(tipologia.toLowerCase())) &&
                    (luogo == null || e.getLuogo().toLowerCase().equals(luogo.toLowerCase()))){
                elencoEventiDisponibili.add(e);
            }
        }
        return elencoEventiDisponibili;
    }
    
    
    public void inserisciEventoStandard(String nome, String luogo, Date data, float prezzobase, int disponibilita) {
        this.eventoBuilder = new EventoStandardConcreteBuilder();
        eventoBuilder.createNuovoEvento(nome, luogo, data, prezzobase, disponibilita);
        this.eventoCorrente = eventoBuilder.getEvento();
    }
    
    public void selezionaTipologia(String tipologia){
        eventoBuilder.buildTipologia(tipologia);
    }
    
    public void inserisciServizioDettagli(boolean servizio, float prezzoServizio, String dettagli){
        eventoBuilder.buildServizio(servizio);
        eventoBuilder.buildPrezzoServizio(prezzoServizio);
        eventoBuilder.buildDettagli(dettagli);
    }
    
    public void confermaEvento() {
        int id = elencoEventi.size() + 1; 
        eventoBuilder.buildIdEvento(id);
        Evento e = eventoBuilder.getEvento();
        elencoEventi.put(id, e);
    }
    
   
    
    public Evento selezionaEvento(int idEvento) {
        this.eventoCorrente = elencoEventi.get(idEvento);
        return this.eventoCorrente;
    }
    
    public void acquistaBiglietto(boolean servizio, String dettagli, int quantita, String codice) throws Exception {
        Cliente.Categoria cat = clienteCorrente.getCategoria();
        String tipologia = eventoCorrente.getTipologia();
        
        float pb = eventoCorrente.getPrezzoBase();
        float ps = 0;
        
        if (eventoCorrente instanceof EventoStandard) {
            EventoStandard es = (EventoStandard) eventoCorrente;
            ps = es.getPrezzoServizio();

            if ("laboratorio".equalsIgnoreCase(es.getTipologia()) && "livello avanzato".equalsIgnoreCase(es.getDettagli())) {
            
                if (dettagli == null || !dettagli.toLowerCase().contains("attestato")) {
                    throw new Exception("Per i laboratori di livello avanzato è necessario inserire i riferimenti dell'attestato nei dettagli.");
                }
            }
        }

        if (!eventoCorrente.isAcquistabile(codice)) {
            throw new Exception("Acquisto non consentito: disponibilità esaurita o codice non valido.");
        }

        this.biglietti = new ArrayList<>();

        eventoCorrente.aggiornaDisponibilita(quantita);
    }
   
    public void reset() {
        this.elencoClienti.clear();
        this.elencoEventi.clear();
        this.clienteCorrente = null;
        this.eventoCorrente = null;
    }
    
    public Cliente getClienteCorrente() {
        return clienteCorrente;
    }
    
    public Map<String, Cliente> getClienti(){
        return elencoClienti;
    }
    
    public Evento getEventoCorrente(){
        return eventoCorrente;
    }
    
    public Map<Integer, Evento> getEventi(){
        return elencoEventi;
    }
    
    public List<Biglietto> getBiglietti(){
        return biglietti;
    }
}
