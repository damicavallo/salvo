package com.accenture.salvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ShipRepository ship;

    @Autowired
    private SalvoRepository salvoRepository;



    @RequestMapping("/games")
    public Map<String,Object> getGames(Authentication authentication){
            Map<String, Object> dto = new LinkedHashMap<String, Object>();
            if (isGuest(authentication)){
            dto.put("player", "Guest");}
            else{ dto.put("player",
                    makePlayerDTO(playerRepository.findByUserName(authentication.getName())));}
            dto.put("games",gameRepository.findAll().stream().map(g->makeGameDTO(g)).collect(Collectors.toList()));
            return dto;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> createNewGame(Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "Unauthorized"), HttpStatus.UNAUTHORIZED);
        }else if(playerRepository.findByUserName((authentication.getName()))!= null)
            {
                Game game = new Game(new Date());
                GamePlayer gamePlayer = new GamePlayer(game,playerRepository.findByUserName(authentication.getName()));
                gameRepository.save(game);
                gamePlayerRepository.save(gamePlayer);
                return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
            }
         else return new ResponseEntity<>(makeMap("error","Failed to load player"),HttpStatus.NOT_FOUND);

    }
    @RequestMapping(path = "/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> joinGame(@PathVariable Long gameId, Authentication authentication) {
        Game game=gameRepository.getOne(gameId);
        Long idGame = game.getId();
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "Unauthorized"), HttpStatus.UNAUTHORIZED);
        }
        else if(idGame==null){
            return new ResponseEntity<>(makeMap("error", "Game failed"), HttpStatus.FORBIDDEN);
        }
        else if(game.getPlayers().size()==2){
            return new ResponseEntity<>(makeMap("error", "Game is full"), HttpStatus.CONFLICT);
        }
        else{
            Player player= playerRepository.findByUserName(authentication.getName());
            GamePlayer gamePlayer= new GamePlayer(game,player);
            gamePlayerRepository.save(gamePlayer);
            return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }


    }
    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String username,@RequestParam String password) {
        if (username.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
        }
        Player player = playerRepository.findByUserName(username);
        if (player != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.FORBIDDEN);
        }
        Player newUser = playerRepository.save(new Player(username,password));
        return new ResponseEntity<>(makeMap("id", newUser.getUserName()), HttpStatus.CREATED);
    }


    @RequestMapping("/leaderBoard")
    public List<Object> getScoreBoard(){
        return playerRepository.findAll().stream().map(p->makeScorePlayerDTO(p)).collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{gameId}")
    public Object gamePlayerView(@PathVariable Long gameId,Authentication authentication){
        GamePlayer gamePlayer = gamePlayerRepository.findOne(gameId);
        if(!(authentication.getName().equals(gamePlayer.getPlayer().getUserName()))){
            return new ResponseEntity<>(makeMap("error", "Unauthorized"), HttpStatus.UNAUTHORIZED);
        }else{
        return makeGameViewDTO(gamePlayer);}
    }


    @RequestMapping(path = "/games/players/{gamePlayerId}/ships",method = RequestMethod.POST)
    public ResponseEntity<Object> setShip(@PathVariable long gamePlayerId,@RequestBody List <Ship> listShips,Authentication authentication){
        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);

        if ((isGuest(authentication))|| (gamePlayer==null)|| ((gamePlayer!=null) && (gamePlayer.getPlayer().getUserName()!= authentication.getName()))) {
            return new ResponseEntity<>(makeMap("error", "Failed to add ships"), HttpStatus.UNAUTHORIZED);
        }
        else if(gamePlayer.getShips().size()+listShips.size()>5){
            return new ResponseEntity<>(makeMap("error", "You have "+gamePlayer.getShips().size()+" ships placed. You must place at most 5"), HttpStatus.FORBIDDEN);
        }
        else
        gamePlayer.setListShips(listShips);
        ship.save(listShips);
        gamePlayerRepository.save(gamePlayer);
        return new ResponseEntity<>(makeMap("success","Ships were placed"), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games/players/{gamePlayerID}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> setSalvoes(@PathVariable Long gamePlayerID, @RequestBody Salvo salvo, Authentication authentication) {

        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerID);
        salvo.setGamePlayer(gamePlayer);

        if ((isGuest(authentication))|| (gamePlayer==null)|| ((gamePlayer!=null) && (gamePlayer.getPlayer().getUserName()!= authentication.getName()))) {
            return new ResponseEntity<>(makeMap("error", "Failed to add salvoes"), HttpStatus.UNAUTHORIZED);
        }
        else if(thereAreSalvoesInThatTurn(gamePlayer,salvo)){
            return new ResponseEntity<>(makeMap("error", "You already placed the salvoes in that turn"), HttpStatus.FORBIDDEN);
        }
        else
            salvoRepository.save(salvo);
            gamePlayer.getSalvoes().add(salvo);
            gamePlayerRepository.save(gamePlayer);
            return new ResponseEntity<>(makeMap("success","Salvo were placed"), HttpStatus.CREATED);

    }
    private Boolean thereAreSalvoesInThatTurn(GamePlayer gamePlayer, Salvo salvo){
        return gamePlayer.getSalvoes().stream().filter(s->s.getTurn()==salvo.getTurn()).count()!=0;

    }


    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private Map<String,Object> makePlayerGuest(Player player){
        Map<String,Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",player.getId());
        dto.put("email",player.getUserName());
        return dto;
    }
    private Map<String, Object> makeScorePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("name", player.getUserName());
        dto.put("score", makeScoreOfPlayer(player));
        return dto;
    }
    private Map<String, Object> makeScoreOfPlayer(Player player) {
            Map<String, Object> dto = new LinkedHashMap<String, Object>();
            dto.put("total",player.getTotalScore());
            dto.put("won",player.getTotalWonLostTied(1.0));
            dto.put("lost",player.getTotalWonLostTied(0.0));
            dto.put("tied",player.getTotalWonLostTied(0.5));
            return dto;
        }

    private Map<String, Object> makeGameViewDTO(GamePlayer unGamePlayer) {
        GamePlayer opponent= unGamePlayer.getGame().getGameplayers().stream().filter(gp->gp.getPlayer().getId()!=unGamePlayer.getPlayer().getId()).findFirst().get();
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", unGamePlayer.getGame().getId());
        dto.put("created", unGamePlayer.getGame().getFechaCreacion());
        dto.put("gamePlayers",makeGamePlayerList(unGamePlayer.getGame().getGameplayers()));
        dto.put("ships",makeShipList(unGamePlayer.getShips()));
        dto.put("salvoes",makeSalvoesList(unGamePlayer));
        dto.put("self",makeHitsList(opponent.getSalvoes(),unGamePlayer));
        return dto;
    }


    private List<Object> makeHitsList(Set<Salvo> salvoes,GamePlayer gamePlayer){
        return salvoes.stream().map(s->viewdamagedto(s,gamePlayer)).collect(Collectors.toList());

    }
    private Map<String, Object> viewdamagedto(Salvo salvo, GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
            dto.put("turn", salvo.getTurn());
            dto.put("locations", salvo.getLocations());
            dto.put("damages", salvo.hitsRecibidos(gamePlayer.getShips()));
            dto.put("missed",0);
            return dto;
    }




    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getFechaCreacion());
        dto.put("gamePlayers",makeGamePlayerList(game.getGameplayers()));
        dto.put("scores",makeScoreList(game.getScores()));
        return dto;
    }

    private List<Object> makeScoreList(Set<Score> gpData){
        return gpData.stream().map(m->makeScoreDTO(m)).collect(Collectors.toList());

    }

    private List<Object> makeSalvoesList(GamePlayer unGamePlayer){
        Set<Salvo> listasalvoes= unGamePlayer.getGame().getGameplayers().stream().map(s->s.getSalvoes())
                .flatMap(listaInterna->listaInterna.stream()).collect(toSet());
        return listasalvoes.stream().map(m->makeSalvoDTO(m)).collect(Collectors.toList());
    }

    private List<Object> makeGamePlayerList(Set<GamePlayer> gamePlayerList){
        return gamePlayerList.stream().map(m->makeGamePlayerDTO(m)).collect(Collectors.toList());
    }
    private List<Object> makeShipList(Set<Ship> shipList) {
        return shipList.stream().map(m -> makeShipDTO(m)).collect(Collectors.toList());
    }

    private Map<String,Object> makeScoreDTO(Score puntaje){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("playerID",puntaje.getPlayer().getId());
        dto.put("score",puntaje.getScore());
        dto.put("finishDate",puntaje.getFinishDate());
        return dto;
    }


    private Map<String, Object> makeGamePlayerDTO(GamePlayer gameplayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",gameplayer.getId());
        dto.put("player",makePlayerDTO(gameplayer.getPlayer()));
        dto.put("joindate",gameplayer.getFechaCreacion());
        return dto;
    }
    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getId());
        dto.put("email", player.getUserName());
        return dto;
    }
    private Map<String, Object> makeShipDTO(Ship ship){
        Map<String,Object> dto = new LinkedHashMap<String,Object>();
        dto.put("type",ship.getTipoBarco());
        dto.put("locations",ship.getLocations());
        return dto;
    }

    private Map<String, Object> makeSalvoDTO(Salvo salvo){
        Map<String,Object> dto = new LinkedHashMap<String,Object>();
        dto.put("turn",salvo.getTurn());
        dto.put("player",salvo.getGamePlayerId());
        dto.put("locations",salvo.getLocations());
        return dto;
    }

}
