/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.controller;

import com.culturalflow.model.Cliente;
import com.culturalflow.model.Evento;
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
    private Cliente clienteCorrente;
    
    private CulturalFlow(){
        this.elencoClienti = new HashMap<>();
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
    
}
