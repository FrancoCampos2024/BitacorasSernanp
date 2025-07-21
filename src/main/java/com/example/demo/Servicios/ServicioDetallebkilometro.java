package com.example.demo.Servicios;

import com.example.demo.Entidad.DETALLEBHORAS;
import com.example.demo.Entidad.DETALLEBKILOMETRO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServicioDetallebkilometro {
    abstract public List<DETALLEBKILOMETRO>listaporbitacora(int idbitacora);
    abstract  public List<Integer> buscarDiasRegistrados(int idbitacora);
    abstract  public void agregardetalle(DETALLEBKILOMETRO detalle);
    abstract public DETALLEBKILOMETRO buscarporid(int iddetallekm);
    abstract public void elimininardetalle(DETALLEBKILOMETRO  detallebkilometro);
    abstract public Page<DETALLEBKILOMETRO> listaporbitacora(int idbitacora, Pageable pageable);
    abstract public List<DETALLEBKILOMETRO> listardetalleparaconsumoporvale(int idvale,int mes,int anio);
    abstract public boolean fueUsado(int iddestinovale);
    abstract public List<DETALLEBKILOMETRO> obtenerPorBitacora(int idbitacora);
}
