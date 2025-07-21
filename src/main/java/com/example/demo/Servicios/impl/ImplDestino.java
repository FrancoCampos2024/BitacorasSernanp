package com.example.demo.Servicios.impl;

import com.example.demo.Entidad.DESTINO;
import com.example.demo.Repositorio.RepositorioDestino;
import com.example.demo.Servicios.ServicioDestino;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ServicioDestino")
public class ImplDestino implements ServicioDestino {


    @Autowired
    private RepositorioDestino repositorioDestino;

    @Override
    public List<DESTINO> listardestinos() {
        return repositorioDestino.findAll();
    }

    @Override
    public void agregarDestino(DESTINO destino) {
        repositorioDestino.save(destino);
    }

    @Override
    public void editarDestino(DESTINO destino) {
        repositorioDestino.save(destino);
    }
    @Override
    public DESTINO buscarPorId(int iddestino) {
        return repositorioDestino.findById(iddestino).orElse(null);
    }

    @Override
    public boolean estaRelacionado(int iddestino) {
        return repositorioDestino.estaRelacionado(iddestino);
    }

    @Override
    public DESTINO buscarPorNombre(String destino) {
        return repositorioDestino.findByDestino(destino);
    }

    @Override
    public Page<DESTINO> listarPaginado(Pageable pageable) {
        return repositorioDestino.findAll(pageable);
    }
}
