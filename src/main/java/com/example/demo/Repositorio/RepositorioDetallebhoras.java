package com.example.demo.Repositorio;

import com.example.demo.Entidad.DETALLEBHORAS;
import com.example.demo.Entidad.DETALLEBKILOMETRO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository("RepositorioDetallebhoras")
public interface RepositorioDetallebhoras extends JpaRepository<DETALLEBHORAS, Serializable> {

    @Query("SELECT dh FROM DETALLEBHORAS dh WHERE dh.bitacora.idbitacora = :idbitacora ORDER BY dh.dia ASC")
    List<DETALLEBHORAS> listarsegunbitacora(@Param("idbitacora") Integer idbitacora);

    @Query("SELECT dh.dia FROM DETALLEBHORAS dh WHERE dh.bitacora.idbitacora = :idbitacora")
    List<Integer> buscarDiasRegistrados(@Param("idbitacora") int idbitacora);

    boolean existsByDestinovale_Iddestinovale(int iddestinovale);

    Page<DETALLEBHORAS> findByBitacora_IdbitacoraOrderByDiaAsc(int idbitacora, Pageable pageable);

    @Query("SELECT d FROM DETALLEBHORAS d WHERE d.destinovale.valeCombustible.idvcombustible = :idvale AND d.bitacora.mes = :mes AND d.bitacora.anio = :anio")
    List<DETALLEBHORAS> listardetalleparaconsumoporvale(@Param("idvale") int idvale, @Param("mes") int mes, @Param("anio") int anio);

    @Query("SELECT d FROM DETALLEBHORAS d WHERE d.bitacora.idbitacora = :idbitacora")
    List<DETALLEBHORAS> obtenerPorBitacora(@Param("idbitacora") int idbitacora);

}
