package ras.adlrr.RASBet.model.readers;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import ras.adlrr.RASBet.model.APIGameReader;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;

public class UcrasAPIReader implements APIGameReader{
    private int sport_id;

    public UcrasAPIReader(int sport_id){
        this.sport_id = sport_id;
    }

    public List<Game> getAPIGames(){
        String json = readJSONfromHTTPRequest("http://ucras.di.uminho.pt/v1/games/");

        JSONArray ja = new JSONArray(json);
        for(int i = 0; i < ja.length(); i++){
            JSONObject jo = (JSONObject) ja.get(i);

            String iso8601 = (String) jo.get("commenceTime");
            ZonedDateTime zdt = ZonedDateTime.parse(iso8601);
            LocalDateTime ldt = zdt.toLocalDateTime();

            String homeTeam = (String) jo.get("homeTeam");
            String awayTeam = (String) jo.get("awayTeam");


            JSONArray bookmakers = (JSONArray) jo.get("bookmakers");
            JSONObject fst = (JSONObject) bookmakers.get(0);
            JSONArray markets = (JSONArray) fst.get("markets");
            JSONObject markets2 = (JSONObject) markets.get(0);
            JSONArray outcomes = (JSONArray) markets2.get("outcomes");

            float drawOdd = 0, homeOdd = 0, awayOdd = 0;
            for (int j = 0; j < outcomes.length(); j++){
                JSONObject obj = (JSONObject) outcomes.get(j);
                Number odd = (Number) obj.get("price");
                if (homeTeam.equals(obj.get("name"))){
                    homeOdd = odd.floatValue();
                }
                else if (awayTeam.equals(obj.get("name"))){
                    awayOdd = odd.floatValue();
                }
                else if (obj.get("name").equals("Draw")){
                    drawOdd = odd.floatValue();
                }
            }
            Participant home = new Participant(homeTeam, homeOdd, 0);
            Participant away = new Participant(awayTeam, awayOdd, 0);
            Participant draw = new Participant("draw", drawOdd, 0);
            List<Participant> ps = new ArrayList<>();
            ps.add(home); ps.add(away); ps.add(draw);
        }

        return null;

            // TODO: mudar para diferentes desportos e mudar o resultado
            // TODO: get ID do desporto
            //Game g = new Game(5, (String) jo.get("id"), ldt, ps, 1, Game.CLOSED);
            //TODO
            //gameRepository.addGame(g);
    }

    public String readJSONfromHTTPRequest(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int rCode = connection.getResponseCode();
            if (rCode != 200){
                throw new RuntimeException("HTTP Response Code: " + rCode);
            }

            StringBuilder sb = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()){
                sb.append(scanner.nextLine());
            }

            scanner.close();
            return sb.toString();
        }
        catch (Exception e){
           return "";
        }
    }
}
