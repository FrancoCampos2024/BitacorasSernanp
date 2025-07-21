package com.example.demo.Entidad;


import jakarta.persistence.*;

@Entity
@Table(name = "TIPO_COMBUSTIBLE")
public class TIPOCOMBUSTIBLE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idtipocombustible;

    private String nombre;

    public int getIdtipocombustible() {
        return idtipocombustible;
    }

    public void setIdtipocombustible(int idtipocombustible) {
        this.idtipocombustible = idtipocombustible;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
