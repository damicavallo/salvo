package com.accenture.salvo;

import javax.persistence.*;
import java.util.Date;


@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    private double score;

    private Date finishDate;

    public Score(){}

    public Score(Game game, Player player,double puntaje) {
        this.game = game;
        this.player = player;
        this.score = puntaje;
        this.finishDate = Date.from(game.getFechaCreacion().toInstant().plusSeconds(1800));
    }

    public long getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    public double getScore() {
        return score;
    }

    public Date getFinishDate() {
        return finishDate;
    }


}
