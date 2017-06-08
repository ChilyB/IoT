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
 * Trieda obsahujúca metódy na obsluhu komunikácie s mySQL serverom s cestou "/HTML/data".
 * Slúži na zobrazovanie HTML stránky.
 * 
 */
@Path("/HTML/data")
@Produces(MediaType.TEXT_HTML)
public class DataHTMLResource {
    
    /**
     * Metóda GET na získanie dát zo servera podľa zadaných parametrov.
     * @param user objekt používateľa použitého pri prihlásení
     * @param nazov názov uložených dát
     * @param idZariadenia id zariadenia, ktorému dáta patria
     * @return vracia objekt dátovej triedy, ktorá obsahuje list dát
     */
    @GET
    @PermitAll
    @Timed
    public DataListView getData(@Auth User user, @QueryParam("nazov") Optional<String> nazov,@QueryParam("idZariadenia") Optional<String> idZariadenia) {
        
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
        session.beginTransaction();
        
        //Query query2 = session.createQuery("from Zariadenie where id = "+list.get(0).getIdZariadenia()+"");
        
        Query query2 = session.createQuery("from Zariadenie where id = "+idZariadenia.get()+"");
        Zariadenie uniqueResult = (Zariadenie) query2.uniqueResult();
        
        session.getTransaction().commit();
        session.close();
        
        int idUzivatela = uniqueResult.getIdUzivatela();
        DataListView dataview = new DataListView(list,idUzivatela);
        
        
        return dataview;
    }
    /**
     * metóda vytvorí nové dáta, s údajmi, ktoré sú v parametroch
     * @param user objekt používateľa použitého pri prihlásení
     * @param idZariadenia id zariadenia, ktorému patria dáta
     * @param nazov názov dát
     * @param hodnota hodnota dát
     * @param jednotka jednotka, v ktorej sú dáta
     * @return vracia objekt dátovej triedy, ktorá obsahuje list dát
     */
    @GET
    @PermitAll
    @Path("/new")
    @Timed
    public DataListView createZariadenie(@Auth User user, @QueryParam("idZariadenia") Optional<String> idZariadenia, @QueryParam("nazov") Optional<String> nazov,@QueryParam("hodnota") Optional<String> hodnota,@QueryParam("jednotka") Optional<String> jednotka) {
        
        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        if(nazov.isPresent() && idZariadenia.isPresent() && hodnota.isPresent() && jednotka.isPresent()){
            Data temp = new Data(1, Integer.parseInt(idZariadenia.get()), nazov.get(), Double.parseDouble(hodnota.get()), jednotka.get(), new Date(System.currentTimeMillis()).toString());
            session.save(temp);
        }
        Query query;
        query = session.createQuery("from Data where idZariadenia = \'"+idZariadenia.get()+"\' ");   
        List<Data> list = query.list();
        
        Query query2 = session.createQuery("from Zariadenie where id = "+list.get(0).getIdZariadenia()+"");
        Zariadenie uniqueResult = (Zariadenie) query2.uniqueResult();
        
        session.getTransaction().commit();
        session.close();
        int idUzivatela = uniqueResult.getIdUzivatela();
        DataListView dataview = new DataListView(list,idUzivatela);
        
        
        return dataview;
    }
    /**
     * Metóda vymaže dáta podľa zadaného id
     * @param user objekt používateľa použitého pri prihlásení
     * @param id id dát, ktoré majú byť vymazané
     * @return vracia objekt dátovej triedy, ktorá obsahuje list dát
     */
    @GET
    @PermitAll
    @Path("/delete")
    public DataListView DeleteData(@Auth User user, @QueryParam("id") Optional<String> id) {

        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Data where id = \'" + id.get()+"\' ");
        Data uniqueResult = (Data) query.uniqueResult();
        session.getTransaction().commit();
        
        
       
       
        if (uniqueResult == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
        
        session.beginTransaction();
        session.delete(uniqueResult);
        session.getTransaction().commit();
        session.beginTransaction();
        Query query3;
        query3 = session.createQuery("from Data where idZariadenia = \'"+uniqueResult.getIdZariadenia()+"\' ");   
        List<Data> list = query3.list();
        session.getTransaction().commit();
         session.beginTransaction();
        Query query2 = session.createQuery("from Zariadenie where id = \'"+uniqueResult.getIdZariadenia()+"\' ");
        Zariadenie uniqueResult2 = (Zariadenie) query2.uniqueResult();
        session.getTransaction().commit();
        session.close();
        
        int idUzivatela = uniqueResult2.getIdUzivatela();
        DataListView dataview = new DataListView(list,idUzivatela);
        
        
        return dataview;
    }
    
    /*
        @GET
    @Path("/new/{idZariadenia}")
    @Timed
    public DataListView createZariadenie(@PathParam("idZariadenia") String idZariadenia, @QueryParam("nazov") Optional<String> nazov,@QueryParam("hodnota") Optional<String> hodnota,@QueryParam("jednotka") Optional<String> jednotka) {
        
        Session session = ServerApp.buildSessionFactory.openSession();
        session.beginTransaction();
        if(nazov.isPresent() && hodnota.isPresent() && jednotka.isPresent()){
            Data temp = new Data(1, Integer.parseInt(idZariadenia), nazov.get(), Double.parseDouble(hodnota.get()), jednotka.get(), new Date(System.currentTimeMillis()).toString());
            session.save(temp);
        }
        Query query;
        query = session.createQuery("from Data where idZariadenia = \'"+idZariadenia+"\' ");   
        List<Data> list = query.list();
        
        Query query2 = session.createQuery("from Zariadenie where id = "+list.get(0).getIdZariadenia()+"");
        Zariadenie uniqueResult = (Zariadenie) query2.uniqueResult();
        
        session.getTransaction().commit();
        session.close();
        int idUzivatela = uniqueResult.getIdUzivatela();
        DataListView dataview = new DataListView(list,idUzivatela);
        
        
        return dataview;
    }*/
}
