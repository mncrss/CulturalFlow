/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author monic
 */
public class EventoPopUpTest {
    @Test
    public void testIsAcquistabile() {
        EventoPopUp ep = new EventoPopUp();
        ep.setDisponibilita(10);
        ep.setCodiceAccesso("POP-123");
        
        assertFalse("L'evento deve nascere non pubblico", ep.isPublic());
        
        Calendar calFuturo = Calendar.getInstance();
        calFuturo.add(Calendar.DAY_OF_MONTH, 5);
        ep.setScadenzaPriorita(calFuturo.getTime());

        assertFalse("Codice errato deve fallire", ep.isAcquistabile("ERRATO"));
        assertFalse("Stato deve rimanere non pubblico", ep.isPublic());
        assertTrue("Codice corretto deve passare", ep.isAcquistabile("POP-123"));

        Calendar calPassato = Calendar.getInstance();
        calPassato.add(Calendar.DAY_OF_MONTH, -1);
        ep.setScadenzaPriorita(calPassato.getTime());

        assertTrue("Deve essere acquistabile dopo la scadenza", ep.isAcquistabile(null));
        assertTrue("L'attributo isPublic deve essere diventato true", ep.isPublic());
        
        ep.setDisponibilita(0);
        assertFalse("Senza posti disponibili deve restituire SEMPRE false", ep.isAcquistabile("POP-123"));
    }
}
