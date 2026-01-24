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
public class Cliente {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private Date dataNascita;
    private Categoria categoria;
    
    public Cliente(String nome, String cognome, String email, String password, Date dataNascita, String categoriaScelta) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.dataNascita = dataNascita;
        this.categoria = Categoria.valueOf(categoriaScelta);
    }
    
    public String getNome(){
        return nome;
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public enum Categoria {
        Studente, Senior, Standard, AccessoSpeciale
    }
}


