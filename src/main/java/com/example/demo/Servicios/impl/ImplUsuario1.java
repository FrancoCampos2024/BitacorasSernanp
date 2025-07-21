package com.example.demo.Servicios.impl;

import com.example.demo.Entidad.USUARIO;
import com.example.demo.Repositorio.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
@Service
public class ImplUsuario1 implements UserDetailsService {

    @Autowired
    private RepositorioUsuario usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        USUARIO usuario = usuarioRepositorio.findByNombre(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new ImplUsuario2(usuario);
    }
}
