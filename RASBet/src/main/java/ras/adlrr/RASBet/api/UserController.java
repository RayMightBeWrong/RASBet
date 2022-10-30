package ras.adlrr.RASBet.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ras.adlrr.RASBet.model.Admin;
import ras.adlrr.RASBet.model.Expert;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.model.User;
import ras.adlrr.RASBet.service.UserService;



@RequestMapping("/api/users/")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Save a admin to table
     *
     * @param  user    the admin that you want to add
     * @return         0 case added, 1 case emailExists
     */
    @PostMapping("/admins/")
    public int registerAdmin(@RequestBody Admin user){
        return userService.addAdmin(user);
    }

    /**
     * Save a expert to table
     *
     * @param  user    the expert that you want to add
     * @return         0 case added, 1 case emailExists
     */
    @PostMapping("/experts/")
    public int registerExpert(@RequestBody Expert user){
        return userService.addExpert(user);
    }

    /**
     * Save a gambler to table
     *
     * @param  user    the gambler that you want to add
     * @return         0 case added, 1 case emailExists
     */
    @PostMapping("/gamblers/")
    public int registerGambler(@RequestBody Gambler user){
        return userService.addGambler(user);
    }

    @GetMapping(path = "/admins/{id}")
    public Admin getAdmin(@PathVariable("id") int id){
        return userService.getAdmin(id);
    }

    @GetMapping(path = "/experts/{id}")
    public Expert getExpert(@PathVariable("id") int id){
        return userService.getExpert(id);
    }

    @GetMapping(path = "/gamblers/{id}")
    public Gambler getGambler(@PathVariable("id") int id){
        return userService.getGambler(id);
    }

    @DeleteMapping(path = "/admins/{id}")
    public int removeAdmin(@PathVariable int id) {
        return userService.removeAdmin(id);
    }

    @DeleteMapping(path = "/expert/{id}")
    public int removeExpert(@PathVariable int id) {
        return userService.removeExpert(id);
    }

    @DeleteMapping(path = "/gamblers/{id}")
    public int removeGambler(@PathVariable int id) {
        return userService.removeGambler(id);
    }

    @GetMapping("/admins/*")
    public List<Admin> getListOfAdmins() {
        return userService.getListOfAdmins();
    }

    @GetMapping("/experts/*")
    public List<Expert> getListOfExperts() {
        return userService.getListOfExperts();
    }

    @GetMapping("/gamblers/*")
    public List<Gambler> getListOfGamblers() {
        return userService.getListOfGamblers();
    }

    //todo add mapping
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }
}
