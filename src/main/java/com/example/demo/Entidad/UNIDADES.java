package com.example.demo.Entidad;


import jakarta.persistence.*;

@Entity
@Table(name = "UNIDADES")
public class UNIDADES {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idunidad;

    @ManyToOne
    @JoinColumn(name = "idtipou", foreignKey = @ForeignKey(name = "fk_unidad_tipounidad"))
    private TIPOUNIDAD tipoUnidad;

    private String nombre;
    private boolean estado;

    @ManyToOne
    @JoinColumn(name = "idtipocombustible", foreignKey = @ForeignKey(name = "fk_unidad_tipocombustible"))
    private TIPOCOMBUSTIBLE tipoCombustible;

    private String identificador;




    public int getIdunidad() {
        return idunidad;
    }

    public void setIdunidad(int idunidad) {
        this.idunidad = idunidad;
    }

    public TIPOUNIDAD getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(TIPOUNIDAD tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TIPOCOMBUSTIBLE getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(TIPOCOMBUSTIBLE tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
