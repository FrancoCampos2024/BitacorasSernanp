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


@Repository("RepositorioDetallebkilometro")
public interface RepositorioDetallebkilometro extends JpaRepository<DETALLEBKILOMETRO, Serializable> {

    @Query("SELECT dkm FROM DETALLEBKILOMETRO dkm WHERE dkm.bitacora.idbitacora = :idbitacora ORDER BY dkm.dia ASC")
    List<DETALLEBKILOMETRO> listarsegunbitacora(@Param("idbitacora") Integer idbitacora);

    @Query("SELECT dkm.dia FROM DETALLEBKILOMETRO dkm WHERE dkm.bitacora.idbitacora = :idbitacora")
    List<Integer> buscarDiasRegistrados(@Param("idbitacora") int idbitacora);

    Page<DETALLEBKILOMETRO> findByBitacora_IdbitacoraOrderByDiaAsc(int idbitacora, Pageable pageable);

    boolean existsByDestinovale_Iddestinovale(int iddestinovale);

    @Query("SELECT d FROM DETALLEBKILOMETRO d WHERE d.bitacora.idbitacora = :idbitacora")
    List<DETALLEBKILOMETRO> obtenerPorBitacora(@Param("idbitacora") int idbitacora);

    @Query("SELECT d FROM DETALLEBKILOMETRO d WHERE d.destinovale.valeCombustible.idvcombustible = :idvale AND d.bitacora.mes = :mes AND d.bitacora.anio = :anio")
    List<DETALLEBKILOMETRO> listardetalleparaconsumoporvale(@Param("idvale") int idvale, @Param("mes") int mes, @Param("anio") int anio);

}
