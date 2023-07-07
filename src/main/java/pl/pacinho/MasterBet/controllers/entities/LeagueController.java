package pl.pacinho.MasterBet.controllers.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pacinho.MasterBet.controllers.SimpleCrudController;
import pl.pacinho.MasterBet.entities.League;
import pl.pacinho.MasterBet.service.LeagueService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/league")
public class LeagueController implements SimpleCrudController<League> {

    private final LeagueService leagueService;

    @Autowired
    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

   @Override
    public List<League> getAll(){
        return leagueService.getAll();
    }

    @Override
    public Optional<League> getById(@PathVariable(name = "id") long id){
        return leagueService.getById(id);
    }

    @Override
    public Optional<League> getByName(@RequestParam(name = "name") String name){
        return leagueService.getByName(name);
    }

    @Override
    public League add(@RequestBody League league){
        return  leagueService.addOrUpdate(league);
    }

    @Override
    public League update(@RequestBody League league){
        return leagueService.addOrUpdate(league);
    }

    @Override
    public String delete(@RequestBody League league){
        leagueService.delete(league);
        return "Delete finish";
    }

    @Override
    public String delete(@PathVariable(name = "id") long id){
        leagueService.deleteById(id);
        return "DeleteById finish";
    }
}
