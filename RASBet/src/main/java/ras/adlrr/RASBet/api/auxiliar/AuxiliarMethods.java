package ras.adlrr.RASBet.api.auxiliar;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public class AuxiliarMethods {

    public static ResponseEntity createBadRequest(String error){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Error", error);
        return new ResponseEntity(headers, HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<T> createClassBadRequest(String error, Class<T> type){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Error", error);
        return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<List<T>> createListBadRequest(String error, Class<T> type){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Error", error);
        return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<Set<T>> createSetBadRequest(String error, Class<T> type){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Error", error);
        return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
    }
}
