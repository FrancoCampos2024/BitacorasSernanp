package com.example.demo.Controlador;


import com.example.demo.Entidad.*;
import com.example.demo.Servicios.*;
import com.example.demo.Servicios.impl.ImplBitacora;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;



@Controller
@RequestMapping("Bitacoras")
public class BitacoraControlador {

    @Autowired
    @Qualifier("ServicioUnidades")
    private ServicioUnidades servicioUnidades;
    @Autowired
    private ServicioBitacora servicioBitacora;
    @Autowired
    private ServicioDetallebhoras servicioDetallebhoras;
    @Autowired
    private ServicioDetallebkilometro servicioDetallebkilometro;
    @Autowired
    private ServicioResponsable servicioResponsable;
    @Autowired
    private ServicioValecombustible servicioValecombustible;
    @Autowired
    private ServicioDestinovale servicioDestinovale;
    @Autowired
    private ServletContext servletContext;

    @GetMapping("/Listabitacoras/{id}")
    public String listarbitacorasporUnidades(
            @PathVariable int id,
            @RequestParam(name = "mes", required = false, defaultValue = "0") int mes,
            @RequestParam(name = "anio", required = false, defaultValue = "0") int anio,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 5, Sort.by("anio").descending().and(Sort.by("mes").descending()));
        Page<BITACORA> bitacorasPage = obtenerBitacorasPorFiltros(id, mes, anio, pageable);


        // Datos para filtros
        List<Integer> mesesRegistrados = servicioBitacora.buscarmessegununidad(id, LocalDate.now().getYear());
        String[] nombresMeses = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        List<Map<String, Object>> mesesDisponibles = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            if (!mesesRegistrados.contains(i)) {
                Map<String, Object> mesDisp = new HashMap<>();
                mesDisp.put("numero", i);
                mesDisp.put("nombre", nombresMeses[i - 1]);
                mesesDisponibles.add(mesDisp);
            }
        }

        BITACORA bitacora = new BITACORA();
        bitacora.setAnio(LocalDate.now().getYear());

        model.addAttribute("bitacora", bitacora);
        model.addAttribute("unidad", servicioUnidades.buscarporid(id));
        model.addAttribute("bitacorasPage", bitacorasPage);
        model.addAttribute("mesesDisponibles", mesesDisponibles);
        model.addAttribute("anios", servicioBitacora.obtenerAniosPorUnidad(id));
        model.addAttribute("mesSel", mes);
        model.addAttribute("anioSel", anio);

        return "Bitacoras/ListaBitacoras";
    }

    public Page<BITACORA> obtenerBitacorasPorFiltros(int idUnidad, int mes, int anio, Pageable pageable) {
        if (mes == 0 && anio == 0) {
            return servicioBitacora.listarporUnidad(idUnidad, pageable);
        } else if (mes == 0) {
            return servicioBitacora.listarporUnidadYAnio(idUnidad, anio, pageable);
        } else if (anio == 0) {
            return servicioBitacora.listarporUnidadYMes(idUnidad, mes, pageable);
        } else {
            return servicioBitacora.listarporUnidadYMesYAnio(idUnidad, mes, anio, pageable);
        }
    }



    @PostMapping("/Agregar/{id}")
    public String agregarBitacora(@ModelAttribute BITACORA bitacora,
                                  @PathVariable int id,

                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        int anio = bitacora.getAnio();
        int mes = bitacora.getMes();

        // Validaciones
        if (anio < 2017 || anio == 0) {
            redirectAttributes.addFlashAttribute("mostrarModal", true);
            redirectAttributes.addFlashAttribute("advertencia", "El año ingresado no es válido. Debe ser mayor o igual a 2017.");
            redirectAttributes.addFlashAttribute("bitacora", bitacora); // conservar datos
            return "redirect:/Bitacoras/Listabitacoras/" + id;
        }

        if (mes <= 0 || mes > 12) {
            redirectAttributes.addFlashAttribute("mostrarModal", true);
            redirectAttributes.addFlashAttribute("advertencia", "Debe seleccionar un mes válido.");
            redirectAttributes.addFlashAttribute("bitacora", bitacora);
            return "redirect:/Bitacoras/Listabitacoras/" + id;
        }

        if(servicioBitacora.existeBitacora(id, anio, mes)) {
            redirectAttributes.addFlashAttribute("mostrarModal", true);
            redirectAttributes.addFlashAttribute("advertencia", "Ya existe una bitácora para ese mes y año.");
            redirectAttributes.addFlashAttribute("bitacora", bitacora);
            return "redirect:/Bitacoras/Listabitacoras/" + id;
        }

        // Si pasa validaciones
        bitacora.setUnidad(servicioUnidades.buscarporid(id));
        servicioBitacora.agregarbitacora(bitacora);

        redirectAttributes.addFlashAttribute("guardadoExito", true);
        return "redirect:/Bitacoras/Listabitacoras/" + id;
    }


    @GetMapping("/MesesDisponibles")
    @ResponseBody
    public List<Map<String, Object>> mesesDisponibles(@RequestParam int idUnidad, @RequestParam int anio) {
        List<Integer> mesesRegistrados = servicioBitacora.buscarmessegununidad(idUnidad, anio);

        String[] nombresMeses = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        int mesActual = LocalDate.now().getMonthValue(); // ej. 7 (julio)
        int anioActual = LocalDate.now().getYear();       // ej. 2025

        List<Map<String, Object>> mesesDisponibles = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            boolean esMesNoRegistrado = !mesesRegistrados.contains(i);
            boolean esAnioPasado = anio < anioActual;
            boolean esAnioActualYMesValido = (anio == anioActual && i <= mesActual);

            if (esMesNoRegistrado && (esAnioPasado || esAnioActualYMesValido)) {
                Map<String, Object> mes = new HashMap<>();
                mes.put("numero", i);
                mes.put("nombre", nombresMeses[i - 1]);
                mesesDisponibles.add(mes);
            }
        }

        return mesesDisponibles;
    }




    @GetMapping("/Detallebitacora/{idb}")
    public String detallebitacora(@PathVariable int idb,
                                  @RequestParam(defaultValue = "0") int page,
                                  Model model) {

        BITACORA bitacora = servicioBitacora.buscarporid(idb);
        model.addAttribute("bitacora", bitacora);

        // Si la unidad mide por Kilómetros
        if (bitacora.getUnidad().getTipoUnidad().getMedicion().equalsIgnoreCase("Km")) {
            Pageable pageable = PageRequest.of(page, 6);
            Page<DETALLEBKILOMETRO>detalles=servicioDetallebkilometro.listaporbitacora(idb, pageable);
            model.addAttribute("detallebitacorakmPage", detalles);
            return "Bitacoras/DetalleBitacoraKM";
        } else {
            // Si la unidad mide por Horas —> aplicar paginación
            Pageable pageable = PageRequest.of(page, 6);
            Page<DETALLEBHORAS> detalles = servicioDetallebhoras.listaporbitacora(idb, pageable);
            model.addAttribute("detallebitacorahPage", detalles);
            return "Bitacoras/DetalleBitacorah";
        }
    }



    @GetMapping("/Agregardetallebitacora/{idb}")
    public String agregardetallebitacora(@PathVariable int idb, Model model) {
        BITACORA bitacora = servicioBitacora.buscarporid(idb);
        model.addAttribute("bitacora",bitacora);
        model.addAttribute("diadisponibles",obtenerDiasDisponibles(idb,bitacora.getAnio(),bitacora.getMes()));
        model.addAttribute("responsables",servicioResponsable.listarResponsables());
        model.addAttribute("vales", servicioDestinovale.valesdisponibles(bitacora.getUnidad().getTipoCombustible().getIdtipocombustible()));

        if(bitacora.getUnidad().getTipoUnidad().getMedicion().equals("Km")){
            model.addAttribute("DetallebitacoraKM",new DETALLEBKILOMETRO());
            return "Bitacoras/AgregarDetalleBitacoraKM";
        }else{
            model.addAttribute("Detallebitacorah",new DETALLEBHORAS());
            return "Bitacoras/AgregarDetalleBitacorah";
        }
    }


    @PostMapping("/Agregadodetallebitacorah/{idb}")
    public String agregardetallebitacorah(
            @ModelAttribute DETALLEBHORAS detallebhoras,
            @RequestParam(name = "responsable.idresponsable", required = false) Integer idResponsable,
            @RequestParam(name = "destinovale.iddestinovale", required = false) Integer iddVale,
            @PathVariable int idb,
            Model model,
            RedirectAttributes redirectAttributes) {

        BITACORA bitacora = servicioBitacora.buscarporid(idb);
        detallebhoras.setBitacora(bitacora);

        String mensaje = validarDetalleHoras(detallebhoras, idResponsable, iddVale);

        if (mensaje != null) {
            model.addAttribute("bitacora", bitacora);
            model.addAttribute("Detallebitacorah", detallebhoras);
            model.addAttribute("vales", servicioDestinovale.valesdisponibles(bitacora.getUnidad().getTipoCombustible().getIdtipocombustible()));
            model.addAttribute("responsables", servicioResponsable.listarResponsables());
            model.addAttribute("diadisponibles",obtenerDiasDisponibles(idb,bitacora.getAnio(),bitacora.getMes()));
            model.addAttribute("errorValidacion", mensaje);
            return "Bitacoras/AgregarDetalleBitacorah";
        }

        if (iddVale != null && iddVale > 0) {
            DESTINOVALE desv= servicioDestinovale.obtenerPorId(iddVale);
            desv.setSaldorestante(desv.getSaldorestante()-detallebhoras.getCombustible());
            servicioDestinovale.agregarDestinovale(desv);
            detallebhoras.setDestinovale(servicioDestinovale.obtenerPorId(iddVale));
        } else {
            detallebhoras.setDestinovale(null);
        }

        detallebhoras.setResponsable(servicioResponsable.buscarResponsable(idResponsable));
        servicioDetallebhoras.agregardetalle(detallebhoras);
        redirectAttributes.addFlashAttribute("guardadoExito", true);
        return "redirect:/Bitacoras/Detallebitacora/" + bitacora.getIdbitacora();
    }


    private String validarDetalleHoras(DETALLEBHORAS detalle, Integer idResponsable, Integer idVale) {
        Integer dia = detalle.getDia();
        LocalTime hinicio = detalle.getHinicio();
        LocalTime hfinal = detalle.getHfinal();
        Float hoperacion = detalle.getHoperacion();
        Float combustible = detalle.getCombustible();
        String justificacion = detalle.getJustificacion();
        String destino = detalle.getDestino();

        // Validación básica de campos obligatorios
        if (dia == null || dia <= 0) {
            return "Debes seleccionar un día.";
        }
        if (hinicio == null || hfinal == null) {
            return "Debes ingresar hora de inicio y fin.";
        }
        if (hoperacion == null || hoperacion <= 0) {
            return "Las horas de operación deben ser mayores a 0.";
        }
        if (justificacion == null || justificacion.trim().isEmpty()) {
            return "Debes ingresar una justificación.";
        }
        if (destino == null || destino.trim().isEmpty()) {
            return "Debes indicar el destino o uso del generador.";
        }

        // Validación de vale + combustible
        boolean usaCombustible = combustible != null && combustible > 0;
        boolean seleccionoVale = idVale != null && idVale > 0;

        if (usaCombustible && !seleccionoVale) {
            return "Si usas combustible, debes seleccionar un vale.";
        }

        if (!usaCombustible && seleccionoVale) {
            return "Has seleccionado un vale, pero no se ingresó cantidad de combustible.";
        }

        if (usaCombustible && seleccionoVale) {
            DESTINOVALE dv = servicioDestinovale.obtenerPorId(idVale);
            if (dv == null) {
                return "El vale seleccionado no existe.";
            }
            if (combustible > dv.getSaldorestante()) {
                return "La cantidad de combustible supera el saldo disponible del vale (Saldo: " + dv.getSaldorestante() + " gls).";
            }
            detalle.setDestinovale(dv);
        } else {
            detalle.setDestinovale(null);
        }

        if (idResponsable == null || idResponsable == 0) {
            return "Debes seleccionar un responsable.";
        }

        return null; // Sin errores
    }


    @PostMapping("/Agregadodetallebitacorakm/{idb}")
    public String agregardetallebitacorakm(@ModelAttribute DETALLEBKILOMETRO detallebkilometro,
                                           @PathVariable int idb,
                                           @RequestParam(name = "responsable.idresponsable", required = false) Integer idResponsable,
                                           @RequestParam(name = "destinovale.iddestinovale", required = false) Integer iddVale,
                                           RedirectAttributes redirectAttributes, Model model) {

        BITACORA bitacora = servicioBitacora.buscarporid(idb);
        detallebkilometro.setBitacora(bitacora);

        // Validación
        String mensajeValidacion = validarDetalleKilometro(detallebkilometro, idResponsable, iddVale);
        if (mensajeValidacion != null) {
            // Reenviamos los datos al formulario para mantener la selección
            model.addAttribute("DetallebitacoraKM", detallebkilometro);
            model.addAttribute("bitacora", bitacora);
            model.addAttribute("responsables", servicioResponsable.listarResponsables());
            model.addAttribute("vales", servicioDestinovale.valesdisponibles(bitacora.getUnidad().getTipoCombustible().getIdtipocombustible()));
            model.addAttribute("diadisponibles", obtenerDiasDisponibles(bitacora.getIdbitacora(), bitacora.getAnio(), bitacora.getMes()));
            model.addAttribute("mensajeError", mensajeValidacion);
            return "Bitacoras/AgregarDetalleBitacoraKM";
        }

        if (iddVale != null && iddVale > 0) {
            DESTINOVALE desv= servicioDestinovale.obtenerPorId(iddVale);
            desv.setSaldorestante(desv.getSaldorestante()-detallebkilometro.getCombustiblegls());
            servicioDestinovale.agregarDestinovale(desv);
            detallebkilometro.setDestinovale(servicioDestinovale.obtenerPorId(iddVale));
        } else {
            detallebkilometro.setDestinovale(null);
        }

        if(detallebkilometro.getDestinovale()!=null) {
            detallebkilometro.setAnotaciones("N° Vale credito: " + detallebkilometro.getDestinovale().getValeCombustible().getNvale() +
                    " = " + detallebkilometro.getCombustiblegls() + " galones  de " + detallebkilometro.getBitacora().getUnidad().getTipoCombustible().getNombre());
        }
        servicioDetallebkilometro.agregardetalle(detallebkilometro);
        redirectAttributes.addFlashAttribute("guardadoExito", true);
        return "redirect:/Bitacoras/Detallebitacora/" + idb;
    }


    private String validarDetalleKilometro(DETALLEBKILOMETRO detalle, Integer idResponsable, Integer idVale) {
        Integer dia = detalle.getDia();
        Integer kmInicial = detalle.getKminicial();
        Integer kmFinal = detalle.getKmfinal();
        Integer kmRecorridos = detalle.getKmrecorridos();
        Float combustible = detalle.getCombustiblegls();
        String trabajosRealizados = detalle.getTrabajosrealizados();

        // Validación básica
        if (dia == null || dia <= 0) {
            return "Debes seleccionar un día.";
        }
        if (kmInicial == null || kmFinal == null || kmRecorridos == null) {
            return "Debes completar los campos de kilómetros.";
        }
        if (kmInicial < 0 || kmFinal < 0 || kmRecorridos < 0) {
            return "Los valores de kilómetros no pueden ser negativos.";
        }
        if (kmFinal < kmInicial) {
            return "El kilómetro final no puede ser menor que el inicial.";
        }
        if (kmRecorridos == 0) {
            return "Los kilómetros recorridos deben ser mayores a 0.";
        }

        // Validación de vale y combustible
        if ((combustible == null || combustible == 0) && idVale != null && idVale > 0) {
            return "Has seleccionado un vale, pero no se ingresó cantidad de combustible.";
        }

        if (combustible != null && combustible > 0) {
            if (idVale == null || idVale == 0) {
                return "Si usas combustible, debes seleccionar un vale.";
            }

            DESTINOVALE destinovale = servicioDestinovale.obtenerPorId(idVale);
            if (destinovale == null) {
                return "El vale seleccionado no existe.";
            }

            if (combustible > destinovale.getSaldorestante()) {
                return "La cantidad de combustible supera el saldo disponible del vale (Saldo: " + destinovale.getSaldorestante() + " gls).";
            }

            // Solo se asigna si todo va bien
            detalle.setDestinovale(destinovale);
        } else {
            // Si no se usa combustible, se limpia por si acaso
            detalle.setDestinovale(null);
        }

        // Validación de responsable
        if (idResponsable == null || idResponsable == 0) {
            return "Debes seleccionar un responsable.";
        }

        // Validación de justificación
        if (trabajosRealizados == null || trabajosRealizados.trim().isEmpty()) {
            return "Debes ingresar los trabajos realizados para justificar el recorrido.";
        }

        return null; // ✅ Todo correcto
    }




    @GetMapping("/Editardetallebitacora/{iddb}/{idb}")
    public String Editardetallebitacorah(@PathVariable int iddb,@PathVariable int idb, Model model) {

        BITACORA bitacora = servicioBitacora.buscarporid(idb);
        model.addAttribute("bitacora",bitacora);
        model.addAttribute("responsables",servicioResponsable.listarResponsables());
        model.addAttribute("vales", servicioDestinovale.valesdisponibles(bitacora.getUnidad().getTipoCombustible().getIdtipocombustible()));

        if(bitacora.getUnidad().getTipoUnidad().getMedicion().equals("Km")){
            model.addAttribute("DetallebitacoraKM",servicioDetallebkilometro.buscarporid(iddb));
            return "Bitacoras/EditarDetalleBitacorakm";
        }else{
            model.addAttribute("Detallebitacorah",servicioDetallebhoras.obtenerdetalle(iddb));
            return "Bitacoras/EditarDetalleBitacorah";
        }
    }

    @PostMapping("/EditadodetallebitacoraKM/{iddb}/{idb}")
    public String editadodetallebitacorakm(@ModelAttribute DETALLEBKILOMETRO Detallebitacorakm,
                                           @PathVariable int idb,
                                           @PathVariable int iddb,
                                           @RequestParam(name = "responsable.idresponsable", required = false) Integer idResponsable,
                                           @RequestParam(name = "destinovale.iddestinovale", required = false) Integer idVale,
                                           RedirectAttributes redirectAttributes,
                                           Model model) {

        BITACORA bitacora = servicioBitacora.buscarporid(idb);
        DETALLEBKILOMETRO detalleExistente = servicioDetallebkilometro.buscarporid(iddb);

        if (bitacora == null || detalleExistente == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró la bitácora o el detalle.");
            return "redirect:/Bitacoras/Detallebitacora/" + idb;
        }

        // Asignar bitácora por seguridad
        Detallebitacorakm.setBitacora(bitacora);

        // Validar datos ingresados
        String mensajeError = validarDetalleKilometro(Detallebitacorakm, idResponsable, idVale);
        if (mensajeError != null) {
            // Preparar datos para que el formulario no se vacíe
            if (idVale != null && idVale > 0) {
                Detallebitacorakm.setDestinovale(servicioDestinovale.obtenerPorId(idVale));
            }
            if (idResponsable != null && idResponsable > 0) {
                Detallebitacorakm.setResponsable(servicioResponsable.buscarResponsable(idResponsable));
            }
            Detallebitacorakm.setBitacora(bitacora);
            Detallebitacorakm.setIddetallekm(iddb);

            model.addAttribute("bitacora", bitacora);
            model.addAttribute("DetallebitacoraKM", Detallebitacorakm);
            model.addAttribute("responsables", servicioResponsable.listarResponsables());
            model.addAttribute("vales", servicioDestinovale.valesdisponibles(bitacora.getUnidad().getTipoCombustible().getIdtipocombustible()));
            model.addAttribute("errorValidacion", mensajeError);
            return "Bitacoras/EditarDetalleBitacorakm";
        }

        // Si pasa la validación, actualizamos campos
        detalleExistente.setDia(Detallebitacorakm.getDia());
        detalleExistente.setKminicial(Detallebitacorakm.getKminicial());
        detalleExistente.setKmfinal(Detallebitacorakm.getKmfinal());
        detalleExistente.setKmrecorridos(Detallebitacorakm.getKmrecorridos());
        detalleExistente.setCombustiblegls(Detallebitacorakm.getCombustiblegls());
        detalleExistente.setAceitemotor(Detallebitacorakm.getAceitemotor());
        detalleExistente.setAceitetransmision(Detallebitacorakm.getAceitetransmision());
        detalleExistente.setBateriacambio(Detallebitacorakm.getBateriacambio());
        detalleExistente.setFiltroaceitecambio(Detallebitacorakm.getFiltroaceitecambio());
        detalleExistente.setFiltropurificadorcambio(Detallebitacorakm.getFiltropurificadorcambio());
        detalleExistente.setServiciengrase(Detallebitacorakm.getServiciengrase());
        detalleExistente.setServiciomantenimiento(Detallebitacorakm.getServiciomantenimiento());
        detalleExistente.setTrabajosrealizados(Detallebitacorakm.getTrabajosrealizados());
        detalleExistente.setAnotaciones(Detallebitacorakm.getAnotaciones());
        detalleExistente.setResponsable(servicioResponsable.buscarResponsable(idResponsable));
        detalleExistente.setDestinovale(idVale != null && idVale > 0 ? servicioDestinovale.obtenerPorId(idVale) : null);
        detalleExistente.setBitacora(bitacora);

        servicioDetallebkilometro.agregardetalle(detalleExistente);
        redirectAttributes.addFlashAttribute("guardadoExito", true);
        return "redirect:/Bitacoras/Detallebitacora/" + idb;
    }


    @PostMapping("/Editadodetallebitacorah/{iddb}/{idb}")
    public String editadodetallebitacorah(
            @ModelAttribute DETALLEBHORAS Detallebitacorah,
            @RequestParam(name = "responsable.idresponsable", required = false) Integer idResponsable,
            @RequestParam(name = "destinovale.iddestinovale", required = false) Integer idVale,
            @PathVariable int iddb,
            @PathVariable int idb,
            Model model,
            RedirectAttributes redirectAttributes) {

        BITACORA bitacora = servicioBitacora.buscarporid(idb);
        DETALLEBHORAS detalleExistente = servicioDetallebhoras.obtenerdetalle(iddb);

        System.out.println("id deta:"+Detallebitacorah.getDia());
        System.out.println(" id bita:" +bitacora.getIdbitacora());


        // Validar
        String mensaje = validarDetalleHoras(Detallebitacorah, idResponsable, idVale);

        if (mensaje != null) {
            // Setear para que se mantengan seleccionados los valores en el form
            if (idVale != null && idVale > 0) {
                Detallebitacorah.setDestinovale(servicioDestinovale.obtenerPorId(idVale));
            }
            if (idResponsable != null && idResponsable > 0) {
                Detallebitacorah.setResponsable(servicioResponsable.buscarResponsable(idResponsable));
            }

            Detallebitacorah.setIddetallebitacora(iddb);
            Detallebitacorah.setBitacora(bitacora);

            model.addAttribute("bitacora", bitacora);
            model.addAttribute("Detallebitacorah", Detallebitacorah);
            model.addAttribute("vales", servicioDestinovale.valesdisponibles(bitacora.getUnidad().getTipoCombustible().getIdtipocombustible()));
            model.addAttribute("responsables", servicioResponsable.listarResponsables());
            model.addAttribute("errorValidacion", mensaje);
            return "Bitacoras/EditarDetalleBitacorah";
        }

        if (detalleExistente != null && bitacora != null) {
            detalleExistente.setHinicio(Detallebitacorah.getHinicio());
            detalleExistente.setHfinal(Detallebitacorah.getHfinal());
            detalleExistente.setHoperacion(Detallebitacorah.getHoperacion());
            detalleExistente.setAceite(Detallebitacorah.getAceite());
            detalleExistente.setCombustible(Detallebitacorah.getCombustible());
            detalleExistente.setDestino(Detallebitacorah.getDestino());
            detalleExistente.setDia(Detallebitacorah.getDia());
            detalleExistente.setJustificacion(Detallebitacorah.getJustificacion());
            detalleExistente.setReporte(Detallebitacorah.getReporte());

            detalleExistente.setResponsable((idResponsable != null && idResponsable > 0)
                    ? servicioResponsable.buscarResponsable(idResponsable) : null);

            detalleExistente.setDestinovale((idVale != null && idVale > 0)
                    ? servicioDestinovale.obtenerPorId(idVale) : null);

            detalleExistente.setBitacora(bitacora);
            servicioDetallebhoras.agregardetalle(detalleExistente);

            redirectAttributes.addFlashAttribute("guardadoExito", true);
        }

        return "redirect:/Bitacoras/Detallebitacora/" + idb;
    }



    @GetMapping("/Eliminar/{iddb}/{idb}")
    public String eliminar(@ModelAttribute DETALLEBHORAS detalleForm,@PathVariable int idb,@PathVariable int iddb) {
        if(servicioBitacora.buscarporid(idb).getUnidad().getTipoUnidad().getMedicion().equals("Km")){
            DETALLEBKILOMETRO detalleExistente= servicioDetallebkilometro.buscarporid(iddb);
            if(detalleExistente.getDestinovale()!=null){
            DESTINOVALE destinovale=servicioDestinovale.obtenerPorId(detalleExistente.getDestinovale().getIddestinovale());
            destinovale.setSaldorestante(destinovale.getSaldorestante()+detalleExistente.getCombustiblegls());
            servicioDestinovale.agregarDestinovale(destinovale);
            }
            servicioDetallebkilometro.elimininardetalle(detalleExistente);
        }else{
            DETALLEBHORAS detalleExistente = servicioDetallebhoras.obtenerdetalle(iddb);
            if(detalleExistente.getDestinovale()!=null) {
                DESTINOVALE destinovale = servicioDestinovale.obtenerPorId(detalleExistente.getDestinovale().getIddestinovale());
                destinovale.setSaldorestante(destinovale.getSaldorestante() + detalleExistente.getCombustible());
                servicioDestinovale.agregarDestinovale(destinovale);
            }
            servicioDetallebhoras.elimininardetalle(detalleExistente);
        }
        return "redirect:/Bitacoras/Detallebitacora/" + idb;
    }



    public List<Integer> obtenerDiasDisponibles(int idbitacora, int anio, int mes) {
        List<Integer> diasOcupados;
        if(servicioBitacora.buscarporid(idbitacora).getUnidad().getTipoUnidad().getMedicion().equals("Km")){
            diasOcupados = servicioDetallebkilometro.buscarDiasRegistrados(idbitacora);
        }else{
            diasOcupados=servicioDetallebhoras.buscarDiasRegistrados(idbitacora);
        }
        List<Integer> dias = new ArrayList<>();

        int diasDelMes = YearMonth.of(anio, mes).lengthOfMonth();

        for (int i = 1; i <= diasDelMes; i++) {
            if (!diasOcupados.contains(i)) {
                dias.add(i);
            }
        }
        return dias;
    }

    @GetMapping("/ExportarPartePDF/{id}")
    public void exportarPartePDF(@PathVariable("id") int idbitacora, HttpServletResponse response) throws Exception {
        BITACORA bitacora = servicioBitacora.buscarporid(idbitacora);

        if (bitacora.getUnidad().getTipoUnidad().getMedicion().equalsIgnoreCase("Km")) {
            generarPDFKilometros(bitacora, response);
        } else {
            generarPDFHoras(bitacora, response);
        }
    }



    public void generarPDFHoras(BITACORA bitacora, HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ParteDiario.pdf");

        List<DETALLEBHORAS> detalles = servicioDetallebhoras.obtenerPorBitacora(bitacora.getIdbitacora());
        Map<Integer, DETALLEBHORAS> mapaDetalles = detalles.stream()
                .collect(Collectors.toMap(DETALLEBHORAS::getDia, d -> d));

        int mes = bitacora.getMes();
        int anio = bitacora.getAnio();
        YearMonth yearMonth = YearMonth.of(anio, mes);
        int diasDelMes = yearMonth.lengthOfMonth();

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        InputStream is = getClass().getResourceAsStream("/static/IMG/BitacoraHorascaratula.jpg");
        if (is != null) {
            Image portada = Image.getInstance(is.readAllBytes());
            portada.setRotationDegrees(-90f); // gira la imagen -90° (vertical visual)
            portada.scaleToFit(PageSize.A4.getHeight(), PageSize.A4.getWidth());
            portada.setAbsolutePosition(0, 0);
            document.add(portada);
            document.newPage(); // Sigue con el contenido en horizontal
        } else {
            throw new FileNotFoundException("No se encontró la imagen de portada en /static/img/BitacoraHorascaratula.jpg");
        }

        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font fontCabeceraPeque = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
        Font fontFilaPeque = FontFactory.getFont(FontFactory.HELVETICA, 7);
        Font fontCabeceraGrande = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        Paragraph titulo = new Paragraph("PARTE DIARIO", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingBefore(5f);
        titulo.setSpacingAfter(2f);
        document.add(titulo);

        String infoCabecera = "Mes: " + obtenerNombreMes(mes).toUpperCase() + "   Año: " + anio + "   Unidad: " +
                bitacora.getUnidad().getIdentificador() + " - " + bitacora.getUnidad().getNombre();
        Paragraph cabecera = new Paragraph(infoCabecera, fontCabeceraGrande);
        cabecera.setAlignment(Element.ALIGN_LEFT);
        cabecera.setSpacingAfter(8f);
        document.add(cabecera);

        PdfPTable tabla = new PdfPTable(11);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{2.5f, 2.5f, 2.5f, 2.5f, 1.5f, 2.7f, 7f, 7f, 5f, 3f, 4f});

        // Fila 1
        tabla.addCell(celdaCabecera("Día", fontCabeceraPeque, 2));
        tabla.addCell(celdaCabeceraColspan("Horas de Operación", fontCabeceraPeque, 3));
        tabla.addCell(celdaCabeceraColspan("Consumo Gln", fontCabeceraPeque, 2));
        tabla.addCell(celdaCabeceraColspan("Itinerario", fontCabeceraPeque, 2));
        tabla.addCell(celdaCabecera("Operador Responsable", fontCabeceraPeque, 2));
        tabla.addCell(celdaCabecera("N° Vale", fontCabeceraPeque, 2));
        tabla.addCell(celdaCabecera("Servicio de Mantenimiento", fontCabeceraPeque, 2));

        // Fila 2
        String[] subHeaders = {"Hora\nInicio", "Hora\nFin", "Horas\nOperación", "Aceite", "Combustible", "Salida - Destino, Uso", "Justificación"};
        for (String h : subHeaders) {
            PdfPCell cell = new PdfPCell(new Phrase(h, fontCabeceraPeque));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cell);
        }

        float totalHoras = 0f, totalCombustible = 0f;

        for (int dia = 1; dia <= diasDelMes; dia++) {
            DETALLEBHORAS d = mapaDetalles.get(dia);
            tabla.addCell(celdaDato(String.valueOf(dia), fontFilaPeque));

            if (d != null) {
                tabla.addCell(celdaDato(String.valueOf(d.getHinicio()), fontFilaPeque));
                tabla.addCell(celdaDato(String.valueOf(d.getHfinal()), fontFilaPeque));
                tabla.addCell(celdaDato(d.getHoperacion() + " H", fontFilaPeque));
                tabla.addCell(celdaDato(d.getAceite(), fontFilaPeque));
                tabla.addCell(d.getCombustible() == 0 ?
                        new Phrase(" ", fontFilaPeque) :
                        new Phrase(d.getCombustible() + " gln", fontFilaPeque));
                tabla.addCell(celdaDato(d.getDestino(), fontFilaPeque));
                tabla.addCell(celdaDato(d.getJustificacion(), fontFilaPeque));
                tabla.addCell(celdaDato(
                        servicioResponsable.buscarResponsable(d.getResponsable().getIdresponsable()).getNombre(),
                        fontFilaPeque));

                long nvale = 0;
                if (d.getDestinovale() != null && d.getDestinovale().getValeCombustible() != null) {
                    nvale = d.getDestinovale().getValeCombustible().getNvale();
                }
                tabla.addCell(nvale == 0 ?
                        new Phrase(" ", fontFilaPeque) :
                        new Phrase(String.valueOf(nvale), fontFilaPeque));
                tabla.addCell(celdaDato(d.getReporte(), fontFilaPeque));

                totalHoras += d.getHoperacion();
                totalCombustible += d.getCombustible();
            } else {
                for (int j = 0; j < 10; j++) tabla.addCell(celdaDato(" ", fontFilaPeque));
            }
        }

        // Fila Totales
        PdfPCell totalCell = new PdfPCell(new Phrase("TOTAL", fontCabeceraPeque));
        totalCell.setColspan(3);
        totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(totalCell);

        tabla.addCell(celdaDato(String.format("%.2f H", totalHoras), fontCabeceraPeque));
        tabla.addCell(celdaDato(" ", fontCabeceraPeque));
        tabla.addCell(celdaDato(String.format("%.2f gln", totalCombustible), fontCabeceraPeque));

        for (int i = 0; i < 5; i++) tabla.addCell(celdaDato(" ", fontCabeceraPeque));

        document.add(tabla);
        document.close();
    }



    public void generarPDFKilometros(BITACORA bitacora, HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ParteKilometros.pdf");

        List<DETALLEBKILOMETRO> detalles = servicioDetallebkilometro.obtenerPorBitacora(bitacora.getIdbitacora());
        Map<Integer, DETALLEBKILOMETRO> mapaDetalles = detalles.stream()
                .collect(Collectors.toMap(DETALLEBKILOMETRO::getDia, d -> d));

        int mes = bitacora.getMes();
        int anio = bitacora.getAnio();
        YearMonth yearMonth = YearMonth.of(anio, mes);
        int diasDelMes = yearMonth.lengthOfMonth();
        int velocimetroinicial=0,velocimetrofinal=0;

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        InputStream is = getClass().getResourceAsStream("/static/IMG/BitacoraKmcaratula.jpg");
        if (is != null) {
            Image portada = Image.getInstance(is.readAllBytes());
            portada.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            portada.setAbsolutePosition(
                    (PageSize.A4.getWidth() - portada.getScaledWidth()) / 2,
                    (PageSize.A4.getHeight() - portada.getScaledHeight()) / 2
            );
            document.add(portada);
            document.newPage(); // Sigue con el contenido en horizontal
        } else {
            throw new FileNotFoundException("No se encontró la imagen de portada en /static/img/BitacoraHorascaratula.jpg");
        }

        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font fontCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font fontFila = FontFactory.getFont(FontFactory.HELVETICA, 8);

        // ------------------ PÁGINA 1: RESUMEN DIARIO ------------------
        Paragraph titulo1 = new Paragraph("RESUMEN DIARIO", fontTitulo);
        titulo1.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo1);

        Paragraph cabecera = new Paragraph("MES DE " + obtenerNombreMes(mes).toUpperCase() + "    " + anio, fontCabecera);
        cabecera.setAlignment(Element.ALIGN_RIGHT);
        cabecera.setSpacingAfter(10f);
        document.add(cabecera);

// Tabla de 12 columnas
        PdfPTable tabla1 = new PdfPTable(12);
        tabla1.setWidthPercentage(100);
        tabla1.setWidths(new float[]{1f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f});

// Cabeceras (12 en total)
        String[] headers = {
                "DIA", "KMS INICIAL", "COMBUSTIBLE\nEN GLS.",
                "ACEITE DE\nMOTOR EN\nCUARTOS", "ACEITE DE\nTRANSMISIÓN\nEN CUARTOS",
                "SERVICIO DE\nENGRASADO\nY LAVADO", "SERVICIO DE\nMANTENIMIENTO",
                "FILTRO DE\nACEITE\nCAMBIO", "FILTRO\nPURIFICADOR\nCAMBIO",
                "BATERÍA\nCAMBIO", "KMS FINAL", "VALE" // columna 12 añadida
        };

        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, fontCabecera));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla1.addCell(cell);
        }

// Variables
        double totalCombustible = 0;
        boolean vc = false;

        for (int dia = 1; dia <= diasDelMes; dia++) {
            DETALLEBKILOMETRO d = mapaDetalles.get(dia);
            tabla1.addCell(new Phrase(String.valueOf(dia), fontFila));

            if (d != null) {
                if (!vc && d.getKminicial() != 0) {
                    velocimetroinicial = d.getKminicial();
                    vc = true;
                }
                if (d.getKmfinal() != 0) {
                    velocimetrofinal = d.getKmfinal();
                }

                tabla1.addCell(new Phrase(String.valueOf(d.getKminicial()), fontFila));
                tabla1.addCell(new Phrase((d.getCombustiblegls() != 0) ? d.getCombustiblegls() + " gln" : " ", fontFila));
                tabla1.addCell(new Phrase((d.getAceitemotor() != 0) ? String.valueOf(d.getAceitemotor()) : " ", fontFila));
                tabla1.addCell(new Phrase((d.getAceitetransmision() != 0) ? String.valueOf(d.getAceitetransmision()) : " ", fontFila));
                tabla1.addCell(new Phrase((d.getServiciengrase() != null) ? d.getServiciengrase() : " ", fontFila));
                tabla1.addCell(new Phrase((d.getServiciomantenimiento() != null) ? d.getServiciomantenimiento() : " ", fontFila));
                tabla1.addCell(new Phrase((d.getFiltroaceitecambio() != null) ? d.getFiltroaceitecambio() : " ", fontFila));
                tabla1.addCell(new Phrase((d.getFiltropurificadorcambio() != null) ? d.getFiltropurificadorcambio() : " ", fontFila));
                tabla1.addCell(new Phrase((d.getBateriacambio() != null) ? d.getBateriacambio() : " ", fontFila));
                tabla1.addCell(new Phrase(String.valueOf(d.getKmfinal()), fontFila));
                tabla1.addCell(new Phrase((d.getDestinovale()!=null) ? d.getDestinovale().getValeCombustible().getNvale()+"" : " ", fontFila));

                if (d.getCombustiblegls() != 0)
                    totalCombustible += d.getCombustiblegls();
            } else {
                // Si no hay datos, 11 columnas vacías + día ya está
                for (int i = 0; i < 11; i++) {
                    tabla1.addCell(new Phrase(" ", fontFila));
                }
            }
        }

        // Fila de totales
        // Fila de Totales
        PdfPCell totalCell = new PdfPCell(new Phrase("TOTALES", fontCabecera));
        totalCell.setColspan(2);
        totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(totalCell);
        tabla1.addCell(new Phrase(" ", fontFila));
        tabla1.addCell(new Phrase(String.format("%.2f gln", totalCombustible), fontFila));
        for (int i = 0; i < 9; i++) {
            tabla1.addCell(new Phrase(" ", fontFila));
        }

        document.add(tabla1);


        // ------------------ PÁGINA 2: RESUMEN MENSUAL ------------------
        document.newPage();
        Paragraph titulo2 = new Paragraph("RESUMEN MENSUAL", fontTitulo);
        titulo2.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo2);

        Paragraph cabecera2 = new Paragraph("Periodo de 01 DE " + obtenerNombreMes(mes).toUpperCase() + " al " + diasDelMes + " DE " + obtenerNombreMes(mes).toUpperCase() + " " + anio +
                "\nVelocímetro Comienzo: " + velocimetroinicial + "  Final: " + velocimetrofinal +
                "\nAgencia: PNSDV    Placa: " + bitacora.getUnidad().getIdentificador(), fontCabecera);
        cabecera2.setSpacingAfter(10f);
        document.add(cabecera2);

        PdfPTable tabla2 = new PdfPTable(3);
        tabla2.setWidthPercentage(100);
        tabla2.setWidths(new float[]{1f, 1.15f, 7f}); // ← CORREGIDO: 3 columnas bien definidas
        tabla2.addCell(new PdfPCell(new Phrase("Día", fontCabecera)));
        tabla2.addCell(new PdfPCell(new Phrase("Kilómetros", fontCabecera)));
        tabla2.addCell(new PdfPCell(new Phrase("Detallar trabajos realizados", fontCabecera)));

        for (int dia = 1; dia <= diasDelMes; dia++) {
            DETALLEBKILOMETRO d = mapaDetalles.get(dia);
            tabla2.addCell(new Phrase(String.valueOf(dia), fontFila));
            tabla2.addCell(new Phrase((d != null && d.getKmrecorridos() != 0) ? String.valueOf(d.getKmrecorridos()) : " ", fontFila));
            tabla2.addCell(new Phrase((d != null && d.getTrabajosrealizados() != null) ? d.getTrabajosrealizados() : " ", fontFila));
        }
        document.add(tabla2);

        // ------------------ PÁGINA 3: ANOTACIONES ------------------
        document.newPage();

        Paragraph titulo3 = new Paragraph("ANOTACIONES", fontTitulo);
        titulo3.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo3);

        Paragraph cabecera3 = new Paragraph("AGENCIA: PNSDV\nPLACA: " + bitacora.getUnidad().getIdentificador(), fontCabecera);
        cabecera3.setSpacingAfter(10f);
        document.add(cabecera3);

        PdfPTable tabla3 = new PdfPTable(2);
        tabla3.setWidthPercentage(100);
        tabla3.setWidths(new float[]{1f, 9f});
        tabla3.addCell(new PdfPCell(new Phrase("Día", fontCabecera)));
        tabla3.addCell(new PdfPCell(new Phrase("Anotaciones", fontCabecera)));

        for (int dia = 1; dia <= diasDelMes; dia++) {
            DETALLEBKILOMETRO d = mapaDetalles.get(dia);
            tabla3.addCell(new Phrase(String.valueOf(dia), fontFila));
            tabla3.addCell(new Phrase((d != null && d.getAnotaciones() != null) ? d.getAnotaciones() : " ", fontFila));
        }

        document.add(tabla3);
        document.close();
    }

    private String obtenerNombreMes(int mes) {
        switch (mes) {
            case 1: return "Enero";
            case 2: return "Febrero";
            case 3: return "Marzo";
            case 4: return "Abril";
            case 5: return "Mayo";
            case 6: return "Junio";
            case 7: return "Julio";
            case 8: return "Agosto";
            case 9: return "Septiembre";
            case 10: return "Octubre";
            case 11: return "Noviembre";
            case 12: return "Diciembre";
            default: return "";
        }
    }


    @GetMapping("/VistaPreviaPDF/{id}")
    public ResponseEntity<byte[]> vistaPreviaPDF(@PathVariable("id") int idbitacora) throws Exception {
        System.out.println("id bitacora: "+idbitacora);
        BITACORA bitacora = servicioBitacora.buscarporid(idbitacora);
        byte[] pdfBytes = generarPDFVistaPrevia(bitacora);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("vistaprevia_" + idbitacora + ".pdf").build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    public byte[] generarPDFVistaPrevia(BITACORA bitacora) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document;

        if (bitacora.getUnidad().getTipoUnidad().getMedicion().equalsIgnoreCase("Km")) {
            document = new Document(PageSize.A4); // Vertical para kilómetros
        } else {
            document = new Document(PageSize.A4.rotate()); // Horizontal para horas
        }

        PdfWriter.getInstance(document, out);
        document.open();

        int mes = bitacora.getMes();
        int anio = bitacora.getAnio();
        YearMonth yearMonth = YearMonth.of(anio, mes);
        int diasDelMes = yearMonth.lengthOfMonth();


        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font fontCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font fontFila = FontFactory.getFont(FontFactory.HELVETICA, 9);

        if (bitacora.getUnidad().getTipoUnidad().getMedicion().equalsIgnoreCase("Km")) {
            List<DETALLEBKILOMETRO> detalles = servicioDetallebkilometro.obtenerPorBitacora(bitacora.getIdbitacora());
            Map<Integer, DETALLEBKILOMETRO> mapaDetalles = detalles.stream()
                    .collect(Collectors.toMap(DETALLEBKILOMETRO::getDia, d -> d));


            InputStream is = getClass().getResourceAsStream("/static/IMG/BitacoraKmcaratula.jpg");
            if (is != null) {
                Image portada = Image.getInstance(is.readAllBytes());
                portada.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                portada.setAbsolutePosition(
                        (PageSize.A4.getWidth() - portada.getScaledWidth()) / 2,
                        (PageSize.A4.getHeight() - portada.getScaledHeight()) / 2
                );
                document.add(portada);
                document.newPage(); // Sigue con el contenido en horizontal
            } else {
                throw new FileNotFoundException("No se encontró la imagen de portada en /static/img/BitacoraHorascaratula.jpg");
            }

            int velocimetroinicial = 0, velocimetrofinal = 0;
            double totalCombustible = 0;
            boolean vc = false;

// --- Página 1: RESUMEN DIARIO ---
            document.newPage();
            Paragraph titulo1 = new Paragraph("RESUMEN DIARIO", fontTitulo);
            titulo1.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo1);

            Paragraph cabecera1 = new Paragraph("MES DE " + obtenerNombreMes(mes).toUpperCase() + "    " + anio, fontCabecera);
            cabecera1.setAlignment(Element.ALIGN_RIGHT);
            cabecera1.setSpacingAfter(10f);
            document.add(cabecera1);

            PdfPTable tabla1 = new PdfPTable(12);
            tabla1.setWidthPercentage(100);
            tabla1.setWidths(new float[]{1f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f});

            String[] headers = {
                    "DÍA", "KMS INICIAL", "COMBUSTIBLE\nEN GLS.", "ACEITE DE\nMOTOR EN\nCUARTOS",
                    "ACEITE DE\nTRANSMISIÓN\nEN CUARTOS", "SERVICIO DE\nENGRASADO\nY LAVADO",
                    "SERVICIO DE\nMANTENIMIENTO", "FILTRO DE\nACEITE\nCAMBIO", "FILTRO\nPURIFICADOR\nCAMBIO",
                    "BATERÍA\nCAMBIO", "KMS FINAL", "VALE"
            };

            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontCabecera));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);
            }

            for (int dia = 1; dia <= diasDelMes; dia++) {
                DETALLEBKILOMETRO d = mapaDetalles.get(dia);
                tabla1.addCell(new Phrase(String.valueOf(dia), fontFila));

                if (d != null) {
                    if (!vc && d.getKminicial() != 0) {
                        velocimetroinicial = d.getKminicial();
                        vc = true;
                    }
                    if (d.getKmfinal() != 0) {
                        velocimetrofinal = d.getKmfinal();
                    }

                    tabla1.addCell(new Phrase(String.valueOf(d.getKminicial()), fontFila));
                    tabla1.addCell(new Phrase((d.getCombustiblegls() != 0) ? d.getCombustiblegls() + " gln" : " ", fontFila));
                    tabla1.addCell(new Phrase((d.getAceitemotor() != 0) ? String.valueOf(d.getAceitemotor()) : " ", fontFila));
                    tabla1.addCell(new Phrase((d.getAceitetransmision() != 0) ? String.valueOf(d.getAceitetransmision()) : " ", fontFila));
                    tabla1.addCell(new Phrase((d.getServiciengrase() != null) ? d.getServiciengrase() : " ", fontFila));
                    tabla1.addCell(new Phrase((d.getServiciomantenimiento() != null) ? d.getServiciomantenimiento() : " ", fontFila));
                    tabla1.addCell(new Phrase((d.getFiltroaceitecambio() != null) ? d.getFiltroaceitecambio() : " ", fontFila));
                    tabla1.addCell(new Phrase((d.getFiltropurificadorcambio() != null) ? d.getFiltropurificadorcambio() : " ", fontFila));
                    tabla1.addCell(new Phrase((d.getBateriacambio() != null) ? d.getBateriacambio() : " ", fontFila));
                    tabla1.addCell(new Phrase(String.valueOf(d.getKmfinal()), fontFila));
                    tabla1.addCell(new Phrase((d.getDestinovale()!=null) ? d.getDestinovale().getValeCombustible().getNvale()+"" : " ", fontFila));

                    if (d.getCombustiblegls() != 0)
                        totalCombustible += d.getCombustiblegls();
                } else {
                    for (int i = 0; i < 11; i++) tabla1.addCell(new Phrase(" ", fontFila));
                }
            }

            // Fila de totales
            PdfPCell totalCell = new PdfPCell(new Phrase("TOTALES", fontCabecera));
            totalCell.setColspan(2);
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla1.addCell(totalCell);
            tabla1.addCell(new Phrase(" ", fontFila));
            tabla1.addCell(new Phrase(String.format("%.2f gln", totalCombustible), fontFila));
            for (int i = 0; i < 8; i++) tabla1.addCell(new Phrase(" ", fontFila));

            document.add(tabla1);

            //----Hoja Mantenimientos-----
            document.newPage();

            Paragraph tituloMantenimiento = new Paragraph("SERVICIOS DE MANTENIMIENTO REALIZADOS", fontCabecera);
            tituloMantenimiento.setAlignment(Element.ALIGN_CENTER);
            tituloMantenimiento.setSpacingAfter(8f);
            document.add(tituloMantenimiento);

            PdfPTable tabla2 = new PdfPTable(2);
            tabla2.setWidthPercentage(100);
            tabla2.setWidths(new float[]{1f, 7f});

            tabla2.addCell(new PdfPCell(new Phrase("DÍA", fontCabecera)));
            tabla2.addCell(new PdfPCell(new Phrase("SERVICIOS DE MANTENIMIENTO REALIZADOS", fontCabecera)));

            List<Integer> diasConMantenimiento = detalles.stream()
                    .filter(d -> d.getMantenimiendodescripcion() != null && !d.getMantenimiendodescripcion().isBlank())
                    .map(DETALLEBKILOMETRO::getDia)
                    .sorted()
                    .collect(Collectors.toList());

            for (Integer dia : diasConMantenimiento) {
                DETALLEBKILOMETRO d = mapaDetalles.get(dia);
                tabla2.addCell(new Phrase(String.valueOf(dia+"/"+d.getBitacora().getMes()), fontFila));
                tabla2.addCell(new Phrase(d.getMantenimiendodescripcion(), fontFila));
            }

            for (int dia = 1; dia <= diasDelMes; dia++) {
                if (!diasConMantenimiento.contains(dia)) {
                    tabla2.addCell(new Phrase(" ", fontFila));
                    tabla2.addCell(new Phrase(" ", fontFila));
                }
            }

            document.add(tabla2);


            // --- Página 3: RESUMEN MENSUAL ---
            document.newPage();
            Paragraph titulo3 = new Paragraph("RESUMEN MENSUAL", fontTitulo);
            titulo3.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo3);

            Paragraph cabecera3 = new Paragraph("Periodo de 01 DE " + obtenerNombreMes(mes).toUpperCase() + " al " + diasDelMes + " DE " + obtenerNombreMes(mes).toUpperCase() + " " + anio +
                    "\nVelocímetro Comienzo: " + velocimetroinicial + "  Final: " + velocimetrofinal +
                    "\nAgencia: PNSDV    Placa: " + bitacora.getUnidad().getIdentificador(), fontCabecera);
            cabecera3.setSpacingAfter(10f);
            document.add(cabecera3);

            PdfPTable tabla3 = new PdfPTable(3);
            tabla3.setWidthPercentage(100);
            tabla3.setWidths(new float[]{1f, 1.15f, 7f}); // ← CORREGIDO: 3 columnas bien definidas
            tabla3.addCell(new PdfPCell(new Phrase("Día", fontCabecera)));
            tabla3.addCell(new PdfPCell(new Phrase("Kilómetros", fontCabecera)));
            tabla3.addCell(new PdfPCell(new Phrase("Detallar trabajos realizados", fontCabecera)));

            for (int dia = 1; dia <= diasDelMes; dia++) {
                DETALLEBKILOMETRO d = mapaDetalles.get(dia);
                tabla3.addCell(new Phrase(String.valueOf(dia), fontFila));
                tabla3.addCell(new Phrase((d != null && d.getKmrecorridos() != 0) ? String.valueOf(d.getKmrecorridos()) : " ", fontFila));
                tabla3.addCell(new Phrase((d != null && d.getTrabajosrealizados() != null) ? d.getTrabajosrealizados() : " ", fontFila));
            }
            document.add(tabla3);


            // --- Página 4: ANOTACIONES ---
            document.newPage();
            Paragraph titulo4 = new Paragraph("ANOTACIONES", fontTitulo);
            titulo4.setAlignment(Element.ALIGN_CENTER);
            titulo4.setSpacingAfter(8f);
            document.add(titulo4);

            PdfPTable tabla4 = new PdfPTable(2);
            tabla4.setWidthPercentage(100);
            tabla4.setWidths(new float[]{1f, 9f});
            tabla4.addCell(new PdfPCell(new Phrase("Día", fontCabecera)));
            tabla4.addCell(new PdfPCell(new Phrase("Anotaciones", fontCabecera)));

            List<Integer> diasConVale = detalles.stream()
                    .filter(d -> d.getDestinovale() != null) //
                    .map(DETALLEBKILOMETRO::getDia)
                    .sorted()
                    .collect(Collectors.toList());


            for (Integer dia : diasConVale) {
                DETALLEBKILOMETRO d = mapaDetalles.get(dia);
                tabla4.addCell(new Phrase(String.valueOf(dia+"/"+d.getBitacora().getMes()), fontFila));
                tabla4.addCell(new Phrase(d.getAnotaciones(), fontFila));
            }

            for (int dia = 1; dia <= diasDelMes; dia++) {
                if (!diasConVale.contains(dia)) {
                    tabla4.addCell(new Phrase(" ", fontFila));
                    tabla4.addCell(new Phrase(" ", fontFila));
                }
            }
            document.add(tabla4);


        } else {
            // ========== MEDICIÓN HORAS ==========
            List<DETALLEBHORAS> detalles = servicioDetallebhoras.obtenerPorBitacora(bitacora.getIdbitacora());
            Map<Integer, DETALLEBHORAS> mapaDetalles = detalles.stream()
                    .collect(Collectors.toMap(DETALLEBHORAS::getDia, d -> d));

            InputStream is = getClass().getResourceAsStream("/static/IMG/BitacoraHorascaratula.jpg");
            if (is != null) {
                Image portada = Image.getInstance(is.readAllBytes());
                portada.setRotationDegrees(-90f); // gira la imagen -90° (vertical visual)
                portada.scaleToFit(PageSize.A4.getHeight(), PageSize.A4.getWidth());
                portada.setAbsolutePosition(0, 0);
                document.add(portada);
                document.newPage(); // Sigue con el contenido en horizontal
            } else {
                throw new FileNotFoundException("No se encontró la imagen de portada en /static/img/BitacoraHorascaratula.jpg");
            }

            Font fontCabeceraPeque = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Font fontFilaPeque = FontFactory.getFont(FontFactory.HELVETICA, 7);
            Font fontCabeceraGrande = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

            Paragraph titulo = new Paragraph("PARTE DIARIO", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingBefore(5f);
            titulo.setSpacingAfter(2f);
            document.add(titulo);

            String infoCabecera = "Mes: " + obtenerNombreMes(mes).toUpperCase() + "   Año: " + anio + "   Unidad: " +
                    bitacora.getUnidad().getIdentificador() + " - " + bitacora.getUnidad().getNombre();
            Paragraph cabecera = new Paragraph(infoCabecera, fontCabeceraGrande);
            cabecera.setAlignment(Element.ALIGN_LEFT);
            cabecera.setSpacingAfter(8f);
            document.add(cabecera);

            PdfPTable tabla = new PdfPTable(11);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{2.5f, 2.5f, 2.5f, 2.5f, 1.5f, 2.7f, 7f, 7f, 5f, 3f, 4f});

            // Fila 1
            tabla.addCell(new PdfPCell(new Phrase("Día", fontCabeceraPeque)){{setRowspan(2); setHorizontalAlignment(Element.ALIGN_CENTER);}});
            tabla.addCell(new PdfPCell(new Phrase("Horas de Operación", fontCabeceraPeque)){{setColspan(3); setHorizontalAlignment(Element.ALIGN_CENTER);}});
            tabla.addCell(new PdfPCell(new Phrase("Consumo Gln", fontCabeceraPeque)){{setColspan(2); setHorizontalAlignment(Element.ALIGN_CENTER);}});
            tabla.addCell(new PdfPCell(new Phrase("Itinerario", fontCabeceraPeque)){{setColspan(2); setHorizontalAlignment(Element.ALIGN_CENTER);}});
            tabla.addCell(new PdfPCell(new Phrase("Operador Responsable", fontCabeceraPeque)){{setRowspan(2); setHorizontalAlignment(Element.ALIGN_CENTER);}});
            tabla.addCell(new PdfPCell(new Phrase("N° Vale", fontCabeceraPeque)){{setRowspan(2); setHorizontalAlignment(Element.ALIGN_CENTER);}});
            tabla.addCell(new PdfPCell(new Phrase("Servicio de Mantenimiento", fontCabeceraPeque)){{setRowspan(2); setHorizontalAlignment(Element.ALIGN_CENTER);}});

            // Fila 2
            String[] subHeaders = {"Hora\nInicio", "Hora\nFin", "Horas\nOperación", "Aceite", "Combustible", "Salida - Destino, Uso", "Justificación"};
            for (String h : subHeaders) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontCabeceraPeque));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(cell);
            }

            float totalHoras = 0f, totalCombustible = 0f;

            for (int dia = 1; dia <= diasDelMes; dia++) {
                DETALLEBHORAS d = mapaDetalles.get(dia);
                tabla.addCell(new Phrase(String.valueOf(dia), fontFilaPeque));
                if (d != null) {

                    tabla.addCell(new Phrase(String.valueOf(d.getHinicio()), fontFilaPeque));
                    tabla.addCell(new Phrase(String.valueOf(d.getHfinal()), fontFilaPeque));
                    tabla.addCell(new Phrase(d.getHoperacion() + " H", fontFilaPeque));
                    tabla.addCell(new Phrase(d.getAceite(), fontFilaPeque));
                    if(d.getCombustible()==0){tabla.addCell(new Phrase( " ", fontFilaPeque));}
                    else{tabla.addCell(new Phrase(d.getCombustible() + " gln", fontFilaPeque));}
                    tabla.addCell(new Phrase(d.getDestino(), fontFilaPeque));
                    tabla.addCell(new Phrase(d.getJustificacion(), fontFilaPeque));
                    tabla.addCell(new Phrase(servicioResponsable.buscarResponsable(d.getResponsable().getIdresponsable()).getNombre(), fontFilaPeque));

                    long nvale = 0;
                    if (d.getDestinovale() != null && d.getDestinovale().getValeCombustible() != null) {
                        nvale = d.getDestinovale().getValeCombustible().getNvale();
                    }
                    if(nvale==0){tabla.addCell(new Phrase(" ", fontFilaPeque));}
                    else{tabla.addCell(new Phrase(nvale + "", fontFilaPeque));}

                    tabla.addCell(new Phrase(d.getReporte(), fontFilaPeque));

                    totalHoras += d.getHoperacion();
                    totalCombustible += d.getCombustible();
                } else {
                    for (int j = 0; j < 10; j++) tabla.addCell(new Phrase(" ", fontFilaPeque));
                }
            }

            // === Fila de Totales ===
            PdfPCell totalCell = new PdfPCell(new Phrase("TOTAL", fontCabeceraPeque));
            totalCell.setColspan(3); // Día + H. Inicio + H. Fin
            totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(totalCell);

            // Celda aceite (vacía o con total si deseas)
            tabla.addCell(new Phrase(String.format("%.2f H", totalHoras), fontCabeceraPeque));

            tabla.addCell(new Phrase(" ", fontCabeceraPeque));

            // Celda con total de combustible
            tabla.addCell(new Phrase(String.format("%.2f gln", totalCombustible), fontCabeceraPeque));

            // Celdas vacías para las demás columnas (Destino, Justificación, Operador, Vale, Mantenimiento)
            for (int i = 0; i < 5; i++) {
                tabla.addCell(new Phrase(" ", fontCabeceraPeque));
            }


            document.add(tabla);
        }

        document.close();
        return out.toByteArray();
    }




    private PdfPCell celda(String texto, Font fuente) {
        PdfPCell c = new PdfPCell(new Phrase(texto, fuente));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return c;
    }

    private PdfPCell celda(String texto, Font fuente, float alturaMinima) {
        PdfPCell c = celda(texto, fuente);
        c.setMinimumHeight(alturaMinima);
        return c;
    }

    private PdfPCell celda(String texto, Font fuente, int rowspan, int colspan) {
        PdfPCell c = celda(texto, fuente);
        c.setRowspan(rowspan);
        c.setColspan(colspan);
        return c;
    }
    private PdfPCell celdaCabecera(String texto, Font font, int rowspan) {
        PdfPCell c = new PdfPCell(new Phrase(texto, font));
        c.setRowspan(rowspan);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return c;
    }

    private PdfPCell celdaCabeceraColspan(String texto, Font font, int colspan) {
        PdfPCell c = new PdfPCell(new Phrase(texto, font));
        c.setColspan(colspan);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return c;
    }

    private PdfPCell celdaDato(String texto, Font font) {
        PdfPCell c = new PdfPCell(new Phrase(texto != null ? texto : " ", font));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return c;
    }














}
