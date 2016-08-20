package com.gosenk.sports.alarm.scrape.repository;

import com.gosenk.sports.alarm.scrape.dso.League;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueRepository extends CrudRepository<League, String>{

}
