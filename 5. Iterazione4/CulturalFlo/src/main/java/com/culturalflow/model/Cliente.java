/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author monic
 */
public class Cliente {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private Date dataNascita;
    private Categoria categoria;
    private List <Evento> wishlist; 
    private List<Biglietto> elencoBiglietti;
    private List<Rimborso> rimborsi;
    private List<Consulenza> consulenzeRichieste;
    private List<Contest> partecipazioni;
    
    private List<String[]> inviti = new java.util.ArrayList<>();
    
    public Cliente(String nome, String cognome, String email, String password, Date dataNascita, String categoriaScelta) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.dataNascita = dataNascita;
        this.categoria = Categoria.valueOf(categoriaScelta);
        this.wishlist = new ArrayList<>();
        this.elencoBiglietti = new ArrayList<>();
        this.rimborsi = new ArrayList<>();
        this.consulenzeRichieste = new ArrayList<>();
        this.partecipazioni = new ArrayList<>();
    }
    
    public String getNome(){
        return nome;
    }
    
    public String getCognome(){
        return cognome;
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public String getPassword(){
        return password;
    }
    
    public Date getDataNascita(){
        return dataNascita;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public List<Evento> getWishlist() {
        return wishlist;
    }
    
    public enum Categoria {
        Studente, Senior, Standard, AccessoSpeciale
    }
    
    public void addBiglietto(Biglietto b) {
        if (b != null) {
            this.elencoBiglietti.add(b);
        }
    }
    
    public void addEventoWishlist(Evento e) {
        if (e != null) {
            if (!this.wishlist.contains(e)) {
                this.wishlist.add(e);
                System.out.println("DEBUG: Evento " + e.getNome() + " aggiunto alla wishlist.");
            } else {
                System.out.println("DEBUG: L'evento " + e.getNome() + " è già presente.");
            }
        }
    }
    
    public void removeEventoWishlist(Evento e) {
        if (e != null) {
            this.wishlist.remove(e);
        System.out.println("DEBUG: Evento rimosso dalla wishlist.");
        }
    }
    
    public boolean haInteresseElevato(String categoriaRicercata){
        int contatore = 0;
        
        for (Evento e : this.wishlist){
            if (e.getTipologia().equals(categoriaRicercata)){
                contatore++;
            }
        }
        return contatore >= 3;
    }
    
    public void addRimborso(Rimborso r){
        if(r != null){
            this.rimborsi.add(r);
        }
    }
    
    public void addConsulenza(Consulenza c) {
        if (c != null) {
            this.consulenzeRichieste.add(c);
        }
    }
    
    public int contaPartecipazioniAperte() {
        int contatore = 0;
        for (Contest c : this.partecipazioni) {
            if ("Aperto".equalsIgnoreCase(c.getStato())) {
                contatore++;
            }
        }
        return contatore;
    }  
    
    public void addPartecipazione(Contest c) {
        if (c != null) {
            this.partecipazioni.add(c);
        }
    }
    
    public List<Biglietto> getBiglietti(){
        return elencoBiglietti;
    }
    
    public List<Rimborso> getRimborsi(){
        return rimborsi;
    }
    
    public List<Consulenza> getConsulenze() {
        return consulenzeRichieste;
    }
    
    public List<String[]> getInviti() {
        return inviti;
    }
    
    public void aggiungiInvito(String nome, String codice, Date data) {
        String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
        this.inviti.add(new String[]{nome, codice, dataStr});
    }
}


