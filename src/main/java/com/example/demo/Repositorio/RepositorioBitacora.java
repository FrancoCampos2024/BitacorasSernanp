package com.example.demo.Repositorio;

import com.example.demo.Entidad.BITACORA;
import com.example.demo.Entidad.DETALLEBKILOMETRO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository("RepositorioBitacora")
public interface RepositorioBitacora extends JpaRepository<BITACORA, Serializable> {

    @Query("SELECT b FROM BITACORA b WHERE b.unidad.idunidad = :idUnidad")
    List<BITACORA> listarsegunidunidad(@Param("idUnidad") Integer idUnidad);

    @Query("SELECT b.mes FROM BITACORA b WHERE b.unidad.idunidad = :idUnidad AND b.anio = :anio")
    List<Integer> mesesregistrados(@Param("idUnidad") int idUnidad, @Param("anio") int anio);

    @Query("SELECT b FROM BITACORA b WHERE b.unidad.idunidad = :idUnidad " +
            "AND (:mes = 0 OR b.mes = :mes) " +
            "AND (:anio = 0 OR b.anio = :anio) " +
            "ORDER BY b.anio DESC, b.mes DESC")
    List<BITACORA> filtrarPorUnidadYFecha(
            @Param("idUnidad") Integer idUnidad,
            @Param("mes") Integer mes,
            @Param("anio") Integer anio
    );

    @Query("SELECT b FROM BITACORA b WHERE b.unidad.idunidad = :id ORDER BY b.anio DESC, b.mes DESC")
    List<BITACORA> listarsegunidunidad(@Param("id") int id);


    @Query("SELECT b FROM BITACORA b " +
            "WHERE b.mes = :mes AND b.anio = :anio " +
            "AND (SIZE(b.detalleBKilometro) > 0 OR SIZE(b.detalleBHoras) > 0)")
    Page<BITACORA> findBitacorasConDetallePorMesYAnio(@Param("mes") int mes, @Param("anio") int anio,Pageable pageable);



    @Query("SELECT DISTINCT b.anio FROM BITACORA b WHERE b.unidad.idunidad = :idUnidad ORDER BY b.anio DESC")
    List<Integer> obtenerAniosPorUnidad(@Param("idUnidad") Integer idUnidad);

    Page<BITACORA> findByUnidad_Idunidad(int idUnidad, Pageable pageable);
    Page<BITACORA> findByUnidad_IdunidadAndMes(int idUnidad, int mes, Pageable pageable);
    Page<BITACORA> findByUnidad_IdunidadAndAnio(int idUnidad, int anio, Pageable pageable);
    Page<BITACORA> findByUnidad_IdunidadAndMesAndAnio(int idUnidad, int mes, int anio, Pageable pageable);

    @Query("SELECT DISTINCT b.anio FROM BITACORA b ORDER BY b.anio DESC")
    List<Integer> obtenerAniosConBitacoras();

    Optional<BITACORA>findByUnidad_IdunidadAndMesAndAnio(int idunidad, int mes, int anio);



}
