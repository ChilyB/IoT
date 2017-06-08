/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semestralka;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import java.util.Optional;




/**
 * Trieda obsahujúca metódu pre autentifikáciu užívateľa
 * 
 */


public class BasicAuthenticator  implements Authenticator<BasicCredentials, User>{

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
       if ("secret".equals(credentials.getPassword())) {
            return Optional.of(new User(credentials.getUsername()) {});
        }
        return Optional.empty();
    }

}
