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
import java.math.BigDecimal;
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
        book.setId(1);
        book.setTitle("Cien Años de Soledad");
        book.setIsbn("isbn");
        book.setAuthor("Gabriel Garcia Marquez");
        book.setPrice(BigDecimal.valueOf(20.00));
        Book book1 = new Book();
        book1.setId(2);
        book1.setTitle("Iliada");
        book1.setIsbn("isbn");
        book1.setAuthor("Homero");
        book1.setPrice(BigDecimal.valueOf(20.00));
        servicio.insert(book);
        servicio.insert(book1);

        System.out.println("se inserto");


        servicio.findAll().forEach(b -> System.out.println(b.getId()+" "+b.getAuthor()));

        WebServer.builder()
                .routing(it -> it
                        .get("/books", (req, res) -> res.send(gson.toJson(servicio.findAll())))
                        .get("/books/{id}", (req, res) ->{
                            Integer reqInteger = Integer.valueOf(req.path().pathParameters().get("id"));
                            res.send(gson.toJson(servicio.findById(reqInteger)));
                        } )
                        .post("/books",(req, res) ->{
                            String reqBody = req.content().as(String.class);
                            Book reqEntity = gson.fromJson(reqBody, Book.class);
                            res.send(gson.toJson(servicio.insert(reqEntity)));
                        }).put("/books",(req, res) -> {
                            String reqBody = req.content().as(String.class);
                            Book reqEntity = gson.fromJson(reqBody, Book.class);
                            res.send(gson.toJson(servicio.update(reqEntity)));
                        })
                        .delete("/books/{id}",(req, res) ->{
                                    Integer resInteger = Integer.valueOf(req.path().pathParameters().get("id"));
                                    res.send(gson.toJson(servicio.delete(resInteger)));
                                }
                                ))
                .port(8080)
                .build()
                .start();

        shutdown();

    }
    public static void shutdown()  {
        lifecycle.stopApplication(null);
    }


}
