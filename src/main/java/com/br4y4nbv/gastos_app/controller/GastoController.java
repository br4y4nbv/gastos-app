package com.br4y4nbv.gastos_app.controller;

import com.br4y4nbv.gastos_app.dto.GastoForm;
import com.br4y4nbv.gastos_app.service.GastoService;
import com.br4y4nbv.gastos_app.dto.GastoFiltro;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller // Recibe peticiones web y devuelve nombres de vistas (HTML)
@RequestMapping("/gastos") // Todas las rutas de esta clase empiezan con /gastos
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;

    // GET /gastos?page=0
    // @RequestParam lee el parámetro de la URL; si no viene, usa 0.
   @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int page,
                        @ModelAttribute("filtro") GastoFiltro filtro,
                        Model model) {

        // Si el rango está invertido, se avisa y lo ignora
        if (filtro.getDesde() != null && filtro.getHasta() != null
                && filtro.getDesde().isAfter(filtro.getHasta())) {
            model.addAttribute("errorFiltro", "La fecha 'desde' no puede ser posterior a 'hasta'");
            filtro.setDesde(null);
            filtro.setHasta(null);
        }

        // Se agrega "id" como segundo criterio para que el orden sea estable entre páginas cuando hay gastos con la misma fecha
        var pageable = PageRequest.of(page, 10,
                Sort.by(Sort.Direction.DESC, "fecha").and(Sort.by(Sort.Direction.DESC, "id")));

        model.addAttribute("pagina", gastoService.listar(filtro, pageable));
        model.addAttribute("categorias", gastoService.listarCategorias());
        return "gastos/lista";
    }

    // GET /gastos/nuevo: muestra el formulario vacío
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        GastoForm form = new GastoForm();
        form.setFecha(LocalDate.now()); // La fecha de hoy aparece precargada
        model.addAttribute("gastoForm", form);
        model.addAttribute("categorias", gastoService.listarCategorias()); // Para el <select>
        return "gastos/form";
    }

    // GET /gastos/5/editar: @PathVariable toma el {id} de la URL
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("gastoForm", gastoService.obtenerForm(id));
        model.addAttribute("categorias", gastoService.listarCategorias());
        return "gastos/form";  // Reutiliza el mismo formulario para crear y editar
    }

    // POST /gastos/guardar: recibe el formulario enviado
    // @Valid: ejecuta las validaciones de GastoForm
    // BindingResult: guarda los errores. DEBE ir justo después del objeto validado.
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("gastoForm") GastoForm form,
                          BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            // Hay errores: se vuelve a mostrar el formulario (sin redirigir)
            // para que se vean los mensajes y no se pierdan los datos escritos.
            // Hay que volver a cargar las categorías porque el Model es nuevo.
            model.addAttribute("categorias", gastoService.listarCategorias());
            return "gastos/form";
        }
        gastoService.guardar(form);

        // Flash attribute: el mensaje sobrevive a UN solo redirect y luego desaparece
        ra.addFlashAttribute("mensaje", "Gasto guardado correctamente");

        // Patrón Post/Redirect/Get: tras un POST se redirige a un GET.
        // Así, si el usuario recarga la página, no se reenvía el formulario (no duplica gastos)
        return "redirect:/gastos";
    }

    // Eliminar se usa POST (no GET) para que no se pueda borrar con solo abrir un enlace
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        gastoService.eliminar(id);
        ra.addFlashAttribute("mensaje", "Gasto eliminado");
        return "redirect:/gastos";
    }
}