package com.example.TrainingWeb.presentation;

import com.example.TrainingWeb.business.MessageHelper;
import com.example.TrainingWeb.business.audit.boundary.AuditBean;
import com.example.TrainingWeb.business.audit.entity.AuditEntry;
import com.example.TrainingWeb.business.note.boundary.NoteBean;
import com.example.TrainingWeb.business.note.entity.Note;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Named
@ViewScoped
public class NoteGUI implements Serializable {

    @EJB
    private NoteBean noteBean;

    @EJB
    private AuditBean auditBean;

    @Getter
    @Setter
    private Note note;

    @Getter
    private List<Note> notes;

    @Getter
    @Setter
    private Note selectedNote;

    public NoteGUI() {
    }

    @PostConstruct
    private void init() {
        this.note = new Note("");
        this.notes = noteBean.getAllNotes();
    }

    public void createNote() {
        if (note.getDescription().isBlank()) {
            MessageHelper.addMessage(FacesMessage.SEVERITY_ERROR,"Note shouldn't be empty!");
            return;
        }
        note.setCreated(LocalDateTime.now());
        Note createdNote = noteBean.saveNote(note);
        notes.add(0,createdNote);
        note.setDescription("");
        auditBean.log(new AuditEntry("Note created", LocalDateTime.now()), "Note has been created!");
    }

    public void deleteNote() {
        noteBean.deleteNote(selectedNote);
        notes.remove(selectedNote);
        auditBean.log(new AuditEntry("Note deleted", LocalDateTime.now()), "Note has been deleted!");
    }


    public void updateNote(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            Note note = (Note) event.getComponent().getAttributes().get("editedNote");
            this.note = noteBean.updateNote(note);
            this.notes = noteBean.getAllNotes();
            auditBean.log(new AuditEntry("Note updated", LocalDateTime.now()), "Note has been updated!");
        }
    }

    public void clearAll() {
        noteBean.deleteAllNotes();
        notes.clear();
        auditBean.log(new AuditEntry("All notes deleted", LocalDateTime.now()), "All notes have been cleared!");
    }

}
