package com.distribuida.servicios;

import com.distribuida.db.Book;

import java.util.List;

public interface IServicioBook {

    Book insert(Book b);

    List<Book> findAll();

    Book findById(Integer id);

    Book update(Book b);

    Integer delete(Integer id);
}
