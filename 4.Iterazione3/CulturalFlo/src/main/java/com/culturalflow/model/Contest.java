/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/**
 *
 * @author monic
 */
public class Contest {
    private int idContest;
    private String nome;
    private String descrizione;
    private String premio;
    private String stato;
    private Date dataInizio;
    private Date dataFine;
    private Date dataEstrazione;
    private Evento eventoRiferimento;
    private Map<String, Cliente> elencoPartecipanti;
    private Cliente vincitore;
    private String voucher;
    
    private static int counter = 0;
    
    public Contest(String nome, String descrizione, String premio, String stato, Date dataInizio, Date dataFine, Date dataEstrazione, Evento evento) {
        this.idContest = counter++;
        this.nome = nome;
        this.descrizione = descrizione;
        this.premio = premio;
        this.stato = stato;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.dataEstrazione = dataEstrazione;
        this.eventoRiferimento = evento;
        this.elencoPartecipanti = new HashMap<>();
    }
    
    public void aggiungiPartecipante(String email, Cliente cliente) {
        if (!elencoPartecipanti.containsKey(email)) {
            this.elencoPartecipanti.put(email, cliente);
        }
    }
    
    public void estraiVincitore() throws Exception {
        List<Cliente> partecipanti = new ArrayList<>(elencoPartecipanti.values());
    
        if (partecipanti.isEmpty()) {
            throw new Exception("Impossibile eseguire l'estrazione: non ci sono iscritti al contest.");
        }
        Random rand = new Random();
        int indiceEstratto = rand.nextInt(partecipanti.size());
        this.vincitore = partecipanti.get(indiceEstratto);
    
        this.voucher = "VOUCHER-" + idContest + "-" + (1000 + rand.nextInt(9000));
        this.stato = "Chiuso";
    }
    
    public int getIdContest() { 
        return idContest; 
    }
    
    public String getNome(){
        return nome;
    }
    
    public void setIdContest(int idContest) { 
        this.idContest = idContest; 
    }
    
    public void setNome(String nome) { 
        this.nome = nome; 
    }
    
    public void setDescrizione(String descrizione) { 
        this.descrizione = descrizione; 
    }
    
    public void setPremio(String premio) { 
        this.premio = premio; 
    }
    
    public String getStato(){
        return stato;
    }
    
    public void setStato(String stato) { 
        this.stato = stato; 
    }
    
    public void setDataInizio(Date dataInizio) { 
        this.dataInizio = dataInizio; 
    }
    
    public void setDataFine(Date dataFine) { 
        this.dataFine = dataFine; 
    }
    
    public Date getDataEstrazione(){
        return dataEstrazione;
    }
    
    public void setDataEstrazione(Date dataEstrazione) { 
        this.dataEstrazione = dataEstrazione; 
    }
    
    public Evento getEventoRiferimento() {
        return eventoRiferimento;
    }
    
    public Map<String, Cliente> getElencoPartecipanti() { 
        return elencoPartecipanti; 
    }
    
    public Cliente getVincitore(){
        return vincitore;
    }
}
