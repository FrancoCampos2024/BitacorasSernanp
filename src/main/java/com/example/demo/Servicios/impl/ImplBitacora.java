package com.example.demo.Servicios.impl;

import com.example.demo.Entidad.BITACORA;
import com.example.demo.Entidad.DETALLEBKILOMETRO;
import com.example.demo.Repositorio.RepositorioBitacora;
import com.example.demo.Servicios.ServicioBitacora;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@Service("ServicioBitacora")
public class ImplBitacora implements ServicioBitacora {

    @Autowired
    @Qualifier("RepositorioBitacora")
    private RepositorioBitacora repositorioBitacora;

    @Override
    public List<BITACORA> listarsegunidunidad(int id) {
        return repositorioBitacora.listarsegunidunidad(id);
    }

    @Override
    public List<Integer> buscarmessegununidad(int id, int anio) {
        return repositorioBitacora.mesesregistrados(id,anio);
    }

    @Override
    public void agregarbitacora(BITACORA bitacora) {
        repositorioBitacora.save(bitacora);
    }

    @Override
    public BITACORA buscarporid(int idbitacora) {
        return repositorioBitacora.findById(idbitacora).orElse(null);
    }

    @Override
    public List<BITACORA> filtroparahtml(int idunidad,int mes,int anio) {
        return repositorioBitacora.filtrarPorUnidadYFecha(idunidad,mes,anio);
    }

    @Override
    public List<Integer> obtenerAniosPorUnidad(int idunidad) {
        return repositorioBitacora.obtenerAniosPorUnidad(idunidad);
    }



    @Override
    public Page<BITACORA> listarporUnidad(int idUnidad, Pageable pageable) {
        return repositorioBitacora.findByUnidad_Idunidad(idUnidad, pageable);
    }

    @Override
    public Page<BITACORA> listarporUnidadYMes(int idUnidad, int mes, Pageable pageable) {
        return repositorioBitacora.findByUnidad_IdunidadAndMes(idUnidad, mes, pageable);
    }

    @Override
    public Page<BITACORA> listarporUnidadYAnio(int idUnidad, int anio, Pageable pageable) {
        return repositorioBitacora.findByUnidad_IdunidadAndAnio(idUnidad, anio, pageable);
    }

    @Override
    public Page<BITACORA> listarporUnidadYMesYAnio(int idUnidad, int mes, int anio, Pageable pageable) {
        return repositorioBitacora.findByUnidad_IdunidadAndMesAndAnio(idUnidad, mes, anio, pageable);
    }

    @Override
    public Page<BITACORA> obtenerBitacorasConDetallePorMesYAnio(int mes, int anio,Pageable pageable) {
        return repositorioBitacora.findBitacorasConDetallePorMesYAnio(mes,anio,pageable);
    }

    @Override
    public List<Integer> obtenerAniosDisponibles() {
        return repositorioBitacora.obtenerAniosConBitacoras();
    }

    @Override
    public boolean existeBitacora(int idunidad, int mes, int anio) {
        Optional<BITACORA> bitacora = repositorioBitacora.findByUnidad_IdunidadAndMesAndAnio(idunidad, mes, anio);
        return bitacora.isPresent();
    }


}
