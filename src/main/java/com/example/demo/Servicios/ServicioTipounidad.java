package com.example.demo.Servicios;

import com.example.demo.Controlador.TipounidadControlador;
import com.example.demo.Entidad.TIPOUNIDAD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServicioTipounidad {

    abstract public List<TIPOUNIDAD> getTipounidad();
    abstract public void agregarTipounidad(TIPOUNIDAD tipounidad);
    abstract public TIPOUNIDAD buscarTipounidadid(int id);
    abstract public void editarTipounidad(TIPOUNIDAD tipounidad);
    abstract public boolean existePorNombre(String nombre);
    abstract public void eliminarTipounidad(TIPOUNIDAD tipounidad);
    abstract public TIPOUNIDAD buscarpornombre(String nombre);
    abstract public boolean estaRelacionado(int nombre);
    abstract public Page<TIPOUNIDAD> listarPaginado(Pageable pageable);

}
