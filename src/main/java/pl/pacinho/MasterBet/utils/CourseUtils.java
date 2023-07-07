package pl.pacinho.MasterBet.utils;

import pl.pacinho.MasterBet.model.CourseModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CourseUtils {

    public static CourseModel getCourses(double a, double b) {
        Double sub = Math.abs(a - b);

        BigDecimal courseA = BigDecimal.ZERO;
        BigDecimal courseB = BigDecimal.ZERO;
        BigDecimal courseX = BigDecimal.ZERO;

        if (sub <= 3) {
            if (a > b) {
                courseA = RandomUtils.getRandomBigDecimal(2, 3);
                courseB = courseA.add(RandomUtils.getRandomBigDecimal(0.5, 2));
            } else if (a < b) {
                courseB = RandomUtils.getRandomBigDecimal(2, 3);
                courseA = courseB.add(RandomUtils.getRandomBigDecimal(0.5, 2));
            } else {
                courseA = RandomUtils.getRandomBigDecimal(2, 3);
                courseB = courseA.add(RandomUtils.getRandomBigDecimal(0.5, 1));
            }
            courseX = RandomUtils.getRandomBigDecimal(1.1, 2);
        } else if (sub > 3 && sub <= 6) {
            if (a > b) {
                courseA = RandomUtils.getRandomBigDecimal(1.1, 2);
                courseB = RandomUtils.getRandomBigDecimal(6, 7);
            } else if (a < b) {
                courseB = RandomUtils.getRandomBigDecimal(1.1, 2);
                courseA = RandomUtils.getRandomBigDecimal(6, 7);
            }
            courseX = RandomUtils.getRandomBigDecimal(4.01, 5);
        } else if (sub > 6) {
            if (a > b) {
                courseA = RandomUtils.getRandomBigDecimal(1.1, 2);
                courseB = RandomUtils.getRandomBigDecimal(8.5, 10);
            } else if (a < b) {
                courseB = RandomUtils.getRandomBigDecimal(1.1, 2);
                courseA = RandomUtils.getRandomBigDecimal(8.5, 10);
            }
            courseX = RandomUtils.getRandomBigDecimal(4.5, 6);
        }
        return new CourseModel(courseA.setScale(2, RoundingMode.CEILING),
                courseX.setScale(2, RoundingMode.CEILING),
                courseB.setScale(2, RoundingMode.CEILING));
    }
}
