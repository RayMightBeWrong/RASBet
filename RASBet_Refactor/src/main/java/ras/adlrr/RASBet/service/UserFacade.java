package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.model.*;
import ras.adlrr.RASBet.service.interfaces.notifications.INotificationService;
import ras.adlrr.RASBet.service.interfaces.users.IAdminService;
import ras.adlrr.RASBet.service.interfaces.users.IExpertService;
import ras.adlrr.RASBet.service.interfaces.users.IGamblerService;
import ras.adlrr.RASBet.service.interfaces.users.IUserService;

import java.util.List;
import java.util.Map;

@Service("userFacade")
public class UserFacade implements IUserService, IAdminService, IGamblerService, IExpertService {
    private final IUserService userService;
    private final IGamblerService gamblerService;
    private final IAdminService adminService;
    private final IExpertService expertService;
    private final INotificationService notificationService;

    @Autowired
    public UserFacade(@Qualifier("userService") IUserService userService, @Qualifier("gamblerService") IGamblerService gamblerService,
                      @Qualifier("adminService") IAdminService adminService, @Qualifier("expertService") IExpertService expertService,
                      @Qualifier("notificationService") INotificationService notificationService) {
        this.userService = userService;
        this.gamblerService = gamblerService;
        this.adminService = adminService;
        this.expertService = expertService;
        this.notificationService = notificationService;
    }

    // ------------ Global User Methods ------------

    public User getUserByEmail(String email){
        return userService.getUserByEmail(email);
    }

    public Map<String, Integer> logIn(String email, String password){
        return userService.logIn(email, password);
    }

    public boolean existsUserWithEmail(String email){
        return userService.existsUserWithEmail(email);
    }

    // ------------ Gambler Methods ------------

    public Gambler getGamblerById(int id){
        return gamblerService.getGamblerById(id);
    }

    public Gambler getGamblerByEmail(String email) {
        return gamblerService.getGamblerByEmail(email);
    }

    public String getGamblerEmail(int id) {
        return gamblerService.getGamblerEmail(id);
    }

    public Gambler addGambler(Gambler gambler) throws Exception {
        if(existsUserWithEmail(gambler.getEmail()))
            throw new Exception("Email already used by another user!");

        gambler = gamblerService.addGambler(gambler);

        String message = "Congratulations! You just created an account at RASBet!";
        String subject = "[RASBet] Created Account at RASBet";
        Notification notification = new Notification(gambler.getId(), gambler.getEmail(), message, subject);
        notificationService.addNotification(notification);

        return gambler;
    }

    public void removeGambler(int id) throws Exception {
        gamblerService.removeGambler(id);
    }

    public List<Gambler> getListOfGamblers(){
        return gamblerService.getListOfGamblers();
    }

    public boolean gamblerExistsById(int gambler_id){
        return gamblerService.gamblerExistsById(gambler_id);
    }

    public boolean gamblerExistsByEmail(String email){
        return gamblerService.gamblerExistsByEmail(email);
    }

    public Gambler updateGambler(int gambler_id, String name, String email, String password, Integer phoneNumber,
                                 String nationality, String city, String address, String postal_code, String occupation) throws Exception {

        Gambler gambler = getGamblerById(gambler_id);
        if(gambler == null)
            throw new Exception("Gambler does not exist!");

        String currentEmail = gambler.getEmail();
        if(email != null && !currentEmail.equals(email) && existsUserWithEmail(email))
            throw new Exception("Email already used by another user!");

        Gambler res = gamblerService.updateGambler(gambler_id, name, email, password, phoneNumber,
                                                   nationality, city, address, postal_code, occupation);

        String message = "The present e-mail serves as confirmation that some information has been at your RASBet account.";
        String subject = "[RASBet] Updated Information";
        Notification notification = new Notification(gambler.getId(), gambler.getEmail(), message, subject);
        notificationService.addNotification(notification);

        return res;
    }

    // ------------ Admin Methods ------------

    public Admin getAdminById(int id){
        return adminService.getAdminById(id);
    }

    public Admin getAdminByEmail(String email) {
        return adminService.getAdminByEmail(email);
    }

    public Admin addAdmin(Admin admin) throws Exception {
        if(existsUserWithEmail(admin.getEmail()))
            throw new Exception("Email already used by another user!");
        return adminService.addAdmin(admin);
    }

    public void removeAdmin(int id) throws Exception {
        adminService.removeAdmin(id);
    }

    public List<Admin> getListOfAdmins(){
        return adminService.getListOfAdmins();
    }

    public boolean adminExistsById(int admin_id){
        return adminService.adminExistsById(admin_id);
    }

    public boolean adminExistsByEmail(String email){
        return adminService.adminExistsByEmail(email);
    }

    // ------------ Expert Methods ------------

    public Expert getExpertById(int id){
        return expertService.getExpertById(id);
    }

    public Expert getExpertByEmail(String email) {
        return expertService.getExpertByEmail(email);
    }

    public Expert addExpert(Expert expert) throws Exception {
        if(existsUserWithEmail(expert.getEmail()))
            throw new Exception("Email already used by another user!");
        return expertService.addExpert(expert);
    }

    public void removeExpert(int id) throws Exception {
        expertService.removeExpert(id);
    }

    public List<Expert> getListOfExperts(){
        return expertService.getListOfExperts();
    }

    public boolean expertExistsById(int expert_id){
        return expertService.expertExistsById(expert_id);
    }

    public boolean expertExistsByEmail(String email){
        return expertService.expertExistsByEmail(email);
    }
}
