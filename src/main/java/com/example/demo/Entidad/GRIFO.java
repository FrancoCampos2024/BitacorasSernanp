package com.example.demo.Entidad;


import jakarta.persistence.*;

@Entity
@Table(name = "GRIFO")
public class GRIFO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idgrifo;

    private String nombre;
    private boolean estado;

    public int getIdgrifo() {
        return idgrifo;
    }

    public void setIdgrifo(int idgrifo) {
        this.idgrifo = idgrifo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
