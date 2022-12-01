package ras.adlrr.RASBet.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.model.Admin;
import ras.adlrr.RASBet.model.Expert;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.model.User;
import ras.adlrr.RASBet.service.interfaces.users.IAdminService;
import ras.adlrr.RASBet.service.interfaces.users.IExpertService;
import ras.adlrr.RASBet.service.interfaces.users.IGamblerService;
import ras.adlrr.RASBet.service.interfaces.users.IUserService;

import java.util.Map;

@Service("userService")
public class UserService implements IUserService {
    private final IGamblerService gamblerService;
    private final IAdminService adminService;
    private final IExpertService expertService;

    @Autowired
    public UserService(@Qualifier("gamblerService") IGamblerService gamblerService, @Qualifier("adminService") IAdminService adminService,
                       @Qualifier("expertService") IExpertService expertService) {
        this.gamblerService = gamblerService;
        this.adminService = adminService;
        this.expertService = expertService;
    }

    /**
     * Searches, in the repositories of the different types of users, for a user associated with the email given.
     * @param email identifies the user that is the aim of the search
     * @return User associated with the email given as argument
     */
    public User getUserByEmail(String email){
        User user;
        if((user = gamblerService.getGamblerByEmail(email)) == null)
            if((user = adminService.getAdminByEmail(email)) == null)
                user = expertService.getExpertByEmail(email);
        return user;
    }

    /**
     * Tries to find a match to the credentials given. If successful returns a map with user_id and the type of user.
     * Note: type of user -> (0 - Gambler, 1 - Admin, 2 - Expert)
     * @param email Represents the user
     * @param password Private key, needed to log in
     * @return null if the credentials do not have a match. Otherwise, returns a map with user_id and the type of user.
     */
    public Map<String, Integer> logIn(String email, String password){
        int type = -1;

        User user = this.getUserByEmail(email);
        if(user != null && user.getPassword().equals(password)){
            if (user instanceof Gambler)
                type = 0;
            else if (user instanceof Admin)
                type = 1;
            else if (user instanceof Expert)
                type = 2;

            return Map.of("user_id", user.getId(), "type", type);
        }

        return null;
    }

    /**
     * Verifies if there is a user, of any type, associated with the given email
     * @param email serves as identification of the user
     * @return true if the user exists, otherwise returns null
     */
    public boolean existsUserWithEmail(String email){
        boolean exists;
        if(!(exists = gamblerService.gamblerExistsByEmail(email)))
            if(!(exists = adminService.adminExistsByEmail(email)))
                exists = expertService.expertExistsByEmail(email);
        return exists;
    }
}
