package com.example.demo.Repositorio;

import com.example.demo.Entidad.TIPOCOMBUSTIBLE;
import com.example.demo.Entidad.TIPOUNIDAD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;


@Repository("RepositorioTipocombustible")
public interface RepositorioTipocombustible extends JpaRepository<TIPOCOMBUSTIBLE, Serializable> {

    @Query("SELECT COUNT(t) > 0 FROM TIPOCOMBUSTIBLE t WHERE LOWER(t.nombre) = LOWER(:nombre)")
    boolean existePorNombre(@Param("nombre") String nombre);

    @Query("SELECT COUNT(v) > 0 FROM VALECOMBUSTIBLE v WHERE v.tipoCombustible.idtipocombustible = :id")
    boolean estaRelacionadoConVale(@Param("id") int id);

    @Query("SELECT COUNT(u) > 0 FROM UNIDADES u WHERE u.tipoCombustible.idtipocombustible = :id")
    boolean estaRelacionadoConUnidad(@Param("id") int id);



    TIPOCOMBUSTIBLE findByNombre(String nombre);
}
