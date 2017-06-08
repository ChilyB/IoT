/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semestralka;


import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import io.dropwizard.Configuration;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;


//import com.example.helloworld.resources.HelloWorldResource;
//import com.example.helloworld.health.TemplateHealthCheck;
/**
 * 
 * main trieda programu obsahujúca inicializáciu programu
 */
public class ServerApp extends Application<Configuration> {

    static SessionFactory buildSessionFactory;
    static Configuration conf = new Configuration();
    public static void main(String[] args) throws Exception {


        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        buildSessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        
        new ServerApp().run(args);
    }

  
    /**
     * Nahradzuje základnú metódu na inicializáciu.
     * @param bootstrap 
     */
    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        bootstrap.addBundle(new ViewBundle<Configuration>(){
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(Configuration conf) {
                
                return super.getViewConfiguration(conf); 
            }
        });
    }
    
    /**
     * Nahradenie funkcie zaručujucej beh servera. Obsahuje inicializáciu resource tried a ich pridelenie serveru.
     * @param configuration základné nadstavenie
     * @param environment parameter prostredia servera
     */
    @Override
    public void run(Configuration configuration,
            Environment environment) {
        

        final DataResource dataResource = new DataResource();
        final UzivatelResource uzivatelResource = new UzivatelResource();
        final ZariadenieResource zariadenieResource = new ZariadenieResource();
        final DataHTMLResource dataHTMLResource = new DataHTMLResource();
        final ZariadenieHTMLResource zariadeniaHTMLResource = new ZariadenieHTMLResource();
        final UzivatelHTMLResource uzivatelHTMLResource = new UzivatelHTMLResource();
        
        final TableHealthCheck uzivatelHealthCheck= new TableHealthCheck("Uzivatel");
        final TableHealthCheck zariadenieHealthCheck= new TableHealthCheck("Zariadenie");
        final TableHealthCheck dataHealthCheck= new TableHealthCheck("Data");
        environment.healthChecks().register("tabulkaU", uzivatelHealthCheck);
        environment.healthChecks().register("tabulkaZ", zariadenieHealthCheck);
        environment.healthChecks().register("tabulkaD", dataHealthCheck);
        
        
        
        
        
        
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new BasicAuthenticator())
                .setAuthorizer(new BasicAuthorizer())
                .setRealm("ZAHESLOVANE")
                .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        
        environment.jersey().register(dataResource);
        environment.jersey().register(zariadenieResource);
        environment.jersey().register(uzivatelResource);
        environment.jersey().register(dataHTMLResource);
        environment.jersey().register(zariadeniaHTMLResource);
        environment.jersey().register(uzivatelHTMLResource);
    }

}
