package pl.pacinho.MasterBet.utils;

import org.apache.commons.math3.util.Precision;
import pl.pacinho.MasterBet.entities.Match;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RiskCalculator {

    public static BigDecimal calculate(Match match, BigDecimal selectedBetCourse) {
        return (((selectedBetCourse).multiply(BigDecimal.valueOf(100)))
                .divide(match.getCourseA().add(match.getCourseB()).add(match.getCourseX()), 2, RoundingMode.HALF_UP));
    }
}
