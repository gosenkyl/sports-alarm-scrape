package service;

import dto.Team;

import java.util.Collection;
import java.util.List;

public interface LeagueService {

    Collection<Team> getTeams() throws Exception;

}
