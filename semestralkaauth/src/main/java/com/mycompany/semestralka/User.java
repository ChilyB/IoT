/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semestralka;

import java.security.Principal;



/**
 *  Dátová trieda obsahujúca údaje používateľa
 * 
 */
class User implements Principal {
    private String name = "admin";
    
    
    public User(String name){
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }
    
}
