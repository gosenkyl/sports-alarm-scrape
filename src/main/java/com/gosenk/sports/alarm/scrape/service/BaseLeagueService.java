package com.gosenk.sports.alarm.scrape.service;

import com.gosenk.sports.alarm.scrape.dso.League;
import com.gosenk.sports.alarm.scrape.dso.Team;
import com.gosenk.sports.alarm.scrape.repository.GameRepository;
import com.gosenk.sports.alarm.scrape.repository.LeagueRepository;
import com.gosenk.sports.alarm.scrape.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public abstract class BaseLeagueService {

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private GameRepository gameRepository;

    protected Map<String, Team> teamMap = new HashMap();
    private League league = null;

    public void setLeague(League league){
        leagueRepository.save(league);
        this.league = league;
    }

    protected Team getAndAddTeam(String teamId){
        Team team = teamMap.get(teamId);
        if(team == null){
            team = new Team();
            team.setId(league.getId() + "-" + teamId);
            team.setIdentifier(teamId);
            team.setLeague(league);

            teamRepository.save(team);

            teamMap.put(teamId, team);
        }
        return team;
    }

    protected void saveSchedule(Collection<Team> teams){
        for(Team team : teams){
            gameRepository.save(team.getSchedule());
        }
    }

}
