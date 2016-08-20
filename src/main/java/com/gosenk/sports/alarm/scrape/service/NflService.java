package com.gosenk.sports.alarm.scrape.service;

import com.gosenk.sports.alarm.scrape.dso.Game;
import com.gosenk.sports.alarm.scrape.dso.League;
import com.gosenk.sports.alarm.scrape.dso.Team;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("nfl")
public class NflService extends BaseLeagueService implements LeagueService{

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");

    private static final String currentWeekURL = "http://www.nfl.com/liveupdate/scorestrip/ss.xml";
    // "http://www.nfl.com/liveupdate/scorestrip/postseason/ss.xml" PLAYOFFS
    // The schedule changes on Wednesday 7:00 UTC during the regular season

    private static final String baseURL = "http://www.nfl.com/ajax/scorestrip?season=:PARSE_SEASON&seasonType=:PARSE_TYPE&week=:PARSE_WEEK";

    @PostConstruct
    public void createLeague(){
        League league = new League();
        league.setId("NFL");
        setLeague(league);
    }

    @Override
    public Collection<Team> getTeams() {
        return getNFLTeams();
    }

    private Collection<Team> getNFLTeams(){

        URL url;
        URLConnection connection;

        try {
            // "P" = "Pregame"- "H" = "Halftime" - "5" = "Overtime" - "F" = "Final" - "FO" = "Final Overtime";

            String baseUrl = baseURL.replace(":PARSE_TYPE", "REG").replace(":PARSE_SEASON", "2016");

            int week = 1;
            while(true){

                url = new URL(baseUrl.replace(":PARSE_WEEK", String.valueOf(week)));
                connection = url.openConnection();

                Document doc = parseXML(connection.getInputStream());
                Node schedule = doc.getElementsByTagName("ss").item(0);
                Node games = schedule.getFirstChild();

                if(games == null){
                    // No more weeks left
                    break;
                }

                System.out.println("WEEK #"+week);

                NodeList descNodes = games.getChildNodes();

                // Loop through the games
                for (int i = 0; i < descNodes.getLength(); i++) {
                    Node curNode = descNodes.item(i);
                    NamedNodeMap nodeMap = curNode.getAttributes();

                    // Get home team for this game
                    String homeTeamId = nodeMap.getNamedItem("h").getNodeValue();

                    Team homeTeam = getAndAddTeam(homeTeamId);

                    // Get away team for this game
                    String awayTeamId = nodeMap.getNamedItem("v").getNodeValue();
                    Team awayTeam = getAndAddTeam(awayTeamId);

                    String eid = nodeMap.getNamedItem("eid").getNodeValue();
                    String year = eid.substring(0, 3);
                    String month = eid.substring(4, 6);
                    String day = eid.substring(6, 8);

                    String dayLong = nodeMap.getNamedItem("d").getNodeValue();
                    String time = nodeMap.getNamedItem("t").getNodeValue();

                    String[] timeParts = time.split(":");

                    int hour = Integer.parseInt(timeParts[0]);

                    if(hour < 9){
                        hour += 12;
                    }

                    Date gameDate = sdf.parse(eid.substring(0, 8) + " " + hour + ":" + timeParts[1]);

                    // Home Team
                    Game homeGame = new Game();

                    homeGame.setId(eid+"-home");
                    homeGame.setTeam(homeTeam);
                    homeGame.setTime(gameDate);
                    homeGame.setHomeFlag(true);
                    homeGame.setOpponentTeam(awayTeam);

                    homeTeam.getSchedule().add(homeGame);

                    // Away Team
                    Game awayGame = new Game();

                    awayGame.setId(eid+"-away");
                    awayGame.setTeam(awayTeam);
                    awayGame.setTime(gameDate);
                    awayGame.setHomeFlag(false);
                    awayGame.setOpponentTeam(homeTeam);

                    awayTeam.getSchedule().add(awayGame);

                    System.out.println(awayGame.getOpponentTeam().getId() + " vs " + homeGame.getOpponentTeam().getId());
                }

                // If list size isn't the week size, team has a bye week
                /*for(TeamObj team : teams.values()){
                    if(team.getSchedule().size() < week){
                        GameObj game = new GameObj();
                        game.setOpponentMascot("Bye");
                        team.getSchedule().add(game);
                    }
                }*/

                week++;

            }
        } catch(Exception e){
            e.printStackTrace();
        }

        saveSchedule(teamMap.values());

        return teamMap.values();
    }

    private static Document parseXML(InputStream stream) throws Exception {

        DocumentBuilderFactory objDocumentBuilderFactory;
        DocumentBuilder objDocumentBuilder;
        Document doc;
        try {
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();

            doc = objDocumentBuilder.parse(stream);
        }
        catch(Exception ex) {
            throw ex;
        }

        return doc;
    }

}
