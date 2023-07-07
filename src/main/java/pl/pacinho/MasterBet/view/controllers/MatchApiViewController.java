package pl.pacinho.MasterBet.view.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pacinho.MasterBet.entities.Match;
import pl.pacinho.MasterBet.service.MatchService;

@Controller
@RequestMapping("/api/match/crud")
@RequiredArgsConstructor
public class MatchApiViewController {

    private final MatchService matchService;

    @RequestMapping("/updateScore")
    public String updateScore(@ModelAttribute(value="match") Match match, Model model) {
        matchService.updateScore(match);
        return "redirect:/view/matches/scores";
    }

}
