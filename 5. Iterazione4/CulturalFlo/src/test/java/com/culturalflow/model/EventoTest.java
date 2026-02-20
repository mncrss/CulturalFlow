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
public class EventoTest {
    @Test
    public void testIsAcquistabile(){
        Evento e = new EventoStandard();
        e.setDisponibilita(5);
        assertTrue("Deve essere acquistabile se posti > 0", e.isAcquistabile(null));
        e.setDisponibilita(0);
        assertFalse("Non deve essere acquistabile se posti = 0", e.isAcquistabile(null));
    }
    
    @Test
    public void testAggiornaDisponibilita(){
        Evento e = new EventoStandard();
        e.setDisponibilita(10);
        
        e.aggiornaDisponibilita(3);
        assertEquals("La disponibilita deve scendere a 7", 7, e.getDisponibilita());
    }
}
