package com.example.TrainingWeb.business;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MessageHelper {

    public static void addMessage(FacesMessage.Severity severity, String message) {
        FacesMessage msg = new FacesMessage(severity, message, "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void addMessage(String message) {
        addMessage(FacesMessage.SEVERITY_INFO, message);
    }

    public static void addErrorMessage(String message) {
        addMessage(FacesMessage.SEVERITY_ERROR, message);
    }

}
