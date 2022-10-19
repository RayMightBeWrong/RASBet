package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class User {
    private int ID;
    private String name;
    // TODO: falta password (meter depois com a base de dados)

    @JsonCreator
    public User(@JsonProperty("id") int ID, @JsonProperty("name") String name){
        this.ID = ID;
        this.name = name;
    }

    public User(User user){
        this.ID = user.ID;
        this.name = user.name;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public abstract User clone();
}
