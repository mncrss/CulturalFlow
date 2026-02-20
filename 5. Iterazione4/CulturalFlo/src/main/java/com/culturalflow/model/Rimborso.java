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
public class Rimborso {
    private Date dataRichiesta;
    private float importo; 
    private String stato;
    private Biglietto biglietto;
    
    public Rimborso(float importo, Biglietto biglietto) {
        this.dataRichiesta = new Date();
        this.importo = importo;
        this.stato = "Valido";
        this.biglietto = biglietto;
    }

    public Date getDataRichiesta() { 
        return dataRichiesta; 
    }
    
    public float getImporto() { 
        return importo; 
    }
    
    public String getStato(){
        return stato;
    }
    
    public Biglietto getBiglietto(){
        return biglietto;
    }
    
    public void setDataRichiesta(Date dataRichiesta){
        this.dataRichiesta = dataRichiesta;
    }
    
    public void setStato(){
        this.stato = "Rimborsato";
    }
}
