package com.br4y4nbv.gastos_app.controller;

import com.br4y4nbv.gastos_app.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final DashboardService dashboardService;

    @GetMapping("/")
    public String dashboard(Model model) {
        LocalDate hoy = LocalDate.now();
        Locale es = Locale.forLanguageTag("es");

        model.addAttribute("mes", hoy.getMonth().getDisplayName(TextStyle.FULL, es) + " " + hoy.getYear());
        model.addAttribute("resumen", dashboardService.resumenDelMes());
        model.addAttribute("ultimos", dashboardService.ultimosGastos());
        return "dashboard";
    }
}