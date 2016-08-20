package com.gosenk.sports.alarm.scrape.dso;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "league")
public class League extends BaseEntity{

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "league")
    private Set<Team> teams = new HashSet<>(0);

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }
}
