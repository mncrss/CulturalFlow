/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

/**
 *
 * @author monic
 */
public class ScontoSenior implements ScontoStrategy {
    @Override
    public float applicaSconto(float pb, float ps) {
        return pb; 
    }
}
