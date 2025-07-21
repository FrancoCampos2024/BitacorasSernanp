package com.example.demo.Repositorio;

import com.example.demo.Entidad.RESPONSABLE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("RepositorioResponsable")
public interface RepositorioResponsable extends JpaRepository<RESPONSABLE, Integer> {

    @Query("SELECT COUNT(v) > 0 FROM DESTINOVALE v WHERE v.responsable.idresponsable = :id")
    boolean estaRelacionadoConVale(@Param("id") int id);

    @Query("SELECT COUNT(u) > 0 FROM DETALLEBKILOMETRO u WHERE u.responsable.idresponsable = :id")
    boolean estaRelacionadoConDetallebkm(@Param("id") int id);
    @Query("SELECT COUNT(u) > 0 FROM DETALLEBHORAS u WHERE u.responsable.idresponsable = :id")
    boolean estaRelacionadoConDetallebhoras(@Param("id") int id);

    @Query("SELECT r FROM RESPONSABLE r WHERE LOWER(TRIM(r.nombre)) = LOWER(TRIM(:nombre)) " +
            "AND LOWER(TRIM(r.apellidopaterno)) = LOWER(TRIM(:paterno)) " +
            "AND LOWER(TRIM(r.apellidomaterno)) = LOWER(TRIM(:materno))")
    RESPONSABLE buscarPorNombreCompleto(@Param("nombre") String nombre,
                                        @Param("paterno") String paterno,
                                        @Param("materno") String materno);

}
