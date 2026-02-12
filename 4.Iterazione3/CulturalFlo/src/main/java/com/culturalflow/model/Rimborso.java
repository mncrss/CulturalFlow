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
    
    public Rimborso(float importo) {
        this.dataRichiesta = new Date();
        this.importo = importo;
    }

    public Date getDataRichiesta() { 
        return dataRichiesta; 
    }
    
    public float getImporto() { 
        return importo; 
    }
}
