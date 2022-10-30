package ras.adlrr.RASBet.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.AdminRepository;
import ras.adlrr.RASBet.dao.ExpertRepository;
import ras.adlrr.RASBet.dao.GamblerRepository;
import ras.adlrr.RASBet.dao.UserRepository;
import ras.adlrr.RASBet.model.Admin;
import ras.adlrr.RASBet.model.Expert;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.model.User;

@Service
public class UserService {
    private final GamblerRepository gamblerRepository;
    private final AdminRepository adminRepository;
    private final ExpertRepository expertRepository;

    @Autowired
    public UserService(GamblerRepository gamblerRepository, AdminRepository adminRepository, ExpertRepository expertRepository){
        this.gamblerRepository = gamblerRepository;
        this.adminRepository = adminRepository;
        this.expertRepository = expertRepository;
    }

    public Gambler getGambler(int id){
        return gamblerRepository.findById(id).orElse(null);
    }

    public Admin getAdmin(int id){
        return adminRepository.findById(id).orElse(null);
    }

    public Expert getExpert(int id){
        return expertRepository.findById(id).orElse(null);
    }

    public int addGambler(Gambler user){
        gamblerRepository.save(user);
        return 1;
    }

    public int addAdmin(Admin user){
        adminRepository.save(user);
        return 1;
    }

    public int addExpert(Expert user){
        expertRepository.save(user);
        return 1;
    }
    public int removeGambler(int id){
        gamblerRepository.deleteById(id);
        return 1;
    }
    public int removeAdmin(int id){
        adminRepository.deleteById(id);
        return 1;
    }

    public int removeExpert(int id){
        expertRepository.deleteById(id);
        return 1;
    }

    public List<Gambler> getListOfGamblers(){
        return gamblerRepository.findAll();
    }

    public List<Admin> getListOfAdmins(){
        return adminRepository.findAll();
    }

    public List<Expert> getListOfExperts(){
        return expertRepository.findAll();
    }
}
