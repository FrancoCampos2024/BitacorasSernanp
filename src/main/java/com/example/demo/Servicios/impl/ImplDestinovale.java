package com.example.demo.Servicios.impl;

import com.example.demo.Entidad.DESTINOVALE;
import com.example.demo.Repositorio.RepositorioDestinovale;
import com.example.demo.Servicios.ServicioDestinovale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("ServicioDestinovale")
public class ImplDestinovale implements ServicioDestinovale {

    @Autowired
    @Qualifier("RepositorioDestinovale")
    private RepositorioDestinovale repositorioDestinovale;
    @Override
    public List<DESTINOVALE> listarsegunidvale(int id) {
        return List.of();
    }

    @Override
    public DESTINOVALE obtenerPorId(int id) {
        return repositorioDestinovale.findById(id).orElse(null);
    }

    @Override
    public List<DESTINOVALE> valesdisponibles(int idtipocmbustible) {
        return repositorioDestinovale.valesDisponiblesPorTipo(idtipocmbustible);
    }

    @Override
    public void agregarDestinovale(DESTINOVALE destinovale) {
        repositorioDestinovale.save(destinovale);
    }

    @Override
    public void desagsinardestino(DESTINOVALE destinovale) {
        repositorioDestinovale.delete(destinovale);
    }
}
