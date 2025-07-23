package com.example.demo.Controlador;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class InicioControlador {

    @GetMapping("/Bienvenido")
    public String Bienvenido() {
        return "Extras/Bienvenido";
    }

    @GetMapping("/login")
    public String mostrarLogin(HttpServletRequest request) {
        // Si ya tiene sesión activa, redirige según su rol
        if (request.getUserPrincipal() != null) {
            var roles = request.getUserPrincipal().toString();
            // Puedes mejorar esto con SecurityContextHolder si gustas
            return "redirect:/Bienvenido"; // o según el rol
        }

        // Si no hay sesión activa, muestra login
        return "Login/Login";
    }



}
