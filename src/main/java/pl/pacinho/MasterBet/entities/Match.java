package pl.pacinho.MasterBet.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pl.pacinho.MasterBet.model.MatchResult;
import pl.pacinho.MasterBet.utils.ResultUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Entity
@Table(name = "Matches")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Match {

    @Id
    @GenericGenerator(name = "matchId", strategy = "increment")
    @GeneratedValue(generator = "matchId")
    private long id;

    @ManyToOne
    @JoinColumn(name = "TEAM_A")
    private Team teamA;

    @ManyToOne
    @JoinColumn(name = "TEAM_B")
    private Team teamB;

    private Date eventDate;

    @Getter(AccessLevel.NONE)
    @Column(name = "COURSE_A",columnDefinition = "DOUBLE PRECISION")
    private BigDecimal courseA;

    @Getter(AccessLevel.NONE)
    @Column(name = "COURSE_B",columnDefinition = "DOUBLE PRECISION")
    private BigDecimal courseB;

    @Getter(AccessLevel.NONE)
    @Column(name = "COURSE_X",columnDefinition = "DOUBLE PRECISION")
    private BigDecimal courseX;

    @Column(nullable = true)
    private Integer scoreA;
    @Column(nullable = true)
    private Integer scoreB;

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private MatchResult result;

    @Transient
    private String userType;

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String score;

    public Match(Team teamA,
                 Team teamB,
                 Date eventDate,
                 BigDecimal courseA,
                 BigDecimal courseB,
                 BigDecimal courseX) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.eventDate = eventDate;
        this.courseA = courseA.setScale(2, RoundingMode.CEILING);;
        this.courseB = courseB.setScale(2, RoundingMode.CEILING);;
        this.courseX = courseX.setScale(2, RoundingMode.CEILING);;
    }

    public MatchResult getResult() {
        if (result != null) {
            return result;
        }
        return ResultUtils.getByScore(scoreA, scoreB);
    }

    public String getScore() {
        if (scoreA == null || scoreB == null) {
            return "";
        }
        return scoreA + ":" + scoreB;
    }

    public BigDecimal getCourseByType(MatchResult matchResult) {
        return matchResult == MatchResult.A ? courseA : matchResult == MatchResult.B ? courseB : courseX;
    }

    public BigDecimal getCourseA() {
        return courseA.setScale(2, RoundingMode.CEILING);
    }

    public BigDecimal getCourseB() {
        return courseB.setScale(2, RoundingMode.CEILING);
    }

    public BigDecimal getCourseX() {
        return courseX.setScale(2, RoundingMode.CEILING);
    }

}
