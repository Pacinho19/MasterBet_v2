package pl.pacinho.MasterBet.view.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pacinho.MasterBet.entities.Match;
import pl.pacinho.MasterBet.repositories.UserRepository;
import pl.pacinho.MasterBet.service.MatchService;
import pl.pacinho.MasterBet.utils.SessionUtils;
import pl.pacinho.MasterBet.utils.UserUtils;

@Controller
@RequestMapping("/view/matches")
public class MatchViewController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchService matchService;

//    @GetMapping
//    public String getMatches(Model model){
//        UserDetails userDetails = SessionUtils.currentUserDetails();
//        if (userDetails == null) {
//            return "redirect:/unauthorized";
//        }
//        Optional<User> byUsername = userRepository.findByUsername(userDetails.getUsername());
//        if(!byUsername.isPresent()){
//            return "redirect:/unauthorized";
//        }
//        List<Match> matches = matchService.getAll();
//        model.addAttribute("matches",matches);
//        return "matches";
//    }

    @GetMapping("/scores")
    public String matchesScoresPagination(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                          @RequestParam(value = "filterType", required = false, defaultValue = "f1") String filterType,
                                          Model model) {
        if (filterType.equals("f1")) {
            model.addAttribute("matches", matchService.getPagesWhereDateIsBeforeNow(pageNumber, size));
        } else if (filterType.equals("f2")) {
            model.addAttribute("matches", matchService.getPagesWhereDateIsBeforeNowAndScoreIsNotNull(pageNumber, size));
        } else if (filterType.equals("f3")) {
            model.addAttribute("matches", matchService.getPagesWhereDateIsBeforeNowAndScoreIsNull(pageNumber, size));
        }

        model.addAttribute("filterType", filterType);
        UserDetails userDetails = SessionUtils.currentUserDetails();
        if (userDetails == null) {
            return "redirect:/unauthorized";
        }
        boolean admin = UserUtils.checkRole(userDetails, "ADMIN");
        model.addAttribute("match", new Match());
        model.addAttribute("admin", admin);
        model.addAttribute("user", userRepository.findByUsername(userDetails.getUsername()).get());
        return "scores";
    }

    @GetMapping
    public String matchesPagination(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size, Model model) {
        UserDetails userDetails = SessionUtils.currentUserDetails();
        if (userDetails == null) {
            return "redirect:/unauthorized";
        }
        model.addAttribute("matches", matchService.getPagesWhereDateIsAfterNow(pageNumber, size));
        model.addAttribute("user", userRepository.findByUsername(userDetails.getUsername()).get());
        return "matches";
    }

}
