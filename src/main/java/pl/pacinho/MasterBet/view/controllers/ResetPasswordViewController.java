package pl.pacinho.MasterBet.view.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.pacinho.MasterBet.config.CryptoConfig;
import pl.pacinho.MasterBet.entities.User;
import pl.pacinho.MasterBet.service.MyUserDetailsService;
import pl.pacinho.MasterBet.utils.EmailSender;

import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/resetPassword")
@RequiredArgsConstructor
public class ResetPasswordViewController {

    protected final Logger logger = Logger.getLogger(ResetPasswordViewController.class);

    private final MyUserDetailsService userService;
    private final EmailSender emailSender;
    @Autowired
    private CryptoConfig cryptoConfig;

    @GetMapping
    public String resetFirstStepView() {
        return "resetPassword";
    }

    @PostMapping
    public String sendEmail(Model model, String email) {
        Optional<User> byEmail = userService.getByEmail(email);
        if (!byEmail.isPresent()) {
            logger.info("[RESET PASSWORD] -> Not found user by email : "  + email);
            throw new UsernameNotFoundException("Could not find any optionalUser with the email ");
        }
        User user = byEmail.get();
        String unlockCode = cryptoConfig.hashBySHA256(new Date().toString());
        String resetPasswordLink = "http://localhost:8092/resetPassword/resetLastStep?token=" + unlockCode;
        emailSender.sendSimpleEmail(email, "Reset Password", "Hello,"
                + "You have requested to reset your password."
                + " Click the link below to change your password:"
                + resetPasswordLink + "\n"
                + "Ignore this email if you do remember your password, "
                + "or you have not made the request.");

        logger.info("[RESET PASSWORD] -> User " + user.getUsername() + " Reset password. Send token " + unlockCode + " to email " + email);
        user.setResetPasswordCode(unlockCode);
        userService.save(user);
        model.addAttribute("message", "Please check your email (" + user.getEmail() + ") inbox and confirm reset password !");
        return "login";
    }

    @GetMapping("/resetLastStep")
    public String resetLastStepView(Model model, @RequestParam("token") String token) {
        Optional<User> byResetPasswordToken = userService.getByResetPasswordToken(token);
        model.addAttribute("user", byResetPasswordToken.get());
        if (!byResetPasswordToken.isPresent()) {
            logger.info("[RESET PASSWORD] -> Not found user by token : " + token);
            model.addAttribute("tokenError", true);
        }
        return "resetPasswordLastStep";
    }

    @PostMapping("/reset")
    public String reset(String password1, int idUser) {
        Optional<User> byId = userService.getById(idUser);
        if(byId.isPresent()){
            User user = byId.get();
            user.setPassword(cryptoConfig.encoder().encode(password1));
            userService.save(user);
            logger.info("[RESET PASSWORD] -> User " + user.getUsername() + " Reset password complete!");
        }
        return "redirect:/login?message=4";
    }

}
