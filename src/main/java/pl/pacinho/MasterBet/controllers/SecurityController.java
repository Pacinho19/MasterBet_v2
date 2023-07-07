package pl.pacinho.MasterBet.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {
    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "401";
    }
}
