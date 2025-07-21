package com.example.demo.Servicios.impl;

import com.example.demo.Entidad.VALECOMBUSTIBLE;
import com.example.demo.Repositorio.RepositorioValecombustible;
import com.example.demo.Servicios.ServicioValecombustible;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service("Serviciovalecombustible")
public class ImplValecombustible implements ServicioValecombustible {

    @Autowired
    @Qualifier("RepositorioValecombustible")
    private RepositorioValecombustible repo;

    @Override
    public List<VALECOMBUSTIBLE> listarValecombustible() {
        return repo.findAll();
    }

    @Override
    public void AgregarValecombustible(VALECOMBUSTIBLE val) {
        repo.save(val);
    }

    @Override
    public VALECOMBUSTIBLE obtenerPorId(int id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void editarValecombustible(VALECOMBUSTIBLE val) {
        repo.save(val);
    }

    @Override
    public List<VALECOMBUSTIBLE> valesdisponibles() {
        return repo.valesDisponibles();
    }

    @Override
    public Page<VALECOMBUSTIBLE> listarValecombustiblePaginado(org.springframework.data.domain.Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public VALECOMBUSTIBLE buscarPorNroVale(long nvale) {
        return repo.findByNvale(nvale);
    }

    @Override
    public Page<VALECOMBUSTIBLE> buscarPorCoincidenciaNvale(String texto, Pageable pageable) {
        return repo.buscarPorCoincidencia(texto,pageable);
    }

    @Override
    public Page<VALECOMBUSTIBLE> buscarPorRangoFechas(Date desde, Date hasta, Pageable pageable) {
        return repo.buscarPorRangoFechas(desde,hasta,pageable);
    }


}
