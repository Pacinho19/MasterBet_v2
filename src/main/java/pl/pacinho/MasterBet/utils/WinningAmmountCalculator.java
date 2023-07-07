package pl.pacinho.MasterBet.utils;

import org.apache.commons.math3.util.Precision;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WinningAmmountCalculator {

    public static BigDecimal getValue(BigDecimal totalCourse, BigDecimal ammount) {
        return (totalCourse.multiply(ammount).multiply(BigDecimal.valueOf(0.85d)).setScale(2, RoundingMode.CEILING));
    }
}
