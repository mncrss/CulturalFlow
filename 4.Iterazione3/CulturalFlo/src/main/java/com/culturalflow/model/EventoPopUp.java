/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

import java.util.Date;

/**
 *
 * @author monic
 */
public class EventoPopUp extends Evento{
    private String codiceAccesso;
    private Date scadenzaPriorita;
    private String target;
    private boolean isPublic;
    
    public EventoPopUp() {
        super(); 
        this.isPublic = false;
    }
    
    public String getCodiceAccesso() {
        return codiceAccesso;
    }

    public void setCodiceAccesso(String codiceAccesso) {
        this.codiceAccesso = codiceAccesso;
    }

    public Date getScadenzaPriorita() {
        return scadenzaPriorita;
    }

    public void setScadenzaPriorita(Date scadenzaPriorita) {
        this.scadenzaPriorita = scadenzaPriorita;
    }
    
    public String getTarget() {
        return target;
    }

    @Override
    public String getTipologia() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    @Override
    public boolean isAcquistabile(String codiceInserito) {
        if (!super.isAcquistabile(codiceInserito)) {
            return false;
        }

        Date oggi = new Date();
        if (oggi.after(this.scadenzaPriorita)) {
            this.isPublic = true;
            return true;
        }
        
        if (this.isPublic) {
            return true;
        }
        
        if (codiceInserito != null && codiceInserito.equals(this.codiceAccesso)) {
            return true;
        }

        return false;
    }
}
