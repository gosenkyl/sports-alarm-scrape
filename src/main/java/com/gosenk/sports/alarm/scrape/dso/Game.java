package com.gosenk.sports.alarm.scrape.dso;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "game")
public class Game extends BaseEntity{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "opponent_team_id", nullable = false)
    private Team opponentTeam;

    @Column(name="time")
    private Date time;

    @Column(name="home_flag")
    private boolean homeFlag;


    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getOpponentTeam() {
        return opponentTeam;
    }

    public void setOpponentTeam(Team opponentTeam) {
        this.opponentTeam = opponentTeam;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isHomeFlag() {
        return homeFlag;
    }

    public void setHomeFlag(boolean homeFlag) {
        this.homeFlag = homeFlag;
    }
}
