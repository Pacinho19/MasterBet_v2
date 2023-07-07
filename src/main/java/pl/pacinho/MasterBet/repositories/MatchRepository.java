package pl.pacinho.MasterBet.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.MasterBet.entities.Match;

import java.util.Date;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    Page<Match> findAllByEventDateGreaterThan(Date date, Pageable pageable);
    Page<Match> findAllByEventDateLessThan(Date date, Pageable pageable);

    Page<Match> findAllByScoreAIsNotNullAndEventDateLessThan(Date date, Pageable request);
    List<Match> findFirst10ByScoreAIsNotNullAndEventDateLessThanOrderByEventDateDesc(Date date);


    Page<Match> findAllByScoreAIsNullAndEventDateLessThan(Date date, Pageable request);
}
