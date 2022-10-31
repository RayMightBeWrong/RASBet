package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;
import ras.adlrr.RASBet.service.GameService;

import java.util.List;

@RequestMapping("/api/games/")
@RestController
public class GameController {
    private final GameService gameService;

    /* **** Game Methods **** */
    @Autowired
    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    @PostMapping
    public Game addGame(@RequestBody Game g) {
        return gameService.addGame(g);
    }

    @GetMapping(path = "{id}")
    public Game getGame(@PathVariable("id") int id){
        return gameService.getGame(id);
    }

    @DeleteMapping(path = "{id}")
    public Game deleteGame(@PathVariable("id") int id) {
        return gameService.removeGame(id);
    }

    @GetMapping
    public List<Game> getGames() {
        return gameService.getGames();
    }

    /* **** Participants Methods **** */
    @GetMapping("/{game_id}/participants/")
    public List<Participant> getGameParticipants(@PathVariable("game_id") int gameID) {
        return gameService.getGameParticipants(gameID);
    }

    @PutMapping("/{game_id}/participants/")
    public Participant addParticipantToGame(@PathVariable("game_id") int gameID, @RequestBody Participant p){
        return gameService.addParticipantToGame(gameID, p);
    }

    @DeleteMapping("/{game_id}/participants/{pid}")
    public Participant deleteParticipantFromGame(@PathVariable("game_id") int gameID, @PathVariable("pid") int participant_id){
        return gameService.removeParticipantFromGame(gameID, participant_id);
    }

    @GetMapping("/{game_id}/participants/{pid}")
    public Participant getParticipantFromGame(@PathVariable("game_id") int gameID, @PathVariable("pid") int participant_id){
        return gameService.getParticipantFromGame(participant_id);
    }
}
