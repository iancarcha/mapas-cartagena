package com.example.mapas_cartagena.ui;

public class Incidencia {

    String latitud;
    String longitud;
    String direccio;
    String problema;
    String url;

    public Incidencia(String latitud, String longitud, String direccio,
                      String problema, String url) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccio = direccio;
        this.problema = problema;
        this.url = url;
    }

    public Incidencia() {
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
