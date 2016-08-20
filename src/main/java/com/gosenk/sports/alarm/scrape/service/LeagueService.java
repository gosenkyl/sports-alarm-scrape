package com.gosenk.sports.alarm.scrape.service;

import com.gosenk.sports.alarm.scrape.dso.Team;

import java.util.Collection;

public interface LeagueService {

    Collection<Team> getTeams() throws Exception;

}
