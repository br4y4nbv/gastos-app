package com.br4y4nbv.gastos_app.controller;

import com.br4y4nbv.gastos_app.dto.GastoForm;
import com.br4y4nbv.gastos_app.service.GastoService;
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

@Controller
@RequestMapping("/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int page, Model model) {
        var pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "fecha"));
        model.addAttribute("pagina", gastoService.listar(pageable));
        return "gastos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        GastoForm form = new GastoForm();
        form.setFecha(LocalDate.now());
        model.addAttribute("gastoForm", form);
        model.addAttribute("categorias", gastoService.listarCategorias());
        return "gastos/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("gastoForm", gastoService.obtenerForm(id));
        model.addAttribute("categorias", gastoService.listarCategorias());
        return "gastos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("gastoForm") GastoForm form,
                          BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", gastoService.listarCategorias());
            return "gastos/form";
        }
        gastoService.guardar(form);
        ra.addFlashAttribute("mensaje", "Gasto guardado correctamente");
        return "redirect:/gastos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        gastoService.eliminar(id);
        ra.addFlashAttribute("mensaje", "Gasto eliminado");
        return "redirect:/gastos";
    }
}