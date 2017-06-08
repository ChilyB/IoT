
package com.mycompany.semestralka;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * Dátová trieda obsahuje dáta o užívateľovy. Predstavuje JSON v tvare:
 * {
 *  	"id":	1,
 *	"meno": "meno",
 *	"priezvisko": "priezviisko"
 * }
 *
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"meno",
"priezvisko"
})
@Entity
public class Uzivatel {

@JsonProperty("id")
@Id
@GeneratedValue
private Integer id;
@JsonProperty("meno")
private String meno;
@JsonProperty("priezvisko")
private String priezvisko;


public Uzivatel() {
}

/**
* Konštruktor objektu dátovej triedy.
* @param id id číslo užívateľa, ktoré je generované automaticky
* @param meno meno užívateľa
* @param priezvisko priezvisko užívateľa
*/
public Uzivatel(Integer id, String meno, String priezvisko) {
super();
this.id = id;
this.meno = meno;
this.priezvisko = priezvisko;
}

@JsonProperty("id")
public Integer getId() {
return id;
}

@JsonProperty("id")
public void setId(Integer id) {
this.id = id;
}

public Uzivatel withId(Integer id) {
this.id = id;
return this;
}

@JsonProperty("meno")
public String getMeno() {
return meno;
}

@JsonProperty("meno")
public void setMeno(String meno) {
this.meno = meno;
}

public Uzivatel withMeno(String meno) {
this.meno = meno;
return this;
}

@JsonProperty("priezvisko")
public String getPriezvisko() {
return priezvisko;
}

@JsonProperty("priezvisko")
public void setPriezvisko(String priezvisko) {
this.priezvisko = priezvisko;
}

public Uzivatel withPriezvisko(String priezvisko) {
this.priezvisko = priezvisko;
return this;
}

}