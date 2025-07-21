package com.example.demo.Servicios.impl;

import com.example.demo.Controlador.TipocombustibleControlador;
import com.example.demo.Entidad.TIPOCOMBUSTIBLE;
import com.example.demo.Entidad.TIPOUNIDAD;
import com.example.demo.Repositorio.RepositorioTipocombustible;
import com.example.demo.Servicios.ServicioTipocombustible;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("ServicioTipocombustible")
public class ImplTipocombustible implements ServicioTipocombustible {
    @Autowired
    @Qualifier("RepositorioTipocombustible")
    private RepositorioTipocombustible repositorioTipocombustible;

    @Override
    public List<TIPOCOMBUSTIBLE> listarTipocombustible() {
        return repositorioTipocombustible.findAll();
    }

    @Override
    public TIPOCOMBUSTIBLE buscarTipocombustible(int id) {
        return repositorioTipocombustible.findById(id).orElse(null);
    }

    @Override
    public void añadirTipocombustible(TIPOCOMBUSTIBLE tipo) {
        repositorioTipocombustible.save(tipo);
    }

    @Override
    public void editarTipocombustible(TIPOCOMBUSTIBLE tipo) {
        repositorioTipocombustible.save(tipo);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return repositorioTipocombustible.existePorNombre(nombre);
    }

    @Override
    public TIPOCOMBUSTIBLE buscarpornombre(String nombre) {
        return repositorioTipocombustible.findByNombre(nombre);
    }

    @Override
    public void borrarTipocombustible(TIPOCOMBUSTIBLE tipo) {
        repositorioTipocombustible.delete(tipo);
    }

    @Override
    public boolean estaRelacionado(int idtipocombustible) {
        return repositorioTipocombustible.estaRelacionadoConVale(idtipocombustible) || repositorioTipocombustible.estaRelacionadoConUnidad(idtipocombustible);
    }

    @Override
    public Page<TIPOCOMBUSTIBLE> listarPaginado(Pageable pageable) {
        return repositorioTipocombustible.findAll(pageable);
    }


}
