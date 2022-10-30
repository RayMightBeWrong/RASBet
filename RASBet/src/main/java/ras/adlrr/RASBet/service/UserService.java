package ras.adlrr.RASBet.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    /**
     * Save a gambler to table
     *
     * @param  user    the gambler that you want to add
     * @return         0 case added, 1 case emailExists
     */
    public int addGambler(Gambler user){
        int returnValue = 0;
        try {
        gamblerRepository.save(user);
        }
        catch (DataIntegrityViolationException e){ //Isto pode ser mudado
            returnValue = 1;
        }
        return returnValue;
    }

    /**
     * Save a admin to table
     *
     * @param  user    the admin that you want to add
     * @return         0 case added, 1 case emailExists
     */
    public int addAdmin(Admin user){
        int returnValue = 0;
        try {
            adminRepository.save(user);
        }
        catch (DataIntegrityViolationException e){ //Isto pode ser mudado
            returnValue = 1;
        }
        return returnValue;
    }

    /**
     * Save a expert to table
     *
     * @param  user    the expert that you want to add
     * @return         0 case added, 1 case emailExists
     */
    public int addExpert(Expert user){
        int returnValue = 0;
        try {
        expertRepository.save(user);
        }
        catch (DataIntegrityViolationException e){ //Isto pode ser mudado
            returnValue = 1;
        }
        return returnValue;
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

    public User getUserByEmail(String email){
        User user = new User();

        List<Integer> ids = adminRepository.findIdByEmail(email);

        if(ids.isEmpty()){
            ids = gamblerRepository.findIdByEmail(email);

            if(ids.isEmpty()){
                ids = expertRepository.findIdByEmail(email);

                if(!ids.isEmpty()) //Caso encontre expert
                    user = this.getExpert(ids.get(0));

            } else { //Caso encontre gambler
                user = this.getGambler(ids.get(0));
            }

        } else { //Caso encontre admin
            user = this.getAdmin(ids.get(0));
        }
        return user;
    }
}
