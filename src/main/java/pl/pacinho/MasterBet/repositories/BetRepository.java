package pl.pacinho.MasterBet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.MasterBet.entities.Bet;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {
}
