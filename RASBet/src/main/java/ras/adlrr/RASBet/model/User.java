public abstract class User {
    private int ID;
    private String name;
    // DEBUG: falta password (meter depois com a base de dados)

    public User(int ID, String name){
        this.ID = ID;
        this.name = name;
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
