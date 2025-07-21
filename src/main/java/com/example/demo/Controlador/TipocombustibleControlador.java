package com.example.demo.Controlador;

import com.example.demo.Entidad.GRIFO;
import com.example.demo.Entidad.TIPOCOMBUSTIBLE;
import com.example.demo.Servicios.ServicioTipocombustible;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("Tipocombustible")
public class TipocombustibleControlador {

    @Autowired
    @Qualifier("ServicioTipocombustible")
    private ServicioTipocombustible servicioTipocombustible;

    @GetMapping("/Listar")
    public String Listar(Model model) {
        List<TIPOCOMBUSTIBLE> tcombustible=servicioTipocombustible.listarTipocombustible();
        model.addAttribute("tcombustibles",tcombustible);
        model.addAttribute("tipo",new TIPOCOMBUSTIBLE());
        return "Combustible/Listar";
    }

    @PostMapping("/Guardar")
    public String Guardar(@ModelAttribute("tcombustible") TIPOCOMBUSTIBLE tcombustible) {
        servicioTipocombustible.añadirTipocombustible(tcombustible);
        return "redirect:/Tipocombustible/Listar";
    }

    @PostMapping("/Editar/{id}")
    public String Editar(@PathVariable int id, @ModelAttribute TIPOCOMBUSTIBLE tipo) {
        TIPOCOMBUSTIBLE existente = servicioTipocombustible.buscarTipocombustible(id);
        System.out.println("Nuevo: "+tipo.getNombre());
        System.out.println("existente: "+existente.getNombre());

        if (existente != null) {
            existente.setNombre(tipo.getNombre());
            servicioTipocombustible.editarTipocombustible(existente);
        }
        return "redirect:/Tipocombustible/Listar";
    }




}
