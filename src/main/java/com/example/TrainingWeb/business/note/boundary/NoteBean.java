package com.example.TrainingWeb.business.note.boundary;

import com.example.TrainingWeb.business.note.control.NoteDAO;
import com.example.TrainingWeb.business.note.entity.Note;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Named
@Stateless
public class NoteBean {

    @Inject
    private NoteDAO noteDAO;

    public Note saveNote(Note note) {
        return noteDAO.getEntityManager().merge(note);
    }

    public List<Note> getAllNotes() {
        return noteDAO.findAll();
    }

    public void deleteNote(Note note) {
        Note noteToDelete = noteDAO.getEntityManager().find(Note.class, note.getId());
        noteDAO.deleteNote(noteToDelete);
    }

    public Note updateNote(Note note) {
        Note noteToUpdate = noteDAO.getEntityManager().find(Note.class, note.getId());
        noteToUpdate.setDescription(note.getDescription());
        noteToUpdate.setLastModified(LocalDateTime.now());
        return noteDAO.updateNote(noteToUpdate);
    }

    public void deleteAllNotes() {
        noteDAO.deleteAllNotes();
    }
}
