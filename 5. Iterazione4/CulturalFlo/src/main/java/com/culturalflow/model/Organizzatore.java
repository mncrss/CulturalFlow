/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author monic
 */
public class Organizzatore {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String nomeOrganizzazione;
    private boolean isSponsor;
    
    private Map<Integer, Contest> contestIndetti;
    private Map<Integer, Evento> eventiGestiti;
    
    public Organizzatore(String nome, String cognome, String email, String password, String nomeOrganizzazione, boolean isSponsor) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.nomeOrganizzazione = nomeOrganizzazione;
        this.isSponsor = isSponsor;
        this.contestIndetti = new HashMap<>();
        this.eventiGestiti = new HashMap<>();
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getCognome(){
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSponsor() {
        return isSponsor;
    }

    public void setSponsor(boolean isSponsor) {
        this.isSponsor = isSponsor;
    }

    public String getNomeOrganizzazione() {
        return nomeOrganizzazione;
    }
    
    public Map<Integer, Contest> getContestIndetti() {
        return contestIndetti;
    }
    
    public void addContest(Contest c) {
        this.contestIndetti.put(c.getIdContest(), c);
    }
    
    public String getPassword(){
        return password;
    }
    
    public Map<Integer, Evento> getEventiGestiti() {
        return eventiGestiti;
    }
    
    public void addEvento(Evento e){
        this.eventiGestiti.put(e.getIdEvento(), e);
    }
}
