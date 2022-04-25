package com.example.TrainingWeb.business.audit.control;

import com.example.TrainingWeb.business.audit.entity.AuditEntry;
import com.example.TrainingWeb.business.note.entity.Note;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

public class AuditDAO {

    @Setter
    @Getter
    @PersistenceContext
    private EntityManager entityManager;

    public List<AuditEntry> findAll() {
        return entityManager.createNamedQuery("auditentry.findAll", AuditEntry.class)
                .getResultList();
    }

    public void log(AuditEntry entry) {
        entityManager.merge(entry);
    }
}
