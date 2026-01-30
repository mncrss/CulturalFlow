/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

import static org.junit.Assert.*;
import org.junit.Test;
/**
 *
 * @author monic
 */
public class ScontoStandardTest {
    @Test
    public void testApplicaScontoStandard() {
        ScontoStrategy strategy = new ScontoStandard();
        float prezzoBase = 50.0f;
        float prezzoServizio = 5.0f;
        
        float prezzoScontato = strategy.applicaSconto(prezzoBase, prezzoServizio);
        
        assertEquals("Lo sconto standard non deve modificare il prezzo", 55.0f, prezzoScontato, 0.001);
    }
}
