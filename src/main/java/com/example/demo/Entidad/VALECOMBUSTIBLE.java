package com.example.demo.Entidad;


import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "VALECOMBUSTIBLE")
public class VALECOMBUSTIBLE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idvcombustible;

    private long nvale;
    private float cantidad;
    private java.sql.Date fecha;
    private float saldorestante;

    @ManyToOne
    @JoinColumn(name = "idgrifo", foreignKey = @ForeignKey(name = "fk_vale_grifo"))
    private GRIFO grifo;

    @ManyToOne
    @JoinColumn(name = "idtipocombustible", foreignKey = @ForeignKey(name = "fk_vale_tipocombustible"))
    private TIPOCOMBUSTIBLE tipoCombustible;

    @OneToMany(mappedBy = "valeCombustible", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DESTINOVALE> destinos;

    public int getIdvcombustible() {
        return idvcombustible;
    }

    public void setIdvcombustible(int idvcombustible) {
        this.idvcombustible = idvcombustible;
    }

    public long getNvale() {
        return nvale;
    }

    public void setNvale(long nvale) {
        this.nvale = nvale;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getSaldorestante() {
        return saldorestante;
    }

    public void setSaldorestante(float saldorestante) {
        this.saldorestante = saldorestante;
    }

    public GRIFO getGrifo() {
        return grifo;
    }

    public void setGrifo(GRIFO grifo) {
        this.grifo = grifo;
    }

    public TIPOCOMBUSTIBLE getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(TIPOCOMBUSTIBLE tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    public List<DESTINOVALE> getDestinos() {
        return destinos;
    }

    public void setDestinos(List<DESTINOVALE> destinos) {
        this.destinos = destinos;
    }
}
