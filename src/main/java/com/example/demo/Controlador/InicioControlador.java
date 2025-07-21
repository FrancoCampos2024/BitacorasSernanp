package com.example.demo.Controlador;


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
    public String login() {
        return "Login/Login"; // login.html en templates
    }

}
