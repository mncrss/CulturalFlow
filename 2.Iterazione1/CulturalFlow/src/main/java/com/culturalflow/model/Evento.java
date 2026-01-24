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
public class Evento {
    private int idEvento;
    private String nome;
    private String luogo;
    private Date data;
    private float prezzobase;
    private String tipologia; 
    private boolean servizio; 
    private String dettagli;

    // Costruttore vuoto (verrà popolato dal Builder)
    public Evento() {}

    // Metodi setter che verranno usati dal Builder
    public void setIdEvento(int id) { 
        this.idEvento = id; 
    }
    
    public String getNome(){
        return nome;
    }
    
    public void setNome(String nome) { 
        this.nome = nome; 
    }
    
    public String getLuogo(){
        return luogo;
    }
    
    public void setLuogo(String luogo) {
        this.luogo = luogo; 
    }
    
    public void setData(Date data) { 
        this.data = data; 
    }
    
    public void setPrezzoBase(float prezzo) { 
        this.prezzobase = prezzo; 
    }
    
    public String getTipologia(){
        return tipologia;
    }
    
    public void setTipologia(String tipo) { 
        this.tipologia = tipo; 
    }
    
    public boolean getServizio(){
        return servizio;
    }
    
    public void setServizio(boolean servizio) { 
        this.servizio = servizio; 
    }
    
    public String getDettagli(){
        return dettagli;
    }
    
    public void setDettagli(String dettagli) { 
        this.dettagli = dettagli; 
    }
}
