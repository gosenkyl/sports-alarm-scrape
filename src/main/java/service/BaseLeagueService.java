package service;

import dto.League;
import dto.Team;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseLeagueService {

    protected Map<String, Team> teamMap = new HashMap();
    protected League league;

    protected Team getAndAddTeam(String teamId){
        Team team = teamMap.get(teamId);
        if(team == null){
            team = new Team();
            team.setId(teamId);
            team.setLeagueId(league.getId());
            teamMap.put(teamId, team);
        }
        return team;
    }

}
