package com.example.demo.Servicios.impl;

import com.example.demo.Entidad.TIPOUNIDAD;
import com.example.demo.Repositorio.RepositorioTipounidad;
import com.example.demo.Servicios.ServicioTipounidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("ServicioTipounidad")
public class ImplTipounidad implements ServicioTipounidad {
    @Autowired
    @Qualifier("RepositorioTipounidad")
    private RepositorioTipounidad repositorioTipounidad;

    @Override
    public List<TIPOUNIDAD> getTipounidad() {
        return repositorioTipounidad.findAll();
    }

    @Override
    public void agregarTipounidad(TIPOUNIDAD tipounidad) {
        repositorioTipounidad.save(tipounidad);
    }

    @Override
    public TIPOUNIDAD buscarTipounidadid(int id) {
        return repositorioTipounidad.findById(id).orElse(null);
    }

    @Override
    public void editarTipounidad(TIPOUNIDAD tipounidad) {
        repositorioTipounidad.save(tipounidad);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return repositorioTipounidad.existePorNombre(nombre);
    }

    @Override
    public void eliminarTipounidad(TIPOUNIDAD tipounidad) {
        repositorioTipounidad.delete(tipounidad);
    }

    @Override
    public TIPOUNIDAD buscarpornombre(String nombre) {
        return repositorioTipounidad.findByNombre(nombre);
    }

    @Override
    public boolean estaRelacionado(int id) {
        return repositorioTipounidad.estaRelacionado(id);
    }

    @Override
    public Page<TIPOUNIDAD> listarPaginado(Pageable pageable) {
        return repositorioTipounidad.findAll(pageable);
    }
}
