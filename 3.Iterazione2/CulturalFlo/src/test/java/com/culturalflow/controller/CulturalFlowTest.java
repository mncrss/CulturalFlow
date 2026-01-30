/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.culturalflow.controller;

import com.culturalflow.model.Evento;
import com.culturalflow.model.Cliente;
import com.culturalflow.model.EventoPopUp;
import com.culturalflow.model.EventoStandard;
import com.culturalflow.model.Organizzatore;
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
    public void testCreaPopUp() throws Exception{
        Calendar cal = Calendar.getInstance();
        Date oggi = cal.getTime();
                
        cal.add(Calendar.DAY_OF_MONTH, 10);
        Date dataEvento = cal.getTime();
        
        cal.setTime(oggi);
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Date scadenzaPriorita = cal.getTime();
        
        Organizzatore orgSemplice = new Organizzatore("Paperino", "Pietro", "admin@sponsor.it", "password", "Organizzazione", false);
    
        Exception exception = assertThrows(Exception.class, () -> {
            culturalFlow.creaPopUp(orgSemplice, "Evento", "Catania", dataEvento, 10.0f, 100, "Mostra", scadenzaPriorita);
        });
        assertEquals("Solo gli Sponsor possono creare eventi Pop-Up.", exception.getMessage());

        Organizzatore orgSponsor = new Organizzatore("Pippo", "Pluto", "admin@sponsor.it", "password", "PippoPluto", true);
    
        culturalFlow.creaPopUp(orgSponsor, "Evento 2", "Catania", dataEvento, 15.0f, 100, "Mostra", scadenzaPriorita);
    
        Evento ev = culturalFlow.getEventoCorrente();
        assertNotNull(ev);
        assertTrue(ev instanceof EventoPopUp);
        assertEquals("Mostra", ev.getTipologia());

        assertFalse(culturalFlow.getEventi().isEmpty());
        assertEquals("Evento 2", culturalFlow.getEventi().get(1).getNome());
    }
    
    @Test
    public void testInviaInvitiPrioritari() throws Exception {
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2000, Calendar.JUNE, 27);
        Date dataNascita = cal1.getTime();
        
        culturalFlow.inserisciDati("Monica", "Russo", "monica@email.it", "password123", dataNascita, "Standard");
        culturalFlow.confermaRegistrazione();
        
        for(int i=0; i<3; i++) {
            culturalFlow.inserisciEventoStandard("Mostra "+i, "Catania", new Date(), 10.0f, 100);
            culturalFlow.selezionaTipologia("Mostra");
            culturalFlow.confermaEvento();
            culturalFlow.aggiungiInWishlist(culturalFlow.getEventoCorrente().getIdEvento());
        }

        culturalFlow.inserisciDati("Gaetano", "Pennisi", "gaetano@email.it", "password123", dataNascita, "Standard");
        culturalFlow.confermaRegistrazione();
        culturalFlow.inserisciEventoStandard("Altra Mostra", "Catania", new Date(), 10.0f, 100);
        culturalFlow.selezionaTipologia("Mostra");
        culturalFlow.confermaEvento();
        culturalFlow.aggiungiInWishlist(culturalFlow.getEventoCorrente().getIdEvento());

        Organizzatore orgSponsor = new Organizzatore("Pippo", "Pluto", "admin@sponsor.it", "password", "PippoPluto", true);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 5);
        culturalFlow.creaPopUp(orgSponsor, "PopUp", "Catania", new Date(), 20.0f, 100, "Mostra", cal.getTime());
    
        culturalFlow.inviaInvitiPrioritari();

        Cliente cInteressato = culturalFlow.getClienti().get("monica@email.it");
        Cliente cNonInteressato = culturalFlow.getClienti().get("gaetano@email.it");
        String target = ((EventoPopUp)culturalFlow.getEventoCorrente()).getTarget();
    
        assertTrue("Il cliente interessato deve risultare idoneo per il target", cInteressato.haInteresseElevato(target));
        assertFalse("Il cliente non interessato non deve risultare idoneo", cNonInteressato.haInteresseElevato(target));
    }
    
    @Test
    public void testInserisciEventoStandard(){
        culturalFlow.inserisciEventoStandard("Concerto di Natale", "Catania", new Date(), 20.0f, 100);
        Evento e = culturalFlow.getEventoCorrente();
        
        assertNotNull(e);
    }
    
    @Test
    public void testSelezionaTipologia(){
        culturalFlow.inserisciEventoStandard("Concerto di Natale", "Catania", new Date(), 20.0f, 100);
        Evento e = culturalFlow.getEventoCorrente();
        culturalFlow.selezionaTipologia("Concerto");
        
        assertEquals("Concerto", e.getTipologia());
    }
    
    @Test
    public void testInserisciServizioDettagli(){
        culturalFlow.inserisciEventoStandard("Concerto di Natale", "Catania", new Date(), 20.0f, 100);
        culturalFlow.inserisciServizioDettagli(true, 5.0f, "Cappelli di Babbo Natale");
        
        Evento e = culturalFlow.getEventoCorrente();
        assertTrue(((EventoStandard)e).getServizio());
        assertEquals("Cappelli di Babbo Natale", ((EventoStandard)e).getDettagli());
        assertEquals(5.0f, ((EventoStandard)e).getPrezzoServizio(), 0.001);
    }
    
    @Test
    public void testConfermaEvento(){
        culturalFlow.inserisciEventoStandard("Concerto di Natale", "Catania", new Date(), 20.0f, 100);
        Evento e = culturalFlow.getEventoCorrente();
        culturalFlow.confermaEvento();
        
        assertNotNull(e);
        assertTrue(culturalFlow.getEventi().containsValue(e));
    }
    
    @Test
    public void testCercaEvento(){
        culturalFlow.inserisciEventoStandard("Concerto di Natale", "Catania", new Date(), 20.0f, 100);
        Evento e1 = culturalFlow.getEventoCorrente();
        culturalFlow.selezionaTipologia("Concerto");
        culturalFlow.confermaEvento();

        culturalFlow.inserisciEventoStandard("Mostra di Picasso", "Catania", new Date(), 30.0f, 100);
        Evento e2 = culturalFlow.getEventoCorrente();
        culturalFlow.selezionaTipologia("Mostra");
        culturalFlow.confermaEvento();

        culturalFlow.inserisciEventoStandard("Coldplay Tour", "Messina", new Date(), 15.0f, 100);
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
    
    @Test
    public void testSelezionaEvento() {
        culturalFlow.inserisciEventoStandard("Teatro Greco", "Siracusa", new Date(), 25.0f, 100);
        culturalFlow.confermaEvento();
        int id = culturalFlow.getEventoCorrente().getIdEvento();

        culturalFlow.selezionaEvento(id);

        assertNotNull(culturalFlow.getEventoCorrente());
        assertEquals("Teatro Greco", culturalFlow.getEventoCorrente().getNome());
    }
    
    @Test
    public void testAcquistaBiglietto() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(1995, Calendar.JANUARY, 1); // Maggiorenne
        culturalFlow.inserisciDati("Monica", "Russo", "monica@email.it", "password123", cal.getTime(), "Studente");
        culturalFlow.confermaRegistrazione();

        culturalFlow.inserisciEventoStandard("Teatro Greco", "Siracusa", new Date(), 20.0f, 100);
        culturalFlow.selezionaTipologia("Concerto");
        culturalFlow.confermaEvento();
        int idStandard = culturalFlow.getEventoCorrente().getIdEvento();

        Calendar calScadenza = Calendar.getInstance();
        calScadenza.add(Calendar.DAY_OF_MONTH, 5); 
        Date scadenzaPriorita = calScadenza.getTime();
        
        Organizzatore org = new Organizzatore("Sponsor", "S", "a@s.it", "password", "Org", true);
        culturalFlow.creaPopUp(org, "Mostra Quadri", "Catania", new Date(), 10.0f, 100, "Mostra", scadenzaPriorita);
        EventoPopUp ep = (EventoPopUp) culturalFlow.getEventoCorrente();
        int idPopUp = ep.getIdEvento();
        String codice = ep.getCodiceAccesso();
        
        culturalFlow.inserisciEventoStandard("Workshop Coding", "Palermo", new Date(), 40.0f, 50);
        culturalFlow.selezionaTipologia("laboratorio");
        culturalFlow.inserisciServizioDettagli(false, 0, "livello avanzato");
        culturalFlow.confermaEvento();
        int idLab = culturalFlow.getEventoCorrente().getIdEvento();
        
        culturalFlow.selezionaEvento(idStandard);
        culturalFlow.acquistaBiglietto(false, "tribuna", 1, null);
        assertEquals(1, culturalFlow.getBiglietti().size());
        assertEquals(18.0f, culturalFlow.getBiglietti().get(0).getPrezzoFinale(), 0.001);
        culturalFlow.confermaAcquisto();
        
        culturalFlow.selezionaEvento(idPopUp);
        assertThrows(Exception.class, () -> {
            culturalFlow.acquistaBiglietto(false, null, 1, "CODICE_ERRATO");
        });
        culturalFlow.acquistaBiglietto(false, null, 1, codice);
        assertEquals(1, culturalFlow.getBiglietti().size());
        assertEquals(7.5f, culturalFlow.getBiglietti().get(0).getPrezzoFinale(), 0.001);
        culturalFlow.confermaAcquisto();
        
        culturalFlow.selezionaEvento(idLab);
        culturalFlow.acquistaBiglietto(false, "attestato livello base", 1, null);
        assertEquals(1, culturalFlow.getBiglietti().size());
        assertEquals(30.0f, culturalFlow.getBiglietti().get(0).getPrezzoFinale(), 0.001);
        culturalFlow.confermaAcquisto();
        
        Cliente c = culturalFlow.getClienteCorrente();
        assertEquals(3, c.getBiglietti().size());
        assertTrue(culturalFlow.getBiglietti().isEmpty());
    }
    
    @Test
    public void testConfermaAcquisto() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(1990, Calendar.MAY, 15);
        culturalFlow.inserisciDati("Monica", "Russo", "monica@email.it", "password123", cal.getTime(), "Standard");
        culturalFlow.confermaRegistrazione();

        culturalFlow.inserisciEventoStandard("Teatro Bellini", "Catania", new Date(), 30.0f, 100);
        culturalFlow.selezionaTipologia("Conferenza");
        culturalFlow.confermaEvento();
        int id = culturalFlow.getEventoCorrente().getIdEvento();

        culturalFlow.selezionaEvento(id);
        culturalFlow.acquistaBiglietto(false, null, 2, null);

        assertEquals(2, culturalFlow.getBiglietti().size());
        assertTrue(culturalFlow.getClienteCorrente().getBiglietti().isEmpty());

        culturalFlow.confermaAcquisto();

        assertTrue("La lista nel controller deve essere vuota dopo la conferma", 
                   culturalFlow.getBiglietti().isEmpty());
        
        assertEquals("Il cliente deve avere ora 2 biglietti", 
                     2, culturalFlow.getClienteCorrente().getBiglietti().size());
    }
    
    @Test
    public void testAggiungiInWishlist() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(1995, Calendar.JANUARY, 1);
        culturalFlow.inserisciDati("Monica", "Russo", "monica@email.it", "password123", cal.getTime(), "Standard");
        culturalFlow.confermaRegistrazione(); 

        culturalFlow.inserisciEventoStandard("Mostra d'Arte", "Catania", new Date(), 15.0f, 100);
        culturalFlow.selezionaTipologia("Mostra");
        culturalFlow.confermaEvento();
        
        int id = culturalFlow.getEventoCorrente().getIdEvento();

        culturalFlow.aggiungiInWishlist(id);

        Cliente c = culturalFlow.getClienteCorrente();
        
        assertNotNull("Il cliente corrente deve essere impostato", c);
        assertEquals("La wishlist deve contenere 1 evento", 1, c.getWishlist().size());
        assertEquals("L'evento in wishlist deve essere 'Mostra d'Arte'","Mostra d'Arte", c.getWishlist().get(0).getNome());
    }
    
    @Test
    public void testRimuoviDaWishlist() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(1995, Calendar.JANUARY, 1);
        culturalFlow.inserisciDati("Gaetano", "Pennisi", "gaetano@email.it", "password123", cal.getTime(), "Standard");
        culturalFlow.confermaRegistrazione();

        culturalFlow.inserisciEventoStandard("Concerto Rock", "Catania", new Date(), 20.0f, 100);
        culturalFlow.selezionaTipologia("Concerto");
        culturalFlow.confermaEvento();
        int id = culturalFlow.getEventoCorrente().getIdEvento();
        
        culturalFlow.aggiungiInWishlist(id);
        assertEquals("La wishlist dovrebbe avere 1 evento", 1, culturalFlow.getClienteCorrente().getWishlist().size());

        culturalFlow.rimuoviDaWishlist(id);

        Cliente c = culturalFlow.getClienteCorrente();
        assertTrue("La wishlist deve essere vuota dopo la rimozione", c.getWishlist().isEmpty());
    }
    
    @Test
    public void testMostraWishlist() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(1995, Calendar.JANUARY, 1);
        culturalFlow.inserisciDati("Monica", "Russo", "monica@email.it", "password123", cal.getTime(), "Standard");
        culturalFlow.confermaRegistrazione();

        culturalFlow.inserisciEventoStandard("Evento A", "Catania", new Date(), 10.0f, 100);
        culturalFlow.confermaEvento();
        culturalFlow.aggiungiInWishlist(culturalFlow.getEventoCorrente().getIdEvento());
        
        culturalFlow.inserisciEventoStandard("Evento B", "Palermo", new Date(), 15.0f, 100);
        culturalFlow.confermaEvento();
        culturalFlow.aggiungiInWishlist(culturalFlow.getEventoCorrente().getIdEvento());

        List<Evento> wishlist = culturalFlow.mostraWishlist();

        assertNotNull("La wishlist non deve essere null", wishlist);
        assertEquals("La wishlist deve contenere 2 eventi", 2, wishlist.size());
        assertEquals("Il primo evento deve essere Evento A", "Evento A", wishlist.get(0).getNome());
        assertEquals("Il secondo evento deve essere Evento B", "Evento B", wishlist.get(1).getNome());
    }
}