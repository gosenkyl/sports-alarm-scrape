package service;

import dto.Team;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MlbService implements LeagueService{

    private static final String baseURL = "http://gd2.mlb.com/components/game/mlb/year_:PARSE_YEAR/month_:PARSE_MONTH/day_:PARSE_DAY/master_scoreboard.json";

    private Map<String, Team> mlbTeamMap = new HashMap();

    @Override
    public Collection<Team> getTeams() throws Exception {
        return getMLBTeams();
    }

    private Collection<Team> getMLBTeams() throws Exception {

        String year = "2016";

        // Given year, loop through months/days between X and Y
        String month = "08";
        String day = "30";

        String url = baseURL.replace(":PARSE_YEAR", year).replace(":PARSE_MONTH", month).replace(":PARSE_DAY", day);

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);

        System.out.println("STATUS CODE: " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());


        return mlbTeamMap.values();
    }


}
