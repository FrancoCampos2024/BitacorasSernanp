package com.example.demo.Controlador;


import com.example.demo.Entidad.*;
import com.example.demo.Servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Catalogo")
public class CatalogoControlador {

    @Autowired
    private ServicioTipounidad servicioTipounidad;

    @Autowired
    private ServicioTipocombustible servicioTipocombustible;

    @Autowired
    private ServicioDestino servicioDestino;

    @Autowired
    private ServicioGrifo servicioGrifo;

    @Autowired
    private ServicioResponsable servicioResponsable;

    @GetMapping("/Tablasdatosoperativas")
    public String Principal() {
        return "Catalogo/CatalogoPrincipal";
    }

    @GetMapping("/TipoUnidad")
    public String listarTipoUnidad(@RequestParam(defaultValue = "0") int pagina, Model model) {

        Pageable pageable = PageRequest.of(pagina, 10);
        Page<TIPOUNIDAD> tipounidadPage = servicioTipounidad.listarPaginado(pageable);

        Map<Integer, Boolean> deshabilitados = new HashMap<>();
        for (TIPOUNIDAD tipo : tipounidadPage) {
            boolean enUso = servicioTipounidad.estaRelacionado(tipo.getIdtipou());
            deshabilitados.put(tipo.getIdtipou(), enUso);
        }

        model.addAttribute("listaTipoUnidad", tipounidadPage); // Página completa
        model.addAttribute("TipoUnidad", new TIPOUNIDAD());
        model.addAttribute("TipoUnidadbd", new TIPOUNIDAD());
        model.addAttribute("deshabilitados", deshabilitados);

        return "Tipounidad/ListarTipounidad";
    }

    @GetMapping("/AgregarTipounidad")
    public String AgregarTipoUnidad(Model model ,@RequestParam(defaultValue = "0") int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        Page<TIPOUNIDAD> tipounidadPage = servicioTipounidad.listarPaginado(pageable);
        Map<Integer, Boolean> deshabilitados = new HashMap<>();
        for (TIPOUNIDAD tipo : tipounidadPage) {
            boolean enUso = servicioTipounidad.estaRelacionado(tipo.getIdtipou());
            deshabilitados.put(tipo.getIdtipou(), enUso);
        }

        model.addAttribute("listaTipoUnidad", tipounidadPage);
        model.addAttribute("TipoUnidad", new TIPOUNIDAD());
        model.addAttribute("TipoUnidadbd", new TIPOUNIDAD());
        model.addAttribute("deshabilitados", deshabilitados);
        return "Tipounidad/ListarTipounidad";
    }

    @PostMapping("/Agregadotipounidad")
    public String agregadoTipoUnidad(@RequestParam(defaultValue = "0") int pagina,@ModelAttribute("TipoUnidad") TIPOUNIDAD TipoUnidad, Model model, RedirectAttributes redirectAttributes) {
        String Error= validarTipoUnidad(TipoUnidad);
        if (Error!=null) {
            Pageable pageable = PageRequest.of(pagina, 10);
            Page<TIPOUNIDAD> tipounidadPage = servicioTipounidad.listarPaginado(pageable);
            Map<Integer, Boolean> deshabilitados = new HashMap<>();
            for (TIPOUNIDAD tipo : tipounidadPage) {
                boolean enUso = servicioTipounidad.estaRelacionado(tipo.getIdtipou());
                deshabilitados.put(tipo.getIdtipou(), enUso);
            }
            model.addAttribute("listaTipoUnidad", tipounidadPage);
            model.addAttribute("TipoUnidad", TipoUnidad);
            model.addAttribute("Error", Error);
            model.addAttribute("mostrarModalerror",true);
            model.addAttribute("TipoUnidadbd", new TIPOUNIDAD());
            model.addAttribute("deshabilitados", deshabilitados);
            return "Tipounidad/ListarTipounidad";
        }

        servicioTipounidad.agregarTipounidad(TipoUnidad);
        redirectAttributes.addFlashAttribute("guardadoExito", true);
        return "redirect:/Catalogo/TipoUnidad";

    }

    @GetMapping("/EditarTipounidad/{id}")
    public String EditarTipoUnidad(Model model,@PathVariable int id,@RequestParam(defaultValue = "0") int pagina) {

        Pageable pageable = PageRequest.of(pagina, 10);
        Page<TIPOUNIDAD> tipounidadPage = servicioTipounidad.listarPaginado(pageable);
        Map<Integer, Boolean> deshabilitados = new HashMap<>();
        for (TIPOUNIDAD tipo : tipounidadPage) {
            boolean enUso = servicioTipounidad.estaRelacionado(tipo.getIdtipou());
            deshabilitados.put(tipo.getIdtipou(), enUso);
        }
        TIPOUNIDAD tipobd = servicioTipounidad.buscarTipounidadid(id);
        model.addAttribute("listaTipoUnidad", tipounidadPage);
        model.addAttribute("TipoUnidad", new TIPOUNIDAD());
        model.addAttribute("TipoUnidadbd", tipobd);
        model.addAttribute("abrirModalEditar", true);
        model.addAttribute("deshabilitados", deshabilitados);
        return "Tipounidad/ListarTipounidad";
    }

    @PostMapping("/Editadotipounidad/{id}")
    public String EditarTipoUnidad(@RequestParam(defaultValue = "0") int pagina, @ModelAttribute("TipoUnidadbd") TIPOUNIDAD TipoUnidadbd, Model model, RedirectAttributes redirectAttributes, @PathVariable int id) {
        String ErrorE= validarTipoUnidad(TipoUnidadbd);
        if (ErrorE!=null) {
            Pageable pageable = PageRequest.of(pagina, 10);
            Page<TIPOUNIDAD> tipounidadPage = servicioTipounidad.listarPaginado(pageable);
            Map<Integer, Boolean> deshabilitados = new HashMap<>();
            for (TIPOUNIDAD tipo : tipounidadPage) {
                boolean enUso = servicioTipounidad.estaRelacionado(tipo.getIdtipou());
                deshabilitados.put(tipo.getIdtipou(), enUso);
            }
            TipoUnidadbd.setIdtipou(id);
            model.addAttribute("listaTipoUnidad",tipounidadPage);
            model.addAttribute("TipoUnidad", new TIPOUNIDAD());
            model.addAttribute("errorE", ErrorE);
            model.addAttribute("mostrarModalerror",true);
            model.addAttribute("TipoUnidadbd", TipoUnidadbd);
            model.addAttribute("abrirModalEditar", true);
            model.addAttribute("deshabilitados", deshabilitados);
            return "Tipounidad/ListarTipounidad";
        }

        TIPOUNIDAD tipobd= servicioTipounidad.buscarTipounidadid(id);
        tipobd.setNombre(TipoUnidadbd.getNombre());
        tipobd.setMedicion(TipoUnidadbd.getMedicion());
        servicioTipounidad.editarTipounidad(TipoUnidadbd);
        redirectAttributes.addFlashAttribute("guardadoExito", true);
        return "redirect:/Catalogo/TipoUnidad";

    }

    @GetMapping("/Eliminartipounidad/{id}")
    public String eliminartipounidad(@PathVariable int id, RedirectAttributes redirectAttributes) {
        TIPOUNIDAD tipobd = servicioTipounidad.buscarTipounidadid(id);
        servicioTipounidad.eliminarTipounidad(tipobd);
        return "redirect:/Catalogo/TipoUnidad";
    }



    private String validarTipoUnidad(TIPOUNIDAD tipo) {
        if (tipo.getNombre() == null || tipo.getNombre().trim().isEmpty()) {
            return "El campo 'Nombre' no puede estar vacío.";
        }
        if (tipo.getNombre().length() > 255) {
            return "El nombre no debe exceder los 255 caracteres.";
        }

        // Verificar duplicado solo si ya existe con otro ID
        TIPOUNIDAD existente = servicioTipounidad.buscarpornombre(tipo.getNombre().trim());
        if (existente != null && existente.getIdtipou()!=tipo.getIdtipou()){
            return "El nombre ya existe.";
        }

        if (tipo.getMedicion() == null || tipo.getMedicion().trim().isEmpty()) {
            return "Debe seleccionar una opción en 'Medición'.";
        }
        if (!tipo.getMedicion().equals("Km") && !tipo.getMedicion().equals("Horas")) {
            return "La medición debe ser 'Kilómetros' o 'Horas'.";
        }

        return null;
    }





    @GetMapping("/TipoCombustible")
    public String listarTipoCombustible(@RequestParam(defaultValue = "0") int pagina,Model model) {
        Pageable pageable = PageRequest.of(pagina, 10);
        Page<TIPOCOMBUSTIBLE> tipocombustiblePage = servicioTipocombustible.listarPaginado(pageable);
        Map<Integer, Boolean> deshabilitados = new HashMap<>();

        for (TIPOCOMBUSTIBLE tipo : tipocombustiblePage) {
            boolean relacionado = servicioTipocombustible.estaRelacionado(tipo.getIdtipocombustible());
            deshabilitados.put(tipo.getIdtipocombustible(), relacionado);
        }

        model.addAttribute("listaTipoCombustible", tipocombustiblePage);
        model.addAttribute("deshabilitados", deshabilitados);
        model.addAttribute("TipoCombustible", new TIPOCOMBUSTIBLE());
        model.addAttribute("TipoCombustiblebd", new TIPOCOMBUSTIBLE());
        return "TipoCombustible/ListarTipoCombustible";
    }

    @PostMapping("/Agregadotipocombustible")
    public String agregarTipoCombustible(@ModelAttribute("TipoCombustible") TIPOCOMBUSTIBLE tipo,
                                         Model model,
                                         @RequestParam(defaultValue = "0") int pagina,
                                         RedirectAttributes redirectAttributes) {
        String error = validar(tipo);
        if (error != null) {
            Pageable pageable = PageRequest.of(pagina, 10);
            Page<TIPOCOMBUSTIBLE> tipocombustiblePage = servicioTipocombustible.listarPaginado(pageable);
            Map<Integer, Boolean> deshabilitados = new HashMap<>();

            for (TIPOCOMBUSTIBLE tipoc : tipocombustiblePage) {
                boolean relacionado = servicioTipocombustible.estaRelacionado(tipoc.getIdtipocombustible());
                deshabilitados.put(tipoc.getIdtipocombustible(), relacionado);
            }

            model.addAttribute("listaTipoCombustible", tipocombustiblePage);
            model.addAttribute("deshabilitados", deshabilitados);
            model.addAttribute("TipoCombustible", tipo);
            model.addAttribute("TipoCombustiblebd", new TIPOCOMBUSTIBLE());
            model.addAttribute("Error", error);
            model.addAttribute("mostrarModalerror", true);
            return "TipoCombustible/ListarTipoCombustible";
        }

        servicioTipocombustible.añadirTipocombustible(tipo);
        redirectAttributes.addFlashAttribute("guardadoExito", true);
        return "redirect:/Catalogo/TipoCombustible";
    }

    @GetMapping("/EditarTipocombustible/{id}")
    public String editarVista(@PathVariable int id, Model model,@RequestParam(defaultValue = "0") int pagina) {
        TIPOCOMBUSTIBLE encontrado = servicioTipocombustible.buscarTipocombustible(id);
        Pageable pageable = PageRequest.of(pagina, 10);
        Page<TIPOCOMBUSTIBLE> tipocombustiblePage = servicioTipocombustible.listarPaginado(pageable);
        Map<Integer, Boolean> deshabilitados = new HashMap<>();

        for (TIPOCOMBUSTIBLE tipoc : tipocombustiblePage) {
            boolean relacionado = servicioTipocombustible.estaRelacionado(tipoc.getIdtipocombustible());
            deshabilitados.put(tipoc.getIdtipocombustible(), relacionado);
        }

        model.addAttribute("listaTipoCombustible", tipocombustiblePage);
        model.addAttribute("deshabilitados", deshabilitados);
        model.addAttribute("TipoCombustible", new TIPOCOMBUSTIBLE());
        model.addAttribute("TipoCombustiblebd", encontrado);
        model.addAttribute("abrirModalEditar", true);
        return "TipoCombustible/ListarTipoCombustible";
    }

    @PostMapping("/Editadotipocombustible/{id}")
    public String editarTipoCombustible(@ModelAttribute("TipoCombustiblebd") TIPOCOMBUSTIBLE tipo,
                                        Model model,
                                        RedirectAttributes redirectAttributes,
                                        @RequestParam(defaultValue = "0") int pagina,
                                        @PathVariable int id) {
        String error = validar(tipo);
        if (error != null) {
            tipo.setIdtipocombustible(id);
            Pageable pageable = PageRequest.of(pagina, 10);
            Page<TIPOCOMBUSTIBLE> tipocombustiblePage = servicioTipocombustible.listarPaginado(pageable);
            Map<Integer, Boolean> deshabilitados = new HashMap<>();

            for (TIPOCOMBUSTIBLE tipoc : tipocombustiblePage) {
                boolean relacionado = servicioTipocombustible.estaRelacionado(tipoc.getIdtipocombustible());
                deshabilitados.put(tipoc.getIdtipocombustible(), relacionado);
            }

            model.addAttribute("listaTipoCombustible", tipocombustiblePage);
            model.addAttribute("deshabilitados", deshabilitados);
            model.addAttribute("TipoCombustible", new TIPOCOMBUSTIBLE());
            model.addAttribute("TipoCombustiblebd", tipo);
            model.addAttribute("errorE", error);
            model.addAttribute("abrirModalEditar", true);
            model.addAttribute("mostrarModalerror", true);
            return "TipoCombustible/ListarTipoCombustible";
        }

        TIPOCOMBUSTIBLE encontrado = servicioTipocombustible.buscarTipocombustible(id);
        if (encontrado != null) {
            encontrado.setNombre(tipo.getNombre());
            servicioTipocombustible.editarTipocombustible(encontrado);
            redirectAttributes.addFlashAttribute("guardadoExito", true);
        }
        return "redirect:/Catalogo/TipoCombustible";
    }

    private String validar(TIPOCOMBUSTIBLE tipo) {
        if (tipo.getNombre() == null || tipo.getNombre().trim().isEmpty()) {
            return "El campo 'Nombre' no puede estar vacío.";
        }

        String nombre = tipo.getNombre().trim();

        if (nombre.length() > 255) {
            return "El nombre no debe exceder los 255 caracteres.";
        }

        if (nombre.matches("\\d+")) {
            return "El nombre no puede contener solo números. Debe incluir letras.";
        }

        TIPOCOMBUSTIBLE existente = servicioTipocombustible.buscarpornombre(nombre);
        if (existente != null && existente.getIdtipocombustible()!=tipo.getIdtipocombustible()) {
            return "El nombre ya existe.";
        }

        return null;
    }



    @GetMapping("/Grifo")
    public String listarGrifos(Model model,@RequestParam(defaultValue = "0") int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        Page<GRIFO> grifoPage = servicioGrifo.listarPaginado(pageable);
        Map<Integer, Boolean> deshabilitados = new HashMap<>();
        for (GRIFO g : grifoPage) {
            deshabilitados.put(g.getIdgrifo(), servicioGrifo.estaRelacionado(g.getIdgrifo()));
        }
        model.addAttribute("deshabilitados", deshabilitados);

        model.addAttribute("listaGrifos",grifoPage);
        model.addAttribute("Grifo", new GRIFO());
        model.addAttribute("GrifoBd", new GRIFO());
        return "Grifo/ListarGrifo";
    }
    @GetMapping("/AgregarGrifo")
    public String AgregarGrifo(Model model,@RequestParam(defaultValue = "0") int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        Page<GRIFO> grifoPage = servicioGrifo.listarPaginado(pageable);
        Map<Integer, Boolean> deshabilitados = new HashMap<>();
        for (GRIFO g : grifoPage) {
            deshabilitados.put(g.getIdgrifo(), servicioGrifo.estaRelacionado(g.getIdgrifo()));
        }
        model.addAttribute("deshabilitados", deshabilitados);

        model.addAttribute("listaGrifos",grifoPage);
        model.addAttribute("Grifo", new GRIFO());
        model.addAttribute("GrifoBd", new GRIFO());
        return "Grifo/ListarGrifo";
    }

    @PostMapping("/AgregadoGrifo")
    public String agregarGrifo(@ModelAttribute("Grifo") GRIFO grifo,
                               Model model,
                               @RequestParam(defaultValue = "0") int pagina,
                               RedirectAttributes redirectAttributes) {

        String error = validar(grifo);
        if (error != null) {
            Pageable pageable = PageRequest.of(pagina, 10);
            Page<GRIFO> grifoPage = servicioGrifo.listarPaginado(pageable);
            Map<Integer, Boolean> deshabilitados = new HashMap<>();
            for (GRIFO g : grifoPage) {
                deshabilitados.put(g.getIdgrifo(), servicioGrifo.estaRelacionado(g.getIdgrifo()));
            }
            model.addAttribute("deshabilitados", deshabilitados);

            model.addAttribute("listaGrifos",grifoPage);
            model.addAttribute("Grifo", grifo);
            model.addAttribute("GrifoBd", new GRIFO());
            model.addAttribute("Error", error);
            model.addAttribute("mostrarModalerror", true);
            return "Grifo/ListarGrifo";
        }

        grifo.setEstado(true);
        servicioGrifo.añadirGrifo(grifo);
        redirectAttributes.addFlashAttribute("guardadoExito", true);
        return "redirect:/Catalogo/Grifo";
    }

    @GetMapping("/EditarGrifo/{id}")
    public String editargrifo(@PathVariable int id, Model model,@RequestParam(defaultValue = "0") int pagina) {
        GRIFO encontrado = servicioGrifo.buscarPorId(id);
        Pageable pageable = PageRequest.of(pagina, 10);
        Page<GRIFO> grifoPage = servicioGrifo.listarPaginado(pageable);
        Map<Integer, Boolean> deshabilitados = new HashMap<>();
        for (GRIFO g : grifoPage) {
            deshabilitados.put(g.getIdgrifo(), servicioGrifo.estaRelacionado(g.getIdgrifo()));
        }
        model.addAttribute("deshabilitados", deshabilitados);

        model.addAttribute("listaGrifos",grifoPage);
        model.addAttribute("Grifo", new GRIFO());
        model.addAttribute("GrifoBd", encontrado);
        model.addAttribute("abrirModalEditar", true);
        return "Grifo/ListarGrifo";
    }

    @PostMapping("/EditadoGrifo/{id}")
    public String editarGrifo(@ModelAttribute("GrifoBd") GRIFO grifo,
                              Model model,
                              @RequestParam(defaultValue = "0") int pagina,
                              RedirectAttributes redirectAttributes,
                              @PathVariable int id) {

        String error = validar(grifo);
        if (error != null) {
            grifo.setIdgrifo(id);
            Pageable pageable = PageRequest.of(pagina, 10);
            Page<GRIFO> grifoPage = servicioGrifo.listarPaginado(pageable);
            Map<Integer, Boolean> deshabilitados = new HashMap<>();
            for (GRIFO g : grifoPage) {
                deshabilitados.put(g.getIdgrifo(), servicioGrifo.estaRelacionado(g.getIdgrifo()));
            }
            model.addAttribute("deshabilitados", deshabilitados);

            model.addAttribute("listaGrifos",grifoPage);
            model.addAttribute("Grifo", new GRIFO());
            model.addAttribute("GrifoBd", grifo);
            model.addAttribute("errorE", error);
            model.addAttribute("abrirModalEditar", true);
            model.addAttribute("mostrarModalerror", true);
            return "Grifo/ListarGrifo";
        }

        GRIFO encontrado = servicioGrifo.buscarPorId(id);
        if (encontrado != null) {
            encontrado.setNombre(grifo.getNombre());
            servicioGrifo.editargrifo(encontrado);
            redirectAttributes.addFlashAttribute("guardadoExito", true);
        }
        return "redirect:/Catalogo/Grifo";
    }

    @GetMapping("/CambiarEstado/{id}")
    public String cambiarEstadoUnidad(@PathVariable int id) {
        GRIFO grifo = servicioGrifo.buscarPorId(id);
        if (grifo != null) {
            grifo.setEstado(!grifo.isEstado());
            servicioGrifo.añadirGrifo(grifo);
        }
        return "redirect:/Catalogo/Grifo";
    }

    private String validar(GRIFO grifo) {
        if (grifo.getNombre() == null || grifo.getNombre().trim().isEmpty()) {
            return "El campo 'Nombre' no puede estar vacío.";
        }
        String nombre = grifo.getNombre().trim();

        if (nombre.length() > 255) {
            return "El nombre no debe exceder los 255 caracteres.";
        }
        // Verificar duplicado, ignorando el mismo registro que se está editando
        GRIFO existente = servicioGrifo.buscarPorNombre(nombre);
        if (existente != null && existente.getIdgrifo() != grifo.getIdgrifo()) {
            return "El nombre ya está registrado para otro grifo.";
        }
        return null;
    }




    @GetMapping("/Destino")
    public String listarDestinos(Model model, @RequestParam(defaultValue = "0") int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        Page<DESTINO> destinoPage = servicioDestino.listarPaginado(pageable);

        Map<Integer, Boolean> deshabilitados = new HashMap<>();
        for (DESTINO d : destinoPage) {
            deshabilitados.put(d.getIddestino(), servicioDestino.estaRelacionado(d.getIddestino()));
        }
        model.addAttribute("deshabilitados", deshabilitados);
        model.addAttribute("listaDestinos", destinoPage);
        model.addAttribute("Destino", new DESTINO());
        model.addAttribute("destinoBd", new DESTINO());
        return "Destino/ListarDestino";
    }

    @GetMapping("/AgregarDestino")
    public String AgregarDestino(Model model,@RequestParam(defaultValue = "0") int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        Page<DESTINO> destinoPage = servicioDestino.listarPaginado(pageable);

        Map<Integer, Boolean> deshabilitados = new HashMap<>();
        for (DESTINO d : destinoPage) {
            deshabilitados.put(d.getIddestino(), servicioDestino.estaRelacionado(d.getIddestino()));
        }
        model.addAttribute("deshabilitados", deshabilitados);
        model.addAttribute("listaDestinos", destinoPage);
        model.addAttribute("Destino", new DESTINO());
        model.addAttribute("destinoBd", new DESTINO());
        return "Destino/ListarDestino";
    }

    @PostMapping("/AgregadoDestino")
    public String agregadoDestino(@RequestParam(defaultValue = "0") int pagina,@ModelAttribute("Destino") DESTINO destino, Model model, RedirectAttributes redirectAttributes) {
        String error = validarDestino(destino);
        if (error != null) {
            Pageable pageable = PageRequest.of(pagina, 10);
            Page<DESTINO> destinoPage = servicioDestino.listarPaginado(pageable);

            Map<Integer, Boolean> deshabilitados = new HashMap<>();
            for (DESTINO d : destinoPage) {
                deshabilitados.put(d.getIddestino(), servicioDestino.estaRelacionado(d.getIddestino()));
            }
            model.addAttribute("deshabilitados", deshabilitados);
            model.addAttribute("listaDestinos", destinoPage);
            model.addAttribute("Destino", destino);
            model.addAttribute("Error", error);
            model.addAttribute("mostrarModalerror", true);
            model.addAttribute("destinoBd", new DESTINO());
            return "Destino/ListarDestino";
        }

        destino.setEstado(true);
        servicioDestino.agregarDestino(destino);
        redirectAttributes.addFlashAttribute("guardadoExito", true);
        return "redirect:/Catalogo/Destino";
    }

    @GetMapping("/EditarDestino/{id}")
    public String editarDestino(@PathVariable int id, Model model,@RequestParam(defaultValue = "0") int pagina) {
        DESTINO destinoBd = servicioDestino.buscarPorId(id);
        Pageable pageable = PageRequest.of(pagina, 10);
        Page<DESTINO> destinoPage = servicioDestino.listarPaginado(pageable);

        Map<Integer, Boolean> deshabilitados = new HashMap<>();
        for (DESTINO d : destinoPage) {
            deshabilitados.put(d.getIddestino(), servicioDestino.estaRelacionado(d.getIddestino()));
        }
        model.addAttribute("deshabilitados", deshabilitados);
        model.addAttribute("listaDestinos", destinoPage);
        model.addAttribute("Destino", new DESTINO());
        model.addAttribute("destinoBd", destinoBd);
        model.addAttribute("abrirModalEditar", true);
        return "Destino/ListarDestino";
    }

    @PostMapping("/EditadoDestino/{id}")
    public String editadoDestino(@RequestParam(defaultValue = "0") int pagina,@ModelAttribute("destinoBd") DESTINO destinoBd, @PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        String errorE = validarDestino(destinoBd);
        if (errorE != null) {
            destinoBd.setIddestino(id);
            Pageable pageable = PageRequest.of(pagina, 10);
            Page<DESTINO> destinoPage = servicioDestino.listarPaginado(pageable);

            Map<Integer, Boolean> deshabilitados = new HashMap<>();
            for (DESTINO d : destinoPage) {
                deshabilitados.put(d.getIddestino(), servicioDestino.estaRelacionado(d.getIddestino()));
            }
            model.addAttribute("deshabilitados", deshabilitados);
            model.addAttribute("listaDestinos", destinoPage);
            model.addAttribute("Destino", new DESTINO());
            model.addAttribute("errorE", errorE);
            model.addAttribute("mostrarModalerror", true);
            model.addAttribute("destinoBd", destinoBd);
            model.addAttribute("abrirModalEditar", true);
            return "Destino/ListarDestino";
        }

        DESTINO destinoExistente = servicioDestino.buscarPorId(id);
        destinoExistente.setDestino(destinoBd.getDestino());
        servicioDestino.editarDestino(destinoExistente);

        redirectAttributes.addFlashAttribute("guardadoExito", true);
        return "redirect:/Catalogo/Destino";
    }

    @GetMapping("/CambiarEstadoDestino/{id}")
    public String cambiarEstadodestino(@PathVariable int id) {
        DESTINO destino = servicioDestino.buscarPorId(id);
        if (destino != null) {
            destino.setEstado(!destino.isEstado());
            servicioDestino.agregarDestino(destino);
        }
        return "redirect:/Catalogo/Destino";
    }



    private String validarDestino(DESTINO destino) {
        if (destino.getDestino() == null || destino.getDestino().trim().isEmpty()) {
            return "El campo 'Destino' no puede estar vacío.";
        }

        if (destino.getDestino().length() > 255) {
            return "El nombre del destino no debe exceder los 255 caracteres.";
        }

        DESTINO existente = servicioDestino.buscarPorNombre(destino.getDestino().trim());
        if (existente != null && existente.getIddestino() != destino.getIddestino()) {
            return "El destino ya está registrado con otro nombre.";
        }

        return null;
    }


    @GetMapping("/Responsable")
    public String listarResponsables(Model model,@RequestParam(defaultValue = "0") int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        Page<RESPONSABLE> responsablePage = servicioResponsable.listarPaginado(pageable);
        Map<Integer, Boolean> deshabilitados = new HashMap<>();
        for (RESPONSABLE r : responsablePage) {
            deshabilitados.put(r.getIdresponsable(), servicioResponsable.estaRelacionado(r.getIdresponsable()));
        }
        model.addAttribute("deshabilitados", deshabilitados);
        model.addAttribute("listaResponsables", responsablePage);
        model.addAttribute("Responsable", new RESPONSABLE());
        model.addAttribute("ResponsableBd", new RESPONSABLE());
        return "Responsable/ListarResponsable";
    }

    @PostMapping("/AgregadoResponsable")
    public String agregarResponsable(@ModelAttribute("Responsable") RESPONSABLE responsable,
                                     Model model,
                                     @RequestParam(defaultValue = "0") int pagina,
                                     RedirectAttributes redirectAttributes) {

        String error = validar(responsable);
        if (error != null) {
            Pageable pageable = PageRequest.of(pagina, 10);
            Page<RESPONSABLE> responsablePage = servicioResponsable.listarPaginado(pageable);
            Map<Integer, Boolean> deshabilitados = new HashMap<>();
            for (RESPONSABLE r : responsablePage) {
                deshabilitados.put(r.getIdresponsable(), servicioResponsable.estaRelacionado(r.getIdresponsable()));
            }
            model.addAttribute("deshabilitados", deshabilitados);
            model.addAttribute("listaResponsables", responsablePage);
            model.addAttribute("Responsable", responsable);
            model.addAttribute("ResponsableBd", new RESPONSABLE());
            model.addAttribute("Error", error);
            model.addAttribute("mostrarModalerror", true);
            return "Responsable/ListarResponsable";
        }

        responsable.setEstado(true);
        servicioResponsable.añadirResponsable(responsable);
        redirectAttributes.addFlashAttribute("guardadoExito", true);
        return "redirect:/Catalogo/Responsable";
    }

    @GetMapping("/EditarResponsable/{id}")
    public String editarResponsable(@PathVariable int id, Model model,@RequestParam(defaultValue = "0") int pagina) {
        RESPONSABLE encontrado = servicioResponsable.buscarResponsable(id);
        Pageable pageable = PageRequest.of(pagina, 10);
        Page<RESPONSABLE> responsablePage = servicioResponsable.listarPaginado(pageable);
        Map<Integer, Boolean> deshabilitados = new HashMap<>();
        for (RESPONSABLE r : responsablePage) {
            deshabilitados.put(r.getIdresponsable(), servicioResponsable.estaRelacionado(r.getIdresponsable()));
        }
        model.addAttribute("deshabilitados", deshabilitados);
        model.addAttribute("listaResponsables", responsablePage);
        model.addAttribute("Responsable", new RESPONSABLE());
        model.addAttribute("ResponsableBd", encontrado);
        model.addAttribute("abrirModalEditar", true);
        return "Responsable/ListarResponsable";
    }

    @PostMapping("/EditadoResponsable/{id}")
    public String editarResponsablePost(@ModelAttribute("ResponsableBd") RESPONSABLE responsable,
                                        Model model,
                                        @RequestParam(defaultValue = "0") int pagina,
                                        RedirectAttributes redirectAttributes,
                                        @PathVariable int id) {

        String error = validar(responsable);
        if (error != null) {
            responsable.setIdresponsable(id);
            Pageable pageable = PageRequest.of(pagina, 10);
            Page<RESPONSABLE> responsablePage = servicioResponsable.listarPaginado(pageable);
            Map<Integer, Boolean> deshabilitados = new HashMap<>();
            for (RESPONSABLE r : responsablePage) {
                deshabilitados.put(r.getIdresponsable(), servicioResponsable.estaRelacionado(r.getIdresponsable()));
            }
            model.addAttribute("deshabilitados", deshabilitados);
            model.addAttribute("listaResponsables", responsablePage);
            model.addAttribute("Responsable", new RESPONSABLE());
            model.addAttribute("ResponsableBd", responsable);
            model.addAttribute("errorE", error);
            model.addAttribute("abrirModalEditar", true);
            model.addAttribute("mostrarModalerror", true);
            return "Responsable/ListarResponsable";
        }

        RESPONSABLE existente = servicioResponsable.buscarResponsable(id);
        if (existente != null) {
            existente.setNombre(responsable.getNombre());
            existente.setApellidopaterno(responsable.getApellidopaterno());
            existente.setApellidomaterno(responsable.getApellidomaterno());
            servicioResponsable.añadirResponsable(existente);
            redirectAttributes.addFlashAttribute("guardadoExito", true);
        }
        return "redirect:/Catalogo/Responsable";
    }

    @GetMapping("/CambiarEstadoResponsable/{id}")
    public String cambiarEstado(@PathVariable int id) {
        RESPONSABLE r = servicioResponsable.buscarResponsable(id);
        if (r != null) {
            r.setEstado(!r.isEstado());
            servicioResponsable.añadirResponsable(r);
        }
        return "redirect:/Catalogo/Responsable";
    }

    private String validar(RESPONSABLE r) {
        if (r.getNombre() == null || r.getNombre().trim().isEmpty()
                || r.getApellidopaterno() == null || r.getApellidopaterno().trim().isEmpty()
                || r.getApellidomaterno() == null || r.getApellidomaterno().trim().isEmpty()) {
            return "Todos los campos son obligatorios.";
        }

        String nombre = r.getNombre().trim();
        String paterno = r.getApellidopaterno().trim();
        String materno = r.getApellidomaterno().trim();

        if (!nombre.matches(".*[a-zA-ZáéíóúÁÉÍÓÚñÑ].*")
                || !paterno.matches(".*[a-zA-ZáéíóúÁÉÍÓÚñÑ].*")
                || !materno.matches(".*[a-zA-ZáéíóúÁÉÍÓÚñÑ].*")) {
            return "Los campos no deben contener solo números.";
        }

        if ((nombre + paterno + materno).length() > 255) {
            return "Los datos ingresados son demasiado largos.";
        }

        RESPONSABLE duplicado = servicioResponsable.buscarPorNombreCompleto(nombre, paterno, materno);
        if (duplicado != null && duplicado.getIdresponsable() != r.getIdresponsable()) {
            return "Ya existe un responsable con ese nombre completo.";
        }

        return null;
    }






}
