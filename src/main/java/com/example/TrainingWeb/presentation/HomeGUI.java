package com.example.TrainingWeb.presentation;

import com.example.TrainingWeb.business.audit.boundary.AuditBean;
import com.example.TrainingWeb.business.audit.entity.AuditEntry;
import com.example.TrainingWeb.business.note.boundary.NoteBean;
import com.example.TrainingWeb.business.note.entity.Note;
import com.example.TrainingWeb.business.spotify.boundary.SpotifyResource;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class HomeGUI implements Serializable {

    @EJB
    private AuditBean auditBean;

    @Getter
    @Setter
    private AuditEntry auditEntry;

    @Getter
    private List<AuditEntry> entries;

    public HomeGUI() {
    }

    @PostConstruct
    private void init() {
        this.entries = auditBean.getAllAuditEntries();
    }

}
