package pl.pacinho.MasterBet.utils;

import org.apache.commons.math3.util.Precision;
import pl.pacinho.MasterBet.model.MatchResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
    public static Double getRandomDouble(double min, double max) {
        return Precision.round(ThreadLocalRandom.current().nextDouble(min, max), 2);
    }

    public static int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static long getRandomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    public static MatchResult getRandomMatchResult() {
        return MatchResult.values()[getRandomInt(0, MatchResult.values().length - 1)];
    }

    public static Date getRandomDate(Date d1, Date d2) {
        return new Date(getRandomLong(d1.getTime(), d2.getTime()));
    }

    public static BigDecimal getRandomBigDecimal(double min, double max) {
       return BigDecimal.valueOf(getRandomDouble(min, max)).setScale(2, RoundingMode.CEILING);
    }
}