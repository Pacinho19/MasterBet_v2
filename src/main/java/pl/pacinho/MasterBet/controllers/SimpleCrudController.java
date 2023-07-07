package pl.pacinho.MasterBet.controllers;


import org.springframework.web.bind.annotation.*;
import pl.pacinho.MasterBet.entities.Team;

import java.util.List;
import java.util.Optional;

public interface SimpleCrudController<T> {

    @GetMapping
    List<T> getAll();

    @GetMapping("/{id}")
    Optional<T> getById(@PathVariable(name = "id") long id);

    @GetMapping("/{name}")
    Optional<T> getByName(@PathVariable(name = "name")String name);

    @PostMapping
    T add(@RequestBody T t);

    @PutMapping
    T update(@RequestBody T t);

    @DeleteMapping
    String delete(@RequestBody T t);

    @DeleteMapping("/{id}")
    String delete(@PathVariable(name = "id") long id);

}
