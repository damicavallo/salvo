package com.accenture.salvo;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String userName;
    private String password;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scores = new HashSet<>();

    public Player () {}


    public Double getTotalScore(){
        return scores.stream().map(s->s.getScore()).mapToDouble(Double::doubleValue).sum();
    }
    public long getTotalWonLostTied(double numero){
        return scores.stream().filter((s->s.getScore()==numero)).count();
    }

    public Score getScoreUnGame(Game unGame) {
        Set<Score> scoresGame = unGame.getScores();
        return scoresGame.stream().findFirst().orElse(null);
    }

    public List<Game> getGames() {
        return gamePlayers.stream().map(gam -> gam.getGame()).collect(toList());
    }

    public Player(String email,String pass){
        userName= email;
        password=pass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String toString() {
        return userName;
    }

    public long getId() {
        return id;
    }

    public Set<Score> getScores() { return scores;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
