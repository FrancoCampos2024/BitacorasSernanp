package com.example.demo.Servicios;

import com.example.demo.Entidad.UNIDADES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ServicioUnidades {
    abstract public List<UNIDADES> listarunidades();
    abstract public void agregarunidad(UNIDADES unidad);
    abstract public UNIDADES buscarporid(int id);
    abstract public List<UNIDADES> buscarPorIdentificadorOTipoUnidad(String identificador, String tipounidad);
    abstract public List<UNIDADES> listarTodoOrdenado();
    abstract public Page<UNIDADES> listarTodoPaginado(Pageable pageable);
    abstract public  Page<UNIDADES> buscarPorIdentificador(String identificador, Pageable pageable);
    abstract public Page<UNIDADES> buscarPorTipoUnidad(String tipoUnidad, Pageable pageable);
    abstract public UNIDADES findByIdentificador(String identificador);


}
