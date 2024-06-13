package com.distribuida.servicios;

import com.distribuida.db.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class ServicioBookImpl implements  IServicioBook{

    @Inject
    EntityManager em;


    @Override
    public void insert(Book b) {
        var tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(b);
            tx.commit();
        }
        catch(Exception ex) {
            tx.rollback();
        }
    }

    @Override
    public List<Book> findAll() {
        return em.createQuery("select b from Book b")
                .getResultList();
    }

    @Override
    public Book findById(Integer id) {
        return em.find(Book.class, id);
    }

    @Override
    public void update(Book b) {
        var tx = em.getTransaction();
        try{
            tx.begin();
            em.merge(b);
            tx.commit();

        }catch (Exception ex){
            tx.rollback();
        }
    }

    @Override
    public void delete(Integer id) {
        var tx = em.getTransaction();
        try{
            tx.begin();
            Book b = this.findById(id);
            em.remove(b);
            tx.commit();

        }catch (Exception ex){
            tx.rollback();
        }
    }
}
