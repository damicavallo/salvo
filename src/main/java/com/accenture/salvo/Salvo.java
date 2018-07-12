package com.accenture.salvo;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

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

    public Map<String,Integer> hitsRecibidos(Set<Ship> ships) {
        Map<String, Integer> dto = new LinkedHashMap<String, Integer>();
        int loop=0;
        int hitanterior=0;
        for (Ship ship : ships) {
            int hit = 0;

            for (int i = 0; i < this.getLocations().size(); ++i) {
                for (int j = 0; j < ship.getLocations().size(); ++j) {
                    if (this.getLocations().get(i) == ship.getLocations().get(j)) {
                        hit += 1;
                    }
                }
            }   if(loop==0){
                dto.put(ship.getTipoBarco() + "Hits", hit);
                dto.put(ship.getTipoBarco(),hitanterior+hit);
               }
                else{
                hitAnterior=hit;
                dto.put(ship.getTipoBarco() + "Hits", hit);
                dto.put(ship.getTipoBarco(),hitAnterior+hit);}

        }

        return dto;
        loop++;
    }





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

