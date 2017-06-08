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
 * Trieda obsahujúca metódy na obsluhu komunikácie s mySQL serverom s cestou "/HTML/zariadenie".
 * Slúži na zobrazovanie HTML stránky.
 * 
 */
@Path("/HTML/zariadenie")
@Produces(MediaType.TEXT_HTML)
public class ZariadenieHTMLResource {
     /**
      * Metóda GET na získanie dát zo servera podľa zadaných parametrov.
     * @param user objekt používateľa použitého pri prihlásení
      * @param nazov názov zariadenia
      * @param idUzivatela id užívateľa, ktorému zariadenie patrí
      * @return vracia objekt dátovej triedy obsahujúci list zariadení
      */
    @GET
    @PermitAll
    @Timed
    public ZariadeniaListView getZariadenie(@Auth User user, @QueryParam("nazov") Optional<String> nazov,@QueryParam("idUzivatela") Optional<String> idUzivatela) {
        
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
        ZariadeniaListView zariadenieview = new ZariadeniaListView(list);
        return zariadenieview;
    }
    /**
     * Metóda vytvorý zariadenie obsahujúce dáta v parametroch
     * @param user objekt používateľa použitého pri prihlásení
     * @param nazov názov zariadenia
     * @param idUzivatela id užívaťeľa, ktorému zariadenie patrí
     * @return vracia dátový objekt, ktorý obsahuje list zariadení
     */
    @GET
    @PermitAll
    @Path("/new")
    @Timed
    public ZariadeniaListView createZariadenie(@Auth User user, @QueryParam("nazov") Optional<String> nazov,@QueryParam("idUzivatela") Optional<String> idUzivatela) {
        
        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        if(nazov.isPresent() && idUzivatela.isPresent()){
            Zariadenie temp = new Zariadenie(1, Integer.parseInt(idUzivatela.get()), nazov.get());
            session.save(temp);
        }
        Query query;
        query = session.createQuery("from Zariadenie where idUzivatela = \'"+idUzivatela.get()+"\' ");
         List<Zariadenie> list = query.list();

        session.getTransaction().commit();
        session.close();
        ZariadeniaListView zariadenieview = new ZariadeniaListView(list);
        return zariadenieview;
    }
    /**
     * Metóda vymaže zariadenie a jeho dáta podľa zadaného id
     * @param user objekt používateľa použitého pri prihlásení
     * @param id id zariadenia, ktoré má byť vymazané
     * @return vracia objekt dátovej triedy, ktorá obsahuje list zariadení
     */
    @GET
    @PermitAll
    @Path("/delete")
    public ZariadeniaListView DeleteZariadenie(@Auth User user, @QueryParam("id") Optional<String> id) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Zariadenie where id=" + id.get());
        Zariadenie uniqueResult = (Zariadenie) query.uniqueResult();
        session.getTransaction().commit();
        session.beginTransaction();
        Query query2;
        query2 = session.createQuery("delete from Data where idZariadenia = \'"+id.get()+"\' ");   
        query2.executeUpdate();
        session.getTransaction().commit();
        
        if (uniqueResult == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
        
        
        session.beginTransaction();
        session.delete(uniqueResult);
        session.getTransaction().commit();
        session.beginTransaction();
        Query query3;
        query3 = session.createQuery("from Zariadenie where idUzivatela = \'"+uniqueResult.getIdUzivatela()+"\' ");   
        List<Zariadenie> listzar = query3.list();
        
        session.getTransaction().commit();
        session.close();
        return new ZariadeniaListView(listzar);
    }
    
    /*
    @GET
    @Path("/new/{idUzivatela}")
    @Timed
    public ZariadeniaListView createZariadenie(@PathParam("id") String idUzivatela, @QueryParam("nazov") Optional<String> nazov) {
        
        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        if(nazov.isPresent()){
            Zariadenie temp = new Zariadenie(1, Integer.parseInt(idUzivatela), nazov.get());
            session.save(temp);
        }
        Query query;
        query = session.createQuery("from Zariadenie");
         List<Zariadenie> list = query.list();

        session.getTransaction().commit();
        session.close();
        ZariadeniaListView zariadenieview = new ZariadeniaListView(list);
        return zariadenieview;
    }
*/

}
