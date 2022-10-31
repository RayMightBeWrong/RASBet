package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

//@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
//@Table(name = "users")
@MappedSuperclass
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int ID;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false,unique = true)
    private String email;

    @JsonCreator
    public User(@JsonProperty("id") int ID, @JsonProperty("name") String name, @JsonProperty("password") String password,@JsonProperty("email") String email){
        this.ID = ID;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User() {}
    public User(User user){
        this.ID = user.ID;
        this.name = user.name;
        this.password = user.password;
        this.email = user.email;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() { return password; }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) { this.password = password; }

}
