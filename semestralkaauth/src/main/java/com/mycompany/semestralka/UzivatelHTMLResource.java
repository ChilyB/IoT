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
 * Trieda obsahujúca metódy na obsluhu komunikácie s mySQL serverom s cestou "/HTML/uzivatel".
 * Slúži na zobrazovanie HTML stránky.
 * 
 */
@Path("/HTML/uzivatel")
@Produces(MediaType.TEXT_HTML)
public class UzivatelHTMLResource {
    /**
     * Metóda GET na získanie dát zo servera podľa zadaných parametrov.
     * @param user objekt používateľa použitého pri prihlásení
     * @param meno meno užívateľa
     * @param priezvisko priezvisko užívateľa
     * @return vracia objekt dátovej triedy, ktorá obsahuje list užívateľov
     */
    @GET
    @PermitAll
    @Timed
    public UzivateliaListView getZariadenie(@Auth User user, @QueryParam("meno") Optional<String> meno,@QueryParam("priezvisko") Optional<String> priezvisko) {
        
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
        UzivateliaListView uzivateliaview = new UzivateliaListView(list);
        return uzivateliaview;
    }
    /**
     * Metóda, ktorá pri jej zavolaní vytvorí v databáze nového užívateľa so zadaným menom a priezviskom
     * @param user objekt používateľa použitého pri prihlásení
     * @param meno meno užívateľa
     * @param priezvisko priezvisko užívateľa
     * @return vracia objekt dátovej triedy, ktorá obsahuje list užívateľov
     */
    @GET
    @PermitAll
    @Path("/new")
    @Timed
    public UzivateliaListView createZariadenie(@Auth User user, @QueryParam("meno") Optional<String> meno,@QueryParam("priezvisko") Optional<String> priezvisko) {
        
        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        if(meno.isPresent() && priezvisko.isPresent()){
            Uzivatel temp = new Uzivatel(1, meno.get(), priezvisko.get());
            session.save(temp);
        }
        Query query;
        query = session.createQuery("from Uzivatel");
        List<Uzivatel> list = query.list();
        session.getTransaction().commit();
        session.close();

      UzivateliaListView uzivateliaview = new UzivateliaListView(list);
        return uzivateliaview;
    }
    /**
     * Metóda vymaže Užívateľa a všetky zariadenia a dáta, ktoré mu patria.
     * @param user objekt používateľa použitého pri prihlásení
     * @param id id uźívateľa, ktorý má byť vymazaný
     * @return vracia objekt dátovej triedy, ktorá obsahuje list užívateľov
     */
    @GET
    @PermitAll
    @Path("/delete")
    public UzivateliaListView DeleteUzivatela(@Auth User user, @QueryParam("id") Optional<String> id) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Uzivatel where id = \'" + id.get()+"\' ");
        
        Uzivatel uniqueResult = (Uzivatel) query.uniqueResult();
        session.getTransaction().commit();
        session.beginTransaction();
        Query query1 = session.createQuery("from Zariadenie where idUzivatela = \'"+id.get()+"\' ");
        List<Zariadenie> zariadenia = query1.list();
        Query query2;
        for(Zariadenie zariadenie : zariadenia){
            query2 = session.createQuery("delete from Data where idZariadenia = \'"+ zariadenie.getId()+"\' ");
            query2.executeUpdate();
        }
        query2 = session.createQuery("delete from Zariadenie where idUzivatela = \'"+id.get()+"\' ");
        query2.executeUpdate();
        
        session.getTransaction().commit();
        
        session.beginTransaction();
        session.delete(uniqueResult);
        session.getTransaction().commit();
        
        session.beginTransaction();
        
        Query querylist = session.createQuery("from Uzivatel");
        List<Uzivatel> uzivatelialist = querylist.list();
        session.getTransaction().commit();
        session.close();
        
        return new UzivateliaListView(uzivatelialist);
    }
   
}
