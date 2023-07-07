package pl.pacinho.MasterBet.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pl.pacinho.MasterBet.model.CouponState;
import pl.pacinho.MasterBet.model.CouponStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Coupons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {

    @Id
    @GenericGenerator(name = "couponId", strategy = "increment")
    @GeneratedValue(generator = "couponId")
    private long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "USER_ID")
    private User user;

    private Date createdDate = new Date();

    @Getter(AccessLevel.NONE)
    @Column(columnDefinition = "DOUBLE PRECISION")
    private BigDecimal amount;

    @Getter(AccessLevel.NONE)
    @Column(columnDefinition = "DOUBLE PRECISION")
    private BigDecimal totalCourse;

    @Getter(AccessLevel.NONE)
    @Column(columnDefinition = "DOUBLE PRECISION")
    private BigDecimal totalRisk;

    @Getter(AccessLevel.NONE)
    @Column(columnDefinition = "DOUBLE PRECISION")
    private BigDecimal toWin;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @Enumerated(EnumType.STRING)
    private CouponState state;

    @JsonManagedReference
    @OneToMany(mappedBy = "coupon", fetch = FetchType.EAGER)
    private List<Bet> bets;

    public Coupon(User user, BigDecimal amount, BigDecimal totalCourse, BigDecimal totalRisk, BigDecimal toWin, CouponStatus status, CouponState state) {
        this.user = user;
        this.amount =  amount.setScale(2, RoundingMode.CEILING);
        this.totalCourse = totalCourse.setScale(2, RoundingMode.CEILING);
        this.totalRisk = totalRisk.setScale(2, RoundingMode.CEILING);
        this.toWin = toWin.setScale(2, RoundingMode.CEILING);
        this.status = status;
        this.state = state;
    }

    public BigDecimal getAmount() {
        return amount.setScale(2, RoundingMode.CEILING);
    }


    public BigDecimal getTotalCourse() {
        return totalCourse.setScale(2, RoundingMode.CEILING);
    }


    public BigDecimal getTotalRisk() {
        return totalRisk.setScale(2, RoundingMode.CEILING);
    }


    public BigDecimal getToWin() {
        return toWin.setScale(2, RoundingMode.CEILING);
    }

}
