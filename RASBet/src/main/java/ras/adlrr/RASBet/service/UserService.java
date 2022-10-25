package ras.adlrr.RASBet.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.UserRepository;
import ras.adlrr.RASBet.model.User;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUser(int id){
        return userRepository.findById(id).orElse(null);
    }

    public int addUser(User user){
        userRepository.save(user);
        return 1;
    }

    public int removeUser(int id){
        userRepository.deleteById(id);
        return 1;
    }

    public List<User> getListOfUsers(){
        return userRepository.findAll();
    }
}
