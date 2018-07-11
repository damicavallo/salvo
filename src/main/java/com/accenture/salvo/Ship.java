package com.accenture.salvo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Ship {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="locations")
    private List<String> locations = new ArrayList<>();

    public Ship (){}

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public Ship (String unTipoBarco,GamePlayer unGamePlayer,List<String> ubicaciones)
    {
        this.type = unTipoBarco;
        this.gamePlayer= unGamePlayer;
        this.locations = ubicaciones;
    }
    public String getTipoBarco() {
        return type;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setType(String type) {
        this.type = type;
    }
}
