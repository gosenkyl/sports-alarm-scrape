import dto.Team;
import service.LeagueService;
import service.MlbService;
import service.NflService;

import java.util.Collection;

public class Application {

    public static void main(String[] arguments) throws Exception{

        // Done - Can generate insert statements
        LeagueService nflService = new NflService();
        //Collection<Team> nflTeams = nflService.getTeams();

        LeagueService mlbService = new MlbService();
        Collection<Team> mlbTeams = mlbService.getTeams();

    }

}
