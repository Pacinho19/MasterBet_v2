package pl.pacinho.MasterBet.utils;

import pl.pacinho.MasterBet.model.MatchResult;

public class ResultUtils {

    public static MatchResult getByScore(Integer scoreA, Integer scoreB) {
        if (scoreA == null || scoreB == null) {
            return null;
        }
        if (scoreA < scoreB) {
            return MatchResult.B;
        }
        if (scoreA > scoreB) {
            return MatchResult.A;
        }
        return MatchResult.X;

    }
}
