package pl.pacinho.MasterBet.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.apache.commons.math3.util.Precision;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "Teams")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    @GenericGenerator(name = "teamId", strategy = "increment")
    @GeneratedValue(generator = "teamId")
    private long id;

    @Column(unique = true)
    private String name;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "LEAGUE_ID")
    private League league;

    private String coach;

    @Column(name = "TEAM_RATING")
    private double teamRating;

    @Column(name = "FORM_RATING")
    private double formRating;

    public Team(String name, League league, String coach, double teamRating, double formRating) {
        this.name = name;
        this.league = league;
        this.coach = coach;
        this.teamRating = teamRating;
        this.formRating = formRating;
    }

    public double getOverallRating() {
        return Precision.round((teamRating + formRating) / 2, 2);
    }
}
