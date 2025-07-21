package com.example.demo.Entidad;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "BITACORA")
public class BITACORA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idbitacora;

    @ManyToOne
    @JoinColumn(name = "idunidad", foreignKey = @ForeignKey(name = "fk_bitacora_unidad"))
    private UNIDADES unidad;

    private int mes;
    private int anio;

    @OneToMany(mappedBy = "bitacora", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DETALLEBHORAS> detalleBHoras;

    @OneToMany(mappedBy = "bitacora", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DETALLEBKILOMETRO> detalleBKilometro;


    public int getIdbitacora() {
        return idbitacora;
    }

    public void setIdbitacora(int idbitacora) {
        this.idbitacora = idbitacora;
    }

    public UNIDADES getUnidad() {
        return unidad;
    }

    public void setUnidad(UNIDADES unidad) {
        this.unidad = unidad;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public List<DETALLEBHORAS> getDetalleBHoras() {
        return detalleBHoras;
    }

    public void setDetalleBHoras(List<DETALLEBHORAS> detalleBHoras) {
        this.detalleBHoras = detalleBHoras;
    }

    public List<DETALLEBKILOMETRO> getDetalleBKilometro() {
        return detalleBKilometro;
    }

    public void setDetalleBKilometro(List<DETALLEBKILOMETRO> detalleBKilometro) {
        this.detalleBKilometro = detalleBKilometro;
    }
}
