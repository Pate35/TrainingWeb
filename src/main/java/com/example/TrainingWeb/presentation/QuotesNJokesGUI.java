package com.example.TrainingWeb.presentation;

import com.example.TrainingWeb.business.MessageHelper;
import com.example.TrainingWeb.business.audit.boundary.AuditBean;
import com.example.TrainingWeb.business.audit.entity.AuditEntry;
import com.example.TrainingWeb.business.quotesnjokes.Type;
import com.example.TrainingWeb.business.quotesnjokes.boundary.QuotesNJokesBean;
import com.example.TrainingWeb.business.quotesnjokes.entity.QuoteNJoke;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named
@ViewScoped
public class QuotesNJokesGUI implements Serializable {

    @EJB
    private QuotesNJokesBean quotesNJokesBean;

    @EJB
    private AuditBean auditBean;

    @Getter
    @Setter
    private QuoteNJoke quoteNJoke;

    @Getter
    private List<QuoteNJoke> initialQuoteNJokes;

    @Getter
    private List<QuoteNJoke> quoteNJokes;

    @Setter
    @Getter
    private Set<String> languageFilterOptions;

    @Setter
    @Getter
    private Set<String> typeFilterOptions;

    @Getter
    @Setter
    private String newQuoteNJokeText;

    @Getter
    @Setter
    private String icon;

    public QuotesNJokesGUI() {
    }

    @PostConstruct
    private void init() {
        this.quoteNJoke = new QuoteNJoke();
        this.initialQuoteNJokes = quotesNJokesBean.getAllQuotes();
        reSyncQuoteNJokes();
    }

    private void reSyncQuoteNJokes() {
        this.quoteNJokes = this.initialQuoteNJokes;
    }

    public void insertQuoteNJoke() {
        if (quoteNJoke.getText().isBlank()) {
            MessageHelper.addMessage(FacesMessage.SEVERITY_ERROR, "Entry shouldn't be empty!");
            return;
        }

        QuoteNJoke createdQuoteNJoke = quotesNJokesBean.saveQuoteNJoke(quoteNJoke);
        initialQuoteNJokes.add(0, createdQuoteNJoke);
        reSyncQuoteNJokes();
        quoteNJoke.setText("");
        auditBean.log(new AuditEntry("Entry inserted!", LocalDateTime.now()), "Entry has been inserted!");
    }


    public void showRandomJoke() {
        List<QuoteNJoke> jokes = getQuotesNJokesWithType(Type.JOKE);
        generateRandomQuoteNJoke(jokes);
        setIcon("pi pi-android");
    }

    public void showRandomQuote() {
        List<QuoteNJoke> quotes = getQuotesNJokesWithType(Type.QUOTE);
        generateRandomQuoteNJoke(quotes);
        setIcon("pi pi-thumbs-up");
    }

    private List<QuoteNJoke> getQuotesNJokesWithType(Type type) {
        return initialQuoteNJokes.stream()
                .filter(quoteNJoke -> quoteNJoke.getLanguage().equals(this.quoteNJoke.getLanguage()))
                .filter(quoteNJoke -> quoteNJoke.getType().equals(type))
                .collect(Collectors.toList());
    }

    private void generateRandomQuoteNJoke(List<QuoteNJoke> jokes) {
        Random r = new Random();
        newQuoteNJokeText = jokes.get(r.nextInt(jokes.size())).getText();
        newQuoteNJokeText = newQuoteNJokeText.replace(" - ", "<br />-<br />");
    }

    public String getRowStyleClass(QuoteNJoke quoteNJoke) {
        if ((languageFilterOptions != null && !languageFilterOptions.isEmpty())
                || typeFilterOptions != null && !typeFilterOptions.isEmpty()) {
            if(quoteNJoke.getType().toString().equals("JOKE") && quoteNJoke.getLanguage().toString().equals("GERMAN")) return "germanJoke";
            if(quoteNJoke.getType().toString().equals("QUOTE") && quoteNJoke.getLanguage().toString().equals("GERMAN")) return "germanQuote";
            if(quoteNJoke.getType().toString().equals("JOKE") && quoteNJoke.getLanguage().toString().equals("ENGLISH")) return "englishJoke";
            if(quoteNJoke.getType().toString().equals("QUOTE") && quoteNJoke.getLanguage().toString().equals("ENGLISH")) return "englishQuote";
        }
        return null;
    }

    public void filterTable() {
        //need to use supplier to use stream more than once
        Supplier<Stream<QuoteNJoke>> qujoStream = () -> this.initialQuoteNJokes.stream();

        qujoStream = filterByLanguage(qujoStream);
        qujoStream = filterByType(qujoStream);

        this.quoteNJokes = qujoStream.get().collect(Collectors.toList());
    }

    private Supplier<Stream<QuoteNJoke>> filterByType(Supplier<Stream<QuoteNJoke>> qujoStream) {
        if ((typeFilterOptions != null && !typeFilterOptions.isEmpty()) && !(typeFilterOptions.contains("JOKE") && typeFilterOptions.contains("QUOTE"))) {
            Supplier<Stream<QuoteNJoke>> temp = qujoStream;
            qujoStream = () -> temp.get().filter(qujo -> qujo.getType().toString().equals(typeFilterOptions.stream().findFirst().get()));
        }
        return qujoStream;
    }

    private Supplier<Stream<QuoteNJoke>> filterByLanguage(Supplier<Stream<QuoteNJoke>> qujoStream) {
        if ((languageFilterOptions != null && !languageFilterOptions.isEmpty()) && !(languageFilterOptions.contains("ENGLISH") && languageFilterOptions.contains("GERMAN"))) {
            Supplier<Stream<QuoteNJoke>> temp = qujoStream;
            qujoStream = () -> temp.get().filter(qujo -> qujo.getLanguage().toString().equals(languageFilterOptions.stream().findFirst().get()));
        }
        return qujoStream;
    }

}
