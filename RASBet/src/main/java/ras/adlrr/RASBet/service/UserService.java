package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.AdminRepository;
import ras.adlrr.RASBet.dao.ExpertRepository;
import ras.adlrr.RASBet.dao.GamblerRepository;
import ras.adlrr.RASBet.model.Admin;
import ras.adlrr.RASBet.model.Expert;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.model.User;

import java.time.LocalDate;
import java.util.List;

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


    // ------------ Gambler Methods ------------

    public Gambler getGamblerById(int id){
        return gamblerRepository.findById(id).orElse(null);
    }

    public Gambler getGamblerByEmail(String email) {
        return gamblerRepository.findByEmail(email).orElse(null);
    }

    public Gambler addGambler(Gambler gambler) throws Exception {
        gambler.setId(0);
        if(existsUserWithEmail(gambler.getEmail()))
            throw new Exception("Email already used by another user!");
        if(gambler.getCc() == null)
            throw new Exception("CC is required!");
        if(gambler.getNif() == null)
            throw new Exception("NIF is required!");
        if(gambler.getDate_of_birth() == null)
            throw new Exception("Date of birth is required!");
        else if(gambler.getDate_of_birth().isAfter(LocalDate.now().minusYears(18)))
            throw new Exception("Minimum age of 18 is required!");
        return gamblerRepository.save(gambler);
    }

    public void removeGambler(int id) throws Exception {
        if(!gamblerRepository.existsById(id))
            throw new Exception("Gambler needs to exist to be removed!");
        gamblerRepository.deleteById(id);
    }

    public List<Gambler> getListOfGamblers(){
        return gamblerRepository.findAll();
    }

    public boolean gamblerExistsById(int gambler_id){
        return gamblerRepository.existsById(gambler_id);
    }

    public Gambler updateGambler(int gambler_id, String name, String email, String password, Integer phoneNumber,
                                 String nationality, String city, String address, String postal_code, String occupation) throws Exception {
        Gambler gambler = getGamblerById(gambler_id);
        if(gambler == null)
            throw new Exception("Gambler does not exist!");

        if(name != null)
            gambler.setName(name);
        if(email != null)
            gambler.setEmail(email);
        if(password != null)
            gambler.setPassword(password);
        if(phoneNumber != null)
            gambler.setPhoneNumber(phoneNumber);
        if(nationality != null)
            gambler.setNationality(nationality);
        if(city != null)
            gambler.setCity(city);
        if(address != null)
            gambler.setAddress(address);
        if(postal_code != null)
            gambler.setPostal_code(postal_code);
        if(occupation != null)
            gambler.setOccupation(occupation);

        String error = validateGamblerAttributes(gambler);
        if(error != null)
            throw new Exception(error);

        return gamblerRepository.save(gambler);
    }

    //TODO - necessario criar metodo que faca a verificacao de todos os campos
    /** @return "null" if all attributes are valid or string mentioning error **/
    private String validateGamblerAttributes(Gambler gambler){
        return null;
    }

    // ------------ Admin Methods ------------

    public Admin getAdminById(int id){
        return adminRepository.findById(id).orElse(null);
    }

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }

    public Admin addAdmin(Admin admin) throws Exception {
        admin.setId(0);
        if(existsUserWithEmail(admin.getEmail()))
            throw new Exception("Email already used by another user!");
        return adminRepository.save(admin);
    }

    public void removeAdmin(int id){
        adminRepository.deleteById(id);
    }

    public List<Admin> getListOfAdmins(){
        return adminRepository.findAll();
    }

    public boolean adminExistsById(int admin_id){
        return adminRepository.existsById(admin_id);
    }

    /** @return "null" if all attributes are valid or string mentioning error **/
    //TODO
    private String validateAdminAttributes(Admin admin){
        return null;
    }

    // ------------ Expert Methods ------------

    public Expert getExpertById(int id){
        return expertRepository.findById(id).orElse(null);
    }

    public Expert getExpertByEmail(String email) {
        return expertRepository.findByEmail(email).orElse(null);
    }

    public Expert addExpert(Expert expert) throws Exception {
        expert.setId(0);
        if(existsUserWithEmail(expert.getEmail()))
            throw new Exception("Email already used by another user!");
        return expertRepository.save(expert);
    }

    public void removeExpert(int id){
        expertRepository.deleteById(id);
    }

    public List<Expert> getListOfExperts(){
        return expertRepository.findAll();
    }

    public boolean expertExistsById(int expert_id){
        return expertRepository.existsById(expert_id);
    }

    /** @return "null" if all attributes are valid or string mentioning error **/
    //TODO
    private String validateExpertAttributes(Expert expert){
        return null;
    }

    // ------------ Shared Methods ------------

    /** @return "null" if all attributes are valid or string mentioning error **/
    //TODO
    private String validateUserAttributes(User user){
        return null;
    }

    public User getUserByEmail(String email){
        User user;
        if((user = getGamblerByEmail(email)) == null)
            if((user = getAdminByEmail(email)) == null)
                user = getExpertByEmail(email);
        return user;
    }

    public int logIn(String email,String password){
        int retValue = -1;
        User user = this.getUserByEmail(email);
        if(user!=null && user.getPassword().equals(password)){
            if (user instanceof Gambler)
                retValue = 0;
            else if (user instanceof Admin)
                retValue = 1;
            else if (user instanceof Expert)
                retValue = 2;
        }
        return retValue;
    }

    private boolean existsUserWithEmail(String email){
        boolean exists;
        if(!(exists = gamblerRepository.existsByEmail(email)))
            if(!(exists = adminRepository.existsByEmail(email)))
                exists = expertRepository.existsByEmail(email);
        return exists;
    }
}
