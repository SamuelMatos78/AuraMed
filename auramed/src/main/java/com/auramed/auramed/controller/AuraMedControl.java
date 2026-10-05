package com.auramed.auramed.controller;
import com.auramed.auramed.model.InternacaoResumo;
import com.auramed.auramed.model.QuartoVagas;
import com.auramed.auramed.service.InternacaoService;
import com.auramed.auramed.service.ProfissionalSaudeService;
import com.auramed.auramed.service.QuartoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDateTime;
import java.util.List;


@Controller
public class AuraMedControl {

    private final InternacaoService internacoes;
    private final ProfissionalSaudeService profissionais;
    private final QuartoService quartos;

    public AuraMedControl(InternacaoService internacoes, ProfissionalSaudeService profissionais, QuartoService quartos) {
        this.internacoes = internacoes; this.profissionais = profissionais; this.quartos = quartos;
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

    @GetMapping("/internacoes")
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

    @GetMapping ("/pacientes")
    public String pacientesPage(){
        return "pacientes";
    }



}
