package ras.adlrr.RASBet.api.auxiliar;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseEntityBadRequest<T> {
    public ResponseEntityBadRequest() {
    }

    public ResponseEntity<T> createBadRequest(String error){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Error", error);
        return new ResponseEntity<T>(headers, HttpStatus.BAD_REQUEST);
    }
}
