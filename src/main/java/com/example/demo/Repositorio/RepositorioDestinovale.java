package com.example.demo.Repositorio;

import com.example.demo.Entidad.DESTINOVALE;
import com.example.demo.Entidad.VALECOMBUSTIBLE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository("RepositorioDestinovale")
public interface RepositorioDestinovale extends JpaRepository<DESTINOVALE, Serializable> {
    @Query("SELECT v FROM DESTINOVALE v " +
            "WHERE v.saldorestante > 0 " +
            "AND v.valeCombustible.tipoCombustible.idtipocombustible = :idTipo")
    List<DESTINOVALE> valesDisponiblesPorTipo(@Param("idTipo") int idTipo);

}
