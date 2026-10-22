package com.br4y4nbv.gastos_app.controller;

import com.br4y4nbv.gastos_app.dto.RegistroForm;
import com.br4y4nbv.gastos_app.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    // El POST de /login lo procesa Spring Security solo; aquí únicamente se muestra la página
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("registroForm", new RegistroForm());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registroForm") RegistroForm form,
                            BindingResult result, RedirectAttributes ra) {

        // Validaciones que las anotaciones no pueden hacer.
        // rejectValue asocia el error a un campo concreto del formulario.
        if (form.getPassword() != null && !form.getPassword().equals(form.getConfirmarPassword())) {
            result.rejectValue("confirmarPassword", "no.coincide", "Las contraseñas no coinciden");
        }
        if (form.getEmail() != null && !result.hasFieldErrors("email")
                && usuarioService.existeEmail(form.getEmail())) {
            result.rejectValue("email", "duplicado", "Este correo ya está registrado");
        }

        if (result.hasErrors()) {
            return "auth/registro";
        }

        usuarioService.registrar(form.getNombre(), form.getEmail(), form.getPassword());
        ra.addFlashAttribute("mensaje", "Cuenta creada. Ya puedes iniciar sesión");
        return "redirect:/login";
    }
}