/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalflow.controller;

import com.culturalflow.model.Evento;
import com.culturalflow.model.Cliente;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author monic
 */
public class CulturalFlowTest {
    private static CulturalFlow culturalFlow;

    @Before
    public void initTest() {
        culturalFlow = CulturalFlow.getInstance();
    }
    
    @After
    public void clearTest(){
        culturalFlow.reset();
    }
    
    @Test
    public void testSingletonInstance() {
        CulturalFlow anotherInstance = CulturalFlow.getInstance();
        assertSame(anotherInstance, culturalFlow);
    } 
    
    @Test
    public void testInserisciDati() throws Exception{
        Calendar cal = Calendar.getInstance();
        cal.set(2000, Calendar.JUNE, 27);
        Date dataNascita = cal.getTime();
        
        culturalFlow.inserisciDati("Gaetano", "Pennisi", "gaetano@gmail.com", "password123", dataNascita, "Standard");
        culturalFlow.confermaRegistrazione();
            
        assertEquals(1, culturalFlow.getClienti().size());
        Cliente c = culturalFlow.getClienti().get("gaetano@gmail.com");
        assertNotNull(c);
       
    }
    
    @Test
    public void testVerificaEta() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -5); 
        Date dataNascita = cal.getTime();

        Exception exception = assertThrows(Exception.class, () -> {
            culturalFlow.inserisciDati("Gaetano", "Pennisi", "gaetano@mail.it", "password123", dataNascita, "Standard");
        });
    }
    
    @Test
    public void testPassword() throws Exception{
        Calendar cal = Calendar.getInstance();
        cal.set(2000, Calendar.JUNE, 27);
        Date dataNascita = cal.getTime();
        
        Exception exception = assertThrows(Exception.class, () -> {
            culturalFlow.inserisciDati("Gaetano", "Pennisi", "gaetano@mail.it", "123", dataNascita, "Standard");
        });
    }
    
    @Test
    public void testConfermaRegistrazione() throws Exception{
        Calendar cal = Calendar.getInstance();
        cal.set(2000, Calendar.JUNE, 27);
        Date dataNascita = cal.getTime();
    
        culturalFlow.inserisciDati("Gaetano", "Pennisi", "gaetano@gmail.com", "password123", dataNascita, "Standard");
        Cliente c = culturalFlow.getClienteCorrente();
        assertNotNull(c);

        culturalFlow.confermaRegistrazione();

        assertThrows(ClienteGiaRegistratoException.class, () ->{
            culturalFlow.inserisciDati("Gaetano", "Pennisi", "gaetano@gmail.com", "password123", dataNascita, "Standard");
        });
    }
    
    @Test
    public void testInserisciEvento(){
        culturalFlow.inserisciEvento("Concerto di Natale", "Catania", new Date(), 20.0f);
        Evento e = culturalFlow.getEventoCorrente();
        
        assertNotNull(e);
    }
    
    @Test
    public void testSelezionaTipologia(){
        culturalFlow.inserisciEvento("Concerto di Natale", "Catania", new Date(), 20.0f);
        Evento e = culturalFlow.getEventoCorrente();
        culturalFlow.selezionaTipologia("Concerto");
        
        assertEquals("Concerto", e.getTipologia());
    }
    
    @Test
    public void testInserisciServizioDettagli(){
        culturalFlow.inserisciEvento("Concerto di Natale", "Catania", new Date(), 20.0f);
        culturalFlow.inserisciServizioDettagli(true, "Cappelli di Babbo Natale gratuiti");
        
        Evento e = culturalFlow.getEventoCorrente();
        assertTrue(e.getServizio());
        assertEquals("Cappelli di Babbo Natale gratuiti", e.getDettagli());
    }
    
    @Test
    public void testConfermaEvento(){
        culturalFlow.inserisciEvento("Concerto di Natale", "Catania", new Date(), 20.0f);
        Evento e = culturalFlow.getEventoCorrente();
        culturalFlow.confermaEvento();
        
        assertNotNull(e);
        assertTrue(culturalFlow.getEventi().containsValue(e));
    }
    
    @Test
    public void testCercaEvento(){
        culturalFlow.inserisciEvento("Concerto di Natale", "Catania", new Date(), 20.0f);
        Evento e1 = culturalFlow.getEventoCorrente();
        culturalFlow.selezionaTipologia("Concerto");
        culturalFlow.confermaEvento();

        culturalFlow.inserisciEvento("Mostra di Picasso", "Catania", new Date(), 30.0f);
        Evento e2 = culturalFlow.getEventoCorrente();
        culturalFlow.selezionaTipologia("Mostra");
        culturalFlow.confermaEvento();

        culturalFlow.inserisciEvento("Coldplay Tour", "Messina", new Date(), 15.0f);
        Evento e3 = culturalFlow.getEventoCorrente();
        culturalFlow.selezionaTipologia("Concerto");
        culturalFlow.confermaEvento();

        assertTrue(culturalFlow.getEventi().containsValue(e1));
        assertTrue(culturalFlow.getEventi().containsValue(e2));
        assertTrue(culturalFlow.getEventi().containsValue(e3));

        List<Evento> perNome = culturalFlow.cercaEvento("Natale", null, null);
        assertEquals(1, perNome.size());
        assertTrue(perNome.get(0).getNome().contains("Natale"));

        List<Evento> perTipo = culturalFlow.cercaEvento(null, "Mostra", null);
        assertEquals(1, perTipo.size());
        assertEquals("Mostra", perTipo.get(0).getTipologia());

        List<Evento> perLuogo = culturalFlow.cercaEvento(null, null, "Catania");
        assertEquals(2, perLuogo.size());

        List<Evento> combinati = culturalFlow.cercaEvento(null, "Concerto", "Messina");
        assertEquals(1, combinati.size());
        assertEquals("Coldplay Tour", combinati.get(0).getNome());

        List<Evento> vuota = culturalFlow.cercaEvento("Laboratorio", null, "Catania");
        assertTrue(vuota.isEmpty());
    }
}