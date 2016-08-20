package com.gosenk.sports.alarm.scrape.dso;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "team")
public class Team extends BaseEntity{

    @Column(name = "identifier")
    private String identifier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "team")
    private Set<Game> schedule = new HashSet<>();

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Set<Game> getSchedule() {
        return schedule;
    }

    public void setSchedule(Set<Game> schedule) {
        this.schedule = schedule;
    }
}
