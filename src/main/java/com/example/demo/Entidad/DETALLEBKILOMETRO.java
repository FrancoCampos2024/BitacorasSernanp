package com.example.demo.Entidad;


import jakarta.persistence.*;

@Entity
@Table(name = "DETALLEBKILOMETRO")
public class DETALLEBKILOMETRO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int iddetallekm;

    @ManyToOne
    @JoinColumn(name = "idbitacora", foreignKey = @ForeignKey(name = "fk_detallekm_bitacora"))
    private BITACORA bitacora;

    @ManyToOne
    @JoinColumn(name = "idvdcombustible", foreignKey = @ForeignKey(name = "fk_detallekm_destinovale"))
    private DESTINOVALE destinovale;

    private int dia;
    private int kminicial;
    private int kmfinal;
    private int kmrecorridos;
    private float combustiblegls;

    private int aceitemotor;
    private int aceitetransmision;
    private String serviciengrase;
    private String serviciomantenimiento;
    private String filtroaceitecambio;
    private String filtropurificadorcambio;
    private String bateriacambio;

    private String trabajosrealizados;
    private String anotaciones;
    private String Mantenimiendodescripcion;

    @ManyToOne
    @JoinColumn(name="idresponsable", foreignKey = @ForeignKey(name = "fk_detallekm_responsable"))
    private RESPONSABLE responsable;


    public int getIddetallekm() {
        return iddetallekm;
    }

    public void setIddetallekm(int iddetallekm) {
        this.iddetallekm = iddetallekm;
    }

    public BITACORA getBitacora() {
        return bitacora;
    }

    public void setBitacora(BITACORA bitacora) {
        this.bitacora = bitacora;
    }

    public DESTINOVALE getDestinovale() {
        return destinovale;
    }

    public void setDestinovale(DESTINOVALE destinovale) {
        this.destinovale = destinovale;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getKminicial() {
        return kminicial;
    }

    public void setKminicial(int kminicial) {
        this.kminicial = kminicial;
    }

    public int getKmfinal() {
        return kmfinal;
    }

    public void setKmfinal(int kmfinal) {
        this.kmfinal = kmfinal;
    }

    public int getKmrecorridos() {
        return kmrecorridos;
    }

    public void setKmrecorridos(int kmrecorridos) {
        this.kmrecorridos = kmrecorridos;
    }

    public float getCombustiblegls() {
        return combustiblegls;
    }

    public void setCombustiblegls(float combustiblegls) {
        this.combustiblegls = combustiblegls;
    }

    public int getAceitemotor() {
        return aceitemotor;
    }

    public void setAceitemotor(int aceitemotor) {
        this.aceitemotor = aceitemotor;
    }

    public int getAceitetransmision() {
        return aceitetransmision;
    }

    public void setAceitetransmision(int aceitetransmision) {
        this.aceitetransmision = aceitetransmision;
    }

    public String getServiciengrase() {
        return serviciengrase;
    }

    public void setServiciengrase(String serviciengrase) {
        this.serviciengrase = serviciengrase;
    }

    public String getServiciomantenimiento() {
        return serviciomantenimiento;
    }

    public void setServiciomantenimiento(String serviciomantenimiento) {
        this.serviciomantenimiento = serviciomantenimiento;
    }

    public String getFiltroaceitecambio() {
        return filtroaceitecambio;
    }

    public void setFiltroaceitecambio(String filtroaceitecambio) {
        this.filtroaceitecambio = filtroaceitecambio;
    }

    public String getFiltropurificadorcambio() {
        return filtropurificadorcambio;
    }

    public void setFiltropurificadorcambio(String filtropurificadorcambio) {
        this.filtropurificadorcambio = filtropurificadorcambio;
    }

    public String getBateriacambio() {
        return bateriacambio;
    }

    public void setBateriacambio(String bateriacambio) {
        this.bateriacambio = bateriacambio;
    }

    public String getTrabajosrealizados() {
        return trabajosrealizados;
    }

    public void setTrabajosrealizados(String trabajosrealizados) {
        this.trabajosrealizados = trabajosrealizados;
    }

    public String getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(String anotaciones) {
        this.anotaciones = anotaciones;
    }

    public RESPONSABLE getResponsable() {
        return responsable;
    }

    public void setResponsable(RESPONSABLE responsable) {
        this.responsable = responsable;
    }

    public String getMantenimiendodescripcion() {
        return Mantenimiendodescripcion;
    }

    public void setMantenimiendodescripcion(String mantenimiendodescripcion) {
        Mantenimiendodescripcion = mantenimiendodescripcion;
    }
}
