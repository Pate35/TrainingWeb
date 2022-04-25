package com.example.TrainingWeb.business.quotesnjokes.boundary;

import com.example.TrainingWeb.business.quotesnjokes.control.QuotesNJokesDAO;
import com.example.TrainingWeb.business.quotesnjokes.entity.QuoteNJoke;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@Stateless
public class QuotesNJokesBean {

    @Inject
    private QuotesNJokesDAO quotesNJokesDAO;

    public List<QuoteNJoke> getAllQuotes() {
        return quotesNJokesDAO.findAll();
    }

    public QuoteNJoke saveQuoteNJoke(QuoteNJoke quoteNJoke) {
        return quotesNJokesDAO.getEntityManager().merge(quoteNJoke);
    }
}
