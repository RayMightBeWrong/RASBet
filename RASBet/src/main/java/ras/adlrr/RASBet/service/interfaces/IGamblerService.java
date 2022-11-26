package ras.adlrr.RASBet.service.interfaces;

import java.util.List;

import ras.adlrr.RASBet.model.Gambler;

public interface IGamblerService {
    public Gambler getGamblerById(int id);

    public Gambler getGamblerByEmail(String email);

    public String getGamblerEmail(int id);

    public Gambler addGambler(Gambler gambler) throws Exception;

    public void removeGambler(int id) throws Exception;

    public List<Gambler> getListOfGamblers();

    public boolean gamblerExistsById(int gambler_id);

    public Gambler updateGambler(int gambler_id, String name, String email, String password, Integer phoneNumber,
                                 String nationality, String city, String address, String postal_code, String occupation) throws Exception;
}
