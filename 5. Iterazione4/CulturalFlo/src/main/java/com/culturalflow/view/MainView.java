/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.culturalflow.view;

import com.culturalflow.controller.CulturalFlow;
import com.culturalflow.model.Cliente;
import com.culturalflow.model.Organizzatore;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author monic
 */
public class MainView extends javax.swing.JFrame {
    CulturalFlow sistema = CulturalFlow.getInstance();
    /**
     * Creates new form MainView
     */
    public MainView() {
        initComponents();
    
        com.culturalflow.controller.FileManager.caricaTutto(sistema); 
        javax.swing.JPanel headerPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        headerPanel.setBackground(new java.awt.Color(240, 240, 240));
        javax.swing.JButton btnLogout = new javax.swing.JButton("Logout");
        btnLogout.setFocusable(false);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eseguiLogout();
            }
        });
        headerPanel.add(btnLogout, java.awt.BorderLayout.EAST);
        headerPanel.add(new javax.swing.JLabel("CulturalFlow"), java.awt.BorderLayout.WEST);
        this.getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);
        headerPanel.setName("headerLogout"); 
        headerPanel.setVisible(false);

        this.revalidate();
        this.getContentPane().add(jTabbedPane2, java.awt.BorderLayout.CENTER);

        setupIniziale();
        spnQuantita.setModel(new javax.swing.SpinnerNumberModel(1, 1, 100, 1));
        jPanel8.setVisible(false);
    }
    

    private void setupIniziale() {
        jTabbedPane2.removeAll();

        jTabbedPane2.addTab("Login", jPanelLogin);
        jTabbedPane2.addTab("Registrazione", jPanelRegistrazione);

        jTabbedPane2.setEnabledAt(0, true);
        jTabbedPane2.setEnabledAt(1, true);

        jTabbedPane2.setSelectedIndex(0);
        jTabbedPane2.revalidate();
        jTabbedPane2.repaint();

        disabilitaPannelliEvento();
    }

    private void disabilitaPannelliEvento() {
        txtTipologia.setEnabled(false);
        rbServizioSi.setEnabled(false);
        rbServizioNo.setEnabled(false);
        txtPrezzoServizio.setEnabled(false);
        txtDettagli.setEnabled(false);

        txtTarget.setEnabled(false);
        txtScadenza.setEnabled(false);
        jLabel20.setVisible(false);
    }

    private void mostraInterfacciaCliente(String nome) {
        for (java.awt.Component c : this.getContentPane().getComponents()) {
            if ("headerLogout".equals(c.getName())) {
                c.setVisible(true);
                break;
            }
        }
        
        jTabbedPane2.addTab("Ricerca Eventi", jPanelRicercaEventi);
        jTabbedPane2.addTab("I miei biglietti", jPanel4);
        jTabbedPane2.addTab("La mia Wishlist", jPanel5);
        jTabbedPane2.addTab("Iscriviti al Contest", jPanel10);
        jTabbedPane2.addTab("Richiedi Consulenza", jPanel16);
        
        jTabbedPane2.removeTabAt(0); 
        jTabbedPane2.removeTabAt(0);
        
        caricaDatiEventi(null);
        caricaWishlist();    
        caricaMieiInviti();  
        caricaMieiBiglietti();
        caricaContestPerCliente();
        aggiornaTabellaMieiContest();
        caricaMieConsulenze();
        javax.swing.JOptionPane.showMessageDialog(this, "Bentornato/a: " + nome);
    }
    
    private void mostraInterfacciaOrganizzatore(String nomeOrg) {
        for (java.awt.Component c : this.getContentPane().getComponents()) {
            if ("headerLogout".equals(c.getName())) {
                c.setVisible(true);
                break;
            }
        }
        
        jTabbedPane2.removeTabAt(0); 
        jTabbedPane2.removeTabAt(0); 

        jTabbedPane2.addTab("Gestione Eventi", jPanelGestioneEventi);
        jTabbedPane2.addTab("Crea Nuovo Evento", jPanelNuovoEvento);
        jTabbedPane2.addTab("Crea Nuovo Concorso", jPanelNuovoContest);
        
        Organizzatore org = sistema.getOrganizzatoreLoggato();
    
        if (org != null) {
            if (!org.isSponsor()) {
                jPanel8.setVisible(false); 
                chkIsPopUp.setVisible(true);
                chkIsPopUp.setEnabled(false);
            } else {
                jPanel8.setVisible(true);
                chkIsPopUp.setVisible(true);
                chkIsPopUp.setEnabled(true);
            }
        }
        
        chkIsStandard.setSelected(true);
        chkIsPopUp.setSelected(false);
        
        txtTarget.setEnabled(false);
        txtScadenza.setEnabled(false);
        txtTipologia.setEnabled(true);
        rbServizioSi.setEnabled(true);
        rbServizioNo.setEnabled(true);

        popolaComboEventi();
        aggiornaTabellaOrganizzatore();
        aggiornaTabellaContestOrganizzatore();
        javax.swing.JOptionPane.showMessageDialog(this, "Benvenuto Organizzatore: " + nomeOrg);
    }

    private void mostraInterfacciaStaff(String nomeStaff) {
        for (java.awt.Component c : this.getContentPane().getComponents()) {
            if ("headerLogout".equals(c.getName())) {
                c.setVisible(true);
                break;
            }
        }
        
        tblConsulenzeStaff.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                    tblConsulenzeStaff();
                }
            }
        });

        jTabbedPane2.removeTabAt(0); 
        jTabbedPane2.removeTabAt(0); 

        jTabbedPane2.addTab("Gestione Consulenze", jPanelGestioneConsulenze);
        caricaTutteLeConsulenze();
        javax.swing.JOptionPane.showMessageDialog(this, "Accesso Staff: " + nomeStaff);
    }
    
    private void caricaDatiEventi(String filtro) {
        DefaultTableModel model = (DefaultTableModel) tblEventi.getModel();
        model.setRowCount(0);

        for (com.culturalflow.model.Evento e : sistema.getEventi().values()) {
            if (filtro != null && !filtro.trim().isEmpty()) {
                String f = filtro.toLowerCase().trim();
                boolean match = e.getNome().toLowerCase().contains(f) || 
                                e.getLuogo().toLowerCase().contains(f) || 
                                e.getTipologia().toLowerCase().contains(f);

                if (!match) continue;
            }

            boolean isPop = (e instanceof com.culturalflow.model.EventoPopUp);
            String tipoLabel = isPop ? "⭐ POP-UP (" + e.getTipologia() + ")" : e.getTipologia();
            String popUpStatus = isPop ? "Sì" : "No";

            String servizioVisibile = "-";
            if (e instanceof com.culturalflow.model.EventoStandard) {
                com.culturalflow.model.EventoStandard es = (com.culturalflow.model.EventoStandard) e;
                servizioVisibile = es.getServizio() ? String.format("%.2f€", es.getPrezzoServizio()) : "No";
            }

            model.addRow(new Object[]{
                e.getIdEvento(), 
                e.getNome(), 
                e.getLuogo(), 
                e.getData(), 
                tipoLabel, 
                e.getPrezzoBase(), 
                e.getDisponibilita(), 
                servizioVisibile, 
                popUpStatus
            });
        }
        if (model.getRowCount() == 0 && filtro != null && !filtro.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessun evento trovato per: " + filtro);
        }
    }
        
   /* private void aggiornaTabellaOrganizzatore() {
        DefaultTableModel model = (DefaultTableModel) tblEventiOrganizzatore.getModel();
        model.setRowCount(0);

        for (com.culturalflow.model.Evento e : sistema.getEventi().values()) {
            Object[] row = {
                    e.getIdEvento(),
                    e.getNome(),
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(e.getData()),
                    (e instanceof com.culturalflow.model.EventoPopUp) ? "Pop-Up" : "Standard",
                    "N/A"
                };
            model.addRow(row);
        }
    }*/
    
    private void aggiornaTabellaOrganizzatore() {
        DefaultTableModel model = (DefaultTableModel) tblEventiOrganizzatore.getModel();
        model.setRowCount(0);

        com.culturalflow.model.Organizzatore orgLoggato = sistema.getOrganizzatoreLoggato();
        if (orgLoggato == null) return;

        for (com.culturalflow.model.Evento e : sistema.getEventi().values()) {
            if (e.getOrganizzatore() != null && e.getOrganizzatore().getEmail().equalsIgnoreCase(orgLoggato.getEmail())) {

                long venduti = sistema.getBiglietti().stream()
                    .filter(b -> b.getEvento().getIdEvento() == e.getIdEvento())
                    .count();

                Object[] row = {
                    e.getIdEvento(),
                    e.getNome(),
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(e.getData()),
                    (e instanceof com.culturalflow.model.EventoPopUp) ? "Pop-Up" : "Standard",
                    venduti
                };
                model.addRow(row);
            }
        }
    }
    
    private void caricaMieiBiglietti() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblMieiBiglietti.getModel();
        model.setRowCount(0);

        com.culturalflow.model.Cliente cliente = sistema.getClienteCorrente();
        if (cliente == null) return;

        for (com.culturalflow.model.Biglietto b : cliente.getBiglietti()) {
            String nomeEvento = b.getEvento().getNome(); 

            String statoBiglietto = "Valido"; 

            if (cliente.getRimborsi() != null) {
                for (com.culturalflow.model.Rimborso r : cliente.getRimborsi()) {
                    if (r.getBiglietto().getIdBiglietto() == b.getIdBiglietto()) {
                        statoBiglietto = r.getStato();
                        break;
                    }
                }
            }

            Object[] riga = {
                b.getIdBiglietto(),
                nomeEvento,
                String.format("%.2f€", b.getPrezzoFinale()),
                b.getDataAcquisto(),
                statoBiglietto 
            };
            model.addRow(riga);
        }
    }
    
    private void caricaWishlist() {
        DefaultTableModel model = (DefaultTableModel) tblWishlist.getModel();
        model.setRowCount(0);
        com.culturalflow.model.Cliente cliente = sistema.getClienteCorrente();

        if (cliente != null) {
            for (com.culturalflow.model.Evento e : cliente.getWishlist()) {
                model.addRow(new Object[]{
                    e.getIdEvento(),
                    e.getNome(),
                    e.getLuogo(),
                    e.getPrezzoBase() + "€",
                    e.getTipologia()
                });
            }
        }
    }
        
    private void eseguiLogout() {
        int conferma = javax.swing.JOptionPane.showConfirmDialog(this, "Vuoi uscire?", "Logout", javax.swing.JOptionPane.YES_NO_OPTION);
        if (conferma == javax.swing.JOptionPane.YES_OPTION) {
            sistema.setOrganizzatoreLoggato(null);
            sistema.setClienteCorrente(null);

            setupIniziale(); 

            this.revalidate();
            this.repaint();
        }
    }

    private void caricaMieiInviti() {
        DefaultTableModel model = (DefaultTableModel) tblInviti.getModel();
        model.setRowCount(0);

        com.culturalflow.model.Cliente cliente = sistema.getClienteCorrente();
        if (cliente == null || cliente.getInviti() == null) {
            return;
        }

        for (String[] inv : cliente.getInviti()) {
            model.addRow(new Object[]{
                inv[0],
                inv[1],
                inv[2]
            });
        }
    }
    
    private void svuotaCampiInserimento() {
        txtNomeEvento.setText("");
        txtLuogo.setText("");
        txtPrezzo.setText("");
        txtDisp.setText("");
        txtdata.setText("");
        txtTipologia.setText("");
        txtPrezzoServizio.setText("");
        txtDettagli.setText("");
        rbServizioNo.setSelected(true);
        txtTarget.setText("");
        txtScadenza.setText("");
        chkIsStandard.setSelected(true);
        chkIsPopUp.setSelected(false);
    }
    
    private void popolaComboEventi() {
        cmbEventiAssociati.removeAllItems();
        com.culturalflow.model.Organizzatore org = sistema.getOrganizzatoreLoggato();
        if (org == null) return;

        try {
            for (com.culturalflow.model.Evento e : sistema.getEventi().values()) {
                if (e.getOrganizzatore() != null && e.getOrganizzatore().getEmail().equalsIgnoreCase(org.getEmail())) {
                    cmbEventiAssociati.addItem(e.getIdEvento() + " - " + e.getNome());
                }
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore nel popolamento della lista: " + ex.getMessage());
        }
    }
    
    /*private void aggiornaTabellaContestOrganizzatore() {
        DefaultTableModel model = (DefaultTableModel) tblContestOrganizzatore.getModel();
        model.setRowCount(0);

        com.culturalflow.model.Organizzatore org = sistema.getOrganizzatoreLoggato();
        if (org == null) return;

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

        for (com.culturalflow.model.Contest c : org.getContestIndetti().values()) {
            String nomeEvento = (c.getEventoRiferimento() != null) ? c.getEventoRiferimento().getNome() : "N/D";

            model.addRow(new Object[]{
                c.getIdContest(),
                c.getNome(),
                nomeEvento,
                c.getPremio(),
                c.getStato(),
                sdf.format(c.getDataEstrazione())
            });
        }
    }*/
    private void aggiornaTabellaContestOrganizzatore() {
        DefaultTableModel model = (DefaultTableModel) tblContestOrganizzatore.getModel();
        model.setRowCount(0);

        Organizzatore org = sistema.getOrganizzatoreLoggato();
        if (org == null) return;

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

        for (com.culturalflow.model.Contest c : sistema.getElencoContest().values()) {
            if (c.getEventoRiferimento() != null && 
                c.getEventoRiferimento().getOrganizzatore().getEmail().equalsIgnoreCase(org.getEmail())) {

                Object[] row = {
                    c.getIdContest(),
                    c.getNome(),
                    c.getEventoRiferimento().getNome(),
                    c.getPremio(),
                    c.getStato(),
                    sdf.format(c.getDataEstrazione())
                };
                model.addRow(row);
            }
        }
    }
    
    private void caricaContestPerCliente() {
        DefaultTableModel model = (DefaultTableModel) tblContestDisponibili.getModel();
        model.setRowCount(0);

        for (com.culturalflow.model.Contest c : sistema.getElencoContest().values()) {

            if ("Aperto".equalsIgnoreCase(c.getStato())) {
                 String nomeEvento = (c.getEventoRiferimento() != null) ? c.getEventoRiferimento().getNome() : "N/D";
                    model.addRow(new Object[]{
                    c.getIdContest(),
                    c.getNome(),
                    nomeEvento,
                    c.getPremio(),
                    c.getDataFine() 
                    });
            }
        }
    }
    
    private void aggiornaTabellaMieiContest() {
        DefaultTableModel model = (DefaultTableModel) tblMieiContest.getModel();
        model.setRowCount(0);

        com.culturalflow.model.Cliente cliente = sistema.getClienteCorrente();
        if (cliente == null) return;

        String emailCliente = cliente.getEmail();

        for (com.culturalflow.model.Contest c : sistema.getElencoContest().values()) {

            if (c.getElencoPartecipanti().containsKey(emailCliente)) {

                String nome = c.getNome();
                String premio = c.getPremio();
                String stato = c.getStato();

                String esito;
                if ("Aperto".equalsIgnoreCase(stato)) {
                    esito = "In attesa di estrazione";
                } else {
                    if (c.getVincitore() != null && emailCliente.equalsIgnoreCase(c.getVincitore().getEmail())) {
                        esito = "HAI VINTO! Voucher: " + c.getVoucher();
                    } else {
                        esito = "Non vinto";
                    }
                }

                model.addRow(new Object[]{nome, premio, stato, esito});
            }
        }
    }
    
    private void caricaTutteLeConsulenze() {
        DefaultTableModel model = (DefaultTableModel) tblConsulenzeStaff.getModel();
        model.setRowCount(0);

        java.util.List<com.culturalflow.model.Consulenza> lista = sistema.visualizzaRichieste();

        for (com.culturalflow.model.Consulenza c : lista) {
            model.addRow(new Object[]{
                c.getIdConsulenza(),
                c.getEmailCliente(),
                c.getOggetto(),
                c.getStato(),
                (c.getSoluzione() == null || c.getSoluzione().isEmpty()) ? "In attesa..." : c.getSoluzione()
            });
        }
    }
    
    private void tblConsulenzeStaff() {
        int riga = tblConsulenzeStaff.getSelectedRow();
        if (riga == -1) return;

        int idConsulenza = (int) tblConsulenzeStaff.getValueAt(riga, 0);

        try {
            sistema.selezionaConsulenza(idConsulenza);
            com.culturalflow.model.Consulenza cons = sistema.getConsulenzaCorrente();
            com.culturalflow.model.Cliente cli = sistema.visualizzaProfiloCliente();

            if (cli != null) {
                lblNome.setText(cli.getNome());
                lblCognome.setText(cli.getCognome());
                lblCategoria.setText(cli.getCategoria().toString());

                txtOggetto.setText(cons.getDescrizione());

                if ("Chiusa".equalsIgnoreCase(cons.getStato())) {
                    txtSoluzione.setText(cons.getSoluzione());
                    btnSoluzione.setEnabled(false); 
                } else {
                    txtSoluzione.setText("");
                    btnSoluzione.setEnabled(true);
                }
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore caricamento: " + ex.getMessage());
        }
    }
    
    private void caricaMieConsulenze() {
        DefaultTableModel model = (DefaultTableModel) tblMieConsulenze.getModel();
        model.setRowCount(0);

        com.culturalflow.model.Cliente cliente = sistema.getClienteCorrente();
        if (cliente == null) return;

        if (cliente.getConsulenze() != null) {
            for (com.culturalflow.model.Consulenza c : cliente.getConsulenze()) {
                model.addRow(new Object[]{
                    c.getIdConsulenza(),
                    c.getOggetto(),
                    (c.getSoluzione() == null || c.getSoluzione().isEmpty()) 
                        ? "In attesa di risposta..." 
                        : c.getSoluzione()
                });
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupServizio = new javax.swing.ButtonGroup();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanelLogin = new javax.swing.JPanel();
        jPanel = new javax.swing.JPanel();
        txtLoginEmail = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtLoginPass = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        bntVaiRegistra = new javax.swing.JButton();
        jPanelRegistrazione = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        txtNome = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCognome = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        txtData = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        comboCategoria = new javax.swing.JComboBox<>();
        btnRegistra = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMieiBiglietti = new javax.swing.JTable();
        btnAnnulla = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblWishlist = new javax.swing.JTable();
        btnRimuovi = new javax.swing.JButton();
        btnAcquistaWishlist = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblInviti = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jPanelNuovoEvento = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtNomeEvento = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtLuogo = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtdata = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtPrezzo = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtDisp = new javax.swing.JTextField();
        chkIsPopUp = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtTipologia = new javax.swing.JTextField();
        rbServizioSi = new javax.swing.JRadioButton();
        rbServizioNo = new javax.swing.JRadioButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtPrezzoServizio = new javax.swing.JTextField();
        txtDettagli = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txtTarget = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtScadenza = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        btnSalvaEvento = new javax.swing.JButton();
        chkIsStandard = new javax.swing.JCheckBox();
        jPanelGestioneEventi = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblEventiOrganizzatore = new javax.swing.JTable();
        btnInserisciEvento = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblContestOrganizzatore = new javax.swing.JTable();
        jLabel29 = new javax.swing.JLabel();
        btnEstraiVincitore = new javax.swing.JButton();
        jPanelRicercaEventi = new javax.swing.JPanel();
        jScollPanel1 = new javax.swing.JScrollPane();
        tblEventi = new javax.swing.JTable();
        btnAcquista = new javax.swing.JButton();
        txtRicercaGlobale = new javax.swing.JTextField();
        btnCerca = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        btnWishlist = new javax.swing.JButton();
        spnQuantita = new javax.swing.JSpinner();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelNuovoContest = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        txtNomeContest = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtDescrizione = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtPremio = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtDataFineContest = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtDataEstrazione = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        cmbEventiAssociati = new javax.swing.JComboBox<>();
        btnSalvaContest = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblContestDisponibili = new javax.swing.JTable();
        btnPartecipaContest = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblMieiContest = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        txtDescrizioneRichiesta = new javax.swing.JTextArea();
        bntInviaRichiesta = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        txtOggettoRichiesta = new javax.swing.JTextArea();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        tblMieConsulenze = new javax.swing.JTable();
        jPanelGestioneConsulenze = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblConsulenzeStaff = new javax.swing.JTable();
        btnSoluzione = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        lblCognome = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lblCategoria = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtOggetto = new javax.swing.JTextArea();
        jLabel35 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        txtSoluzione = new javax.swing.JTextArea();
        jLabel36 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(jTable2);

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel7.setText("Email:");

        jLabel8.setText("Password");

        txtLoginPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoginPassActionPerformed(evt);
            }
        });

        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        bntVaiRegistra.setText("Non sei ancora registrato?");
        bntVaiRegistra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntVaiRegistraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtLoginEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLoginPass, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                        .addComponent(btnLogin)
                        .addGap(99, 99, 99))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                        .addComponent(bntVaiRegistra, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))))
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLoginEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtLoginPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bntVaiRegistra)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelLoginLayout = new javax.swing.GroupLayout(jPanelLogin);
        jPanelLogin.setLayout(jPanelLoginLayout);
        jPanelLoginLayout.setHorizontalGroup(
            jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLoginLayout.createSequentialGroup()
                .addGap(322, 322, 322)
                .addComponent(jPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(348, Short.MAX_VALUE))
        );
        jPanelLoginLayout.setVerticalGroup(
            jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLoginLayout.createSequentialGroup()
                .addGap(153, 153, 153)
                .addComponent(jPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(171, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Login", jPanelLogin);

        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });

        jLabel1.setText("Nome:");

        jLabel5.setText("Cognome:");

        jLabel2.setText("Email:");

        jLabel3.setText("Password:");

        jLabel4.setText("Data di nascita:");

        txtData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataActionPerformed(evt);
            }
        });

        jLabel6.setText("Categoria:");

        comboCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Standard", "Studente", "Senior", "Accesso Speciale" }));
        comboCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCategoriaActionPerformed(evt);
            }
        });

        btnRegistra.setText("Registrati!");
        btnRegistra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 46, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNome)
                                    .addComponent(txtCognome)
                                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPassword)
                                    .addComponent(txtData)
                                    .addComponent(comboCategoria, 0, 166, Short.MAX_VALUE))))
                        .addContainerGap())))
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(btnRegistra)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCognome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addComponent(btnRegistra)
                .addGap(41, 41, 41))
        );

        javax.swing.GroupLayout jPanelRegistrazioneLayout = new javax.swing.GroupLayout(jPanelRegistrazione);
        jPanelRegistrazione.setLayout(jPanelRegistrazioneLayout);
        jPanelRegistrazioneLayout.setHorizontalGroup(
            jPanelRegistrazioneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegistrazioneLayout.createSequentialGroup()
                .addGap(313, 313, 313)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(318, Short.MAX_VALUE))
        );
        jPanelRegistrazioneLayout.setVerticalGroup(
            jPanelRegistrazioneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegistrazioneLayout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(136, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Registrazione", jPanelRegistrazione);

        tblMieiBiglietti.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Biglietto", "Evento", "Prezzo", "Data Acquisto", "Stato"
            }
        ));
        jScrollPane1.setViewportView(tblMieiBiglietti);

        btnAnnulla.setText("Richiedi Rimborso");
        btnAnnulla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnnullaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(205, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(193, 193, 193))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(btnAnnulla)
                        .addGap(399, 399, 399))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btnAnnulla)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("I miei biglietti", jPanel4);

        tblWishlist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID ", "Nome", "Luogo", "Prezzo", "Tipologia"
            }
        ));
        jScrollPane2.setViewportView(tblWishlist);

        btnRimuovi.setText("Rimuovi");
        btnRimuovi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRimuoviActionPerformed(evt);
            }
        });

        btnAcquistaWishlist.setText("Acquista Biglietto");
        btnAcquistaWishlist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcquistaWishlistActionPerformed(evt);
            }
        });

        tblInviti.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nome", "Codice Invito", "Data"
            }
        ));
        jScrollPane6.setViewportView(tblInviti);

        jLabel22.setText("Inviti:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(331, 331, 331)
                        .addComponent(btnRimuovi)
                        .addGap(100, 100, 100)
                        .addComponent(btnAcquistaWishlist))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
                                .addComponent(jScrollPane6)))))
                .addContainerGap(129, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRimuovi)
                    .addComponent(btnAcquistaWishlist))
                .addGap(19, 19, 19)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("La mia Wishlist", jPanel5);

        jLabel11.setText("Nome:");

        txtNomeEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeEventoActionPerformed(evt);
            }
        });

        jLabel12.setText("Luogo:");

        txtLuogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLuogoActionPerformed(evt);
            }
        });

        jLabel13.setText("Data:");

        jLabel14.setText("Prezzo:");

        jLabel15.setText("Disponibilità:");

        txtDisp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDispActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNomeEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDisp, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrezzo, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtdata, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtLuogo, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNomeEvento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtLuogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtdata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPrezzo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18))
        );

        chkIsPopUp.setText("Evento Pop-Up");
        chkIsPopUp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkIsPopUpItemStateChanged(evt);
            }
        });

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setText("Tipologia:");

        txtTipologia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTipologiaActionPerformed(evt);
            }
        });

        groupServizio.add(rbServizioSi);
        rbServizioSi.setText("Previsto");
        rbServizioSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbServizioSiActionPerformed(evt);
            }
        });

        groupServizio.add(rbServizioNo);
        rbServizioNo.setText("Non Previsto");
        rbServizioNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbServizioNoActionPerformed(evt);
            }
        });

        jLabel16.setText("Servizio:");

        jLabel17.setText("Dettagli: ");

        jLabel21.setText("Prezzo Servizio:");

        txtDettagli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDettagliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrezzoServizio))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(rbServizioSi, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(rbServizioNo))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(txtTipologia, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDettagli, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtTipologia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(rbServizioSi)
                    .addComponent(rbServizioNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPrezzoServizio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtDettagli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel18.setText("Target:");

        txtTarget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTargetActionPerformed(evt);
            }
        });

        jLabel19.setText("Scadenza Priorità: ");

        jLabel20.setText("<html><body style='width: 200px'>Il codice di accesso verrà generato automaticamente dal sistema al momento del salvataggio.</body></html>");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTarget)
                            .addComponent(txtScadenza))))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtScadenza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnSalvaEvento.setText("Salva!");
        btnSalvaEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvaEventoActionPerformed(evt);
            }
        });

        chkIsStandard.setText("Evento Standard");
        chkIsStandard.setToolTipText("");
        chkIsStandard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkIsStandardActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelNuovoEventoLayout = new javax.swing.GroupLayout(jPanelNuovoEvento);
        jPanelNuovoEvento.setLayout(jPanelNuovoEventoLayout);
        jPanelNuovoEventoLayout.setHorizontalGroup(
            jPanelNuovoEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNuovoEventoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(chkIsStandard)
                .addGap(225, 225, 225)
                .addComponent(chkIsPopUp)
                .addGap(276, 276, 276))
            .addGroup(jPanelNuovoEventoLayout.createSequentialGroup()
                .addGroup(jPanelNuovoEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelNuovoEventoLayout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelNuovoEventoLayout.createSequentialGroup()
                        .addGap(426, 426, 426)
                        .addComponent(btnSalvaEvento))
                    .addGroup(jPanelNuovoEventoLayout.createSequentialGroup()
                        .addGap(364, 364, 364)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(171, Short.MAX_VALUE))
        );
        jPanelNuovoEventoLayout.setVerticalGroup(
            jPanelNuovoEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNuovoEventoLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(jPanelNuovoEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(chkIsPopUp)
                    .addComponent(chkIsStandard))
                .addGap(18, 18, 18)
                .addGroup(jPanelNuovoEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnSalvaEvento)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Crea Nuovo Evento", jPanelNuovoEvento);

        tblEventiOrganizzatore.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Data", "Tipo", "Biglietti Venduti"
            }
        ));
        jScrollPane3.setViewportView(tblEventiOrganizzatore);

        btnInserisciEvento.setText("Inserisci Evento");
        btnInserisciEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInserisciEventoActionPerformed(evt);
            }
        });

        tblContestOrganizzatore.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome Contest", "Evento Associato", "Premio", "Stato", "Data Estrazione"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(tblContestOrganizzatore);

        jLabel29.setText("I Tuoi Contest:");

        btnEstraiVincitore.setText("Estrai Vincitore!");
        btnEstraiVincitore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstraiVincitoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelGestioneEventiLayout = new javax.swing.GroupLayout(jPanelGestioneEventi);
        jPanelGestioneEventi.setLayout(jPanelGestioneEventiLayout);
        jPanelGestioneEventiLayout.setHorizontalGroup(
            jPanelGestioneEventiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGestioneEventiLayout.createSequentialGroup()
                .addGroup(jPanelGestioneEventiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGestioneEventiLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(jPanelGestioneEventiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 686, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addComponent(btnEstraiVincitore))
                    .addGroup(jPanelGestioneEventiLayout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelGestioneEventiLayout.createSequentialGroup()
                        .addGap(415, 415, 415)
                        .addComponent(btnInserisciEvento)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanelGestioneEventiLayout.setVerticalGroup(
            jPanelGestioneEventiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGestioneEventiLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnInserisciEvento)
                .addGroup(jPanelGestioneEventiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGestioneEventiLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelGestioneEventiLayout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(btnEstraiVincitore)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Gestione Eventi", jPanelGestioneEventi);

        tblEventi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Luogo", "Data", "Tipologia", "Prezzo Base", "Disponibilità", "Servizio (prezzo)", "Is Pop Up"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScollPanel1.setViewportView(tblEventi);

        btnAcquista.setText("Acquista biglietto");
        btnAcquista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcquistaActionPerformed(evt);
            }
        });

        btnCerca.setText("Cerca!");
        btnCerca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCercaActionPerformed(evt);
            }
        });

        jLabel9.setText("Cerca:");

        btnWishlist.setText("Aggiungi in Wishlist");
        btnWishlist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWishlistActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelRicercaEventiLayout = new javax.swing.GroupLayout(jPanelRicercaEventi);
        jPanelRicercaEventi.setLayout(jPanelRicercaEventiLayout);
        jPanelRicercaEventiLayout.setHorizontalGroup(
            jPanelRicercaEventiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRicercaEventiLayout.createSequentialGroup()
                .addGroup(jPanelRicercaEventiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRicercaEventiLayout.createSequentialGroup()
                        .addGap(221, 221, 221)
                        .addComponent(spnQuantita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAcquista)
                        .addGap(144, 144, 144)
                        .addComponent(btnWishlist))
                    .addGroup(jPanelRicercaEventiLayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jScollPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 797, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelRicercaEventiLayout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtRicercaGlobale, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(btnCerca)))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanelRicercaEventiLayout.setVerticalGroup(
            jPanelRicercaEventiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRicercaEventiLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanelRicercaEventiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtRicercaGlobale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCerca))
                .addGap(15, 15, 15)
                .addComponent(jScollPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(jPanelRicercaEventiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnWishlist)
                    .addComponent(btnAcquista)
                    .addComponent(spnQuantita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Ricerca Eventi", jPanelRicercaEventi);
        jTabbedPane2.addTab("Logout", jTabbedPane1);

        jLabel23.setText("Nome Contest:");

        jLabel24.setText("Descrizione:");

        jLabel25.setText("Premio:");

        jLabel26.setText("Data Fine Iscrizioni: ");

        jLabel27.setText("Data Estrazione:");

        jLabel28.setText("Evento Associato:");

        cmbEventiAssociati.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEventiAssociati.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEventiAssociatiActionPerformed(evt);
            }
        });

        btnSalvaContest.setText("Salva!");
        btnSalvaContest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvaContestActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPremio)
                    .addComponent(txtNomeContest)
                    .addComponent(txtDescrizione)
                    .addComponent(txtDataFineContest)
                    .addComponent(txtDataEstrazione)
                    .addComponent(cmbEventiAssociati, 0, 156, Short.MAX_VALUE))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addComponent(btnSalvaContest)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNomeContest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24)
                    .addComponent(txtDescrizione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPremio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDataFineContest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDataEstrazione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(cmbEventiAssociati, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnSalvaContest)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelNuovoContestLayout = new javax.swing.GroupLayout(jPanelNuovoContest);
        jPanelNuovoContest.setLayout(jPanelNuovoContestLayout);
        jPanelNuovoContestLayout.setHorizontalGroup(
            jPanelNuovoContestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNuovoContestLayout.createSequentialGroup()
                .addContainerGap(286, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(274, 274, 274))
        );
        jPanelNuovoContestLayout.setVerticalGroup(
            jPanelNuovoContestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNuovoContestLayout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(147, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Crea Nuovo Concorso", jPanelNuovoContest);

        tblContestDisponibili.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Evento", "Premio", "Data Fine"
            }
        ));
        jScrollPane8.setViewportView(tblContestDisponibili);

        btnPartecipaContest.setText("Iscriviti!");
        btnPartecipaContest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPartecipaContestActionPerformed(evt);
            }
        });

        tblMieiContest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nome", "Premio", "Stato", "Esito"
            }
        ));
        jScrollPane9.setViewportView(tblMieiContest);

        jLabel30.setText("I miei Contest:");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(424, 424, 424)
                        .addComponent(btnPartecipaContest))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 688, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 688, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30))))
                .addContainerGap(133, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnPartecipaContest)
                .addGap(14, 14, 14)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Iscriviti al Contest", jPanel10);

        jLabel34.setText("Descrizione:");

        txtDescrizioneRichiesta.setColumns(20);
        txtDescrizioneRichiesta.setRows(5);
        jScrollPane13.setViewportView(txtDescrizioneRichiesta);

        bntInviaRichiesta.setText("Invia!");
        bntInviaRichiesta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntInviaRichiestaActionPerformed(evt);
            }
        });

        jLabel38.setText("Oggetto:");

        txtOggettoRichiesta.setColumns(20);
        txtOggettoRichiesta.setRows(5);
        jScrollPane14.setViewportView(txtOggettoRichiesta);

        jLabel37.setText("RICHIEDI LA TUA CONSULENZA!");

        tblMieConsulenze.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID Consulenza", "Oggetto Consulenza", "Soluzione"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane15.setViewportView(tblMieConsulenze);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34)
                            .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(bntInviaRichiesta)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bntInviaRichiesta)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Richiedi Consulenza", jPanel15);

        tblConsulenzeStaff.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Consulenza", "Email Cliente", "Oggetto", "Stato", "Soluzione"
            }
        ));
        jScrollPane10.setViewportView(tblConsulenzeStaff);

        btnSoluzione.setText("Invia Soluzione!");
        btnSoluzione.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSoluzioneActionPerformed(evt);
            }
        });

        jPanel14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel31.setText("Nome:");

        jLabel32.setText("Cognome: ");

        jLabel33.setText("Categoria:");

        txtOggetto.setEditable(false);
        txtOggetto.setColumns(20);
        txtOggetto.setRows(5);
        jScrollPane11.setViewportView(txtOggetto);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblNome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCognome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(lblNome, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(lblCognome, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(lblCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel35.setText("Dettagli Cliente:");

        txtSoluzione.setColumns(20);
        txtSoluzione.setRows(5);
        jScrollPane12.setViewportView(txtSoluzione);

        jLabel36.setText("Rispondi al Cliente:");

        javax.swing.GroupLayout jPanelGestioneConsulenzeLayout = new javax.swing.GroupLayout(jPanelGestioneConsulenze);
        jPanelGestioneConsulenze.setLayout(jPanelGestioneConsulenzeLayout);
        jPanelGestioneConsulenzeLayout.setHorizontalGroup(
            jPanelGestioneConsulenzeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGestioneConsulenzeLayout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 754, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(96, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelGestioneConsulenzeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelGestioneConsulenzeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelGestioneConsulenzeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGestioneConsulenzeLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(jPanelGestioneConsulenzeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelGestioneConsulenzeLayout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(btnSoluzione)))
                .addGap(148, 148, 148))
        );
        jPanelGestioneConsulenzeLayout.setVerticalGroup(
            jPanelGestioneConsulenzeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGestioneConsulenzeLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelGestioneConsulenzeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelGestioneConsulenzeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGestioneConsulenzeLayout.createSequentialGroup()
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSoluzione))
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Gestione Consulenze", jPanelGestioneConsulenze);

        getContentPane().add(jTabbedPane2, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnWishlistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWishlistActionPerformed
        // TODO add your handling code here:
        int riga = tblEventi.getSelectedRow();
        if (riga == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleziona un evento dal catalogo prima di aggiungerlo alla wishlist!");
            return;
        }

        int idEvento = (int) tblEventi.getValueAt(riga, 0); 

        try {
            sistema.aggiungiInWishlist(idEvento);
            com.culturalflow.controller.FileManager.salvaTutto(sistema);

            javax.swing.JOptionPane.showMessageDialog(this, "Evento aggiunto con successo alla tua Wishlist!");
            caricaWishlist(); 

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnWishlistActionPerformed

    private void btnCercaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCercaActionPerformed
        // TODO add your handling code here:
        caricaDatiEventi(txtRicercaGlobale.getText());
    }//GEN-LAST:event_btnCercaActionPerformed

    private void btnAcquistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcquistaActionPerformed
        // TODO add your handling code here:
        try {
            int riga = tblEventi.getSelectedRow();
            if (riga == -1) {
                javax.swing.JOptionPane.showMessageDialog(this, "Seleziona un evento dalla tabella!");
                return;
            }

            int idEvento = (int) tblEventi.getValueAt(riga, 0);
            int qtaRichiesta = (int) spnQuantita.getValue();
            String inputCodice = ""; 

            com.culturalflow.model.Evento evento = sistema.selezionaEvento(idEvento);

            if (evento instanceof com.culturalflow.model.EventoPopUp) {
                com.culturalflow.model.EventoPopUp epu = (com.culturalflow.model.EventoPopUp) evento;
                java.util.Date oggi = new java.util.Date();

                if (epu.getScadenzaPriorita() != null && oggi.before(epu.getScadenzaPriorita())) {
                    inputCodice = javax.swing.JOptionPane.showInputDialog(this, 
                        "EVENTO PRIORITARIO: Inserisci il codice di accesso:");

                    if (inputCodice == null) return;
                }
            }

            sistema.acquistaBiglietto(false, "", qtaRichiesta, inputCodice);
            sistema.confermaAcquisto();

            com.culturalflow.controller.FileManager.salvaTutto(sistema);

            javax.swing.JOptionPane.showMessageDialog(this, "Acquisto riuscito!");

            caricaDatiEventi(null); 
            caricaMieiBiglietti();
            spnQuantita.setValue(1);

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore acquisto: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnAcquistaActionPerformed

    private void btnInserisciEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInserisciEventoActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(jPanelNuovoEvento);
    }//GEN-LAST:event_btnInserisciEventoActionPerformed

    private void chkIsStandardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkIsStandardActionPerformed
        // TODO add your handling code here:
        Organizzatore org = sistema.getOrganizzatoreLoggato();
        if (org != null && !org.isSponsor()) {
            chkIsStandard.setSelected(true);
        }

        boolean isStd = chkIsStandard.isSelected();
        if (isStd) {
            chkIsPopUp.setSelected(false);

            txtTipologia.setEnabled(true);
            rbServizioSi.setEnabled(true);
            rbServizioNo.setEnabled(true);

            boolean srvSì = rbServizioSi.isSelected();
            txtPrezzoServizio.setEnabled(srvSì);
            txtDettagli.setEnabled(srvSì);

            txtTarget.setEnabled(false);
            txtScadenza.setEnabled(false);
            jLabel20.setVisible(false);
        } else {
            txtTipologia.setEnabled(false);
            rbServizioSi.setEnabled(false);
            rbServizioNo.setEnabled(false);
            txtPrezzoServizio.setEnabled(false);
            txtDettagli.setEnabled(false);
        }
    }//GEN-LAST:event_chkIsStandardActionPerformed

    private void btnSalvaEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvaEventoActionPerformed
        // TODO add your handling code here:
        try {
            String nome = txtNomeEvento.getText().trim();
            String luogo = txtLuogo.getText().trim();
            float prezzo = Float.parseFloat(txtPrezzo.getText().trim());
            int disp = Integer.parseInt(txtDisp.getText().trim());

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dataEv = sdf.parse(txtdata.getText().trim());

            com.culturalflow.model.Organizzatore orgCorrente = sistema.getOrganizzatoreLoggato();

            if (chkIsPopUp.isSelected()) {
                if (!orgCorrente.isSponsor()) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Errore: Solo gli Sponsor possono creare eventi Pop-Up.");
                    return;
                }

                String target = txtTarget.getText().trim();
                java.util.Date scadenza = sdf.parse(txtScadenza.getText().trim());

                sistema.creaPopUp(orgCorrente, nome, luogo, dataEv, prezzo, disp, target, scadenza); 
            } else if (chkIsStandard.isSelected()) {
                String tipo = txtTipologia.getText().trim();

                sistema.inserisciEventoStandard(orgCorrente, nome, luogo, dataEv, prezzo, disp);
                sistema.selezionaTipologia(tipo);

                boolean srv = rbServizioSi.isSelected();
                if (srv) {
                    float pSrv = Float.parseFloat(txtPrezzoServizio.getText().trim());
                    String dett = txtDettagli.getText().trim();
                    sistema.inserisciServizioDettagli(true, pSrv, dett);
                }
            }

            if(chkIsPopUp.isSelected()){
                sistema.inviaInvitiPrioritari();
            }
            sistema.confermaEvento(); 
            com.culturalflow.controller.FileManager.salvaTutto(sistema);

            javax.swing.JOptionPane.showMessageDialog(this, "Evento inserito correttamente!");

            aggiornaTabellaOrganizzatore();
            jTabbedPane2.setSelectedComponent(jPanelGestioneEventi);
            svuotaCampiInserimento(); 

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnSalvaEventoActionPerformed

    private void txtTargetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTargetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTargetActionPerformed

    private void txtDettagliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDettagliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDettagliActionPerformed

    private void rbServizioNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbServizioNoActionPerformed
        // TODO add your handling code here:
        txtPrezzoServizio.setEnabled(false);
        txtDettagli.setEnabled(false);
        txtPrezzoServizio.setText("0.0");
        txtDettagli.setText("");
    }//GEN-LAST:event_rbServizioNoActionPerformed

    private void rbServizioSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbServizioSiActionPerformed
        // TODO add your handling code here:
        txtPrezzoServizio.setEnabled(true);
        txtDettagli.setEnabled(true);
    }//GEN-LAST:event_rbServizioSiActionPerformed

    private void txtTipologiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTipologiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipologiaActionPerformed

    private void chkIsPopUpItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkIsPopUpItemStateChanged
        // TODO add your handling code here:
        boolean isPop = chkIsPopUp.isSelected();

        if (isPop) {
            chkIsStandard.setSelected(false);

            txtTarget.setEnabled(true);
            txtScadenza.setEnabled(true);
            jLabel20.setVisible(true);

            txtTipologia.setEnabled(false);
            rbServizioSi.setEnabled(false);
            rbServizioNo.setEnabled(false);
            txtPrezzoServizio.setEnabled(false);
            txtDettagli.setEnabled(false);
        } else {
            txtTarget.setEnabled(false);
            txtScadenza.setEnabled(false);
            jLabel20.setVisible(false);
        }
    }//GEN-LAST:event_chkIsPopUpItemStateChanged

    private void txtDispActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDispActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDispActionPerformed

    private void txtLuogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLuogoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLuogoActionPerformed

    private void txtNomeEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeEventoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeEventoActionPerformed

    private void btnAcquistaWishlistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcquistaWishlistActionPerformed
        // TODO add your handling code here:
        try {
            int riga = tblWishlist.getSelectedRow();
            if (riga == -1) {
                javax.swing.JOptionPane.showMessageDialog(this, "Seleziona un evento dalla wishlist!");
                return;
            }

            int idEvento = (int) tblWishlist.getValueAt(riga, 0); 

            sistema.selezionaEvento(idEvento);
            sistema.acquistaBiglietto(false, "", 1, "");
            sistema.confermaAcquisto();
            sistema.rimuoviDaWishlist(idEvento);
            java.util.List<com.culturalflow.model.Biglietto> biglietti = sistema.getClienteCorrente().getBiglietti();
            float prezzoFinale = biglietti.get(biglietti.size() - 1).getPrezzoFinale();
            com.culturalflow.controller.FileManager.salvaTutto(sistema);

            javax.swing.JOptionPane.showMessageDialog(this, "Acquisto dalla wishlist riuscito! Pagato: " + prezzoFinale + "€");

            caricaDatiEventi(null);
            caricaMieiBiglietti();
            caricaWishlist();

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore acquisto da wishlist: " + ex.getMessage());
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnAcquistaWishlistActionPerformed

    private void btnRimuoviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRimuoviActionPerformed
        // TODO add your handling code here:
        int riga = tblWishlist.getSelectedRow();
        if (riga == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleziona un evento dalla wishlist da rimuovere!"); 
            return;
        }

        int idEvento = (int) tblWishlist.getValueAt(riga, 0);
        int conferma = javax.swing.JOptionPane.showConfirmDialog(this,
            "Vuoi davvero rimuovere questo evento dalla tua wishlist?",
            "Conferma Rimozione", javax.swing.JOptionPane.YES_NO_OPTION);

        if (conferma == javax.swing.JOptionPane.YES_OPTION) {
            try {
                sistema.rimuoviDaWishlist(idEvento); 
                com.culturalflow.controller.FileManager.salvaTutto(sistema);

                javax.swing.JOptionPane.showMessageDialog(this, "Evento rimosso con successo.");
                caricaWishlist(); 

            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Errore durante la rimozione: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnRimuoviActionPerformed

    private void btnAnnullaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnnullaActionPerformed
        // TODO add your handling code here:
        int riga = tblMieiBiglietti.getSelectedRow();
        if (riga == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleziona il biglietto da rimborsare!");
            return;
        }

        int idBiglietto = (int) tblMieiBiglietti.getValueAt(riga, 0); 
        int conferma = javax.swing.JOptionPane.showConfirmDialog(this, "Confermi la richiesta di rimborso?", "Annullamento", javax.swing.JOptionPane.YES_NO_OPTION);
        if (conferma == javax.swing.JOptionPane.YES_OPTION) {
            try {
                sistema.richiestaRimborso(idBiglietto);
                sistema.confermaRimborso(); 
                com.culturalflow.controller.FileManager.salvaTutto(sistema);

                javax.swing.JOptionPane.showMessageDialog(this, "Rimborso completato con successo.");
                caricaMieiBiglietti();
                caricaDatiEventi(null);

            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Errore rimborso: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnAnnullaActionPerformed

    private void btnRegistraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistraActionPerformed
        // TODO add your handling code here:
        try {
            String nome = txtNome.getText().trim();
            String cognome = txtCognome.getText().trim();
            String email = txtEmail.getText().trim();
            String pass = new String(txtPassword.getPassword()).trim();
            String categoria = comboCategoria.getSelectedItem().toString();

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dataNascita = sdf.parse(txtData.getText());

            sistema.inserisciDati(nome, cognome, email, pass, dataNascita, categoria);
            sistema.confermaRegistrazione();

            com.culturalflow.controller.FileManager.salvaTutto(sistema);

            javax.swing.JOptionPane.showMessageDialog(this, "Registrato con successo! Ora effettua il Login.");
            jTabbedPane2.setEnabledAt(0, true);
            jTabbedPane2.setSelectedIndex(0);
            jTabbedPane2.setEnabledAt(1, false);

        } catch (com.culturalflow.controller.ClienteGiaRegistratoException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore durante la registrazione: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnRegistraActionPerformed

    private void comboCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCategoriaActionPerformed

    private void txtDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataActionPerformed

    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeActionPerformed

    private void bntVaiRegistraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntVaiRegistraActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setEnabledAt(1, true);
        jTabbedPane2.setSelectedIndex(1);
        jTabbedPane2.setEnabledAt(0, false);
    }//GEN-LAST:event_bntVaiRegistraActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        String email = txtLoginEmail.getText().trim();
        String pass = new String(txtLoginPass.getPassword()).trim();

        if (email.isEmpty() || pass.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Inserire email e password.");
            return;
        }

        if (sistema.getClienti().containsKey(email)) {
            Cliente c = sistema.getClienti().get(email);
            if (c.getPassword().equals(pass)) {
                sistema.setClienteCorrente(c); 
                mostraInterfacciaCliente(c.getNome()); 
                return;
            }
        }

        if (sistema.getOrganizzatori().containsKey(email)) {
        com.culturalflow.model.Organizzatore org = sistema.getOrganizzatori().get(email);
        if (org.getPassword().equals(pass)) {
            sistema.setOrganizzatoreLoggato(org);
            mostraInterfacciaOrganizzatore(org.getNomeOrganizzazione());
            return;
        }
    }
        
        if (sistema.getStaff().containsKey(email)) {
            com.culturalflow.model.Staff s = sistema.getStaff().get(email);
            if (s.getPassword().equals(pass)) {
                sistema.setStaffLoggato(s);
                mostraInterfacciaStaff(s.getNome()); 
                return;
            }
        }

        javax.swing.JOptionPane.showMessageDialog(this, "Email o Password errati.");
    }//GEN-LAST:event_btnLoginActionPerformed

    private void txtLoginPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLoginPassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoginPassActionPerformed

    private void btnSalvaContestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvaContestActionPerformed
        // TODO add your handling code here:
        try {
            String nome = txtNomeContest.getText().trim();
            String desc = txtDescrizione.getText().trim();
            String premio = txtPremio.getText().trim();

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date fine = sdf.parse(txtDataFineContest.getText().trim());
            java.util.Date estrazione = sdf.parse(txtDataEstrazione.getText().trim());
            java.util.Date oggi = new java.util.Date();

            String selectedItem = cmbEventiAssociati.getSelectedItem().toString();
            int idEvento = Integer.parseInt(selectedItem.split(" - ")[0]);
            sistema.creaContest(nome, desc, premio, oggi, fine, estrazione, idEvento);
            sistema.confermaContest();
            com.culturalflow.controller.FileManager.salvaTutto(sistema);

            javax.swing.JOptionPane.showMessageDialog(this, "Concorso creato e salvato con successo!");

            aggiornaTabellaContestOrganizzatore(); 

            jTabbedPane2.setSelectedComponent(jPanelGestioneEventi);
            txtNomeContest.setText("");
            txtDescrizione.setText("");
            txtPremio.setText("");
            txtDataFineContest.setText("");
            txtDataEstrazione.setText("");
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore nella creazione del contest: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnSalvaContestActionPerformed

    private void cmbEventiAssociatiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEventiAssociatiActionPerformed
        // TODO add your handling code here:
        if (cmbEventiAssociati.getSelectedItem() == null) {
            return;
        }
        try {
            String selezionato = cmbEventiAssociati.getSelectedItem().toString();
            int idEvento = Integer.parseInt(selezionato.split(" - ")[0]);
            System.out.println("Evento selezionato per il concorso: ID " + idEvento);
        } catch (Exception e) {
            System.err.println("Errore nel parsing dell'evento selezionato: " + e.getMessage());
        }
    }//GEN-LAST:event_cmbEventiAssociatiActionPerformed

    private void btnEstraiVincitoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstraiVincitoreActionPerformed
        // TODO add your handling code here:     
        int riga = tblContestOrganizzatore.getSelectedRow();
        if (riga == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleziona un contest da chiudere!");
            return;
        }
        int idContest = (int) tblContestOrganizzatore.getValueAt(riga, 0);

        try {
            sistema.eseguiEstrazioneContest(idContest);
            com.culturalflow.model.Contest c = sistema.getEventi().get(idContest) != null ? null : sistema.getContestCorrente(); 
            com.culturalflow.controller.FileManager.salvaTutto(sistema);

            String vincitoreEmail = sistema.getContestCorrente().getVincitore().getEmail();
            String voucher = sistema.getContestCorrente().getVoucher();

            javax.swing.JOptionPane.showMessageDialog(this, "Estrazione completata!\nVincitore: " + vincitoreEmail + "\nVoucher: " + voucher);
            aggiornaTabellaContestOrganizzatore();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore durante l'estrazione: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnEstraiVincitoreActionPerformed

    private void btnPartecipaContestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPartecipaContestActionPerformed
        // TODO add your handling code here:
        int riga = tblContestDisponibili.getSelectedRow();
        if (riga == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleziona un concorso dalla lista!");
            return;
        }
        int idContest = (int) tblContestDisponibili.getValueAt(riga, 0);

        try {
            sistema.selezionaContest(idContest);
            sistema.iscrizioneContest();

            com.culturalflow.controller.FileManager.salvaTutto(sistema);
            javax.swing.JOptionPane.showMessageDialog(this, "Iscrizione completata con successo!");

            aggiornaTabellaMieiContest();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnPartecipaContestActionPerformed

    private void btnSoluzioneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSoluzioneActionPerformed
        // TODO add your handling code here:
        String soluzione = txtSoluzione.getText().trim();

        if (soluzione.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Inserire una soluzione prima di inviare.");
            return;
        }

        try {
            com.culturalflow.model.Consulenza c = sistema.getConsulenzaCorrente();

            if (c == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Errore: Seleziona prima una consulenza dalla tabella!");
                return;
            }
            sistema.inviaSoluzione(soluzione);
            com.culturalflow.controller.FileManager.salvaTutto(sistema);

            javax.swing.JOptionPane.showMessageDialog(this, "Soluzione inviata con successo!");

            caricaTutteLeConsulenze(); 

            txtSoluzione.setText("");
            txtOggetto.setText("");
            lblNome.setText("Nome: ");
            lblCognome.setText("Cognome: ");

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore durante l'invio: " + ex.getMessage());
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnSoluzioneActionPerformed

    private void bntInviaRichiestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntInviaRichiestaActionPerformed
        // TODO add your handling code here:
        String oggetto = txtOggettoRichiesta.getText().trim();
        String descrizione = txtDescrizioneRichiesta.getText().trim();

        if (oggetto.isEmpty() || descrizione.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Compilare tutti i campi.");
            return;
        }

        try {
            sistema.richiediConsulenza(oggetto, descrizione);
            com.culturalflow.controller.FileManager.salvaTutto(sistema);

            javax.swing.JOptionPane.showMessageDialog(this, "Richiesta di consulenza inviata con successo!");

            txtOggettoRichiesta.setText("");
            txtDescrizioneRichiesta.setText("");
            caricaMieConsulenze(); 
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }//GEN-LAST:event_bntInviaRichiestaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntInviaRichiesta;
    private javax.swing.JButton bntVaiRegistra;
    private javax.swing.JButton btnAcquista;
    private javax.swing.JButton btnAcquistaWishlist;
    private javax.swing.JButton btnAnnulla;
    private javax.swing.JButton btnCerca;
    private javax.swing.JButton btnEstraiVincitore;
    private javax.swing.JButton btnInserisciEvento;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnPartecipaContest;
    private javax.swing.JButton btnRegistra;
    private javax.swing.JButton btnRimuovi;
    private javax.swing.JButton btnSalvaContest;
    private javax.swing.JButton btnSalvaEvento;
    private javax.swing.JButton btnSoluzione;
    private javax.swing.JButton btnWishlist;
    private javax.swing.JCheckBox chkIsPopUp;
    private javax.swing.JCheckBox chkIsStandard;
    private javax.swing.JComboBox<String> cmbEventiAssociati;
    private javax.swing.JComboBox<String> comboCategoria;
    private javax.swing.ButtonGroup groupServizio;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelGestioneConsulenze;
    private javax.swing.JPanel jPanelGestioneEventi;
    private javax.swing.JPanel jPanelLogin;
    private javax.swing.JPanel jPanelNuovoContest;
    private javax.swing.JPanel jPanelNuovoEvento;
    private javax.swing.JPanel jPanelRegistrazione;
    private javax.swing.JPanel jPanelRicercaEventi;
    private javax.swing.JScrollPane jScollPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lblCategoria;
    private javax.swing.JLabel lblCognome;
    private javax.swing.JLabel lblNome;
    private javax.swing.JRadioButton rbServizioNo;
    private javax.swing.JRadioButton rbServizioSi;
    private javax.swing.JSpinner spnQuantita;
    private javax.swing.JTable tblConsulenzeStaff;
    private javax.swing.JTable tblContestDisponibili;
    private javax.swing.JTable tblContestOrganizzatore;
    private javax.swing.JTable tblEventi;
    private javax.swing.JTable tblEventiOrganizzatore;
    private javax.swing.JTable tblInviti;
    private javax.swing.JTable tblMieConsulenze;
    private javax.swing.JTable tblMieiBiglietti;
    private javax.swing.JTable tblMieiContest;
    private javax.swing.JTable tblWishlist;
    private javax.swing.JTextField txtCognome;
    private javax.swing.JTextField txtData;
    private javax.swing.JTextField txtDataEstrazione;
    private javax.swing.JTextField txtDataFineContest;
    private javax.swing.JTextField txtDescrizione;
    private javax.swing.JTextArea txtDescrizioneRichiesta;
    private javax.swing.JTextField txtDettagli;
    private javax.swing.JTextField txtDisp;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtLoginEmail;
    private javax.swing.JPasswordField txtLoginPass;
    private javax.swing.JTextField txtLuogo;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtNomeContest;
    private javax.swing.JTextField txtNomeEvento;
    private javax.swing.JTextArea txtOggetto;
    private javax.swing.JTextArea txtOggettoRichiesta;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPremio;
    private javax.swing.JTextField txtPrezzo;
    private javax.swing.JTextField txtPrezzoServizio;
    private javax.swing.JTextField txtRicercaGlobale;
    private javax.swing.JTextField txtScadenza;
    private javax.swing.JTextArea txtSoluzione;
    private javax.swing.JTextField txtTarget;
    private javax.swing.JTextField txtTipologia;
    private javax.swing.JTextField txtdata;
    // End of variables declaration//GEN-END:variables
}
