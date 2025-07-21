package com.example.demo.Servicios;

import com.example.demo.Entidad.VALECOMBUSTIBLE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;

public interface ServicioValecombustible {
        abstract public List<VALECOMBUSTIBLE>listarValecombustible();
        abstract public void AgregarValecombustible(VALECOMBUSTIBLE val);
        abstract public VALECOMBUSTIBLE obtenerPorId(int id);
        abstract public void editarValecombustible(VALECOMBUSTIBLE val);
        abstract public List<VALECOMBUSTIBLE> valesdisponibles();
        abstract Page<VALECOMBUSTIBLE> listarValecombustiblePaginado(Pageable pageable);
        abstract public VALECOMBUSTIBLE buscarPorNroVale(long nvale);
        abstract public Page<VALECOMBUSTIBLE> buscarPorCoincidenciaNvale(String texto,Pageable pageable);
        abstract public Page<VALECOMBUSTIBLE> buscarPorRangoFechas(Date desde, Date hasta, Pageable pageable);
}
