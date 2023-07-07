package pl.pacinho.MasterBet.controllers.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pacinho.MasterBet.entities.User;
import pl.pacinho.MasterBet.service.MyUserDetailsService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @GetMapping( produces = MediaType.IMAGE_JPEG_VALUE)
    public ByteArrayResource getImage() {
        List<User> all = userDetailsService.getAll();
        User user = all.stream().filter(u -> u.getPhoto() != null).findFirst().get();
        return new ByteArrayResource(user.getPhoto());
    }

}
