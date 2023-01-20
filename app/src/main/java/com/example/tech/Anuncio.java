package com.example.tech;


import java.time.LocalDate;
import java.util.Date;


public class Anuncio {



    private String titulo;
    private String descripcion;
    private LocalDate fecha;

    public Anuncio(String descripcion, LocalDate fecha,String titulo ) {
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.titulo = titulo;

    }

    public Anuncio() {
    }



    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }



    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

}
