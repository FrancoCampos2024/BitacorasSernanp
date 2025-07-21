package com.example.demo.Servicios.impl;

import com.example.demo.Entidad.GRIFO;
import com.example.demo.Repositorio.RepositorioGrifo;
import com.example.demo.Servicios.ServicioGrifo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("Serviciogrifo")
public class ImplGrifo implements ServicioGrifo {

    @Autowired
    @Qualifier("RepositorioGrifo")
    private RepositorioGrifo repositorioGrifo;

    @Override
    public List<GRIFO> ListarGrifo() {
        return repositorioGrifo.findAll();
    }

    @Override
    public void añadirGrifo(GRIFO grifo) {
        repositorioGrifo.save(grifo);
    }

    @Override
    public GRIFO buscarPorId(int id) {
        return repositorioGrifo.findById(id).orElse(null);
    }

    @Override
    public void editargrifo(GRIFO grifo) {
        repositorioGrifo.save(grifo);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return repositorioGrifo.existePorNombre(nombre);
    }

    @Override
    public boolean estaRelacionado(int id) {
        return repositorioGrifo.estaRelacionado(id);
    }

    @Override
    public GRIFO buscarPorNombre(String nombre) {
        return repositorioGrifo.findByNombre(nombre);
    }

    @Override
    public Page<GRIFO> listarPaginado(Pageable pageable) {
        return repositorioGrifo.findAll(pageable);
    }

}
