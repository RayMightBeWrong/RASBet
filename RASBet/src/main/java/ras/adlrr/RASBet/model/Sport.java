package ras.adlrr.RASBet.model;

public class Sport {
    private int id; //Unique identifier of the sport
    private String name;
    private int type; //Type of sport (E.g.: Collective and without draw / Non collective and with draw ...)

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public Sport(int id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Sport(Sport sport){
        this.id = sport.id;
        this.name = sport.name;
        this.type = sport.type;
    }
}
