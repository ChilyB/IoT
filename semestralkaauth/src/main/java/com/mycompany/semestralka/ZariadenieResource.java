/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semestralka;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
/**
 * Trieda obsahujúca metódy na obsluhu komunikácie s mySQL serverom s cestou "/zariadenie".
 * 
 * 
 */
@PermitAll
@Path("/zariadenie")
@Produces(MediaType.APPLICATION_JSON)
public class ZariadenieResource {
     /**
      * Metóda GET, slúžiaca na získanie JSON dát z mySQL databázy, je možné vybrať zariadenia s daným názvom alebo užívateľom.
     * @param user objekt používateľa použitého pri prihlásení
      * @param nazov názov zariadenia
      * @param idUzivatela id užívateľa, ktorému zariadenie patrí
      * @return vracia list zariadení podľa zadaných parametrov
      */
    @GET
    @Timed
    public List<Zariadenie> getZariadenie(@Auth User user, @QueryParam("nazov") Optional<String> nazov,@QueryParam("idUzivatela") Optional<String> idUzivatela) {
        
        Session session = ServerApp.buildSessionFactory.openSession();
        Transaction beginTransaction = session.beginTransaction();
        Query query;
        if(nazov.isPresent() && !idUzivatela.isPresent()){
        query = session.createQuery("from Zariadenie where nazov = \'"+nazov.get()+"\' ");   
        //query.setParameter("nazov",nazov.get());
        //query = session.createQuery("from Data where nazov = 'teplota' ");   
        }else if(idUzivatela.isPresent() && !nazov.isPresent()){
        query = session.createQuery("from Zariadenie where idUzivatela = \'"+idUzivatela.get()+"\' ");   
        }else if(idUzivatela.isPresent() && nazov.isPresent()){
        query = session.createQuery("from Zariadenie where nazov = \'"+nazov.get()+"\' and idUzivatela = \'"+idUzivatela.get()+"\' ");   
        }else{
        query = session.createQuery("from Zariadenie");
        }
        List<Zariadenie> list = query.list();

        session.getTransaction().commit();
        session.close();
        return list;
    }
    /**
     * Metóda GET, slúžiaca na získanie JSON dát z mySQL databázy podľa id zariadenia.
     * @param user objekt používateľa použitého pri prihlásení
     * @param id id zariadenia, ktorého údaje chceme zobraziť
     * @return vracia jedno zariadenie s daným id
     */
    @GET
    @Path("/{id}")
    @Timed
    public Zariadenie getZariadenie(@Auth User user, @PathParam("id") String id) {
        Session session = ServerApp.buildSessionFactory.openSession();
        Transaction beginTransaction = session.beginTransaction();

        Query query = session.createQuery("from Zariadenie where id=" + id);
        Zariadenie uniqueResult = (Zariadenie) query.uniqueResult();
        

        session.getTransaction().commit();
        session.close();
        if (uniqueResult == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
         }

        return uniqueResult;
    }
    /**
     * Metóda POST na odoslanie JSON dát do databázy.
     * @param user objekt používateľa použitého pri prihlásení
     * @param input vstupom je objekt dátovej triedy Zariadenie
     * @return vracia daný objekt po jeho odoslaní
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Zariadenie postData(@Auth User user, Zariadenie input) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        session.save(input);
        session.getTransaction().commit();
        session.close();
        return input;
    }
    /**
     * Metóda PUT na update dát, ktoré už sú v databáze. 
     * @param user objekt používateľa použitého pri prihlásení
     * @param input Objekt dátovej triedy Zariadenie. Data, ktoré majú byť prepísané záležia od id
     * @return vracia odoslaný objekt
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Zariadenie putData(@Auth User user, Zariadenie input) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();   
        session.update(input);
        session.getTransaction().commit();
        session.close();
        return input;
    }
    /**
     * Metóda DELETE slúžiaca na vymazanie zariadenia z databázy, id sa zadáva za linkom.
     * @param user objekt používateľa použitého pri prihlásení
     * @param id id zariadenia, ktoré má byť vymazané
     * @return vracia vymazané dáta
     */
    @DELETE
    @Path("/{id}")
    public Zariadenie DeleteData(@Auth User user, @PathParam("id") String id) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Data where id=" + id);
        Zariadenie uniqueResult = (Zariadenie) query.uniqueResult();
        
        session.getTransaction().commit();
        if (uniqueResult == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
        session.beginTransaction();
        session.delete(uniqueResult);
        session.getTransaction().commit();
        session.close();
        return uniqueResult;
    }
    /**
     * Metóda DELETE slúžiaca na vymazanie zariadenia z databázy, id je obsiahnuté v objekte.
     * @param user objekt používateľa použitého pri prihlásení
     * @param input objekt triedy Zariadenie, ktorý má byť vymazaný
     * @return vracia vymazané dáta
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Zariadenie DeleteData(@Auth User user, Zariadenie input) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();       
        session.delete(input);     
        session.getTransaction().commit();
        session.close();
        return input;
    }
}
