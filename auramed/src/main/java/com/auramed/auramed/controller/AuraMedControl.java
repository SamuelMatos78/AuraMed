package com.auramed.auramed.controller;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import com.auramed.auramed.model.Consulta;
import com.auramed.auramed.model.InternacaoResumo;

import com.auramed.auramed.model.QuartoVagas;
import com.auramed.auramed.service.ConsultaService;
import com.auramed.auramed.service.InternacaoService;
import com.auramed.auramed.service.PacienteService;
import com.auramed.auramed.service.ProfissionalSaudeService;
import com.auramed.auramed.service.QuartoService;


@Controller
public class AuraMedControl {

    private final InternacaoService internacoes;
    private final ProfissionalSaudeService profissionais;
    private final QuartoService quartos;
    private final PacienteService pacientes;
    private final ConsultaService consultas;

    public AuraMedControl(InternacaoService internacoes, ProfissionalSaudeService profissionais, QuartoService quartos,
                          PacienteService pacientes, ConsultaService consultas) {
        this.consultas = consultas;
        this.internacoes = internacoes; this.profissionais = profissionais; this.quartos = quartos;
        this.pacientes = pacientes;
    }


    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/home") //contem dashboard
    public String homePage() {
        //model.addAttribute("nome", principal.getName()); entrega futura
        return "home";
    }

    @GetMapping(value = "/internacoes", produces = MediaType.TEXT_HTML_VALUE)
    public String internacoesPage(Model model) {
        //model.addAttribute("nome", principal.getName()); entrega futura
        LocalDateTime agora = LocalDateTime.now();
        List<InternacaoResumo> lista = internacoes.listarResumos();
        List<QuartoVagas> quartosComVaga = quartos.listarComVaga();
        model.addAttribute("agora", agora);
        model.addAttribute("internacoes", lista);
        model.addAttribute("altasAtrasadas", lista.stream()
            .filter(i -> i.isAtiva() && i.dataPrevistaAlta().isBefore(agora)).count());
        model.addAttribute("quartosComVaga", quartosComVaga);
        model.addAttribute("leitosLivres", quartosComVaga.stream().mapToLong(QuartoVagas::vagasDisponiveis).sum());
        model.addAttribute("pacientesDisponiveis", internacoes.pacientesDisponiveis());
        model.addAttribute("profissionais", profissionais.listar());
        return "internacoes";
    }

    @GetMapping("/quartos")
    public String quartosPAge(){

        return "quartos";
    }
    @GetMapping(value = "/profissionais", produces = MediaType.TEXT_HTML_VALUE)
    public String profissionaisPage(){

        return "profissionais";
    }
    @GetMapping(value = "/consultas", produces = MediaType.TEXT_HTML_VALUE)
    public String consultasPage(Model model){
        List<Consulta> lista = consultas.listar().stream()
            .sorted(Comparator.comparing(Consulta::getDataHora).reversed()).toList();
        model.addAttribute("consultas", lista);
        model.addAttribute("pacientes", pacientes.listar());
        model.addAttribute("profissionais", profissionais.listar());
        return "consultas";
    }

    @GetMapping(value = "/pacientes", produces = MediaType.TEXT_HTML_VALUE)
    public String pacientesPage(Model model){
        model.addAttribute("pacientes", pacientes.listar());
        return "pacientes";
    }



}
