package pl.pacinho.MasterBet.scheduler;


import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.pacinho.MasterBet.entities.Bet;
import pl.pacinho.MasterBet.entities.Coupon;
import pl.pacinho.MasterBet.entities.User;
import pl.pacinho.MasterBet.model.CouponState;
import pl.pacinho.MasterBet.model.CouponStatus;
import pl.pacinho.MasterBet.service.CouponService;
import pl.pacinho.MasterBet.service.MyUserDetailsService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CouponChecker {

    protected final Logger logger = Logger.getLogger(CouponChecker.class);

    private final int FIXED_RATE = 1000 * 60 * 5; //5 minutes

    private final CouponService couponService;

    private final MyUserDetailsService userDetailsService;

    public CouponChecker(CouponService couponService, MyUserDetailsService userDetailsService) {
        this.couponService = couponService;
        this.userDetailsService = userDetailsService;
    }

    @Scheduled(fixedRate = FIXED_RATE)
    public void checkCoupons() {
        List<Coupon> allWaiting = couponService.getAllByStatus(CouponStatus.WAITING);
        logger.info("[COUPON] -> " + allWaiting.size() + " coupons is waiting.");
        allWaiting.forEach(c -> {
            checkCoupom(c);
        });

    }

    private void checkCoupom(Coupon c) {
        List<Bet> bets = c.getBets();
        List<Integer> matchResult = bets.stream()
                .map(b -> checkMatch(b))
                .collect(Collectors.toList());


        if (matchResult.contains(0)) {
            c.setStatus(CouponStatus.SETTLED);
            c.setState(CouponState.LOSE);
            couponService.save(c);
            logger.info("[COUPON] -> Coupon " + c.getId() + " Lose (User : " + c.getUser().getUsername() + ")");
            //Lose
        } else if (matchResult.stream().allMatch(i -> i == 1)) {
            //Win
            c.setStatus(CouponStatus.SETTLED);
            c.setState(CouponState.WIN);
            couponService.save(c);
            userDetailsService.updateBallance(c.getUser().getId(), c.getToWin());
            logger.info("[COUPON] -> Coupon " + c.getId() + " Win (User : " + c.getUser().getUsername() + ")");
        }
    }

    private int checkMatch(Bet b) {
        return b.getMatch().getScoreA() != null ?
                b.getMatchResult() == b.getMatch().getResult() ? 1 : 0
                : -1;
    }
}
