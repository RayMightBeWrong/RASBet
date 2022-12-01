package ras.adlrr.RASBet.service.interfaces.users;

import ras.adlrr.RASBet.model.User;

import java.util.Map;

public interface IUserService {
    public User getUserByEmail(String email);

    public Map<String, Integer> logIn(String email, String password);

    boolean existsUserWithEmail(String email);
}
