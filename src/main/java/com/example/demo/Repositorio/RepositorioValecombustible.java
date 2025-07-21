package com.example.demo.Repositorio;

import com.example.demo.Entidad.VALECOMBUSTIBLE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;


@Repository("RepositorioValecombustible")
public interface RepositorioValecombustible extends JpaRepository<VALECOMBUSTIBLE, Serializable> {

    @Query("SELECT v FROM VALECOMBUSTIBLE v WHERE v.saldorestante > 0")
    List<VALECOMBUSTIBLE> valesDisponibles();

    @Query("SELECT v FROM VALECOMBUSTIBLE v WHERE CAST(v.nvale AS string) LIKE CONCAT(:fragmento, '%')")
    Page<VALECOMBUSTIBLE> buscarPorCoincidencia(@Param("fragmento") String fragmento, Pageable pageable);


    @Query("SELECT v FROM VALECOMBUSTIBLE v WHERE v.fecha BETWEEN :desde AND :hasta ORDER BY v.fecha ASC")
    Page<VALECOMBUSTIBLE> buscarPorRangoFechas(@Param("desde") Date desde,
                                               @Param("hasta") Date hasta,
                                               Pageable pageable);

    VALECOMBUSTIBLE findByNvale(long nvale);


}
