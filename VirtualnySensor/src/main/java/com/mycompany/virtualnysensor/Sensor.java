/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.virtualnysensor;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
 /**
  * Trieda obsahuje základnú metódu na odosielanie dát na server, slúži ako virtuálny senzor. Po spustení si vyžiada dáta na odoslanie na server.
  * Argument pri spustení je url na server, kde sa majú dáta odoslať. Defaultne je zadaná adresa http://localhost:8080/data .
  * @author Branislav
  */
public class Sensor {
    static CredentialsProvider provider = new BasicCredentialsProvider();
    static UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "secret");
    
    
    static HttpClient httpClient;
    static Scanner scanner = new Scanner(System.in).useDelimiter("\\s");
    static boolean run = true;
    public static void main (String [] args){
        provider.setCredentials(AuthScope.ANY, credentials);
        httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        
        boolean err = false;
        while (run){
            System.out.println("int:idZariadenia string:nazov double:hodnota string:jednotka");
            if(scanner.hasNext("exit")){
                run = false;
                
            }else{
                
                    if(scanner.hasNextInt()){
                        int idZariadenia = scanner.nextInt();
                        if(scanner.hasNext()){
                            String nazov = scanner.next();
                            if(scanner.hasNextDouble()){
                                double hodnota = scanner.nextDouble();
                                if(scanner.hasNext()){
                                    String jednotka = scanner.next();
                                    System.out.println(posliData(1, idZariadenia, nazov, hodnota, jednotka,"0",args[0]));
                                }else{err=true;}
                            }else{err=true;}
                        }else{err=true;}
                    }else{err=true;}
                scanner.nextLine(); 
                if(err){
                    if(scanner.hasNext("exit")){
                        run = false; 
                    }
                    scanner.nextLine();    
                    err = false; 
                    System.out.println("Zly zapis!");
                }
            }
        }
        System.exit(0);
    }
    
    
    
    /**
     *  Statická metóda slúžiaca na poslanie dát na server. Data odosiela vo formáte JSON funkciou POST.
     * @param id  id odosielanych dát, nezáleží na hodnote, nie je využite a je pri prenose prepísané
     * @param idZariadenia id zariadenia,ku ktorému budú dáta pridelené
     * @param nazov názov typu dát (napr. teplota, tlak)
     * @param hodnota číselná hodnota 
     * @param jednotka jednotka v ktorej, sú dáta vyjadrené
     * @param datum dátum vytvorenia zápisu, nezaleži na hodnote je na serveri prepísaná
     * @param url nepovinné zadanie adresy serveru, ak obsahuje null, bude využitá základná adresa http://localhost:8080/data
     * @return vracia String obsahujúci správu o vykonanej akcií
     */
    static public String posliData(int id,int idZariadenia,String nazov,double hodnota,String jednotka, String datum, String url){
    HttpResponse odpoved;
    
    
    try{
        HttpPost postdata;
        if(!url.equals("")){
        postdata = new HttpPost(url);
        }else{
        postdata = new HttpPost("http://localhost:8080/data");
        }
        StringEntity parametre = new StringEntity("{\"id\": "+id+",\"idZariadenia\": "+idZariadenia+",\"nazov\": \""+nazov+"\",\"hodnota\": "+hodnota+",\"jednotka\": \""+jednotka+"\",\"datum\": \""+datum+"\"}");
        
        
        postdata.addHeader("content-type", "application/json");
        postdata.setEntity(parametre);
        
        odpoved = httpClient.execute(postdata);
    }catch(IOException e){
        return e.getLocalizedMessage();
    }
        return odpoved.toString();
    }
    
}
