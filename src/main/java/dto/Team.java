package dto;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private String id;
    private String leagueId;

    private List<Game> schedule = new ArrayList();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public List<Game> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Game> schedule) {
        this.schedule = schedule;
    }
}
