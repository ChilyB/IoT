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
 * Trieda obsahujúca metódy na obsluhu komunikácie s mySQL serverom s cestou "/uzivatel".
 * 
 * 
 */
@Path("/uzivatel")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
public class UzivatelResource {
    /**
     * Metóda GET, slúžiaca na získanie JSON dát z mySQL databázy, je možné vybrať užívateľov s daným menom a priezviskom.
     * @param user objekt používateľa použitého pri prihlásení
     * @param meno meno uźívateľa
     * @param priezvisko priezvisko užívateľa
     * @return vracia list užívateľov podľa zadaných parametrov
     */
    @GET
    @Timed
    public List<Uzivatel> getZariadenie(@Auth User user, @QueryParam("meno") Optional<String> meno,@QueryParam("priezvisko") Optional<String> priezvisko) {
        
        Session session = ServerApp.buildSessionFactory.openSession();
        Transaction beginTransaction = session.beginTransaction();
        Query query;
        if(meno.isPresent() && !priezvisko.isPresent()){
        query = session.createQuery("from Uzivatel where meno = \'"+meno.get()+"\' ");   
        //query.setParameter("nazov",nazov.get());
        //query = session.createQuery("from Data where nazov = 'teplota' ");   
        }else if(priezvisko.isPresent() && !meno.isPresent()){
        query = session.createQuery("from Uzivatel where priezvisko = \'"+priezvisko.get()+"\' ");   
        }else if(priezvisko.isPresent() && meno.isPresent()){
        query = session.createQuery("from Uzivatel where meno = \'"+meno.get()+"\' and priezvisko = \'"+priezvisko.get()+"\' ");   
        }else{
        query = session.createQuery("from Uzivatel");
        }
        List<Uzivatel> list = query.list();

        session.getTransaction().commit();
        session.close();
        return list;
    }
    /**
     * Metóda GET, slúžiaca na získanie JSON dát z mySQL databázy podľa id užívateľa.
     * @param user objekt používateľa použitého pri prihlásení
     * @param id id užívateľa
     * @return vracia objekt Triedy uzivatel
     */
    @GET
    @Path("/{id}")
    @Timed
    public Uzivatel getZariadenie(@Auth User user, @PathParam("id") String id) {
        Session session = ServerApp.buildSessionFactory.openSession();
        Transaction beginTransaction = session.beginTransaction();

        Query query = session.createQuery("from Uzivatel where id=" + id);
        Uzivatel uniqueResult = (Uzivatel) query.uniqueResult();
        

        session.getTransaction().commit();
        session.close();
        if (uniqueResult == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
         }

        return uniqueResult;
    }
    /**
     *  Metóda POST na odoslanie JSON dát do databázy.
     * @param user objekt používateľa použitého pri prihlásení
     * @param input vstupom je objekt dátovej triedy Uzivatel
     * @return vracia daný objekt po jeho odoslaní
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uzivatel postData(@Auth User user, Uzivatel input) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        session.save(input);
        session.getTransaction().commit();
        session.close();
        return input;
    }
    /**
     ** Metóda PUT na update dát, ktoré už sú v databáze. 
     * @param user objekt používateľa použitého pri prihlásení
     * @param input Objekt dátovej triedy Uzivatel. Data, ktoré majú byť prepísané záležia od id
     * @return vracia odoslaný objekt
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Uzivatel putData(@Auth User user, Uzivatel input) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();   
        session.update(input);
        session.getTransaction().commit();
        session.close();
        return input;
    }
    /**
     * Metóda DELETE slúžiaca na vymazanie uživateľa z databázy, id sa zadáva za linkom.
     * @param user objekt používateľa použitého pri prihlásení
     * @param id id užívateľa, ktorý má byť vymazaný
     * @return vracia vymazané dáta
     */
    @DELETE
    @Path("/{id}")
    public Uzivatel DeleteData(@Auth User user, @PathParam("id") String id) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Uzivatel where id=" + id);
        Uzivatel uniqueResult = (Uzivatel) query.uniqueResult();
        
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
     * 
     * Metóda DELETE slúžiaca na vymazanie užívateľa z databázy, id je obsiahnuté v objekte.
     * @param user objekt používateľa použitého pri prihlásení
     * @param input objekt triedy Uzivatel, ktorý má byť vymazaný
     * @return vracia vymazané dáta
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Uzivatel DeleteData(@Auth User user, Uzivatel input) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();       
        session.delete(input);     
        session.getTransaction().commit();
        session.close();
        return input;
    }
}
