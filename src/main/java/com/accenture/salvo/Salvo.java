package com.accenture.salvo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int turn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="location")
    private List<String> locations = new ArrayList<>();

    public Salvo(){}

    public Salvo (int unTurno,GamePlayer unGamePlayer,List<String> ubicaciones)
    {
        this.turn = unTurno;
        this.gamePlayer= unGamePlayer;
        this.locations = ubicaciones;
    }


    public int getTurn() {
        return turn;
    }
    public List<String> getLocations() {
        return locations;
    }

    public long getGamePlayerId() {
        return this.gamePlayer.getId();
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setTurn(int turno) {
        this.turn = turno;
    }
}

