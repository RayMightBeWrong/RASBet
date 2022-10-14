package ras.adlrr.RASBet.model;

public class Admin extends User{
    public Admin(int ID, String name){
        super(ID, name);
    }

    public User clone(){
        return new Admin(this.getID(), this.getName());
    }
}
