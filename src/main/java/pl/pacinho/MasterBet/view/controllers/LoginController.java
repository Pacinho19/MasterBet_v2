package pl.pacinho.MasterBet.view.controllers;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pacinho.MasterBet.model.messages.LoginMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model, @RequestParam(name = "message", required = false, defaultValue = "-1") int message,
                        HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String errorMessage = "";
        String messageS = "";

        if (message != -1) {
            messageS = LoginMessage.byId(message).getMessage();
        }

        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        model.addAttribute("message",messageS);
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }

}
