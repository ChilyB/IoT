/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semestralka;

import io.dropwizard.auth.Authorizer;

/**
 *  Trieda obsahujúca rolu a meno pre autorizáciu
 * 
 */
public class BasicAuthorizer implements Authorizer<User> {
    
    @Override
    public boolean authorize(User user, String role) {
        return user.getName().equals("admin") && role.equals("ADMIN");
    }
}
