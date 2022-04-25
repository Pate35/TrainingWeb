package com.example.TrainingWeb.business.quotesnjokes.entity;

import com.example.TrainingWeb.business.quotesnjokes.Language;
import com.example.TrainingWeb.business.quotesnjokes.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "quotesnjokes.findAll", query = "select qj from QuoteNJoke qj")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quotesnjokes")
public class QuoteNJoke {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence")
    @SequenceGenerator(name = "hibernate_sequence", sequenceName = "hibernate_sequence", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Language language = Language.ENGLISH;

    @Enumerated(EnumType.STRING)
    private Type type = Type.JOKE;

    private String text;

}
