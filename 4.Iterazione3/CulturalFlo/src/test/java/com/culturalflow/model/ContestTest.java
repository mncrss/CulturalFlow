/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.Calendar;
/**
 *
 * @author monic
 */
public class ContestTest {
    private Contest contest;
    private Evento evento;

    @Before
    public void setUp() {
        evento = new EventoStandard();
        
        contest = new Contest("Vinci il Premio", "Descrizione", "Smartphone", "Aperto", new Date(), new Date(), new Date(), evento);
    }

    @Test
    public void testAggiungiPartecipante() {
        Cliente c = new Cliente("Monica", "Russo", "monica@email.it", "password123", new Date(), "Standard");
        
        contest.aggiungiPartecipante(c.getEmail(), c);
        
        assertEquals(1, contest.getElencoPartecipanti().size());
        assertTrue(contest.getElencoPartecipanti().containsKey("monica@email.it"));
    }

    @Test
    public void testEstraiVincitore() throws Exception {
        Cliente c1 = new Cliente("Monica", "Russo", "monica@email.it", "password", new Date(), "Standard");
        Cliente c2 = new Cliente("Gaetano", "Pennisi", "gaetano@email.it", "password", new Date(), "Standard");
        
        contest.aggiungiPartecipante(c1.getEmail(), c1);
        contest.aggiungiPartecipante(c2.getEmail(), c2);

        contest.estraiVincitore();

        assertNotNull("Deve esserci un vincitore", contest.getVincitore());
        assertEquals("Lo stato deve passare a Chiuso", "Chiuso", contest.getStato());
        
        String emailV = contest.getVincitore().getEmail();
        assertTrue(emailV.equals("monica@email.it") || emailV.equals("gaetano@email.it"));
    }
}
