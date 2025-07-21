package com.example.demo.Entidad;


import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "DETALLEBHORAS")
public class DETALLEBHORAS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int iddetallebitacora;

    @ManyToOne
    @JoinColumn(name = "idbitacora", foreignKey = @ForeignKey(name = "fk_detallehoras_bitacora"))
    private BITACORA bitacora;

    private int dia;
    private LocalTime hinicio;
    private LocalTime hfinal;
    private float Hoperacion;
    private String aceite;
    private float combustible;
    private String destino;
    private String justificacion;
    private String reporte;



    @ManyToOne
    @JoinColumn(name = "idvdcombustible", foreignKey = @ForeignKey(name = "fk_detallehoras_destinovale"))
    private DESTINOVALE destinovale;

    @ManyToOne
    @JoinColumn(name="idresponsable", foreignKey = @ForeignKey(name = "fk_detallehora_responsable"))
    private RESPONSABLE responsable;

    public int getIddetallebitacora() {
        return iddetallebitacora;
    }

    public void setIddetallebitacora(int iddetallebitacora) {
        this.iddetallebitacora = iddetallebitacora;
    }

    public BITACORA getBitacora() {
        return bitacora;
    }

    public void setBitacora(BITACORA bitacora) {
        this.bitacora = bitacora;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public LocalTime getHinicio() {
        return hinicio;
    }

    public void setHinicio(LocalTime hinicio) {
        this.hinicio = hinicio;
    }

    public LocalTime getHfinal() {
        return hfinal;
    }

    public void setHfinal(LocalTime hfinal) {
        this.hfinal = hfinal;
    }

    public float getHoperacion() {
        return Hoperacion;
    }

    public void setHoperacion(float hoperacion) {
        Hoperacion = hoperacion;
    }

    public String getAceite() {
        return aceite;
    }

    public void setAceite(String aceite) {
        this.aceite = aceite;
    }

    public float getCombustible() {
        return combustible;
    }

    public void setCombustible(float combustible) {
        this.combustible = combustible;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public RESPONSABLE getResponsable() {
        return responsable;
    }

    public void setResponsable(RESPONSABLE responsable) {
        this.responsable = responsable;
    }

    public String getReporte() {
        return reporte;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    public DESTINOVALE getDestinovale() {
        return destinovale;
    }

    public void setDestinovale(DESTINOVALE destinovale) {
        this.destinovale = destinovale;
    }
}
