package com.distribuida;


import com.distribuida.db.Book;
import com.distribuida.servicios.IServicioBook;
import com.distribuida.servicios.ServicioBookImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.sun.net.httpserver.HttpExchange;
import io.helidon.common.media.type.MediaTypes;
import io.helidon.http.Http;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRequest;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.http.ServerRequest;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;

import java.io.StringReader;
import java.net.http.HttpResponse;
import java.util.Optional;


public class Principal {

    private static ContainerLifecycle lifecycle = null;

    private  static Gson gson = new Gson();
    public static void main(String[] args) {
        lifecycle = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);

        IServicioBook servicio = CDI.current().select(IServicioBook.class).get();

        Book book = new Book();
        book.setId(3);
        book.setTitle("1984");
        book.setAuthor("Marco ");
        servicio.insert(book);

        System.out.println("se inserto");

        // Retrieve and print all books

        servicio.findAll().forEach(b -> System.out.println(b.getId()+" "+b.getAuthor()));

        WebServer.builder()
                .routing(it -> it
                        .get("/books", (req, res) -> res.send(gson.toJson(servicio.findAll())))
                        .get("/books/{id}", (req, res) -> res.send(gson.toJson(servicio.findById(1))))                        )
                .port(8080)
                .build()
                .start();

        shutdown();

    }
    public static void shutdown()  {
        lifecycle.stopApplication(null);
    }

    /*static Book buscarLibro(ServerRequest req, ServerRequest res) {


    }*/

}
