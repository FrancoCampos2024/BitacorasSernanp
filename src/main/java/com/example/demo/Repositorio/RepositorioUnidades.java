package com.example.demo.Repositorio;

import com.example.demo.Entidad.UNIDADES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


@Repository("RepositorioUnidades")
public interface RepositorioUnidades extends JpaRepository<UNIDADES, Serializable> {

    @Query("SELECT u FROM UNIDADES u ORDER BY u.estado DESC, u.idunidad ASC")
    List<UNIDADES> listarTodoOrdenado();

    @Query("SELECT u FROM UNIDADES u " +
            "WHERE " +
            "(:identificador IS NOT NULL AND :identificador <> '' AND :tipoUnidad = '') AND LOWER(u.identificador) LIKE LOWER(CONCAT('%', :identificador, '%')) " +
            "OR (:tipoUnidad IS NOT NULL AND :tipoUnidad <> '' AND :identificador = '') AND LOWER(u.tipoUnidad.nombre) = LOWER(:tipoUnidad) " +
            "OR (:identificador = '' AND :tipoUnidad = '') " + // Para cuando no hay filtros
            "ORDER BY u.estado DESC, u.idunidad ASC")
    List<UNIDADES> buscarPorIdentificadorOTipoUnidad(
            @Param("identificador") String identificador,
            @Param("tipoUnidad") String tipoUnidad);

    Page<UNIDADES> findByIdentificadorContainingIgnoreCase(String identificador, Pageable pageable);
    Page<UNIDADES> findByTipoUnidad_Nombre(String nombre, Pageable pageable);
    UNIDADES findByIdentificador(String identificador);




}
