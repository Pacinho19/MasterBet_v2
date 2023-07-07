package pl.pacinho.MasterBet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pacinho.MasterBet.utils.MediaResponseEntityUtils;
import pl.pacinho.MasterBet.utils.SessionUtils;
import pl.pacinho.MasterBet.utils.UserUtils;

@RestController
public class AppController {

    @Autowired
    private MediaResponseEntityUtils mediaResponseEntityUtils;

    @GetMapping
    public String nothing() {
        return "Here nothing to show...";
    }

    @GetMapping("/ping")
    public String ping() {
        return "I'm working ! :)";
    }

    @GetMapping("/hello")
    public ResponseEntity hello() {
        UserDetails userDetails = SessionUtils.currentUserDetails();
        if (userDetails == null) {
            return mediaResponseEntityUtils.get("401.png", MediaType.IMAGE_PNG);
        }
        boolean admin = UserUtils.checkRole(userDetails, "ADMIN");
        return mediaResponseEntityUtils.get(admin ? "helloAdmin.jpg" : "helloUser.jpg", MediaType.IMAGE_JPEG);
    }



}
