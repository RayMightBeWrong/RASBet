package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.api.auxiliar.ResponseEntityBadRequest;
import ras.adlrr.RASBet.dao.AdminRepository;
import ras.adlrr.RASBet.dao.ExpertRepository;
import ras.adlrr.RASBet.dao.GamblerRepository;
import ras.adlrr.RASBet.model.Admin;
import ras.adlrr.RASBet.model.Expert;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.model.Notification;
import ras.adlrr.RASBet.model.User;
import ras.adlrr.RASBet.service.interfaces.IAdminService;
import ras.adlrr.RASBet.service.interfaces.IExpertService;
import ras.adlrr.RASBet.service.interfaces.IGamblerService;
import ras.adlrr.RASBet.service.interfaces.INotificationService;
import ras.adlrr.RASBet.service.interfaces.IUserService;

import java.time.ZoneId;
import java.util.regex.*;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService implements IUserService, IAdminService, IGamblerService, IExpertService{
    private final GamblerRepository gamblerRepository;
    private final AdminRepository adminRepository;
    private final ExpertRepository expertRepository;
    private final INotificationService notificationService;

    @Autowired
    public UserService(GamblerRepository gamblerRepository, AdminRepository adminRepository, 
                            ExpertRepository expertRepository, INotificationService notificationService){
        this.gamblerRepository = gamblerRepository;
        this.adminRepository = adminRepository;
        this.expertRepository = expertRepository;
        this.notificationService = notificationService;
    }


    // ------------ Gambler Methods ------------

    /**
     * Checks for the existence of a gambler with the given id. If the gambler exists, returns it.
     * @param id Identification of the gambler
     * @return gambler if it exists, or null
     */
    public Gambler getGamblerById(int id){
        return gamblerRepository.findById(id).orElse(null);
    }

    /**
     * Checks for the existence of a gambler with the given email. If the gambler exists, returns it.
     * @param email Identification of the gambler
     * @return gambler if it exists, or null
     */
    public Gambler getGamblerByEmail(String email) {
        return gamblerRepository.findByEmail(email).orElse(null);
    }

    public String getGamblerEmail(int id) {
        return gamblerRepository.getGamblerEmail(id).orElse(null);
    }

    /**
     * Adds a gambler to the repository
     * @param gambler Gambler to be persisted
     * @return gambler updated by the repository
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    public Gambler addGambler(Gambler gambler) throws Exception {
        gambler.setId(0);
        if(gambler.getCc() == null)
            throw new Exception("CC is required!");
        if(gambler.getNif() == null)
            throw new Exception("NIF is required!");
        if(gambler.getDate_of_birth() == null)
            throw new Exception("Date of birth is required!");
        String attributesError = validateGamblerAttributes(gambler, null);
        if(attributesError != null)
            throw new Exception(attributesError);

        Gambler res = gamblerRepository.save(gambler);
        String message = "Congratulations! You just created an account at RASBet!";
        String subject = "[RASBet] Created Account at RASBet";
        Notification notification = new Notification(gambler.getId(), gambler.getEmail(), message, subject);
        notificationService.addNotification(notification);
        return res;
    }

    /**
     * If a gambler with the given id exists, removes it from the repository
     * @param id Identification of the gambler
     * @throws Exception If the gambler does not exist.
     */
    public void removeGambler(int id) throws Exception {
        if(!gamblerExistsById(id))
            throw new Exception("Gambler does not exist!");
        gamblerRepository.deleteById(id);
    }

    /**
     * @return list of gamblers present in the repository
     */
    public List<Gambler> getListOfGamblers(){
        return gamblerRepository.findAll();
    }

    /**
     * Checks for the existence of a gambler with the given id
     * @param gambler_id Identification of the gambler
     * @return true if a gambler exists with the given identification
     */
    public boolean gamblerExistsById(int gambler_id){
        return gamblerRepository.existsById(gambler_id);
    }

    /**
     *
     * @param gambler_id Identification of the gambler
     * @param name New name
     * @param email New email
     * @param password New password
     * @param phoneNumber New phone number
     * @param nationality New nationality
     * @param city New city
     * @param address New address
     * @param postal_code New postal code
     * @param occupation New occupation
     * @return updated gambler
     * @throws Exception If any error occurs, such as a new attribute or if no gambler exists with the given id.
     */
    public Gambler updateGambler(int gambler_id, String name, String email, String password, Integer phoneNumber,
                                 String nationality, String city, String address, String postal_code, String occupation) throws Exception {
        Gambler gambler = getGamblerById(gambler_id);
        if(gambler == null)
            throw new Exception("Gambler does not exist!");

        if(name != null)
            gambler.setName(name);

        String currentEmail = gambler.getEmail();
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

        String error = validateGamblerAttributes(gambler, currentEmail);
        if(error != null)
            throw new Exception(error);

        Gambler res = gamblerRepository.save(gambler);
        String message = "The present e-mail serves as confirmation that some information has been at your RASBet account.";
        String subject = "[RASBet] Updated Information";
        Notification notification = new Notification(gambler.getId(), gambler.getEmail(), message, subject);
        notificationService.addNotification(notification);
        return res;
    }

    /**
     * Validates all the attributes of a gambler.
     * @param gambler Object that extends the class User, which contains the attributes.
     * @param currentEmail Current email of the user. Needed to not throw exception when a verification of the attributes is requested when performing an update.
     * @return "null" if all attributes are valid or string mentioning error
     */
    private String validateGamblerAttributes(Gambler gambler, String currentEmail){
        String errorUserAttributes = validateUserAttributes(gambler, currentEmail);
        if(errorUserAttributes != null)
            return errorUserAttributes;
        if(!Pattern.matches("^\\d{9}$", gambler.getPhoneNumber().toString()))
            return "A valid portuguese phone number contains 9 numbers.";
        if(!Pattern.matches("^[A-Z\\-][a-z]*(?: [A-Za-z \\-]+)*$", gambler.getNationality()))
            return "Nationality can only contain alpha characters, spaces and hyphens.";
        if(!Pattern.matches("^[A-Z\\-][a-z]*(?: [A-Za-z \\-]+)*$", gambler.getCity()))
            return "City can only contain alpha characters, spaces and hyphens.";
        if(!Pattern.matches("^[A-Z\\-][a-z]*(?:( [A-Za-z \\-]+)|[.,])*$", gambler.getAddress()))
            return "Address can only contain alpha characters, spaces, commas, dots and hyphens.";
        if(!Pattern.matches("^\\d{4}-\\d{3}$", gambler.getPostal_code()))
            return "A valid portuguese postal code is formed by 4 digits followed plus an hyphen plus 3 more digits.";
        if(!Pattern.matches("^[A-Z\\-][a-z]*(?: [A-Za-z \\-]+)*$", gambler.getOccupation()))
            return "Occupation can only contain alpha characters, spaces and hyphens.";
        if(!Pattern.matches("^\\d{8}$", gambler.getCc()))
            return "CC has to be a 8 digits number.";
        if(!Pattern.matches("^\\d{9}$", gambler.getNif().toString()))
            return "NIF has to be a 9 digits number.";
        if(gambler.getDate_of_birth().isAfter(LocalDate.now(ZoneId.of("UTC+00:00")).minusYears(18)))
            return "Minimum age of 18 is required!";

        return null;
    }

    // ------------ Admin Methods ------------

    /**
     * Checks for the existence of an admin with the given id. If the admin exists, returns it.
     * @param id Identification of the admin
     * @return admin if it exists, or null
     */
    public Admin getAdminById(int id){
        return adminRepository.findById(id).orElse(null);
    }

    /**
     * Checks for the existence of an admin with the given email. If the admin exists, returns it.
     * @param email Identification of the admin
     * @return admin if it exists, or null
     */
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }

    /**
     * Adds an admin to the repository
     * @param admin Admin to be persisted
     * @return admin updated by the repository
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    public Admin addAdmin(Admin admin) throws Exception {
        admin.setId(0);
        if(existsUserWithEmail(admin.getEmail()))
            throw new Exception("Email already used by another user!");
        return adminRepository.save(admin);
    }

    /**
     * If an admin with the given id exists, removes it from the repository
     * @param id Identification of the admin
     * @throws Exception If the admin does not exist.
     */
    public void removeAdmin(int id) throws Exception {
        if(!adminExistsById(id))
            throw new Exception("Admin does not exist!");
        adminRepository.deleteById(id);
    }

    /**
     * @return list of admins present in the repository
     */
    public List<Admin> getListOfAdmins(){
        return adminRepository.findAll();
    }

    /**
     * Checks for the existence of an admin with the given id
     * @param admin_id Identification of the admin
     * @return true if an admin exists with the given identification
     */
    public boolean adminExistsById(int admin_id){
        return adminRepository.existsById(admin_id);
    }

    /**
     * Validates all the attributes of an admin.
     * @param admin Object that extends the class User, which contains the attributes.
     * @return "null" if all attributes are valid or string mentioning error
     */
    private String validateAdminAttributes(Admin admin){
        return validateUserAttributes(admin, null);
    }

    // ------------ Expert Methods ------------

    /**
     * Checks for the existence of an expert with the given id. If the expert exists, returns it.
     * @param id Identification of the expert
     * @return expert if it exists, or null
     */
    public Expert getExpertById(int id){
        return expertRepository.findById(id).orElse(null);
    }

    /**
     * Checks for the existence of an expert with the given email. If the expert exists, returns it.
     * @param email Identification of the expert
     * @return expert if it exists, or null
     */
    public Expert getExpertByEmail(String email) {
        return expertRepository.findByEmail(email).orElse(null);
    }

    /**
     * Adds an expert to the repository
     * @param expert Expert to be persisted
     * @return expert updated by the repository
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    public Expert addExpert(Expert expert) throws Exception {
        expert.setId(0);
        if(existsUserWithEmail(expert.getEmail()))
            throw new Exception("Email already used by another user!");
        return expertRepository.save(expert);
    }

    /**
     * If an expert with the given id exists, removes it from the repository
     * @param id Identification of the expert
     * @throws Exception If the expert does not exist.
     */
    public void removeExpert(int id) throws Exception {
        if(!expertExistsById(id))
            throw new Exception("Admin does not exist!");
        expertRepository.deleteById(id);
    }

    /**
     * @return list of experts present in the repository
     */
    public List<Expert> getListOfExperts(){
        return expertRepository.findAll();
    }

    /**
     * Checks for the existence of an expert with the given id
     * @param expert_id Identification of the expert
     * @return true if an expert exists with the given identification
     */
    public boolean expertExistsById(int expert_id){
        return expertRepository.existsById(expert_id);
    }

    /**
     * Validates all the attributes of an expert.
     * @param expert Object that extends the class User, which contains the attributes.
     * @return "null" if all attributes are valid or string mentioning error
     */
    private String validateExpertAttributes(Expert expert){
        return validateUserAttributes(expert, null);
    }

    // ------------ Shared Methods ------------

    /**
     * Validates the attributes common to all types of users, the name, the password and email.
     * @param user Object that extends the class User, which contains the attributes referred in the description of the function.
     * @param currentEmail Current email of the user. Needed to not throw exception when a verification of the attributes is requested when performing an update.
     * @return "null" if all attributes are valid or string mentioning error
     */
    private String validateUserAttributes(User user, String currentEmail){
        if(!user.getEmail().equals(currentEmail) && existsUserWithEmail(user.getEmail()))
            return "Email already used by another user!";
        if(!Pattern.matches("^\\w[\\w ]*$", user.getName()))
            return "A name can only contain alphanumeric characters and spaces. Must also start with an alphanumeric.";
        if(!Pattern.matches("^\\w[\\w.]*@\\w+(?:\\.\\w+)+$", user.getEmail()))
            return "Invalid email format";
        return null;
    }

    /**
     * Searches, in the repositories of the different types of users, for a user associated with the email given.
     * @param email identifies the user that is the aim of the search
     * @return User associated with the email given as argument
     */
    public User getUserByEmail(String email){
        User user;
        if((user = getGamblerByEmail(email)) == null)
            if((user = getAdminByEmail(email)) == null)
                user = getExpertByEmail(email);
        return user;
    }

    /**
     * Tries to find a match to the credentials given. If successful returns the type of user.
     * @param email Represents the user
     * @param password Private key, needed to log in
     * @return integer representing the type of the user (0 - Gambler, 1 - Admin, 2 - Expert) or -1 in case the credentials are wrong
     */
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

    /**
     * Verifies if there is a user, of any type, associated with the given email
     * @param email serves as identification of the user
     * @return true if the user exists, otherwise returns null
     */
    private boolean existsUserWithEmail(String email){
        boolean exists;
        if(!(exists = gamblerRepository.existsByEmail(email)))
            if(!(exists = adminRepository.existsByEmail(email)))
                exists = expertRepository.existsByEmail(email);
        return exists;
    }
}