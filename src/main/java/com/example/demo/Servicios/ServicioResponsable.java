package com.example.demo.Servicios;


import com.example.demo.Entidad.RESPONSABLE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServicioResponsable {
    abstract public List<RESPONSABLE> listarResponsables();
    abstract public RESPONSABLE buscarResponsable(int idr);
    abstract public boolean estaRelacionado(int idresponsable);
    abstract public void añadirResponsable(RESPONSABLE responsable);
    abstract RESPONSABLE buscarPorNombreCompleto(String nombre, String paterno, String materno);
    abstract public Page<RESPONSABLE> listarPaginado(Pageable pageable);

}
