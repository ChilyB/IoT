/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semestralka;

import com.codahale.metrics.health.HealthCheck;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
/**
 * 
 * Objekty triedy slúžia na kontrolu či sú tabuľky v databáze prázdne
 */
public class TableHealthCheck extends HealthCheck {

    private String tabulka;

    public TableHealthCheck(String tabulka) {
        this.tabulka = tabulka;
                
    }

    @Override
    protected Result check(){
        
        
        Session session = ServerApp.buildSessionFactory.openSession();
        Transaction beginTransaction = session.beginTransaction();
        Query query;
        query = session.createQuery("from "+tabulka); 
        List<Object> list = query.list();
        session.getTransaction().commit();
        session.close();
        System.out.println(list.isEmpty());
        
        if (list.isEmpty()) {
             return Result.unhealthy("tabulka "+tabulka+" je prazdna");
        }
        
               
         return Result.healthy();
        
    }
}
