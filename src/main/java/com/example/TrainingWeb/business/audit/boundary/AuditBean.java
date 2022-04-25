package com.example.TrainingWeb.business.audit.boundary;

import com.example.TrainingWeb.business.MessageHelper;
import com.example.TrainingWeb.business.audit.control.AuditDAO;
import com.example.TrainingWeb.business.audit.entity.AuditEntry;
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
public class AuditBean {

    @Inject
    private AuditDAO auditDAO;

    public List<AuditEntry> getAllAuditEntries() {
        return auditDAO.findAll();
    }

    public void log(AuditEntry entry, String growlMessage) {
        auditDAO.log(entry);
        MessageHelper.addMessage(growlMessage);
    }

}
