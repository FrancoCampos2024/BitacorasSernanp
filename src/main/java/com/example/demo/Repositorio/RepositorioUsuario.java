package com.example.demo.Repositorio;

import com.example.demo.Entidad.USUARIO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository("RepositorioUsuario")
public interface RepositorioUsuario extends JpaRepository<USUARIO, Serializable> {
    Optional<USUARIO> findByNombre(String nombre);

}
