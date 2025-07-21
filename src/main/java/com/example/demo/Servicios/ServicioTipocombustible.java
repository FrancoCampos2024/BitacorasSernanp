package com.example.demo.Servicios;


import com.example.demo.Entidad.TIPOCOMBUSTIBLE;
import com.example.demo.Entidad.TIPOUNIDAD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServicioTipocombustible {

    abstract public List<TIPOCOMBUSTIBLE> listarTipocombustible();
    abstract public TIPOCOMBUSTIBLE buscarTipocombustible(int id);
    abstract public void añadirTipocombustible(TIPOCOMBUSTIBLE tipo);
    abstract public void editarTipocombustible(TIPOCOMBUSTIBLE tipo);
    abstract public boolean existePorNombre(String nombre);
    abstract public TIPOCOMBUSTIBLE buscarpornombre(String nombre);
    abstract public void borrarTipocombustible(TIPOCOMBUSTIBLE tipo);
    abstract public boolean estaRelacionado(int idtipocombustible);
    abstract public Page<TIPOCOMBUSTIBLE> listarPaginado(Pageable pageable);

}
