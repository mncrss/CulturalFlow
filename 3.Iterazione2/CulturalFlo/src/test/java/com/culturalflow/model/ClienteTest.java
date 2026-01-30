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
}
