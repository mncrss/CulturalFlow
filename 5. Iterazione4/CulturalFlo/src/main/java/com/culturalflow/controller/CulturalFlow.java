/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.controller;

import com.culturalflow.model.Cliente;
import com.culturalflow.model.Evento;
import com.culturalflow.model.Organizzatore;
import com.culturalflow.model.Biglietto;
import com.culturalflow.model.Consulenza;
import com.culturalflow.model.Contest;
import com.culturalflow.model.EventoPopUp;
import com.culturalflow.model.EventoStandard;
import com.culturalflow.model.Rimborso;
import com.culturalflow.model.Sconto25;
import com.culturalflow.model.ScontoSenior;
import com.culturalflow.model.ScontoStandard;
import com.culturalflow.model.ScontoStrategy;
import com.culturalflow.model.Staff;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author monic
 */
public class CulturalFlow {
    private static CulturalFlow culturalFlow;
    
    private final Map<String, Cliente> elencoClienti;
    private final Map<Integer, Evento> elencoEventi;
    private final Map<Integer, Contest> elencoContest;
    private final Map<Integer, Consulenza> elencoConsulenze;
    private final Map<String, Organizzatore> organizzatori;
    private final Map<String, Staff> staffSupporto;
    
    private Cliente clienteCorrente;
    private Evento eventoCorrente;
    private Contest contestCorrente;
    private Rimborso rimborsoCorrente;
    private Consulenza consulenzaCorrente;
    
    private Organizzatore organizzatoreLoggato;
    private Staff staffLoggato;
    
    private EventoBuilder eventoBuilder;
    
    private List<Biglietto> biglietti;
    
    private CulturalFlow(){
        this.elencoClienti = new HashMap<>();
        this.elencoEventi = new HashMap<>();
        this.elencoContest = new HashMap<>();
        this.elencoConsulenze = new HashMap<>();
        this.organizzatori = new HashMap<>();
        this.staffSupporto = new HashMap<>();
        this.biglietti = new ArrayList<>();
    }
    
    public static CulturalFlow getInstance() {
        if (culturalFlow == null) {
            culturalFlow = new CulturalFlow();
        }
        return culturalFlow;
    }
    
    public void inserisciDati(String nome, String cognome, String email, String password, Date dataNascita, String categoria)
        throws ClienteGiaRegistratoException, Exception {
            
            if(!verificaEta(dataNascita)){
                throw new Exception("Devi essere maggiorenne per registrarti.");
            }
            if(password == null || password.length()<8){
                throw new Exception("Password troppo debole, deve avere almeno 8 caratteri.");
            }
            if(elencoClienti.containsKey(email)){
                throw new ClienteGiaRegistratoException("Cliente già registrato con l'email: " + email);
            }
            this.clienteCorrente = new Cliente(nome, cognome, email, password, dataNascita, categoria);
    }
    
    private boolean verificaEta(Date dataNascita){
        Calendar oggi = Calendar.getInstance();
        Calendar nascita = Calendar.getInstance();
        nascita.setTime(dataNascita);
        
        int eta = oggi.get(Calendar.YEAR) - nascita.get(Calendar.YEAR);
        if (oggi.get(Calendar.DAY_OF_YEAR) < nascita.get(Calendar.DAY_OF_YEAR)){
            eta--;
        }
       
        return eta >= 18;
    }
    
    public void confermaRegistrazione(){
        String email = clienteCorrente.getEmail();
        elencoClienti.put(email, clienteCorrente);
    }
    
    public List<Evento> cercaEvento(String nome, String tipologia, String luogo){
        List<Evento> elencoEventiDisponibili = new ArrayList<>();
        
        for(Evento e : elencoEventi.values()){
            if((nome == null || e.getNome().toLowerCase().contains(nome.toLowerCase())) &&
                    (tipologia == null || e.getTipologia().toLowerCase().equals(tipologia.toLowerCase())) &&
                    (luogo == null || e.getLuogo().toLowerCase().equals(luogo.toLowerCase()))){
                elencoEventiDisponibili.add(e);
            }
        }
        return elencoEventiDisponibili;
    }
    
    private String creaCodice() {
        return "POP-" + (int)(Math.random() * 10000);
    }
    
    public void creaPopUp(Organizzatore org, String nome, String luogo, Date data, float prezzobase, int disponibilita, String target, Date scadenzaPriorita) throws Exception {
        if (!org.isSponsor()) {
        throw new Exception("Solo gli Sponsor possono creare eventi Pop-Up.");
        }

        String codiceAccesso = creaCodice(); 

        this.eventoBuilder = new EventoPopUpConcreteBuilder();
        eventoBuilder.createNuovoEvento(nome, luogo, data, prezzobase, disponibilita);

        eventoBuilder.buildTarget(target);
        eventoBuilder.buildScadenza(scadenzaPriorita);
        eventoBuilder.buildCodiceAccesso(codiceAccesso);
        this.eventoCorrente = eventoBuilder.getEvento();
    }
    
    public void inserisciEventoStandard(Organizzatore org, String nome, String luogo, Date data, float prezzobase, int disponibilita) {
        this.eventoBuilder = new EventoStandardConcreteBuilder();
        eventoBuilder.createNuovoEvento(nome, luogo, data, prezzobase, disponibilita);
        this.eventoCorrente = eventoBuilder.getEvento();
    }
    
    public void selezionaTipologia(String tipologia){
        eventoBuilder.buildTipologia(tipologia);
    }
    
    public void inserisciServizioDettagli(boolean servizio, float prezzoServizio, String dettagli){
        eventoBuilder.buildServizio(servizio);
        eventoBuilder.buildPrezzoServizio(prezzoServizio);
        eventoBuilder.buildDettagli(dettagli);
    }
    
    /*public void confermaEvento() {
        int id = elencoEventi.size() + 1; 
        eventoBuilder.buildIdEvento(id);
        Evento e = eventoBuilder.getEvento();
        if (e.getOrganizzatore() == null && this.organizzatoreLoggato != null) {
            e.setOrganizzatore(this.organizzatoreLoggato);
        }
        elencoEventi.put(id, e);
    }*/
    
    public void confermaEvento() {
        if (this.eventoCorrente == null) return;

        int id = elencoEventi.size() + 1; 
        this.eventoCorrente.setIdEvento(id);

        if (this.eventoCorrente.getOrganizzatore() == null && this.organizzatoreLoggato != null) {
            this.eventoCorrente.setOrganizzatore(this.organizzatoreLoggato);
        }

        elencoEventi.put(id, this.eventoCorrente);

        if (organizzatoreLoggato != null) {
            organizzatoreLoggato.addEvento(this.eventoCorrente);
        }

        this.eventoCorrente = null;
    }
    /*
    public void inviaInvitiPrioritari() {
        if (eventoCorrente instanceof EventoPopUp) {
            EventoPopUp epu = (EventoPopUp) eventoCorrente;
            String codice = epu.getCodiceAccesso();
            int invitiInviati = 0;

            for (Cliente c : elencoClienti.values()) {
                if (c.haInteresseElevato(epu.getTarget())) { 
                    System.out.println("Invio codice prioritario " + codice + " a " + c.getEmail());
                    invitiInviati++;
                }
            }
            if (invitiInviati == 0){
                elencoEventi.remove(epu);
            }
        }
    }*/
    
    /*public void inviaInvitiPrioritari() {
        if (eventoCorrente instanceof EventoPopUp) {
            EventoPopUp epu = (EventoPopUp) eventoCorrente;
            String codice = epu.getCodiceAccesso();
            int invitiInviati = 0;

            String messaggio = "Codice prioritario per " + epu.getNome() + ": " + codice;
            String dataOggi = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

            for (Cliente c : elencoClienti.values()) {
                if (c.haInteresseElevato(epu.getTarget())) { 
                    c.aggiungiInvito(messaggio, dataOggi); 
                    System.out.println("Invio codice prioritario " + codice + " a " + c.getEmail());
                    invitiInviati++;
                }
            }

            if (invitiInviati == 0){
                elencoEventi.remove(epu.getIdEvento());
            }
        }
    }*/
    
    public void inviaInvitiPrioritari() {
        if (eventoCorrente instanceof EventoPopUp) {
            EventoPopUp epu = (EventoPopUp) eventoCorrente;
            String nomeEv = epu.getNome();
            String codice = epu.getCodiceAccesso();
            int invitiInviati = 0;
            //String dataOggi = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
            Date dataOggi = new Date();
            
            for (Cliente c : elencoClienti.values()) {
                if (c.haInteresseElevato(epu.getTarget())) { 
                    c.aggiungiInvito(nomeEv, codice, dataOggi); 
                    invitiInviati++;
                }
            }
            
            if (invitiInviati == 0){
                elencoEventi.remove(epu.getIdEvento());
            }
        }
    }
    
    public Evento selezionaEvento(int idEvento) {
        this.eventoCorrente = elencoEventi.get(idEvento);
        return this.eventoCorrente;
    }
    
    public void acquistaBiglietto(boolean servizio, String dettagli, int quantita, String codice) throws Exception {
        ScontoStrategy strategy;
        Cliente.Categoria cat = clienteCorrente.getCategoria();
        String tipologia = eventoCorrente.getTipologia();
        
        float pb = eventoCorrente.getPrezzoBase();
        float ps = 0;
        
        if (eventoCorrente instanceof EventoStandard) {
            EventoStandard es = (EventoStandard) eventoCorrente;
            ps = es.getPrezzoServizio();

            if ("laboratorio".equalsIgnoreCase(es.getTipologia()) && "livello avanzato".equalsIgnoreCase(es.getDettagli())) {
            
                if (dettagli == null || !dettagli.toLowerCase().contains("attestato")) {
                    throw new Exception("Per i laboratori di livello avanzato è necessario inserire i riferimenti dell'attestato nei dettagli.");
                }
            }
        }

        if (!eventoCorrente.isAcquistabile(codice)) {
            clienteCorrente.addEventoWishlist(eventoCorrente);
            throw new Exception("Evento non disponibile, è stato aggiunto alla tua wishlist");
        }

        this.biglietti = new ArrayList<>();
    
        if (cat == Cliente.Categoria.Studente || cat == Cliente.Categoria.AccessoSpeciale) {
            strategy = new Sconto25();
        } 
        else if (cat == Cliente.Categoria.Senior && tipologia.toLowerCase().equals("mostra")) {
            strategy = new ScontoSenior(); 
        } 
        else {
            strategy = new ScontoStandard();
        }
        
        if ("concerto".equalsIgnoreCase(tipologia) && dettagli != null && "tribuna".equalsIgnoreCase(dettagli)) {
            pb *= 1.20f;
        }
                
        for (int i = 0; i < quantita; i++) {
            Biglietto b = new Biglietto(eventoCorrente, servizio, dettagli, strategy);
            b.calcolaPrezzoScontato(pb, ps);
            this.biglietti.add(b);
        }

        eventoCorrente.aggiornaDisponibilita(quantita);
    }
    
    public void confermaAcquisto() throws Exception {
        if (this.biglietti == null || this.biglietti.isEmpty()) {
            throw new Exception("Nessun biglietto in attesa di conferma.");
        }   

        for (Biglietto b : this.biglietti) {
            this.clienteCorrente.addBiglietto(b);
        }

        this.biglietti.clear();
    }
    
    public void aggiungiInWishlist(int idEvento) {
        Evento e = elencoEventi.get(idEvento);
    
        if (e != null && clienteCorrente != null) {
            clienteCorrente.addEventoWishlist(e);
            System.out.println("Evento '" + e.getNome() + "' aggiunto ai preferiti.");
        }
    }
    
    public void rimuoviDaWishlist(int idEvento) {
        Evento e = elencoEventi.get(idEvento);
    
        if (e != null && clienteCorrente != null) {
            clienteCorrente.removeEventoWishlist(e);
            System.out.println("Evento rimosso dalla wishlist.");
        }
    }
    
    public List<Evento> mostraWishlist() {
        if (clienteCorrente != null) {
            return clienteCorrente.getWishlist();
        }
        return new ArrayList<>(); 
    }
    
    public void creaContest(String nome, String descrizione, String premio, Date dataInizio, Date dataFine, Date dataEstrazione, int idEvento) throws Exception{
        Evento eventoAssociato = elencoEventi.get(idEvento);
        if (eventoAssociato == null) {
            throw new Exception("Errore: l'evento specificato per il contest non esiste.");
        }
        this.contestCorrente = new Contest(nome, descrizione, premio, "Aperto", dataInizio, dataFine, dataEstrazione, eventoAssociato);
    }
    
    public void confermaContest() throws Exception {
        if (this.contestCorrente == null) {
            throw new Exception("Nessun contest in fase di creazione.");
        }
    
        elencoContest.put(contestCorrente.getIdContest(), contestCorrente);
    
        if (organizzatoreLoggato != null) {
            organizzatoreLoggato.addContest(contestCorrente);
        }
      
        this.contestCorrente = null;
    }
    
    public void richiestaRimborso(int idBiglietto) throws Exception{
        List<Biglietto> elencoBiglietti = clienteCorrente.getBiglietti();
        
        Biglietto b = null;
        for(Biglietto bt : elencoBiglietti){
            if(bt.getIdBiglietto() == idBiglietto){
                b = bt;
                break;
            }
        }
        
        if(b == null) throw new Exception("Biglietto non trovato.");
        
        Evento e = b.getEvento();
        if(b.isRimborsabile()){
            float prezzo = b.getPrezzoFinale();
            this.rimborsoCorrente = new Rimborso(prezzo, b);
            this.rimborsoCorrente.setStato();
            e.aggiornaDisponibilita(-1);
        }else{
            throw new Exception("Il biglietto non è rimborsabile.");
        }
    }
    
    public void confermaRimborso() throws Exception{
        if(this.rimborsoCorrente == null){
            throw new Exception("Errore: nessun rimborso in attesa di conferma");
        }
        
        if(this.clienteCorrente != null){
            this.clienteCorrente.addRimborso(this.rimborsoCorrente);
        }
        
        this.rimborsoCorrente = null;
    }
            
    public void richiediConsulenza(String oggetto, String descrizione) {
        String email = this.clienteCorrente.getEmail();
        List<Consulenza> listaClient = this.clienteCorrente.getConsulenze();
        int idConsulenza = elencoConsulenze.size() + 1;
        Consulenza c = new Consulenza(idConsulenza, oggetto, descrizione, email);
        this.elencoConsulenze.put(idConsulenza, c);
    
        this.clienteCorrente.addConsulenza(c);
    }
    
    public List<Consulenza> visualizzaRichieste() {
        return new ArrayList<>(this.elencoConsulenze.values());
    }
    
    public void selezionaConsulenza(int idConsulenza) throws Exception {
        Consulenza c = elencoConsulenze.get(idConsulenza);
    
        if (c == null) {
            throw new Exception("Consulenza non trovata.");
        }
    
        this.consulenzaCorrente = c; 
    }
    
    public Cliente visualizzaProfiloCliente() throws Exception {
        if (this.consulenzaCorrente == null) {
            throw new Exception("Errore: nessuna consulenza selezionata.");
        }

        String email = this.consulenzaCorrente.getEmailCliente();
        Cliente c = elencoClienti.get(email);

        if (c == null) {
            throw new Exception("Errore: profilo cliente non trovato.");
        }
        return c;
    }
    
    public void inviaSoluzione(String soluzione) throws Exception {
        if (this.consulenzaCorrente == null) {
            throw new Exception("Errore: nessuna consulenza selezionata su cui lavorare.");
        }
        this.consulenzaCorrente.setSoluzione(soluzione);
        this.consulenzaCorrente = null;
    }
    
    public List<Contest> visualizzaContest() {
        return new ArrayList<>(this.elencoContest.values());
    }
    
    public void selezionaContest(int idContest) throws Exception {
        Contest c = elencoContest.get(idContest);
    
        if (c == null) {
            throw new Exception("Contest non trovato.");
        }
        this.contestCorrente = c;
    }
    
    public void iscrizioneContest() throws Exception {
        String email = this.clienteCorrente.getEmail();
        int p = this.clienteCorrente.contaPartecipazioniAperte();
    
        if (p < 2) {
            if (!"Aperto".equalsIgnoreCase(this.contestCorrente.getStato())) {
                throw new Exception("Impossibile iscriversi: il contest selezionato è chiuso.");
            }
            Evento evContest = this.contestCorrente.getEventoRiferimento();
            if (evContest == null) {
                throw new Exception("Errore: l'evento specificato per il contest non esiste.");
            }
            boolean haBiglietto = false;
            for (Biglietto b : this.clienteCorrente.getBiglietti()) {
                if (b.getEvento().equals(evContest)) {
                    haBiglietto = true;
                    break;
                }
            }
            if (!haBiglietto) {
                throw new Exception("Requisiti non soddisfatti: non hai acquistato un biglietto per l'evento: " + evContest.getNome());
            }
            
            this.clienteCorrente.addPartecipazione(this.contestCorrente);
            this.contestCorrente.aggiungiPartecipante(email, this.clienteCorrente);
        } else {
            throw new Exception("Limite raggiunto: hai già 2 iscrizioni attive a contest aperti.");
        }
    }
    
    public void eseguiEstrazioneContest(int idContest) throws Exception {
        Contest c = elencoContest.get(idContest);
        if (c == null) throw new Exception("Contest non trovato.");
        if (new Date().before(c.getDataEstrazione())) {
            throw new Exception("Errore: la data di estrazione non è ancora stata raggiunta.");
        }
        c.estraiVincitore();
    }
   
    public void reset() {
        this.elencoClienti.clear();
        this.elencoEventi.clear();
        this.elencoConsulenze.clear();
        this.elencoContest.clear();
        this.clienteCorrente = null;
        this.eventoCorrente = null;
        this.contestCorrente = null;
        this.consulenzaCorrente = null;
    }
    
    public Cliente getClienteCorrente() {
        return clienteCorrente;
    }
    
    public void setClienteCorrente(Cliente clienteCorrente){
        this.clienteCorrente = clienteCorrente;
    }
    
    public Map<String, Cliente> getClienti(){
        return elencoClienti;
    }
    
    public Evento getEventoCorrente(){
        return eventoCorrente;
    }
    
    public Map<Integer, Evento> getEventi(){
        return elencoEventi;
    }
    
    public List<Biglietto> getBiglietti(){
        return biglietti;
    }
    
    public Consulenza getConsulenzaCorrente(){
        return consulenzaCorrente;
    }
    
    public Map<Integer, Consulenza> getElencoConsulenze(){
        return elencoConsulenze;
    }
    
    public Contest getContestCorrente() {
        return contestCorrente;
    }
    
    public Rimborso getRimborsoCorrente() {
        return rimborsoCorrente;
    }
    
    public Organizzatore getOrganizzatoreLoggato() {
        return organizzatoreLoggato;
    }
    
    public Staff getStaffLoggato() {
        return staffLoggato;
    }
    
    public void setOrganizzatoreLoggato(Organizzatore organizzatoreLoggato){
        this.organizzatoreLoggato = organizzatoreLoggato;
    }
    
    public void setStaffLoggato(Staff staffLoggato){
        this.staffLoggato = staffLoggato;
    }
    
    public Map<Integer, Contest> getElencoContest(){
        return elencoContest;
    }
    
    public Map<String,Organizzatore> getOrganizzatori(){
        return organizzatori;
    }
    
    public Map<String,Staff> getStaff(){
        return staffSupporto;
    }
}
