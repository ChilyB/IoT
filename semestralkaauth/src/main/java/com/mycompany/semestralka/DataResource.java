/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semestralka;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import java.util.Date;
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
 * 
 * rieda obsahujúca metódy na obsluhu komunikácie s mySQL serverom s cestou "/data".
 */
@Path("/data")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
public class DataResource {
    
    /**
     * Metóda GET, slúžiaca na získanie JSON dát z mySQL databázy, je možné vybrať data s daným názvom a podľa zariadenia.
     * @param user objekt používateľa použitého pri prihlásení
     * @param nazov názov veličiny dát
     * @param idZariadenia id zariadenia, ktorému dáta patria
     * @return vracia list objektov triedy Data
     */
    @GET 
    @Timed
    public List<Data> getData(@Auth User user, @QueryParam("nazov") Optional<String> nazov,@QueryParam("idZariadenia") Optional<String> idZariadenia) {
        
        Session session = ServerApp.buildSessionFactory.openSession();
        Transaction beginTransaction = session.beginTransaction();
        Query query;
        if(nazov.isPresent() && !idZariadenia.isPresent()){
        query = session.createQuery("from Data where nazov = \'"+nazov.get()+"\' ");   
        //query.setParameter("nazov",nazov.get());
        //query = session.createQuery("from Data where nazov = 'teplota' ");   
        }else if(idZariadenia.isPresent() && !nazov.isPresent()){
        query = session.createQuery("from Data where idZariadenia = \'"+idZariadenia.get()+"\' ");   
        }else if(idZariadenia.isPresent() && nazov.isPresent()){
        query = session.createQuery("from Data where nazov = \'"+nazov.get()+"\' and idZariadenia = \'"+idZariadenia.get()+"\' ");   
        }else{
        query = session.createQuery("from Data");
        }
        List<Data> list = query.list();

        session.getTransaction().commit();
        session.close();
        return list;
    }
    /**
     * Metóda GET, slúžiaca na získanie JSON dát z mySQL databázy podľa id dát.
     * @param user objekt používateľa použitého pri prihlásení
     * @param id dát v databáze
     * @return vracia objekt triedy Data
     */
    @GET
    @Path("/{id}")
    @Timed
    public Data getData(@Auth User user, @PathParam("id") String id) {
        Session session = ServerApp.buildSessionFactory.openSession();
        Transaction beginTransaction = session.beginTransaction();

        Query query = session.createQuery("from Data where id=" + id);
        Data uniqueResult = (Data) query.uniqueResult();
        

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
     * @param input objekt dátovej triedy Data
     * @return vrati objekt, ktorý bol odoslaný
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Data postData(@Auth User user, Data input) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        input.setDatum(new Date(System.currentTimeMillis()).toString());
        session.save(input);
        session.getTransaction().commit();
        session.close();
        return input;
    }
    /**
     * Metóda PUT na update dát, ktoré už sú v databáze. 
     * @param user objekt používateľa použitého pri prihlásení
     * @param input Objekt dátovej triedy Data. Data, ktoré majú byť prepísané záležia od id
     * @return vracia objekt, ktorý bol odoslaný
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Data putData(@Auth User user, Data input) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();   
        input.setDatum(new Date(System.currentTimeMillis()).toString());
        session.update(input);
        session.getTransaction().commit();
        session.close();
        return input;
    }
    /**
     * Metóda DELETE slúžiaca na vymazanie dát z databázy, id sa zadáva za linkom.
     * @param user objekt používateľa použitého pri prihlásení
     * @param id id dátľa, ktoré majú byť vymazaný
     * @return vracia vymazané dáta
     */
    @DELETE
    @Path("/{id}")
    public Data DeleteData(@Auth User user, @PathParam("id") String id) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Data where id=" + id);
        Data uniqueResult = (Data) query.uniqueResult();
        
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
     * Metóda DELETE slúžiaca na vymazanie dát z databázy, id je obsiahnuté v objekte.
     * @param user objekt používateľa použitého pri prihlásení
     * @param input objekt triedy Data, ktorý má byť vymazaný
     * @return vracia vymazané dáta
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Data DeleteData(@Auth User user, Data input) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();       
        session.delete(input);     
        session.getTransaction().commit();
        session.close();
        return input;
    }
    
}
