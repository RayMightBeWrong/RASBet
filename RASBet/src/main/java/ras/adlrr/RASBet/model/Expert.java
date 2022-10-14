public class Expert extends User{
    public Expert(int ID, String name){
        super(ID, name);
    }

    public User clone(){
        return new Expert(this.getID(), this.getName());
    }
}