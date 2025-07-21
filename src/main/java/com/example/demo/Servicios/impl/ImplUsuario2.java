package com.example.demo.Servicios.impl;

import com.example.demo.Entidad.USUARIO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class ImplUsuario2 implements UserDetails {
    private USUARIO usuario;

    public ImplUsuario2(USUARIO usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getNombre();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return usuario.isEstado();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario.isEstado();
    }
}
