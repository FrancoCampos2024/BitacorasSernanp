package com.example.demo.Servicios.impl;

import com.example.demo.Entidad.RESPONSABLE;
import com.example.demo.Repositorio.RepositorioResponsable;
import com.example.demo.Servicios.ServicioResponsable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ServicioResponsable")
public class ImpleResponsable implements ServicioResponsable {
    @Autowired
    @Qualifier("RepositorioResponsable")
    private RepositorioResponsable repositorioResponsable;


    @Override
    public List<RESPONSABLE> listarResponsables() {
        return repositorioResponsable.findAll();
    }

    @Override
    public RESPONSABLE buscarResponsable(int idr) {
        return repositorioResponsable.findById(idr).orElse(null);
    }

    @Override
    public boolean estaRelacionado(int idresponsable) {
        return repositorioResponsable.estaRelacionadoConVale(idresponsable) || repositorioResponsable.estaRelacionadoConDetallebkm(idresponsable) || repositorioResponsable.estaRelacionadoConDetallebhoras(idresponsable);
    }

    @Override
    public void añadirResponsable(RESPONSABLE responsable) {
        repositorioResponsable.save(responsable);
    }

    @Override
    public RESPONSABLE buscarPorNombreCompleto(String nombre, String paterno, String materno) {
        return repositorioResponsable.buscarPorNombreCompleto(nombre, paterno, materno);
    }

    @Override
    public Page<RESPONSABLE> listarPaginado(Pageable pageable) {
        return repositorioResponsable.findAll(pageable);
    }
}
