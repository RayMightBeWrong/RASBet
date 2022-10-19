package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;
import ras.adlrr.RASBet.service.GameService;

import java.time.LocalDateTime;
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

    @GetMapping
    public List<Game> getGames() {
        return gameService.getGames();
    }

    @GetMapping(path = "{id}")
    public Game getGame(@PathVariable("id") int id){
        return gameService.getGame(id);
    }

    @PostMapping
    public int addGame(@RequestBody Game g) {
        return gameService.addGame(g);
    }

    @PutMapping(path = "/{id}/state/{state}")
    public int changeGameState(@PathVariable("id") int id, @PathVariable("state") String state) {
        return gameService.changeGameState(id, state);
    }

    @PutMapping(path = "/changeDate/{id}")
    public int changeGameDate(@PathVariable("id") int id, @RequestBody LocalDateTime date) {
        return gameService.changeGameDate(id,date);
    }


    /* **** Participants Methods **** */

    @GetMapping("/{id}/participants/")
    public List<Participant> getGameParticipants(@PathVariable int gameID) {
        return gameService.getGameParticipants(gameID);
    }

    @PutMapping("/{id}/addParticipant/")
    public int addParticipantToGame(@PathVariable("id") int gameID, @RequestBody Participant p) {
        return gameService.addParticipantToGame(gameID, p);
    }
}
