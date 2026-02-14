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
public class ScontoSeniorTest {
    @Test
    public void testApplicaScontoSenior() {
        ScontoStrategy strategy = new ScontoSenior();
        
        float prezzoBase = 50.0f;
        float prezzoServizio = 12.0f;
        
        float risultato = strategy.applicaSconto(prezzoBase, prezzoServizio);
        
        assertEquals("Per i Senior il prezzo finale deve corrispondere solo al prezzo base", 50.0f, risultato, 0.001);
    }
}
