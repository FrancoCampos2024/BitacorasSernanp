package com.example.demo.Servicios.impl;

import com.example.demo.Entidad.DETALLEBKILOMETRO;
import com.example.demo.Repositorio.RepositorioDetallebkilometro;
import com.example.demo.Servicios.ServicioDetallebkilometro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("Serviciodetallekm")
public class ImplDetallebkilometro implements ServicioDetallebkilometro {
    @Autowired
    private RepositorioDetallebkilometro repositorioDetallebkilometro;


    @Override
    public List<DETALLEBKILOMETRO> listaporbitacora(int idbitacora) {
        return repositorioDetallebkilometro.listarsegunbitacora(idbitacora);
    }

    @Override
    public List<Integer> buscarDiasRegistrados(int idbitacora) {
        return repositorioDetallebkilometro.buscarDiasRegistrados(idbitacora);
    }

    @Override
    public void agregardetalle(DETALLEBKILOMETRO detalle) {
        repositorioDetallebkilometro.save(detalle);
    }

    @Override
    public DETALLEBKILOMETRO buscarporid(int iddetallekm) {
        return repositorioDetallebkilometro.findById(iddetallekm).orElse(null);
    }

    @Override
    public void elimininardetalle(DETALLEBKILOMETRO detalle) {
        repositorioDetallebkilometro.delete(detalle);
    }

    @Override
    public Page<DETALLEBKILOMETRO> listaporbitacora(int idbitacora, Pageable pageable) {
        return repositorioDetallebkilometro.findByBitacora_IdbitacoraOrderByDiaAsc(idbitacora, pageable);
    }

    @Override
    public List<DETALLEBKILOMETRO> listardetalleparaconsumoporvale(int idvale, int mes, int anio) {
        return repositorioDetallebkilometro.listardetalleparaconsumoporvale(idvale, mes, anio);
    }

    @Override
    public boolean fueUsado(int iddestinovale) {
        return repositorioDetallebkilometro.existsByDestinovale_Iddestinovale(iddestinovale);
    }

    @Override
    public List<DETALLEBKILOMETRO> obtenerPorBitacora(int idbitacora) {
        return repositorioDetallebkilometro.obtenerPorBitacora(idbitacora);
    }
}
