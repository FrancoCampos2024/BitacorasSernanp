package com.example.demo.Servicios.impl;

import com.example.demo.Entidad.DETALLEBHORAS;
import com.example.demo.Repositorio.RepositorioDetallebhoras;
import com.example.demo.Servicios.ServicioDetallebhoras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("Serviciodetalleh")
public class ImplDetallebhoras implements ServicioDetallebhoras {

    @Autowired
    private RepositorioDetallebhoras repositorioDetallebhoras;

    @Override
    public List<DETALLEBHORAS> listaporbitacora(int idbitacora) {
        return repositorioDetallebhoras.listarsegunbitacora(idbitacora);
    }

    @Override
    public void agregardetalle(DETALLEBHORAS detalle) {
        repositorioDetallebhoras.save(detalle);
    }

    @Override
    public List<Integer> buscarDiasRegistrados(int idbitacora) {
        return repositorioDetallebhoras.buscarDiasRegistrados(idbitacora);
    }

    @Override
    public DETALLEBHORAS obtenerdetalle(int iddetalle) {
        return repositorioDetallebhoras.findById(iddetalle).orElse(null);
    }

    @Override
    public void elimininardetalle(DETALLEBHORAS detalle) {
        repositorioDetallebhoras.delete(detalle);
    }

    @Override
    public Page<DETALLEBHORAS> listaporbitacora(int idbitacora, Pageable pageable) {
        return repositorioDetallebhoras.findByBitacora_IdbitacoraOrderByDiaAsc(idbitacora, pageable);
    }

    @Override
    public List<DETALLEBHORAS> listardetalleparaconsumoporvale(int idvale, int mes, int anio) {
        return repositorioDetallebhoras.listardetalleparaconsumoporvale(idvale, mes, anio);
    }

    @Override
    public boolean fueUsado(int iddestinovale) {
        return repositorioDetallebhoras.existsByDestinovale_Iddestinovale(iddestinovale);
    }

    @Override
    public List<DETALLEBHORAS> obtenerPorBitacora(int idbitacora) {
        return repositorioDetallebhoras.obtenerPorBitacora(idbitacora);
    }
}
