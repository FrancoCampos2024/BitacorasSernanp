package com.example.demo.Servicios;

import com.example.demo.Entidad.BITACORA;
import com.example.demo.Entidad.DETALLEBKILOMETRO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServicioBitacora {
    abstract public List<BITACORA>listarsegunidunidad(int id);

    abstract public  List<Integer> buscarmessegununidad(int id, int anio);
    abstract public  void agregarbitacora(BITACORA bitacora);

    abstract public  BITACORA buscarporid(int idbitacora);
    abstract public  List<BITACORA> filtroparahtml(int idunidad,int mes,int anio);
    abstract public  List<Integer> obtenerAniosPorUnidad(int idunidad);
    abstract public Page<BITACORA> listarporUnidad(int idUnidad, Pageable pageable);
    abstract public Page<BITACORA> listarporUnidadYMes(int idUnidad, int mes, Pageable pageable);
    abstract public Page<BITACORA> listarporUnidadYAnio(int idUnidad, int anio, Pageable pageable);
    abstract public Page<BITACORA> listarporUnidadYMesYAnio(int idUnidad, int mes, int anio, Pageable pageable);
    abstract public Page<BITACORA> obtenerBitacorasConDetallePorMesYAnio(int mes, int anio,Pageable pageable);
    abstract public List<Integer> obtenerAniosDisponibles();
    abstract public boolean existeBitacora(int idunidad, int mes, int anio);

}
