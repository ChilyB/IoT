/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semestralka;

import io.dropwizard.views.View;
import java.util.List;

/**
 * Dátová trieda pre dáta zariadení slúžiaca na zobrazenie dát do HTML stránky podľa predlohy.
 * Je potomkom triedy View.
 */
public class DataListView extends View {

    private final List<Data> data;
    private final int idUzivatela;
    
    public List<Data> getData() {
        return data;
    }
    
    public int getidUzivatela(){
        return idUzivatela;
    }
    /**
     * Konštruktor pre dáta.Volá sa konštruktor triedy View s parametrom "data.ftl" ako template pre html stranku.
     * @param data list objektov triedy Data
     * @param idUzivatela id užívateľa, ktorému patria dáta a zariadenie
     */
    public DataListView(List<Data> data,int idUzivatela) {
        super("data.ftl");
        this.data = data;
        this.idUzivatela = idUzivatela;
    }

    
}

