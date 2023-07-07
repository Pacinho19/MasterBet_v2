package pl.pacinho.MasterBet.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pl.pacinho.MasterBet.model.MatchResult;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "Bets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Bet {

    @Id
    @GenericGenerator(name = "betId", strategy = "increment")
    @GeneratedValue(generator = "betId")
    private long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "COUPON_ID")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "MATCH_ID")
    private Match match;

    @Enumerated(EnumType.STRING)
    private MatchResult matchResult;

    @Getter(AccessLevel.NONE)
    @Column(columnDefinition = "DOUBLE PRECISION")
    private BigDecimal course;

    @Getter(AccessLevel.NONE)
    @Column(columnDefinition = "DOUBLE PRECISION")
    private BigDecimal risk;

    public Bet(Match match, MatchResult matchResult, BigDecimal course, BigDecimal risk) {
        this.match = match;
        this.matchResult = matchResult;
        this.course = course;
        this.risk = risk;
    }

    public BigDecimal getCourse() {
        return course.setScale(2, RoundingMode.CEILING);
    }

    public BigDecimal getRisk() {
        return risk.setScale(2, RoundingMode.CEILING);
    }
}
