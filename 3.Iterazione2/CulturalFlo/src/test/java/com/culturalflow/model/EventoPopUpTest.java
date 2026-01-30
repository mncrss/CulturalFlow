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
        
        Calendar calFuturo = Calendar.getInstance();
        calFuturo.add(Calendar.DAY_OF_MONTH, 5);
        ep.setScadenzaPriorita(calFuturo.getTime());

        assertFalse("Priorità attiva: con codice errato deve restituire false", ep.isAcquistabile("ERRATO"));
        assertFalse("Priorità attiva: con codice null deve restituire false", ep.isAcquistabile(null));
        assertTrue("Priorità attiva: con codice corretto deve restituire true", ep.isAcquistabile("POP-123"));

        Calendar calPassato = Calendar.getInstance();
        calPassato.add(Calendar.DAY_OF_MONTH, -1);
        ep.setScadenzaPriorita(calPassato.getTime());

        assertTrue("Priorità scaduta: deve restituire true anche con codice errato", ep.isAcquistabile("QUALSIASI"));
        assertTrue("Priorità scaduta: deve restituire true anche con codice null", ep.isAcquistabile(null));

        ep.setDisponibilita(0);
        assertFalse("Senza posti disponibili deve restituire SEMPRE false", ep.isAcquistabile("POP-123"));
    }
}
