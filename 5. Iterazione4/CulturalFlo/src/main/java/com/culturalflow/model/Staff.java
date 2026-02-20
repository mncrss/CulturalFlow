/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.culturalflow.model;

/**
 *
 * @author monic
 */
public class Staff {
    private int idStaff;
    private String nome;
    private String email;
    private String password;

    public Staff(int idStaff, String nome, String email, String password) {
        this.idStaff = idStaff;
        this.nome = nome;
        this.email = email;
        this.password = password;
    }

    public int getIdStaff() { 
        return idStaff; 
    }
    
    public String getNome() { 
        return nome; 
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getPassword(){
        return password;
    }
}
