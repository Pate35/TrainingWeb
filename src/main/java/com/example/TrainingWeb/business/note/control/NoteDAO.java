package com.example.TrainingWeb.business.note.control;

import com.example.TrainingWeb.business.note.entity.Note;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class NoteDAO {

    @Setter
    @Getter
    @PersistenceContext
    private EntityManager entityManager;

    public List<Note> findAll() {
        return entityManager.createNamedQuery("note.findAll", Note.class)
                .getResultList();
    }

    public void deleteNote(Note note) {
        entityManager.remove(note);
    }

    public Note updateNote(Note noteToUpdate) {
        return entityManager.merge(noteToUpdate);
    }

    public void deleteAllNotes() {
        entityManager.createNamedQuery("note.deleteAll")
                .executeUpdate();
    }
    
}
