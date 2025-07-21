package com.example.demo.Repositorio;

import com.example.demo.Entidad.GRIFO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;


@Repository("RepositorioGrifo")
public interface RepositorioGrifo extends JpaRepository<GRIFO, Serializable> {
    @Query("SELECT COUNT(t) > 0 FROM GRIFO t WHERE LOWER(t.nombre) = LOWER(:nombre)")
    boolean existePorNombre(@Param("nombre") String nombre);

    @Query("SELECT COUNT(v) > 0 FROM VALECOMBUSTIBLE v WHERE v.grifo.idgrifo = :id")
    boolean estaRelacionado(@Param("id") int id);

    GRIFO findByNombre(String nombre);


}
