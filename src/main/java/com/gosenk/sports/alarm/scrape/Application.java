package com.gosenk.sports.alarm.scrape;

import com.gosenk.sports.alarm.scrape.dso.Team;
import com.gosenk.sports.alarm.scrape.service.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collection;

@SpringBootApplication
@ComponentScan("com.gosenk.sports.alarm.scrape")
public class Application implements ApplicationRunner {

    //private static final Logger logger = LoggerFactory.getLogger(com.gosenk.sports.alarm.scrape.Application.class);

    @Autowired
    @Qualifier("nfl")
    private LeagueService nflService;

    @Autowired
    @Qualifier("mlb")
    private LeagueService mlbService;

    public static void main(String[] args) {
        int returnCode = 0;

        try {
            SpringApplication.run(Application.class, args);
        } catch(Exception e) {
            e.printStackTrace();
            returnCode = -1;
        }

        System.exit(returnCode);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Collection<Team> nflTeams = nflService.getTeams();
        Collection<Team> mlbTeams = mlbService.getTeams();
    }
}
