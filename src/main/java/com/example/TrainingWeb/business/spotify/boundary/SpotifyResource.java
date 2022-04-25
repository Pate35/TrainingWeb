package com.example.TrainingWeb.business.spotify.boundary;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Stateless
@Path("spotify")
public class SpotifyResource {

    private Client client;
    private WebTarget spotifyApi;

    public static final String client_id = "e67906b44154406686665c79dfb00e72"; // Your client id
    public static final String client_secret = "8120ed56c3074a7bba1e3be93c45c0a1"; // Your secret
    public static final String redirect_uri = "http://localhost:8080/PuiP/pages/spotify.xhtml"; // Your redirect uri

    @PostConstruct
    private void init() {
        this.client = ClientBuilder.newClient();
        this.spotifyApi = this.client.target("https://accounts.spotify.com/");
    }

    @GET
    @Path("login")
    public String authorize() {
        Response response = this.spotifyApi.path("authorize")
                .queryParam("client_id", SpotifyResource.client_id)
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", "http://localhost:8080/PuiP/pages/index.xhtml")
                .queryParam("scope","user-read-private user-read-email")
                .queryParam("show_dialog", true)
                .request().get();

        System.out.println(response.getStatus());
        String payload = response.readEntity(String.class);

        System.out.println(payload);
        return payload;
    }

    @POST
    public void postClichedMessage(String message) {
        // Store the message
//        System.out.println(getClichedMessage());
    }
}
