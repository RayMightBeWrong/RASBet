package ras.adlrr.RASBet.dao;

import java.util.List;
import ras.adlrr.RASBet.model.User;

public interface UserDAO {
    User getUser(int id);
    int addUser(User user);
    int removeUser(int id);
    List<User> getListOfUsers();
}
