
package com.mycompany.semestralka;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * Dátová trieda predstavujúca údaje zariadenia. Vytvorená z JSON
 * {
 *	"id": 1,
 * 	"idUzivatela": 1,
	"nazov": "nazov"
}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"idUzivatela",
"nazov"
})
@Entity
public class Zariadenie {

@JsonProperty("id")
@Id
@GeneratedValue
private Integer id;
@JsonProperty("idUzivatela")
private Integer idUzivatela;
@JsonProperty("nazov")
private String nazov;

/**
* No args constructor for use in serialization
* 
*/
public Zariadenie() {
}

/**
* Konśtruktor dátoveho objektu.
* @param id id zariadenia, je generované automaticky
* @param nazov názov zariadenia
* @param idUzivatela id užívateľa, ktorému zariadenie náleží
*/
public Zariadenie(Integer id, Integer idUzivatela, String nazov) {
super();
this.id = id;
this.idUzivatela = idUzivatela;
this.nazov = nazov;
}

@JsonProperty("id")
public Integer getId() {
return id;
}

@JsonProperty("id")
public void setId(Integer id) {
this.id = id;
}

public Zariadenie withId(Integer id) {
this.id = id;
return this;
}

@JsonProperty("idUzivatela")
public Integer getIdUzivatela() {
return idUzivatela;
}

@JsonProperty("idUzivatela")
public void setIdUzivatela(Integer idUzivatela) {
this.idUzivatela = idUzivatela;
}

public Zariadenie withIdUzivatela(Integer idUzivatela) {
this.idUzivatela = idUzivatela;
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

public Zariadenie withNazov(String nazov) {
this.nazov = nazov;
return this;
}

}