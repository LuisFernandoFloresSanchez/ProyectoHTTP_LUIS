package com.example.proyectohtml;

public class Registro {
    private int idregistro;
    private String nombre;
    private String destino;
    private String razonvisita;
    private String placas;
    private String ine;

    public Registro(){

    }

    public Registro(int idregistro, String nombre, String destino, String razonvisita, String placas, String ine) {
        this.idregistro = idregistro;
        this.nombre = nombre;
        this.destino = destino;
        this.razonvisita = razonvisita;
        this.placas = placas;
        this.ine = ine;
    }

    public int getIdregistro() {
        return idregistro;
    }

    public void setIdregistro(int idregistro) {
        this.idregistro = idregistro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getRazonvisita() {
        return razonvisita;
    }

    public void setRazonvisita(String razonvisita) {
        this.razonvisita = razonvisita;
    }

    public String getPlacas() {
        return placas;
    }

    public void setPlacas(String placas) {
        this.placas = placas;
    }

    public String getIne() {
        return ine;
    }

    public void setIne(String ine) {
        this.ine = ine;
    }
}
