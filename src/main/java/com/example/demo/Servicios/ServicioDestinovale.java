package com.example.demo.Servicios;

import com.example.demo.Entidad.DESTINOVALE;

import java.util.List;

public interface ServicioDestinovale {
    public List<DESTINOVALE> listarsegunidvale(int id);
    abstract public DESTINOVALE obtenerPorId(int id);
    abstract public List<DESTINOVALE> valesdisponibles(int idtipocmbustible);
    abstract public void agregarDestinovale(DESTINOVALE destinovale);
    abstract public void desagsinardestino(DESTINOVALE destinovale);
}
