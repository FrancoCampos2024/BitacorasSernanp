package com.example.demo.Controlador;


import com.example.demo.Entidad.GRIFO;
import com.example.demo.Servicios.ServicioGrifo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("Grifo")
public class GrifoControlador {
    @Autowired
    @Qualifier("Serviciogrifo")
    private ServicioGrifo servicioGrifo;


    @GetMapping("/Listar")
    public String Listargrifo(Model model) {

        List<GRIFO> grifos=servicioGrifo.ListarGrifo();
        model.addAttribute("grifos",grifos);
        model.addAttribute("grifo",new GRIFO());
        return "Grifo/Listar";
    }
    @PostMapping("/Guardar")
    public String Guardarargrifo(@ModelAttribute("grifo") GRIFO grifo) {
        servicioGrifo.añadirGrifo(grifo);
        return "redirect:/Grifo/Listar";
    }

    @PostMapping("/Actualizar/{id}")
    public String actualizar(@PathVariable int id, @ModelAttribute GRIFO grifo) {
        GRIFO existente = servicioGrifo.buscarPorId(id);
        if (existente != null) {
            existente.setNombre(grifo.getNombre());
            servicioGrifo.editargrifo(existente);
        }
        return "redirect:/Grifo/Listar";
    }

}
