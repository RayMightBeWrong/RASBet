package ras.adlrr.RASBet.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.GamblerRepository;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.service.interfaces.users.IGamblerService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class GamblerService implements IGamblerService {
    private final GamblerRepository gamblerRepository;

    @Autowired
    public GamblerService(GamblerRepository gamblerRepository) {
        this.gamblerRepository = gamblerRepository;
    }

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
        String attributesError = validateGamblerAttributes(gambler);
        if(attributesError != null)
            throw new Exception(attributesError);

        return gamblerRepository.save(gambler);
        //String message = "Congratulations! You just created an account at RASBet!";
        //String subject = "[RASBet] Created Account at RASBet";
        //Notification notification = new Notification(gambler.getId(), gambler.getEmail(), message, subject);
        //notificationService.addNotification(notification);
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
     * Checks for the existence of a gambler with the given email
     * @param email Identification of the gambler
     * @return true if a gambler exists with the given identification
     */
    public boolean gamblerExistsByEmail(String email){
        return gamblerRepository.existsByEmail(email);
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

        String error = validateGamblerAttributes(gambler);
        if(error != null)
            throw new Exception(error);

        return gamblerRepository.save(gambler);
        //String message = "The present e-mail serves as confirmation that some information has been at your RASBet account.";
        //String subject = "[RASBet] Updated Information";
        //Notification notification = new Notification(gambler.getId(), gambler.getEmail(), message, subject);
        //notificationService.addNotification(notification);
    }

    //* @param currentEmail Current email of the user. Needed to not throw exception when a verification of the attributes is requested when performing an update.
    /**
     * Validates all the attributes of a gambler.
     * @param gambler Object that extends the class User, which contains the attributes.
     * @return "null" if all attributes are valid or string mentioning error
     */
    private String validateGamblerAttributes(Gambler gambler){
        String errorUserAttributes = UserAuxiliarMethods.validateUserAttributes(gambler);
        if(errorUserAttributes != null)
            return errorUserAttributes;
        if(!Pattern.matches("^\\d{9}$", gambler.getPhoneNumber().toString()))
            return "A valid portuguese phone number contains 9 numbers.";
        if(!Pattern.matches("^[A-Z\\-][a-z]*(?: [A-Za-z \\-]+)*$", gambler.getNationality()))
            return "Nationality can only contain alpha characters, spaces and hyphens.";
        if(!Pattern.matches("^[A-Z\\-][a-z]*(?: [A-Za-z \\-]+)*$", gambler.getCity()))
            return "City can only contain alpha characters, spaces and hyphens.";
        if(!Pattern.matches("^[A-Z\\-][a-z]*(?:( [A-Za-z \\-\\d]+)|[.,])*$", gambler.getAddress()))
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

}
