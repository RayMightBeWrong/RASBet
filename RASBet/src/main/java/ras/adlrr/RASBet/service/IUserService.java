package ras.adlrr.RASBet.service;

import java.util.List;

import ras.adlrr.RASBet.model.Admin;
import ras.adlrr.RASBet.model.Expert;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.model.User;

public interface IUserService {
    public Gambler getGamblerById(int id);

    public Gambler getGamblerByEmail(String email);

    public String getGamblerEmail(int id);

    public Gambler addGambler(Gambler gambler) throws Exception;

    public void removeGambler(int id) throws Exception;

    public List<Gambler> getListOfGamblers();

    public boolean gamblerExistsById(int gambler_id);

    public Gambler updateGambler(int gambler_id, String name, String email, String password, Integer phoneNumber,
                                 String nationality, String city, String address, String postal_code, String occupation) throws Exception;

    public Admin getAdminById(int id);

    public Admin getAdminByEmail(String email);

    public Admin addAdmin(Admin admin) throws Exception;

    public void removeAdmin(int id) throws Exception;

    public List<Admin> getListOfAdmins();

    public boolean adminExistsById(int admin_id);

    public Expert getExpertById(int id);

    public Expert getExpertByEmail(String email);

    public Expert addExpert(Expert expert) throws Exception;

    public void removeExpert(int id) throws Exception;

    public List<Expert> getListOfExperts();

    public boolean expertExistsById(int expert_id);

    public User getUserByEmail(String email);

    public int logIn(String email,String password);

    
}
