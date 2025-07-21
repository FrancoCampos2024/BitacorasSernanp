package com.example.demo.Servicios;

import com.example.demo.Entidad.DETALLEBHORAS;
import com.example.demo.Entidad.DETALLEBKILOMETRO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServicioDetallebhoras {
    abstract public List<DETALLEBHORAS>listaporbitacora(int idbitacora);
    abstract  public void agregardetalle(DETALLEBHORAS detalle);
    abstract  public List<Integer> buscarDiasRegistrados(int idbitacora);
    abstract public DETALLEBHORAS obtenerdetalle(int iddetalle);
    abstract public void elimininardetalle(DETALLEBHORAS detalle);

    abstract public Page<DETALLEBHORAS> listaporbitacora(int idbitacora, Pageable pageable);
    abstract public List<DETALLEBHORAS> listardetalleparaconsumoporvale(int idvale, int mes, int anio);
    abstract public boolean fueUsado(int iddestinovale);
    abstract public List<DETALLEBHORAS> obtenerPorBitacora(int idbitacora);
}
