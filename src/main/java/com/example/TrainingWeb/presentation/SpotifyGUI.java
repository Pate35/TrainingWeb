package com.example.TrainingWeb.presentation;

import com.example.TrainingWeb.business.spotify.boundary.SpotifyResource;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;

@Named
@ViewScoped
public class SpotifyGUI implements Serializable {

    private Client client;
    private WebTarget spotifyApi;

    @Getter
    @Setter
    private String user;

    @Getter
    @Setter
    private String code;

    public SpotifyGUI() {
    }

    @PostConstruct
    private void init() {
        this.client = ClientBuilder.newClient();
        this.spotifyApi = this.client.target("https://accounts.spotify.com/");
        System.out.println("");
        if (isAuthorized()) {
            postApiToken();
        }
    }

    public void login() throws IOException {
        String spotifyAuthorize = "https://accounts.spotify.com/authorize?"
                + "client_id=" + SpotifyResource.client_id
                + "&response_type=code"
                + "&redirect_uri=" + SpotifyResource.redirect_uri
                + "&scope=user-read-private user-read-email"
                + "&show_dialog=true";
        FacesContext.getCurrentInstance().getExternalContext().redirect(spotifyAuthorize);
    }

    private void postApiToken() {
        this.code = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code");

        Form form = new Form()
                .param("grant_type", "authorization_code")
                .param("code", code)
                .param("redirect_uri", "http://localhost:8080/PuiP/pages/index.xhtml");

        this.spotifyApi.path("api/token")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Basic" + (Base64.getEncoder().encodeToString((SpotifyResource.client_id + ':' + SpotifyResource.client_secret).getBytes())))
                .post(Entity.form(form));
    }

    private boolean isAuthorized() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code") != null;
    }


}
