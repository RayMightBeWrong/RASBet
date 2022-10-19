package ras.adlrr.RASBet.dao;

import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Game;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Repository("FakeGameDAO")
public class GameDAS_Fake implements GameDAO{
    private static HashMap<Integer,Game> mapOfGames = new HashMap<>();
    private static int nextId = 0;

    @Override
    public List<Game> getGames() {
        return mapOfGames.values().stream().toList();
    }

    @Override
    public Game getGame(int id) {
        Game g = mapOfGames.get(id);
        if (g != null) return new Game(g);
        else return null;
    }

    @Override
    public int addGame(Game g) {
        g.setId(nextId); nextId++;
        mapOfGames.put(g.getId(), new Game(g));
        return 1;
    }

    @Override
    public int changeGameState(int id, String state){
        Game g = mapOfGames.get(id);

        if(g != null){
            if (state.equals("SUSPENDED")){
                g.setState(Game.SUSPENDED);
                mapOfGames.put(g.getId(), new Game(g));
                return 1;
            }
            if (state.equals("CLOSED")){
                g.setState(Game.CLOSED);
                mapOfGames.put(g.getId(), new Game(g));
                return 1;
            }
            if (state.equals("OPEN")){
                g.setState(Game.OPEN);
                mapOfGames.put(g.getId(), new Game(g));
                return 1;
            }
        }
        return 0;
    }

    @Override
    public int changeGameDate(int id, LocalDateTime date) {
        Game g = mapOfGames.get(id);
        if(g == null){
            return 0;
        }
        else {
            g.setDate(date);
            return 1;
        }
    }
}
