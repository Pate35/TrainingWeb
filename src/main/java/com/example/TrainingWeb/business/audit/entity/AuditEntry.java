package com.example.TrainingWeb.business.audit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "auditentry.findAll", query = "select ae from AuditEntry ae order by id desc")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auditentry")
public class AuditEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence")
    @SequenceGenerator(name = "hibernate_sequence", sequenceName = "hibernate_sequence", allocationSize = 1)
    private Long id;

    private String entry;

    @Column(name = "date", columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    public AuditEntry(String entry, LocalDateTime date) {
        this.entry = entry;
        this.date = date;
    }
}
