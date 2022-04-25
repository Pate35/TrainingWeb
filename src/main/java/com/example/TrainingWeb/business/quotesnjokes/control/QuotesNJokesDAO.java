package com.example.TrainingWeb.business.quotesnjokes.control;

import com.example.TrainingWeb.business.quotesnjokes.entity.QuoteNJoke;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class QuotesNJokesDAO {

    @Setter
    @Getter
    @PersistenceContext
    private EntityManager entityManager;

    public List<QuoteNJoke> findAll() {
        return entityManager.createNamedQuery("quotesnjokes.findAll", QuoteNJoke.class)
                .getResultList();
    }

}
