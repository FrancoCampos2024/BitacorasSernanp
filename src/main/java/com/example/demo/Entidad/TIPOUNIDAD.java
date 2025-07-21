package com.example.demo.Entidad;


import jakarta.persistence.*;

@Entity
@Table(name = "TIPO_UNIDAD")
public class TIPOUNIDAD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idtipou;

    private String nombre;
    private String medicion;

    public int getIdtipou() {
        return idtipou;
    }

    public void setIdtipou(int idtipou) {
        this.idtipou = idtipou;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMedicion() {
        return medicion;
    }

    public void setMedicion(String medicion) {
        this.medicion = medicion;
    }
}
