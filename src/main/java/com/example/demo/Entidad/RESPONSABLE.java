package com.example.demo.Entidad;


import jakarta.persistence.*;

@Entity
@Table(name = "Responsable")
public class RESPONSABLE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idresponsable;

    private String nombre;
    private String apellidopaterno;
    private String apellidomaterno;
    private boolean estado;

    public int getIdresponsable() {
        return idresponsable;
    }

    public void setIdresponsable(int idresponsable) {
        this.idresponsable = idresponsable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidopaterno() {
        return apellidopaterno;
    }

    public void setApellidopaterno(String apellidopaterno) {
        this.apellidopaterno = apellidopaterno;
    }

    public String getApellidomaterno() {
        return apellidomaterno;
    }

    public void setApellidomaterno(String apellidomaterno) {
        this.apellidomaterno = apellidomaterno;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
