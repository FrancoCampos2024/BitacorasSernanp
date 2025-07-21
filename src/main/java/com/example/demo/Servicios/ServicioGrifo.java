package com.example.demo.Servicios;

import com.example.demo.Entidad.GRIFO;
import com.example.demo.Entidad.TIPOUNIDAD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServicioGrifo {

    abstract public List<GRIFO> ListarGrifo();
    abstract public void añadirGrifo(GRIFO grifo);
    abstract public GRIFO buscarPorId(int id);
    abstract public void editargrifo(GRIFO grifo);
    abstract public boolean existePorNombre(String nombre);
    abstract public boolean estaRelacionado(int id);
    abstract public GRIFO buscarPorNombre(String nombre);
    abstract public Page<GRIFO> listarPaginado(Pageable pageable);
}
