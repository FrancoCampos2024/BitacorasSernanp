package com.example.demo.Repositorio;

import com.example.demo.Entidad.DESTINO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("RepositorioDestino")
public interface RepositorioDestino extends JpaRepository<DESTINO, Integer> {

    @Query("SELECT COUNT(dv) > 0 FROM DESTINOVALE dv WHERE dv.destino.iddestino = :id")
    boolean estaRelacionado(@Param("id") int id);

    DESTINO findByDestino(String destino);
}
