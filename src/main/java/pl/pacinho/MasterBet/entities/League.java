package pl.pacinho.MasterBet.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pl.pacinho.MasterBet.model.Country;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Leagues")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class League {

    @Id
    @GenericGenerator(name = "leagueId", strategy = "increment")
    @GeneratedValue(generator = "leagueId")
    private long id;

    @Column(name = "name", unique=true)
    private String name;

    @Column(name = "country")
    @Enumerated(EnumType.STRING)
    private Country country;

    @JsonManagedReference
    @OneToMany(mappedBy = "league", fetch=FetchType.LAZY)
    private List<Team> teams;

    public League(String name, Country country) {
        this.name = name;
        this.country = country;
    }

}
