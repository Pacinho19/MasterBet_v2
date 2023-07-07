package pl.pacinho.MasterBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pacinho.MasterBet.entities.League;
import pl.pacinho.MasterBet.entities.Team;
import pl.pacinho.MasterBet.repositories.TeamRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> getAll(){
        return teamRepository.findAll();
    }

    public Optional<Team> getById(long id) {
        return teamRepository.findById(id);
    }

    public Optional<Team> getByName(String name) {
        return teamRepository.findByName(name);
    }

    public Team addOrUpdate(Team team) {
        return teamRepository.save(team);
    }

    public void delete(Team team){
        teamRepository.delete(team);
    }

    public void deleteById(long id){
        teamRepository.deleteById(id);
    }
}
