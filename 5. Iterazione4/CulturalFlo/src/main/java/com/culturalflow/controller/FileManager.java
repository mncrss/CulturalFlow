/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.controller;

import com.culturalflow.model.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
/**
 *
 * @author monic
 */
public class FileManager {
    private static final String FILE_CLIENTI = "clienti.txt";
    private static final String FILE_ORG = "organizzatori.txt";
    private static final String FILE_STAFF = "staff.txt";
    private static final String FILE_EVENTI = "eventi.txt";
    private static final String FILE_WISHLIST = "wishlist.txt";
    private static final String FILE_BIGLIETTI = "biglietti.txt";
    private static final String FILE_CONTEST = "contest.txt";
    private static final String FILE_PARTECIPANTI = "partecipanti_contest.txt";
    private static final String FILE_CONSULENZE = "consulenze.txt";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static void salvaTutto(CulturalFlow sistema) {
        salvaUtenti(sistema);
        salvaEventi(sistema.getEventi());
        salvaRelazioni(sistema);
        salvaContestEConsulenze(sistema);
        salvaRimborsi(sistema);
        salvaInviti(sistema);
        salvaPartecipantiContest(sistema);
    }

    private static void salvaUtenti(CulturalFlow sistema) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_CLIENTI))) {
            for (Cliente c : sistema.getClienti().values()) {
                String dataStr = sdf.format(c.getDataNascita());
                out.println(c.getNome() + ";" + c.getCognome() + ";" + c.getEmail() + ";" + c.getPassword() + ";" + dataStr + ";" + c.getCategoria());
            }
        } catch (IOException e) { e.printStackTrace(); }

        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_ORG))) {
            for (Organizzatore o : sistema.getOrganizzatori().values()) {
                out.println(o.getNome() + ";" + o.getCognome() + ";" + o.getEmail() + ";" + o.getPassword() + ";" + o.getNomeOrganizzazione() + ";" + o.isSponsor());
            }
        } catch (IOException e) { e.printStackTrace(); }
        
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_STAFF))) {
            for (Staff s : sistema.getStaff().values()) {
                out.println(s.getIdStaff() + ";" + s.getNome() + ";" + s.getEmail() + ";" + s.getPassword());
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static void salvaEventi(Map<Integer, Evento> eventi) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_EVENTI))) {
            for (Evento e : eventi.values()) {
                StringBuilder sb = new StringBuilder();
                String tipo = (e instanceof EventoPopUp) ? "POPUP" : "STANDARD";

                sb.append(tipo).append(";")
                  .append(e.getIdEvento()).append(";")
                  .append(e.getOrganizzatore() != null ? e.getOrganizzatore().getEmail() : "null").append(";")
                  .append(e.getNome()).append(";")
                  .append(e.getLuogo()).append(";")
                  .append(sdf.format(e.getData())).append(";")
                  .append(e.getPrezzoBase()).append(";")
                  .append(e.getDisponibilita()).append(";")
                  .append(e.getTipologia()).append(";");

                if (e instanceof EventoStandard) {
                    EventoStandard es = (EventoStandard) e;
                    sb.append(es.getServizio()).append(";")
                      .append(es.getPrezzoServizio()).append(";")
                      .append(es.getDettagli());
                } else {
                    EventoPopUp ep = (EventoPopUp) e;
                    sb.append(ep.getTarget()).append(";")
                      .append(ep.getCodiceAccesso()).append(";")
                      .append(ep.getScadenzaPriorita() != null ? sdf.format(ep.getScadenzaPriorita()) : "null");
                }
                out.println(sb.toString());
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static void salvaRelazioni(CulturalFlow sistema) {
        try (PrintWriter outW = new PrintWriter(new FileWriter(FILE_WISHLIST));
             PrintWriter outB = new PrintWriter(new FileWriter(FILE_BIGLIETTI))) {
            for (Cliente c : sistema.getClienti().values()) {
                for (Evento ev : c.getWishlist()) 
                    outW.println(c.getEmail() + ";" + ev.getIdEvento());

                for (Biglietto b : c.getBiglietti())
                    outB.println(
                    c.getEmail() + ";" +   
                    b.getEvento().getIdEvento() + ";" + 
                    sdf.format(b.getDataAcquisto()) + ";" + 
                    b.getPrezzoFinale() + ";" +  
                    b.getServizio() + ";" +
                    (b.getDettagli() == null ? "nessuno" : b.getDettagli())
                );
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static void salvaContestEConsulenze(CulturalFlow sistema) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_CONTEST))) {
            for (Contest c : sistema.getElencoContest().values()) {
                String emailVincitore = (c.getVincitore() != null) ? c.getVincitore().getEmail() : "null";
                out.println(c.getIdContest() + ";" + 
                    c.getNome() + ";" + 
                    c.getDescrizione() + ";" +
                    c.getPremio() + ";" + 
                    c.getStato() + ";" + 
                    sdf.format(c.getDataInizio()) + ";" + 
                    sdf.format(c.getDataFine()) + ";" + 
                    sdf.format(c.getDataEstrazione()) + ";" + 
                    c.getEventoRiferimento().getIdEvento() + ";" + 
                    emailVincitore + ";" +
                    c.getVoucher());
            }
        } catch (IOException e) { e.printStackTrace(); }
        
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_CONSULENZE))) {
            for (Consulenza cons : sistema.visualizzaRichieste()) { 
                String soluzione = (cons.getSoluzione() == null || cons.getSoluzione().isEmpty()) ? "null" : cons.getSoluzione();
                out.println(cons.getIdConsulenza() + ";" + 
                    cons.getOggetto() + ";" +      
                    cons.getDescrizione() + ";" +  
                    cons.getStato() + ";" +        
                    cons.getEmailCliente() + ";" + 
                    soluzione);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    private static void salvaRimborsi(CulturalFlow sistema) {
        try (PrintWriter out = new PrintWriter(new FileWriter("rimborsi.txt"))) {
            for (Cliente c : sistema.getClienti().values()) {
                for (Rimborso r : c.getRimborsi()) {
                    out.println(c.getEmail() + ";" + 
                                r.getBiglietto().getIdBiglietto() + ";" + 
                                r.getImporto() + ";" + 
                                sdf.format(r.getDataRichiesta()) + ";" + 
                                r.getStato());
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    private static void salvaInviti(CulturalFlow sistema) {
        try (PrintWriter out = new PrintWriter(new FileWriter("inviti.txt"))) {
            for (Cliente c : sistema.getClienti().values()) {
                for (String[] inv : c.getInviti()) { 
                    out.println(c.getEmail() + ";" + inv[0] + ";" + inv[1] + ";" + inv[2]);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    private static void salvaPartecipantiContest(CulturalFlow sistema) {
        try (PrintWriter out = new PrintWriter(new FileWriter("partecipanti_contest.txt"))) {
            for (Contest c : sistema.getElencoContest().values()) {

                Map<String, Cliente> partecipanti = c.getElencoPartecipanti();
                for (String email : partecipanti.keySet()) {
                    out.println(c.getIdContest() + ";" + email);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio dei partecipanti contest: " + e.getMessage());
        }
    }
    
    public static void caricaTutto(CulturalFlow sistema) {
        caricaClienti(sistema);
        caricaOrganizzatori(sistema);
        caricaStaff(sistema);
        caricaEventi(sistema);
        caricaWishlist(sistema);
        caricaBiglietti(sistema);
        caricaContest(sistema);
        caricaPartecipantiContest(sistema);
        caricaConsulenze(sistema);
        caricaRimborsi(sistema);
        caricaInviti(sistema);
    }

    private static void caricaClienti(CulturalFlow sistema) {
        File f = new File(FILE_CLIENTI);
        if (!f.exists()) return;
        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String riga = s.nextLine();
                if (riga.trim().isEmpty()) continue;
                String[] d = riga.split(";");
                if (d.length >= 6) { 
                    String nome = d[0];
                    String cognome = d[1];
                    String email = d[2];
                    String pass = d[3];
                    Date dataNascita = sdf.parse(d[4]);
                    String categoria = d[5];

                    Cliente c = new Cliente(nome, cognome, email, pass, dataNascita, categoria);
                    sistema.getClienti().put(c.getEmail(), c);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void caricaOrganizzatori(CulturalFlow sistema) {
        File f = new File(FILE_ORG);
        if (!f.exists()) return;
        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String[] d = s.nextLine().split(";");
                String email = d[2].trim();
                Organizzatore o = new Organizzatore(d[0], d[1], email, d[3], d[4], Boolean.parseBoolean(d[5]));
                sistema.getOrganizzatori().put(o.getEmail(), o);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private static void caricaStaff(CulturalFlow sistema) {
        File f = new File("staff.txt");
        if (!f.exists()) return;

        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] d = line.split(";");
                int id = Integer.parseInt(d[0]);
                String nome = d[1];
                String email = d[2];
                String pass = d[3];

                Staff membro = new Staff(id, nome, email, pass);

                sistema.getStaff().put(email, membro); 
            }
        } catch (Exception e) {
            System.err.println("Errore caricamento staff: " + e.getMessage());
        }
    }
    
    private static void caricaEventi(CulturalFlow sistema) {
    File f = new File(FILE_EVENTI);
    if (!f.exists()) return;
    try (Scanner s = new Scanner(f)) {
        while (s.hasNextLine()) {
            String line = s.nextLine();
            if (line.trim().isEmpty()) continue;
            String[] d = line.split(";");

            String tipo = d[0];
            int id = Integer.parseInt(d[1]);
            String emailOrg = d[2]; 
            Organizzatore org = sistema.getOrganizzatori().get(emailOrg);

            Evento ev;
            if (tipo.equals("STANDARD")) {
                EventoStandard es = new EventoStandard();
                es.setIdEvento(id);
                es.setNome(d[3]);
                es.setLuogo(d[4]);
                es.setData(sdf.parse(d[5]));
                es.setPrezzoBase(Float.parseFloat(d[6]));
                es.setDisponibilita(Integer.parseInt(d[7]));
                es.setTipologia(d[8]);
                
                if (d.length > 11) {
                    es.setServizio(Boolean.parseBoolean(d[9]));
                    es.setPrezzoServizio(Float.parseFloat(d[10]));
                    es.setDettagli(d[11]);
                }
                ev = es;
            } else {
                EventoPopUp ep = new EventoPopUp();
                ep.setIdEvento(id);
                ep.setNome(d[3]);
                ep.setLuogo(d[4]);
                ep.setData(sdf.parse(d[5]));
                ep.setPrezzoBase(Float.parseFloat(d[6]));
                ep.setDisponibilita(Integer.parseInt(d[7]));
                ep.setTarget(d[9]); 
                ep.setCodiceAccesso(d[10]);
                ep.setScadenzaPriorita(d[11].equals("null") ? null : sdf.parse(d[11]));
                ev = ep;
            }

            if (ev != null) {
                ev.setOrganizzatore(org);
                sistema.getEventi().put(id, ev);
                if (org != null) {
                    org.addEvento(ev);
                }
            }
        }
    } catch (Exception e) { e.printStackTrace(); }
}
    
    private static void caricaWishlist(CulturalFlow sistema) {
        File f = new File(FILE_WISHLIST);
        if (!f.exists()) return;
        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String[] d = s.nextLine().split(";");
                Cliente c = sistema.getClienti().get(d[0]);
                Evento e = sistema.getEventi().get(Integer.parseInt(d[1]));
                if (c != null && e != null) c.getWishlist().add(e);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private static void caricaBiglietti(CulturalFlow sistema) {
        File f = new File("biglietti.txt");
        if (!f.exists()) return;

        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] d = line.split(";");

                try {
                    int idEvento = Integer.parseInt(d[1]);
                    Evento ev = sistema.getEventi().get(idEvento);

                    if (ev != null) {
                        boolean servizio = (d.length > 4) ? Boolean.parseBoolean(d[4]) : false;
                        String dettagli = (d.length > 5) ? d[5] : "";

                        ScontoStrategy strategy = null; 

                        Biglietto b = new Biglietto(ev, servizio, dettagli, strategy);
                        sistema.getBiglietti().add(b); 
                    }
                } catch (Exception e) {
                    System.err.println("Errore nella riga biglietto: " + line);
                }
            }
        } catch (Exception e) {
            System.err.println("Errore caricamento biglietti: " + e.getMessage());
        }
    }
    
    private static void caricaContest(CulturalFlow sistema) {
        File f = new File("contest.txt");
        if (!f.exists()) return;

        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String[] d = s.nextLine().split(";");
                Evento ev = sistema.getEventi().get(Integer.parseInt(d[8]));

                if (ev != null) {
                    Contest c = new Contest(d[1], d[2], d[3], d[4], sdf.parse(d[5]), sdf.parse(d[6]), sdf.parse(d[7]), ev);
                    c.setIdContest(Integer.parseInt(d[0]));

                    if (!d[9].equals("null")) {
                        Cliente vincitore = sistema.getClienti().get(d[9]);
                    }

                    sistema.getElencoContest().put(c.getIdContest(), c);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private static void caricaPartecipantiContest(CulturalFlow sistema) {
        File f = new File("partecipanti_contest.txt");
        if (!f.exists()) return;

        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(";");

                int idContest = Integer.parseInt(d[0]);
                String emailCliente = d[1];

                Contest contest = sistema.getElencoContest().get(idContest);
                Cliente cliente = sistema.getClienti().get(emailCliente);

                if (contest != null && cliente != null) {
                    contest.aggiungiPartecipante(cliente.getEmail(), cliente);
                    cliente.addPartecipazione(contest); 
                }
            }
        } catch (Exception e) { 
            System.err.println("Errore caricamento partecipanti: " + e.getMessage()); 
        }
    }
    
    private static void caricaConsulenze(CulturalFlow sistema) {
        File f = new File("consulenze.txt");
        if (!f.exists()) return;

        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(";");

                int id = Integer.parseInt(d[0]);
                String emailCli = d[4]; 

                Consulenza cons = new Consulenza(id, d[1], d[2], emailCli);

                if ("Chiusa".equalsIgnoreCase(d[3])) {
                     cons.setSoluzione(d[5]); 
                }

                sistema.getElencoConsulenze().put(id, cons);

                Cliente c = sistema.getClienti().get(emailCli);
                if (c != null) {
                    c.addConsulenza(cons);
                }
            }
        } catch (Exception e) {
            System.err.println("Errore caricamento consulenze: " + e.getMessage());
        }
    }
    
    private static void caricaRimborsi(CulturalFlow sistema) {
        File f = new File("rimborsi.txt");
        if (!f.exists()) return;

        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(";");

                String emailCliente = d[0]; 
                int idB = Integer.parseInt(d[1]);
                float importo = Float.parseFloat(d[2]);

                Biglietto b = null;
                for(Biglietto bt : sistema.getBiglietti()) {
                    if(bt.getIdBiglietto() == idB) {
                        b = bt;
                        break;
                    }
                }
                Rimborso r = new Rimborso(importo, b);

                r.setDataRichiesta(sdf.parse(d[3]));
                r.setStato();

                Cliente c = sistema.getClienti().get(emailCliente);
                if (c != null) {
                    c.addRimborso(r);
                }
            }
        } catch (Exception e) { 
            System.err.println("Errore caricamento rimborsi: " + e.getMessage()); 
        }
    }
    
    private static void caricaInviti(CulturalFlow sistema) {
        File f = new File("inviti.txt");
        if (!f.exists()) return;

        try (Scanner s = new Scanner(f)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            while (s.hasNextLine()) {
                String[] d = s.nextLine().split(";");

                String email = d[0];
                String nomeEvento = d[1];
                String codice = d[2];
                Date data = sdf.parse(d[3]);

                Cliente c = sistema.getClienti().get(email);
                if (c != null) {
                    c.aggiungiInvito(nomeEvento, codice, data);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
