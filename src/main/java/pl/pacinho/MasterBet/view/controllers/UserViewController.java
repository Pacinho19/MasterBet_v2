package pl.pacinho.MasterBet.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.pacinho.MasterBet.config.CryptoConfig;
import pl.pacinho.MasterBet.entities.User;
import pl.pacinho.MasterBet.model.messages.PasswordMessage;
import pl.pacinho.MasterBet.repositories.UserRepository;
import pl.pacinho.MasterBet.utils.ImageUtil;
import pl.pacinho.MasterBet.utils.SessionUtils;
import pl.pacinho.MasterBet.utils.UserUtils;

import java.io.IOException;

@Controller
@RequestMapping("/view/user")
public class UserViewController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CryptoConfig cryptoConfig;

    @GetMapping
    public String userPage(Model model, @RequestParam(value = "error", required = false, defaultValue = "-1") int error) {
        UserDetails userDetails = SessionUtils.currentUserDetails();
        if (userDetails == null) {
            return "redirect:/unauthorized";
        }
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        boolean admin = UserUtils.checkRole(userDetails, "ADMIN");
        model.addAttribute("admin", admin);
        model.addAttribute("user", user);
        model.addAttribute("imgUtil", new ImageUtil());
        model.addAttribute("image", user.getPhoto());
        if (error > 0) {
            model.addAttribute("errorMessage", PasswordMessage.byId(error));
        }
        return "user";
    }

    @PostMapping("/changePass")
    public String changePass(Model model, String password1, String oldPassword) {
        UserDetails userDetails = SessionUtils.currentUserDetails();
        if (userDetails == null) {
            return "redirect:/unauthorized";
        }
        User u = userRepository.findByUsername(userDetails.getUsername()).get();

        if (!cryptoConfig.encoder().matches(oldPassword, u.getPassword())) {
            return "redirect:/view/user?error=" + PasswordMessage.OLD_PASS_WRONG.getId();
        } else if (cryptoConfig.encoder().matches(password1, u.getPassword())) {
            return "redirect:/view/user?error=" + PasswordMessage.OLD_NEW_PASS_EQUALS.getId();
        }
        u.setPassword(cryptoConfig.encoder().encode(password1));
        userRepository.save(u);
        return "redirect:/view/user";
    }

    @PostMapping("/changePhoto")
    public String changePhoto(Model model, @RequestParam("img") MultipartFile img) throws IOException {
        UserDetails userDetails = SessionUtils.currentUserDetails();
        if (userDetails == null) {
            return "redirect:/unauthorized";
        }
        User u = userRepository.findByUsername(userDetails.getUsername()).get();
        u.setPhoto(img.getBytes());
        userRepository.save(u);
        return "redirect:/view/user";
    }
}
