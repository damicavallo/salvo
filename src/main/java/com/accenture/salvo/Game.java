package com.accenture.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.*;
import static java.util.stream.Collectors.toList;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private Date fechaCreacion;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<Score> scores = new HashSet<>();



    public Game(){
        fechaCreacion= new Date();
    }

    public Game(Date unaFecha) {
        fechaCreacion=unaFecha;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public long getId() {
        return id;
    }

    public Set<GamePlayer> getGameplayers() {
        return gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

@JsonIgnore
    public List<Player> getPlayers() {
        return gamePlayers.stream().map(gam -> gam.getPlayer()).collect(toList());
    }

}

