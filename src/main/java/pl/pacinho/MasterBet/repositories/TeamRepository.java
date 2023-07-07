package pl.pacinho.MasterBet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pacinho.MasterBet.entities.League;
import pl.pacinho.MasterBet.entities.Team;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);
}
