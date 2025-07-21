package com.example.demo.Servicios;

import com.example.demo.Entidad.DESTINO;
import com.example.demo.Entidad.GRIFO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServicioDestino {
    abstract public List<DESTINO> listardestinos();
    abstract public void agregarDestino(DESTINO destino);
    abstract public void editarDestino(DESTINO destino);
    abstract public DESTINO buscarPorId(int iddestino);
    abstract public boolean estaRelacionado(int iddestino);
    abstract public DESTINO buscarPorNombre(String destino);
    abstract public Page<DESTINO> listarPaginado(Pageable pageable);
}
