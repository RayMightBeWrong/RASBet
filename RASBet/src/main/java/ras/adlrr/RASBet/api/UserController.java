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

    @PostMapping
    public int addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @GetMapping(path = "{id}")
    public User getUser(@PathVariable("id") int id){
        return userService.getUser(id);
    }

    @DeleteMapping(path = "{id}")
    public int removeUser(@PathVariable int id) {
        return userService.removeUser(id);
    }

    @GetMapping
    public List<User> getListOfSports() {
        return userService.getListOfUsers();
    }
}
