package pl.pacinho.MasterBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pacinho.MasterBet.entities.Coupon;
import pl.pacinho.MasterBet.model.CouponStatus;
import pl.pacinho.MasterBet.repositories.CouponRepository;

import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<Coupon> getAll() {
        return couponRepository.findAll();
    }

    public List<Coupon> getAllByStatus(CouponStatus couponStatus) {
        return couponRepository.findAllByStatus(couponStatus);
    }

    public void save(Coupon coupon) {
        couponRepository.save(coupon);
    }
}
