package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ras.adlrr.RASBet.api.auxiliar.ResponseEntityBadRequest;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;
import ras.adlrr.RASBet.service.interfaces.IBetGameService;
import ras.adlrr.RASBet.service.interfaces.IGameService;
import ras.adlrr.RASBet.service.interfaces.IParticipantService;

import java.util.List;
import java.util.Set;

@RequestMapping("/api/games")
@RestController
public class GameController {
    private final IGameService gameService;
    private final IParticipantService participantService;
    private final IBetGameService betGameService;

    /* **** Game Methods **** */
    @Autowired
    public GameController(IGameService gameService, IParticipantService participantService, IBetGameService betGameService){
        this.gameService = gameService;
        this.participantService = participantService;
        this.betGameService = betGameService;
    }

    @PostMapping
    public ResponseEntity<Game> addGame(@RequestBody Game game){
        try{ 
            return ResponseEntity.ok().body(gameService.addGame(game)); 
        }
        catch (Exception e){
            return new ResponseEntityBadRequest<Game>().createBadRequest(e.getMessage());
        }
    }

    @PostMapping(path = "/update")
    public ResponseEntity updateGames(){
        try{ 
            gameService.updateGames();
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntityBadRequest<Game>().createBadRequest(e.getMessage());
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Game> getGame(@PathVariable("id") int id){
        return ResponseEntity.ok().body(gameService.getGame(id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteGame(@PathVariable("id") int id) {
        try {
            gameService.removeGame(id);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Game>> getGames() {
        return ResponseEntity.ok().body(gameService.getGames());
    }

    @GetMapping(path = "/sorted")
    public ResponseEntity<List<Game>> getGamesSorted() {
        return ResponseEntity.ok().body(gameService.getGamesSorted());
    }

    @PutMapping(path = "/{id}/state/close")
    public ResponseEntity closeGame(@PathVariable("id") int id){
        try {
            betGameService.closeGameAndWithdrawBets(id);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }

    @PutMapping(path = "/{id}/state/{state}")
    public ResponseEntity changeGameState(@PathVariable("id") int id, @PathVariable("state") int state){
        try {
            gameService.changeGameState(id, state);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }

    /* **** Participants Methods **** */
    @GetMapping("/{game_id}/participants")
    public ResponseEntity<Set<Participant>> getGameParticipants(@PathVariable("game_id") int gameID) {
        try{
            return ResponseEntity.ok().body(participantService.getGameParticipants(gameID));
        } catch (Exception e){
            return new ResponseEntityBadRequest<Set<Participant>>().createBadRequest(e.getMessage());
        }
    }

    @PutMapping("/{game_id}/participants")
    public ResponseEntity<Participant> addParticipantToGame(@PathVariable("game_id") int gameID, @RequestBody Participant p){
        try{
            participantService.addParticipantToGame(gameID, p);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntityBadRequest<Participant>().createBadRequest(e.getMessage());
        }
    }

    @DeleteMapping("/participants/{pid}")
    public ResponseEntity deleteParticipant(@PathVariable("pid") int participant_id){
        try{
            participantService.removeParticipant(participant_id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }

    @GetMapping("/participants/{pid}")
    public ResponseEntity<Participant> getParticipant(@PathVariable("pid") int participant_id){
        return ResponseEntity.ok().body(participantService.getParticipant(participant_id));
    }

    @PutMapping("/participants/{pid}/odd/{odd}")
    public ResponseEntity editOddInParticipant(@PathVariable("pid") int participant_id, @PathVariable("odd") float odd){
        try{
            participantService.editOddInParticipant(participant_id, odd);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }

    @PutMapping("/participants/{pid}/score/{score}")
    public ResponseEntity editScoreInParticipant(@PathVariable("pid") int participant_id, @PathVariable("score") int score){
        try{
            participantService.editScoreInParticipant(participant_id, score);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }
}
