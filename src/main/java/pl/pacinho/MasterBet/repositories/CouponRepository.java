package pl.pacinho.MasterBet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.MasterBet.entities.Coupon;
import pl.pacinho.MasterBet.entities.User;
import pl.pacinho.MasterBet.model.CouponStatus;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findAllByUser(User user);

    List<Coupon> findAllByStatus(CouponStatus status);
}
