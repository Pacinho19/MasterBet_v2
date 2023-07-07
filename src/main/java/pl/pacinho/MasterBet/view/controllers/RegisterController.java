package pl.pacinho.MasterBet.view.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pacinho.MasterBet.config.CryptoConfig;
import pl.pacinho.MasterBet.entities.User;
import pl.pacinho.MasterBet.model.messages.RegisterMessage;
import pl.pacinho.MasterBet.model.Role;
import pl.pacinho.MasterBet.repositories.UserRepository;
import pl.pacinho.MasterBet.utils.EmailSender;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    protected final Logger logger = Logger.getLogger(RegisterController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CryptoConfig cryptoConfig;
    @Autowired
    private EmailSender emailSender;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(Model model, User user) throws Exception {

        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        if (byUsername.isPresent()) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", RegisterMessage.USERNAME_ERROR.getMessage());
            return "register";
        }

        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail.isPresent()) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", RegisterMessage.EMAIL_ERROR.getMessage());
            return "register";
        }

        user.setPassword(cryptoConfig.encoder().encode(user.getPassword()));
        user.setRoles(Role.ROLE_UNCONFIRMED_USER);
        User save = userRepository.save(user);
        if (save.getId() > 0) {
            emailSender.sendSimpleEmail(user.getEmail(),
                    "Register in MasterBet 2.0",
                    "Hello " + user.getUsername() + "!\n" +
                            "Link to activate account in MasterBet 2.0  :\n"
                            + "http://localhost:8092/activate?secret=" + cryptoConfig.hashBySHA256(user.getUsername())
                            + "&id=" + save.getId());
            model.addAttribute("message", "Please check your email (" + user.getEmail() + ") inbox and confirm registration !");
            logger.info("[REGISTER] -> Send activation link to " + user.getEmail() + " (user " + user.getUsername() + ")");
            return "login";
        }
        throw new Exception();
    }

    @GetMapping("/activate")
    public String activateAccount(Model model, @RequestParam("secret") String secret, @RequestParam("id") int id) throws Exception {
        Optional<User> byId = userRepository.findByIdAndActive(id, 0);
        if (!byId.isPresent()) {
            model.addAttribute("message", "");
            return "redirect:/login?message=1";
        }

        User user = byId.get();
        String userSecret = cryptoConfig.hashBySHA256(user.getUsername());
        if (userSecret.equals(secret)) {
            user.setRoles(Role.ROLE_USER);
            user.setActive(1);
            user.setBalance(BigDecimal.valueOf(50l));
            userRepository.save(user);
            logger.info("[REGISTER] -> User " + user.getUsername() + " activated !");
            model.addAttribute("message", "");
            return "redirect:/login?message=2";
        }
        model.addAttribute("message", "");
        return "redirect:/login?message=3";
    }
}
