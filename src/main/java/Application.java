import dto.Team;
import service.LeagueService;
import service.NflService;

import java.util.Collection;

public class Application {

    public static void main(String[] arguments){

        // Done - Can generate insert statements
        LeagueService leagueService = new NflService();
        Collection<Team> nflTeasm = leagueService.getTeams();

        // Next -

    }

}
