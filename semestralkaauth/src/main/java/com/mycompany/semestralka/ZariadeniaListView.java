/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semestralka;

import io.dropwizard.views.View;
import java.util.List;

/**
 * Dátová trieda pre zariadenia slúžiaca na zobrazenie dát do HTML stránky podľa predlohy.
 * Je potomkom triedy View.
 */
public class ZariadeniaListView extends View {

    private final List<Zariadenie> zariadenia;

    public List<Zariadenie> getZariadenia() {
        return zariadenia;
    }
/**
 * Konštruktor triedy. Volá sa konštruktor triedy View s parametrom "zariadenia.ftl" ako template pre html stranku.
 * @param zariadenia list zariadení , ktorý sa zobrazí na HTML stránke
 */
    public ZariadeniaListView(List<Zariadenie> zariadenia) {
        super("zariadenia.ftl");
        this.zariadenia =zariadenia;
    }

    
}
