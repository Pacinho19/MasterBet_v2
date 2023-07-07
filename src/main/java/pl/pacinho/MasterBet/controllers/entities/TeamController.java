package pl.pacinho.MasterBet.controllers.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pacinho.MasterBet.controllers.SimpleCrudController;
import pl.pacinho.MasterBet.entities.League;
import pl.pacinho.MasterBet.entities.Team;
import pl.pacinho.MasterBet.service.LeagueService;
import pl.pacinho.MasterBet.service.TeamService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/team")
public class TeamController implements SimpleCrudController<Team> {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @Override
    public List<Team> getAll(){
        return teamService.getAll();
    }

    @Override
    public Optional<Team> getById(@PathVariable(name = "id") long id){
        return teamService.getById(id);
    }

    @Override
    public Optional<Team> getByName(@RequestParam(name = "name") String name){
        return teamService.getByName(name);
    }

    @Override
    public Team add(@RequestBody Team team){
        return  teamService.addOrUpdate(team);
    }

    @Override
    public Team update(@RequestBody Team team){
        return teamService.addOrUpdate(team);
    }

    @Override
    public String delete(@RequestBody Team team){
        teamService.delete(team);
        return "Delete finish";
    }

    @Override
    public String delete(@PathVariable(name = "id") long id){
        teamService.deleteById(id);
        return "DeleteById finish";
    }
}
