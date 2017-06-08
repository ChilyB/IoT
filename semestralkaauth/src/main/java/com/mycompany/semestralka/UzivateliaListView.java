package com.mycompany.semestralka;



import io.dropwizard.views.View;
import java.util.List;

/**
 * Dátová trieda pre užívateľov slúžiaca na zobrazenie dát do HTML stránky podľa predlohy.
 * Je potomkom triedy View.
 */
public class UzivateliaListView extends View {

    private final List<Uzivatel> uzivatelia;

    public List<Uzivatel> getUzivatelia() {
        return uzivatelia;
    }
    /**
     * Konštruktor triedy.Volá sa konštruktor triedy View s parametrom "uzivatelia.ftl" ako template pre html stranku.
     * @param uzivatelia list užívateľov, ktorý sa zobrazí
     */
    public UzivateliaListView(List<Uzivatel> uzivatelia) {
        super("uzivatelia.ftl");
        this.uzivatelia = uzivatelia;
    }

    
}
