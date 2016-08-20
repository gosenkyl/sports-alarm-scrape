package com.gosenk.sports.alarm.scrape.service;

import com.gosenk.sports.alarm.scrape.dso.Game;
import com.gosenk.sports.alarm.scrape.dso.League;
import com.gosenk.sports.alarm.scrape.dso.Team;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("mlb")
public class MlbService extends BaseLeagueService implements LeagueService{

    private static final String baseURL = "http://gd2.mlb.com/components/game/mlb/year_:PARSE_YEAR/month_:PARSE_MONTH/day_:PARSE_DAY/master_scoreboard.json";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd h:mm a");

    @PostConstruct
    public void createLeague(){
        League league = new League();
        league.setId("MLB");
        setLeague(league);
    }

    @Override
    public Collection<Team> getTeams() throws Exception {
        return getMLBTeams();
    }

    private Collection<Team> getMLBTeams() throws Exception {

        SimpleDateFormat startEndSdf = new SimpleDateFormat("MM/dd/yyyy");

        SimpleDateFormat m = new SimpleDateFormat("MM");
        SimpleDateFormat d = new SimpleDateFormat("dd");
        SimpleDateFormat y = new SimpleDateFormat("yyyy");

        String startDate = "04/03/2016";
        String endDate = "10/02/2016";

        Calendar start = Calendar.getInstance();
        start.setTime(startEndSdf.parse(startDate));
        Calendar end = Calendar.getInstance();
        end.setTime(startEndSdf.parse(endDate));

        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            System.out.println(date);

            String month = m.format(date);
            String day = d.format(date);
            String year = y.format(date);

            String fullDateStr = year + "/" + month + "/" + day;

            String url = baseURL.replace(":PARSE_YEAR", year).replace(":PARSE_MONTH", month).replace(":PARSE_DAY", day);

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);

            HttpResponse response = client.execute(request);

            System.out.println("STATUS CODE: " + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            JSONObject initial = new JSONObject(result.toString());
            JSONObject data = initial.getJSONObject("data");
            JSONObject games = data.getJSONObject("games");

            if(games.has("game")) {
                if(games.get("game") instanceof JSONArray){
                    JSONArray gameList = games.getJSONArray("game");
                    Iterator it = gameList.iterator();
                    while (it.hasNext()) {
                        JSONObject gameObj = (JSONObject) it.next();
                        createGames(gameObj, fullDateStr);
                    }
                } else {
                    JSONObject gameObj = games.getJSONObject("game");
                    createGames(gameObj, fullDateStr);
                }
            }
        }

        saveSchedule(teamMap.values());

        return teamMap.values();
    }

    private void createGames(JSONObject gameObj, String fullDateStr) throws ParseException {
        if(gameObj.get("game_type").equals("R")) {
            String homeTeamId = gameObj.get("home_name_abbrev").toString();
            String awayTeamId = gameObj.get("away_name_abbrev").toString();

            Team homeTeam = getAndAddTeam(homeTeamId);
            Team awayTeam = getAndAddTeam(awayTeamId);

            String dateTime = fullDateStr + " " + gameObj.get("time").toString() + " " + gameObj.get("ampm").toString();
            Date gameDate = sdf.parse(dateTime);

            // Home Team
            Game homeGame = new Game();

            homeGame.setId(gameObj.getString("id")+"-home");
            homeGame.setTeam(homeTeam);
            homeGame.setTime(gameDate);
            homeGame.setHomeFlag(true);
            homeGame.setOpponentTeam(awayTeam);

            homeTeam.getSchedule().add(homeGame);

            // Away Team
            Game awayGame = new Game();

            awayGame.setId(gameObj.getString("id")+"-away");
            awayGame.setTeam(awayTeam);
            awayGame.setTime(gameDate);
            awayGame.setHomeFlag(false);
            awayGame.setOpponentTeam(homeTeam);

            awayTeam.getSchedule().add(awayGame);

            System.out.println(awayGame.getOpponentTeam().getId() + " vs " + homeGame.getOpponentTeam().getId());
        }
    }


}
