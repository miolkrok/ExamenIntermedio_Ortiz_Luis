package com.distribuida.servicios;

import com.distribuida.db.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class ServicioBookImpl implements  IServicioBook{

    @Inject
    EntityManager em;


    @Override
    public Book insert(Book b) {
        var tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(b);
            tx.commit();

        }
        catch(Exception ex) {
            tx.rollback();

        }
        return b;
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
    public Book update(Book b) {

        var tx = em.getTransaction();
        try{
            tx.begin();
             em.merge(b);
            tx.commit();

        }catch (Exception ex){
            tx.rollback();
        }
        return b;
    }

    @Override
    public Integer delete(Integer id) {
        var tx = em.getTransaction();
            Book b = this.findById(id);
            if(b != null){
                tx.begin();
                em.remove(b);
                tx.commit();
                return 1;
            }else {
                tx.rollback();
                return 0;
            }
    }
}
