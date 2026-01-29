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
public abstract class Evento {
    private int idEvento;
    private String nome;
    private String luogo;
    private Date data;
    private float prezzoBase;
    private int disponibilita;

    public Evento() {}
    
    public int getIdEvento(){
        return idEvento;
    }

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
    
    public float getPrezzoBase() {
        return prezzoBase;
    }

    public void setPrezzoBase(float prezzo) { 
        this.prezzoBase = prezzo; 
    }

    public int getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(int disponibilita) {
        this.disponibilita = disponibilita;
    }
    
    public void aggiornaDisponibilita(int quantita) {
        this.disponibilita -= quantita;
    }
    
    public String getTipologia() {
        return "";
    }
    
    public boolean isAcquistabile(String codiceInserito) {
        return disponibilita > 0;
    }
}
