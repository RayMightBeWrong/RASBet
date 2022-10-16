package ras.adlrr.RASBet.dao;

import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.User;

@Repository("fakeDAO")
public class UserDataAccessService implements UserDAO{

    private static HashMap<Integer,User> DB = new HashMap<>();
    private int id = 1;

    @Override
    public User getUser(int id) {
        return DB.get(id);
    }

    @Override
    public int addUser(User user) {
        user.setID(id);
        DB.put(id, user);
        id++;
        return 1;
    }

    @Override
    public int removeUser(int id) {
        DB.remove(id);
        return 1;
    }

    @Override
    public List<User> getListOfUsers() {
        return DB.values().stream().toList();
    }
}
