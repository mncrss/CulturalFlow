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
public class EventoStandardTest {
    @Test
    public void testIsAcquistabile() {
        EventoStandard es = new EventoStandard();
        es.setDisponibilita(10);
        
        assertTrue("Deve funzionare con codice null", es.isAcquistabile(null));
        assertTrue("Deve funzionare anche con un codice casuale", es.isAcquistabile("POP-123"));
    }
}
