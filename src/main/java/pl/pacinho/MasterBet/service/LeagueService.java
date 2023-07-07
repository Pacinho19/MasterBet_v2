package pl.pacinho.MasterBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pacinho.MasterBet.entities.League;
import pl.pacinho.MasterBet.repositories.LeagueRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;

    @Autowired
    public LeagueService(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    public List<League> getAll(){
        return leagueRepository.findAll();
    }

    public Optional<League> getById(long id) {
        return leagueRepository.findById(id);
    }

    public Optional<League> getByName(String name) {
        return leagueRepository.findByName(name);
    }

    public League addOrUpdate(League league) {
        return leagueRepository.save(league);
    }

    public void delete(League league){
        leagueRepository.delete(league);
    }

    public void deleteById(long id){
        leagueRepository.deleteById(id);
    }
}
