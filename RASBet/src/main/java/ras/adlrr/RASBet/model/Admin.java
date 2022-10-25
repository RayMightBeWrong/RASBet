package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;

@Entity
public class Admin extends User{
    @JsonCreator
    public Admin(@JsonProperty("id") int ID, @JsonProperty("name") String name, @JsonProperty("password") String password){
        super(ID, name, password);
    }

    public User clone(){
        return new Admin(this.getID(), this.getName(), this.getPassword());
    }
}
