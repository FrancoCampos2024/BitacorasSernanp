package com.example.demo.Entidad;

import jakarta.persistence.*;

@Entity
@Table(name = "DESTINO")
public class DESTINO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int iddestino;
    private String destino;
    private boolean estado;

    public int getIddestino() {
        return iddestino;
    }

    public void setIddestino(int iddestino) {
        this.iddestino = iddestino;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
