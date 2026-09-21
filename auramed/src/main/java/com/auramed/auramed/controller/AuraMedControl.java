package com.auramed.auramed.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller 
public class AuraMedControl {



    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/home") //contem dashboard
    public String homePage() {
        //model.addAttribute("nome", principal.getName()); entrega futura
        return "home";
    }

    @GetMapping("/internacoes")
    public String internacoesPage() {
        //model.addAttribute("nome", principal.getName()); entrega futura
        return "internacoes";
    }

    @GetMapping("/quartos")
    public String quartosPAge(){

        return "quartos";
    }

    @GetMapping ("/pacientes")
    public String pacientesPage(){
        return "pacientes";
    }



}
