package com.distribuida.servicios;

import com.distribuida.db.Book;

import java.util.List;

public interface IServicioBook {

    void insert(Book b);

    List<Book> findAll();

    Book findById(Integer id);

    void update(Book b);

    void delete(Integer id);
}
