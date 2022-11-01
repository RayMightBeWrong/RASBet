package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Admin extends User{
    @JsonCreator
    public Admin(@JsonProperty("id") int ID, @JsonProperty("name") String name, @JsonProperty("password") String password, @JsonProperty("email") String email){
        super(ID, name, password,email);
    }
}
