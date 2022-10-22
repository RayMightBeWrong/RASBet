package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Expert extends User{
    @JsonCreator
    public Expert(@JsonProperty("id") int ID, @JsonProperty("name") String name, @JsonProperty("password") String password){
        super(ID, name, password);
    }

    public User clone(){
        return new Expert(this.getID(), this.getName(),this.getPassword());
    }
}