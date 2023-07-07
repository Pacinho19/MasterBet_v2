package pl.pacinho.MasterBet.initializer;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.pacinho.MasterBet.config.CryptoConfig;
import pl.pacinho.MasterBet.entities.*;
import pl.pacinho.MasterBet.model.*;
import pl.pacinho.MasterBet.repositories.*;
import pl.pacinho.MasterBet.utils.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class InitTestData {

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private CryptoConfig cryptoConfig;

    @Value("${masterbet.insertTestData}")
    private boolean insertTestData;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        if (!insertTestData) {
            return;
        }

//        insertLeague();
//        insertTeams();
//        insertUsers();

        insertMatches();
//        insertCoupon();
    }

    private void insertCoupon() {
        User user = userRepository.findByUsername("user").get();
        List<Match> all = matchRepository.findAll();

        IntStream.range(0, RandomUtils.getRandomInt(1, 3))
                .forEach(i1 -> {
                    List<Match> allTemp = new ArrayList<>(all);
                    List<Bet> bets = new ArrayList<>();
                    IntStream.range(0, RandomUtils.getRandomInt(1, all.size()))
                            .forEach(i -> {
                                        Match match = allTemp.get(RandomUtils.getRandomInt(0, allTemp.size() - 1));
                                        allTemp.remove(match);
                                        MatchResult randomMatchResult = RandomUtils.getRandomMatchResult();
                                        BigDecimal courseByType = match.getCourseByType(randomMatchResult);
                                        bets.add(new Bet(match, randomMatchResult, courseByType, RiskCalculator.calculate(match, courseByType)));
                                    }
                            );

                    BigDecimal totalCource = bets.stream().map(Bet::getCourse).reduce(BigDecimal.ONE, (a, b) -> a.multiply(b));
                    BigDecimal totalRisk = bets.stream().map(Bet::getRisk).max(BigDecimal::compareTo).get();
                    BigDecimal ammount = RandomUtils.getRandomBigDecimal(10, 100);
                    Coupon c = new Coupon(user, ammount, totalCource, totalRisk
                            , WinningAmmountCalculator.getValue(totalCource, ammount), CouponStatus.WAITING, CouponState.WAITING);
                    Coupon completeCoupon = couponRepository.save(c);
                    bets.stream()
                            .peek(bet -> bet.setCoupon(completeCoupon))
                            .forEach(betRepository::save);
                });
    }

    private void insertMatches() {
        List<Team> all = teamRepository.findAll();
        IntStream.range(0, RandomUtils.getRandomInt(1, all.size()) * 3)
                .forEach(i -> {
                            Pair<Team, Team> matchTeams = getMatchTeams(all);
                            CourseModel courses = CourseUtils.getCourses(matchTeams.getLeft().getOverallRating(), matchTeams.getRight().getOverallRating());
                            matchRepository.save(
                                    new Match(matchTeams.getLeft(),
                                            matchTeams.getRight(),
                                            RandomUtils.getRandomDate(DateUtils.getDateFromString("2023-07-01 21:00"), DateUtils.getDateFromString("2023-07-10 22:00")),
                                            courses.getCourseA().setScale(2, RoundingMode.CEILING),
                                            courses.getCourseB().setScale(2, RoundingMode.CEILING),
                                            courses.getCourseX().setScale(2, RoundingMode.CEILING)));
                        }
                );
    }

    private void insertUsers() {
        Arrays.asList(
                new User("user", "user@user.pl", cryptoConfig.encoder().encode("user"), 1, Role.ROLE_USER, new BigDecimal(50)),
                new User("admin", "admin@admin.pl",cryptoConfig.encoder().encode("admin"), 1, Role.ROLE_ADMIN, new BigDecimal(50))
        ).forEach(userRepository::save);
    }

    private void insertTeams() {
        Arrays.asList(
                new Team("Fc Barcelona", leagueRepository.findByName("LaLiga").get(), "Ronald Koeaman", RandomUtils.getRandomDouble(1, 10), RandomUtils.getRandomDouble(1, 10)),
                new Team("Real Madryt", leagueRepository.findByName("LaLiga").get(), "Carlo Ancelotti", RandomUtils.getRandomDouble(1, 10), RandomUtils.getRandomDouble(1, 10)),
                new Team("Sevilla Fc", leagueRepository.findByName("LaLiga").get(), "Michael Ballack", RandomUtils.getRandomDouble(1, 10), RandomUtils.getRandomDouble(1, 10)),
                new Team("Atletico Madryt", leagueRepository.findByName("LaLiga").get(), "Diego Simeone", RandomUtils.getRandomDouble(1, 10), RandomUtils.getRandomDouble(1, 10)),
                new Team("Cadiz", leagueRepository.findByName("LaLiga").get(), "Capitan Jack", RandomUtils.getRandomDouble(1, 10), RandomUtils.getRandomDouble(1, 10)),
                new Team("Levante", leagueRepository.findByName("LaLiga").get(), "Chuck D", RandomUtils.getRandomDouble(1, 10), RandomUtils.getRandomDouble(1, 10)),
                new Team("Granada", leagueRepository.findByName("LaLiga").get(), "Carlo Ancelotti Jr", RandomUtils.getRandomDouble(1, 10), RandomUtils.getRandomDouble(1, 10)),
                new Team("Celta Vigo", leagueRepository.findByName("LaLiga").get(), "Coudet Eduardo", RandomUtils.getRandomDouble(1, 10), RandomUtils.getRandomDouble(1, 10)),
                new Team("Elche", leagueRepository.findByName("LaLiga").get(), "Escriba Francisco", RandomUtils.getRandomDouble(1, 10), RandomUtils.getRandomDouble(1, 10)),
                new Team("Mallorca", leagueRepository.findByName("LaLiga").get(), "Garcia Plaza Luis", RandomUtils.getRandomDouble(1, 10), RandomUtils.getRandomDouble(1, 10))
        ).forEach(teamRepository::save);
    }

    private void insertLeague() {
        Arrays.asList(
                new League("LaLiga", Country.SPAIN),
                new League("Bundesliga", Country.GERMANY),
                new League("Ekstraklasa", Country.POLAND),
                new League("Premier League", Country.ENGLAND),
                new League("League 1", Country.FRANCE)
        ).forEach(leagueRepository::save);
    }

    private Pair<Team, Team> getMatchTeams(List<Team> all) {
        Team team = all.get(RandomUtils.getRandomInt(0, all.size() - 1));

        Team team2 = null;

        while (team2 == null) {
            Team teamTemp = all.get(RandomUtils.getRandomInt(0, all.size() - 1));
            if (teamTemp.getId() == team.getId()) {
                continue;
            }
            team2 = teamTemp;
        }

        return new ImmutablePair<>(team, team2);
    }

}
