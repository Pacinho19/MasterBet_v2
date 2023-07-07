package pl.pacinho.MasterBet.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pacinho.MasterBet.entities.Coupon;
import pl.pacinho.MasterBet.entities.User;
import pl.pacinho.MasterBet.repositories.CouponRepository;
import pl.pacinho.MasterBet.repositories.UserRepository;
import pl.pacinho.MasterBet.utils.SessionUtils;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/view/coupons")
public class CouponViewController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;
    @GetMapping
    public String getCoupons(Model model){
        UserDetails userDetails = SessionUtils.currentUserDetails();
        if (userDetails == null) {
            return "redirect:/unauthorized";
        }
        Optional<User> byUsername = userRepository.findByUsername(userDetails.getUsername());
        if(!byUsername.isPresent()){
            return "redirect:/unauthorized";
        }
        List<Coupon> coupons = couponRepository.findAllByUser(byUsername.get());
        model.addAttribute("coupons",coupons);
        model.addAttribute("user", userRepository.findByUsername(userDetails.getUsername()).get());
        return "coupons";
    }

}
