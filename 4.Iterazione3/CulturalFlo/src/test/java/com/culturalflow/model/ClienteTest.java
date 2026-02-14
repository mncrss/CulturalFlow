/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

import java.util.Calendar;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

/**
 *
 * @author monic
 */
public class ClienteTest {
    private Cliente cliente;
    
    @Before
    public void setUp() {
        Calendar cal = Calendar.getInstance();
        cal.set(1995, Calendar.JANUARY, 1);
        cliente = new Cliente("Monica", "Russo", "monica@email.it", "password", cal.getTime(), "Standard");
    }
    
    @Test
    public void testAddBiglietto() {
        Biglietto b = new Biglietto(new EventoStandard(), false, "Posto A1", new ScontoStandard());
        cliente.addBiglietto(b);
        assertEquals(1, cliente.getBiglietti().size());
    }
    
    @Test
    public void testAddEventoWishlist() {
        Evento e = new EventoStandard();
        e.setNome("Concerto");
        
        cliente.addEventoWishlist(e);
        cliente.addEventoWishlist(e); 

        assertEquals("La wishlist non deve contenere duplicati", 1, cliente.getWishlist().size());
    }
    
    @Test
    public void testHaInteresseElevato() {
        for (int i = 0; i < 3; i++) {
            EventoStandard e = new EventoStandard();
            e.setTipologia("Mostra");
            cliente.addEventoWishlist(e);
        }

        assertTrue("Con 3 eventi l'interesse deve essere elevato", cliente.haInteresseElevato("Mostra"));
        
        cliente.removeEventoWishlist(cliente.getWishlist().get(0));
        assertFalse("Con 2 eventi l'interesse NON deve essere elevato", cliente.haInteresseElevato("Mostra"));
    }
    
    @Test
    public void testAddRimborso() {
        Rimborso r = new Rimborso(15.0f);
        cliente.addRimborso(r);
        assertEquals(1, cliente.getRimborsi().size());
    }
    
    @Test
    public void testContaPartecipazioniAperte() {
        Date d = new Date();
        Contest c1 = new Contest("C1", "D", "P", "Aperto", d, d, d, null);
        Contest c2 = new Contest("C2", "D", "P", "Aperto", d, d, d, null);
        Contest c3 = new Contest("C3", "D", "P", "Chiuso", d, d, d, null);

        cliente.addPartecipazione(c1);
        cliente.addPartecipazione(c2);
        cliente.addPartecipazione(c3);

        int aperte = cliente.contaPartecipazioniAperte();
        
        assertEquals("Il conteggio deve restituire solo le partecipazioni aperte (2 su 3)", 2, aperte);
    }
    
    @Test
    public void testAddConsulenza() {
        Consulenza cons = new Consulenza(1, "Oggetto", "Messaggio", "monica@email.it");
        cliente.addConsulenza(cons);
        
        assertEquals("La lista consulenze deve contenere 1 elemento", 1, cliente.getConsulenze().size());
        assertEquals("L'oggetto della consulenza deve corrispondere", "Oggetto", cliente.getConsulenze().get(0).getOggetto());
    }
}
