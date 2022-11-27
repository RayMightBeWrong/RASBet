package ras.adlrr.RASBet.service.interfaces;

import ras.adlrr.RASBet.model.User;

public interface IUserService {
    public User getUserByEmail(String email);

    public int logIn(String email,String password); 
}
