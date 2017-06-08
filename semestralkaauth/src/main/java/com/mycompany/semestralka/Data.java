
package com.mycompany.semestralka;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
* Dátova trieda slúžiaca na ukladaníe dát senzorov, vygenerovaná s JSON dátového typu a obsahuje prísluśné gettre a settre. Predstavuje JSON 
* {
*    "id": 1,
*    "idZariadenia": 1,
*    "nazov": "teplota",
*    "hodnota": 20.9,
*    "jednotka": "celzius",
*    "datum": "15-2-2017"
*}
*
* 
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"idZariadenia",
"nazov",
"hodnota",
"jednotka",
"datum"
})
@Entity

public class Data {

@JsonProperty("id")
@Id
@GeneratedValue
private Integer id;
@JsonProperty("idZariadenia")
private Integer idZariadenia;
@JsonProperty("nazov")
private String nazov;
@JsonProperty("hodnota")
private Double hodnota;
@JsonProperty("jednotka")
private String jednotka;
@JsonProperty("datum")
private String datum;


public Data() {
}

/**
* Konštruktor na vytvorenie objektu dátovej triedy.
* @param id identifikacne číslo dát na servery, je generované automaticky
* @param idZariadenia id zariadenia, ktorému tieto dáta patria
* @param nazov názov typu dát, ktoré sú obsiahnute v objekte
* @param hodnota číselna hodnota dát
* @param jednotka jednotka daných dát
* @param datum dátum, kedy bol zápis odoslaný na server, generované pri odosielaní na server
*/
public Data(Integer id, Integer idZariadenia, String nazov, Double hodnota, String jednotka, String datum) {
super();
this.id = id;
this.idZariadenia = idZariadenia;
this.nazov = nazov;
this.hodnota = hodnota;
this.jednotka = jednotka;
this.datum = datum;
}

@JsonProperty("id")
public Integer getId() {
return id;
}

@JsonProperty("id")
public void setId(Integer id) {
this.id = id;
}

public Data withId(Integer id) {
this.id = id;
return this;
}

@JsonProperty("idZariadenia")
public Integer getIdZariadenia() {
return idZariadenia;
}

@JsonProperty("idZariadenia")
public void setIdZariadenia(Integer idZariadenia) {
this.idZariadenia = idZariadenia;
}

public Data withIdZariadenia(Integer idZariadenia) {
this.idZariadenia = idZariadenia;
return this;
}

@JsonProperty("nazov")
public String getNazov() {
return nazov;
}

@JsonProperty("nazov")
public void setNazov(String nazov) {
this.nazov = nazov;
}

public Data withNazov(String nazov) {
this.nazov = nazov;
return this;
}

@JsonProperty("hodnota")
public Double getHodnota() {
return hodnota;
}

@JsonProperty("hodnota")
public void setHodnota(Double hodnota) {
this.hodnota = hodnota;
}

public Data withHodnota(Double hodnota) {
this.hodnota = hodnota;
return this;
}

@JsonProperty("jednotka")
public String getJednotka() {
return jednotka;
}

@JsonProperty("jednotka")
public void setJednotka(String jednotka) {
this.jednotka = jednotka;
}

public Data withJednotka(String jednotka) {
this.jednotka = jednotka;
return this;
}

@JsonProperty("datum")
public String getDatum() {
return datum;
}

@JsonProperty("datum")
public void setDatum(String datum) {
this.datum = datum;
}

public Data withDatum(String datum) {
this.datum = datum;
return this;
}

}