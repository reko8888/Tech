package com.example.tech;


import java.util.Date;


public class Anuncio {



    private String titulo;
    private String descripcion;
    private Date fecha;

    public Anuncio(String descripcion, Date fecha,String titulo ) {
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
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}
