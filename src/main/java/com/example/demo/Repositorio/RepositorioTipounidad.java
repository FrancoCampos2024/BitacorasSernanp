package com.example.demo.Repositorio;

import com.example.demo.Entidad.TIPOUNIDAD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;


@Repository("RepositorioTipounidad")
public interface RepositorioTipounidad extends JpaRepository<TIPOUNIDAD, Serializable> {

    @Query("SELECT COUNT(t) > 0 FROM TIPOUNIDAD t WHERE LOWER(t.nombre) = LOWER(:nombre)")
    boolean existePorNombre(@Param("nombre") String nombre);

    @Query("SELECT COUNT(u) > 0 FROM UNIDADES u WHERE u.tipoUnidad.idtipou = :id")
    boolean estaRelacionado(@Param("id") int id);


    TIPOUNIDAD findByNombre(String nombre);
}
