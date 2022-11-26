package ras.adlrr.RASBet.service.interfaces;

import java.util.List;

import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.model.User;

public interface IUserService {
    public User getUserByEmail(String email);

    public int logIn(String email,String password); 
}
