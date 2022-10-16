package ras.adlrr.RASBet.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.UserDAO;
import ras.adlrr.RASBet.model.User;

@Service
public class UserService {
    private final UserDAO userDAO;

    @Autowired
    public UserService(@Qualifier("fakeDAO") UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public User getUser(int id){
        return userDAO.getUser(id);
    }

    public int addUser(User user){
        return userDAO.addUser(user);
    }

    public int removeUser(int id){
        return userDAO.removeUser(id);
    }

    public List<User> getListOfUsers() {
        return userDAO.getListOfUsers();
    }
}
