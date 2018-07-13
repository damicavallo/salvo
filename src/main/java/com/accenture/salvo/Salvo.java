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
        Map<String,Integer> dto = new LinkedHashMap<String,Integer>();
        Map<String,Integer> dto2 = new LinkedHashMap<String,Integer>();
        List<Object> list= new ArrayList<>();

        for (Ship ship: ships){
            int hit = 0;

            for (int i = 0; i < this.getLocations().size(); ++i) {
                for (int j = 0; j < ship.getLocations().size(); ++j) {
                    if (this.getLocations().get(i) == ship.getLocations().get(j)) {
                        hit += 1;
                    }
                }
            }
            int loop=0;
            if (loop==0){
                int hitAnterior=0;
                dto.put(ship.getTipoBarco() + "Hits", hit);
                dto.put(ship.getTipoBarco(),hit);
                list.add(hit);
            loop++;
            }

            else{
                int k=0;
                int hitAnterior= (int) list.get(k);
                dto.put(ship.getTipoBarco() + "Hits", hit);
                dto.put(ship.getTipoBarco(),hitAnterior);
                k++;}
        }

        return dto;
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

