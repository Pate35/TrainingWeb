package com.example.TrainingWeb.business.note.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "note.findAll", query = "select n from Note n order by id desc"),
        @NamedQuery(name = "note.deleteAll", query = "delete from Note")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited
@Table(name = "note")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence")
    @SequenceGenerator(name = "hibernate_sequence", sequenceName = "hibernate_sequence", allocationSize = 1)
    private Long id;

    private String description;

    @Column(name = "created", columnDefinition = "TIMESTAMP")
    private LocalDateTime created;

    @Column(name = "lastModified", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastModified;

    public Note(String description) {
        this.description = description;
    }
}
