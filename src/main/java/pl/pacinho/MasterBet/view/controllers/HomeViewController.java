package pl.pacinho.MasterBet.view.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.pacinho.MasterBet.entities.Bet;
import pl.pacinho.MasterBet.entities.Coupon;
import pl.pacinho.MasterBet.entities.Match;
import pl.pacinho.MasterBet.entities.User;
import pl.pacinho.MasterBet.model.messages.CouponMessage;
import pl.pacinho.MasterBet.model.CouponState;
import pl.pacinho.MasterBet.model.CouponStatus;
import pl.pacinho.MasterBet.model.MatchResult;
import pl.pacinho.MasterBet.repositories.BetRepository;
import pl.pacinho.MasterBet.repositories.CouponRepository;
import pl.pacinho.MasterBet.repositories.UserRepository;
import pl.pacinho.MasterBet.service.MatchService;
import pl.pacinho.MasterBet.utils.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/home")
public class HomeViewController {

    private final MatchService matchService;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final BetRepository betRepository;

    @GetMapping
    public String home(Model model,
                       @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                       @RequestParam(value = "errorMessage", required = false, defaultValue = "-1") int errorMessage,
                       HttpSession session) {
        UserDetails userDetails = SessionUtils.currentUserDetails();
        if (userDetails == null) {
            return "redirect:/unauthorized";
        }
        boolean admin = UserUtils.checkRole(userDetails, "ADMIN");
        model.addAttribute("admin", admin);
        model.addAttribute("user", userRepository.findByUsername(userDetails.getUsername()).get());
        model.addAttribute("matches", matchService.getPagesWhereDateIsBeforeNowAndScoreIsNull(pageNumber, 10));
        model.addAttribute("scores", matchService.getLast10MatchesWithScore());
        model.addAttribute("errorMessage", errorMessage > 0 ? CouponMessage.byId(errorMessage).getMessage() : "");
        model.addAttribute("match", new Match());
        model.addAttribute("coupon", new Coupon());
        List<Bet> list = (List<Bet>) session.getAttribute("coupon");
        model.addAttribute("bets", list);
        model.addAttribute("totalCourse", list == null || list.isEmpty() ? 0 :
                list.stream()
                        .map(b -> b.getCourse())
                        .reduce(BigDecimal.ONE, BigDecimal::multiply)
                        .setScale(2, RoundingMode.CEILING));
        model.addAttribute("totalRisk", list == null || list.isEmpty() ? 0 :
                list.stream()
                        .map(b -> b.getRisk())
                        .max(BigDecimal::compareTo)
                        .get());
        return "home";
    }


    @PostMapping("/addBet")
    public String addBet(HttpSession session, @ModelAttribute(value = "match") Match m) {
        List<Bet> list = (List<Bet>) session.getAttribute("coupon");
        if (list == null) {
            list = new ArrayList<>();
        }
        if (m != null) {
            boolean present = list.stream().anyMatch(b -> b.getMatch().getId() == m.getId());
            if (present) {
                return "redirect:/home?errorMessage=1";
            }

            Match match = matchService.getById(m.getId());
            MatchResult matchType = MatchResult.valueOf(m.getUserType());
            BigDecimal courseByType = match.getCourseByType(matchType);
            Bet bet = new Bet();
            bet.setMatch(match);
            bet.setMatchResult(matchType);
            bet.setCourse(courseByType);
            bet.setRisk(RiskCalculator.calculate(match, courseByType));
            list.add(bet);
            session.setAttribute("coupon", list);
        }
        return "redirect:/home";
    }

    @PostMapping("/removeBet")
    public String removeBet(HttpSession session, @RequestParam(name = "matchId") long matchId) {
        List<Bet> list = (List<Bet>) session.getAttribute("coupon");
        if (list != null) {
            list.remove(list.stream().filter(b -> b.getMatch().getId() == matchId).findFirst().get());
            session.setAttribute("coupon", list);
        }
        return "redirect:/home";
    }

    @PostMapping("/saveCoupon")
    public String saveCoupon(HttpSession session, @ModelAttribute(value = "coupon") Coupon coupon) {
        UserDetails userDetails = SessionUtils.currentUserDetails();
        if (userDetails == null) {
            return "redirect:/unauthorized";
        }
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        if (user.getBalance().compareTo(coupon.getAmount()) == -1) {
            return "redirect:/home?errorMessage=2";
        }
        List<Bet> bets = (List<Bet>) session.getAttribute("coupon");
        if (bets == null || bets.isEmpty()) {
            return "redirect:/home?errorMessage=3";
        }

        BigDecimal totalCourse = bets.stream().map(Bet::getCourse).reduce(BigDecimal.ONE, (a, b) -> a.multiply(b));
        BigDecimal totalRisk = bets.stream().map(Bet::getRisk).max(BigDecimal::compareTo).get();
        BigDecimal amount = coupon.getAmount();
        Coupon c = new Coupon(user, amount, totalCourse, totalRisk
                , WinningAmmountCalculator.getValue(totalCourse, amount), CouponStatus.WAITING, CouponState.WAITING);
        Coupon completeCoupon = couponRepository.save(c);
        bets.stream()
                .peek(bet -> bet.setCoupon(completeCoupon))
                .forEach(betRepository::save);
        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);
        bets.clear();
        session.setAttribute("coupon", bets);
        return "redirect:/view/coupons";
    }
}
