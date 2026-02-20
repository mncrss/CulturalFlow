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
public class Sconto25Test {
    @Test
    public void testApplicaSconto25() {
        ScontoStrategy strategy = new Sconto25();
        float prezzoBase = 90.0f;
        float prezzoServizio = 10.0f;
        
        float prezzoScontato = strategy.applicaSconto(prezzoBase, prezzoServizio);
        
        assertEquals("Lo sconto del 25% su 100 deve restituire 75.0", 75.0f, prezzoScontato, 0.001);
    }
}
