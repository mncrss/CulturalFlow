/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

import static org.junit.Assert.*;
import org.junit.Test;
/**
 *
 * @author monic
 */
public class BigliettoTest {
    @Test
    public void testCalcoloPrezzoScontato() {
        Evento e = new EventoStandard();
        e.setNome("Evento X");
        
        ScontoStrategy strategy = new Sconto25(); 
            
        Biglietto b = new Biglietto(e, true, "Dettagli Posto", strategy);

        float prezzoBase = 90.0f;
        float prezzoServizio = 10.0f;
        
        float prezzo = b.calcolaPrezzoScontato(prezzoBase, prezzoServizio);

        assertEquals("Il prezzo totale (90+10) con sconto 25% deve essere 75.0", 75.0f, prezzo, 0.001);
        assertEquals("Il prezzo finale deve essere memorizzato correttamente nell'attributo", 75.0f, b.getPrezzoFinale(), 0.001);
    }
}
