package com.example.demo.Servicios.impl;

import com.example.demo.Entidad.UNIDADES;
import com.example.demo.Repositorio.RepositorioUnidades;
import com.example.demo.Servicios.ServicioUnidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("ServicioUnidades")
public class ImplUnidades implements ServicioUnidades {
    @Autowired
    @Qualifier("RepositorioUnidades")
    private RepositorioUnidades repositorioUnidades;

    @Override
    public List<UNIDADES> listarunidades() {
        return repositorioUnidades.findAll();
    }

    @Override
    public void agregarunidad(UNIDADES unidad) {
        repositorioUnidades.save(unidad);
    }

    @Override
    public UNIDADES buscarporid(int id) {
        return repositorioUnidades.findById(id).orElse(null);
    }

    @Override
    public List<UNIDADES> buscarPorIdentificadorOTipoUnidad(String identificador, String tipounidad) {
        return repositorioUnidades.buscarPorIdentificadorOTipoUnidad(identificador, tipounidad);
    }

    @Override
    public List<UNIDADES> listarTodoOrdenado() {
        return repositorioUnidades.listarTodoOrdenado();
    }

    @Override
    public Page<UNIDADES> listarTodoPaginado(Pageable pageable) {
        return repositorioUnidades.findAll(pageable);
    }

    @Override
    public Page<UNIDADES> buscarPorIdentificador(String identificador, Pageable pageable) {
        return repositorioUnidades.findByIdentificadorContainingIgnoreCase(identificador, pageable);
    }

    @Override
    public Page<UNIDADES> buscarPorTipoUnidad(String tipoUnidad, Pageable pageable) {
        return repositorioUnidades.findByTipoUnidad_Nombre(tipoUnidad, pageable);
    }

    @Override
    public UNIDADES findByIdentificador(String identificador) {
        return repositorioUnidades.findByIdentificador(identificador);
    }
}
