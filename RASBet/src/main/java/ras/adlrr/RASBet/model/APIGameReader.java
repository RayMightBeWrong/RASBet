package ras.adlrr.RASBet.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public abstract class APIGameReader {
    private ReadJSONBehaviour readMethod;

    public APIGameReader(){}

    public void setReadMethod(ReadJSONBehaviour readMethod){
        this.readMethod = readMethod;
    }

    public abstract List<Game> getAPIGames();

    public String readJSON(String urlString, String path, String keyAPI){
        return this.readMethod.readJSONfromHTTPRequest(urlString, path, keyAPI);
    }

    public String readFromLocalFile(String path) throws Exception{
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader(new File(path)));
            
            String st;
            while ((st = br.readLine()) != null)
                sb.append(st);
            }
        catch (Exception e){
            throw new Exception("Could not read file!");
        }

        return sb.toString();
    }





    public interface ReadJSONBehaviour{
        public String readJSONfromHTTPRequest(String urlString, String path, String keyAPI);
    }

    public class ReadJSONBasic implements ReadJSONBehaviour{
        @Override
        public String readJSONfromHTTPRequest(String urlString, String path, String keyAPI){
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
        
    public class ReadJSONFromExternalAPI implements ReadJSONBehaviour{
        public String readJSONfromHTTPRequest(String url, String path, String keyAPI){
            HttpResponse<String> response = Unirest.get(url)
                                                .header("x-rapidapi-key", keyAPI)
                                                .header("x-rapidapi-host", "v3.football.api-sports.io").asString();
            try {
                Files.write( Paths.get(path), response.getBody().getBytes());
            } catch (Exception e) {
                // ignore
            }
    
            return response.getBody();
        }
    }
}