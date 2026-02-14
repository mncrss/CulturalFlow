/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

import java.util.Calendar;
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
    
    @Test
    public void testIsRimborsabile() {
        ScontoStrategy strategy = new ScontoStandard();
        Calendar cal = Calendar.getInstance();

        Evento eFuturo = new EventoStandard();
        cal.add(Calendar.DAY_OF_MONTH, 10); 
        eFuturo.setData(cal.getTime());
    
        Biglietto bRimborsabile = new Biglietto(eFuturo, false, "", strategy);
        assertTrue("Il biglietto deve essere rimborsabile se mancano più di 7 giorni", bRimborsabile.isRimborsabile());

        cal = Calendar.getInstance(); 
        cal.add(Calendar.DAY_OF_MONTH, 2);
    
        Evento eImminente = new EventoStandard();
        eImminente.setData(cal.getTime());
    
        Biglietto bNonRimborsabile = new Biglietto(eImminente, false, "", strategy);
        assertFalse("Il biglietto NON deve essere rimborsabile se mancano meno di 7 giorni", bNonRimborsabile.isRimborsabile());
    }
}
