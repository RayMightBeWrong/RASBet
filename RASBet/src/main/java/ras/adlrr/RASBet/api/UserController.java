package ras.adlrr.RASBet.api;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ras.adlrr.RASBet.model.*;
import ras.adlrr.RASBet.service.UserService;



@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    // ------------ Gambler Methods ------------

    @PostMapping("/gambler")
    public ResponseEntity addGambler(@RequestBody Gambler gambler){
        try{ return ResponseEntity.ok().body(userService.addGambler(gambler)); }
        catch (Exception e){
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/gambler")
    public ResponseEntity getGambler(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "email", required = false) String email){
        if(id == null && email == null)
            return ResponseEntity.badRequest().body(new AbstractMap.SimpleEntry<>("error","An email or id is required!"));

        Gambler gambler = null;
        if(id != null)
            gambler = userService.getGamblerById(id);

        if(gambler == null && email != null)
            gambler = userService.getGamblerByEmail(email);

        return ResponseEntity.ok().body(gambler);
    }

    @DeleteMapping(path = "/gambler/{id}")
    public ResponseEntity removeGambler(@PathVariable int id){
        try {
            userService.removeGambler(id);
            return new ResponseEntity(HttpStatus.OK); }
        catch (Exception e){
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/gambler/all")
    public ResponseEntity<List<Gambler>> getListOfGamblers(){
        return ResponseEntity.ok().body(userService.getListOfGamblers());
    }

    // ------------ Admin Methods ------------

    @PostMapping("/admin")
    public ResponseEntity addAdmin(@RequestBody Admin admin){
        try{ return ResponseEntity.ok().body(userService.addAdmin(admin)); }
        catch (Exception e){
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/admin")
    public ResponseEntity getAdmin(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "email", required = false) String email){
        if(id == null && email == null)
            return ResponseEntity.badRequest().body(new AbstractMap.SimpleEntry<>("error","An email or id is required!"));

        Admin admin = null;
        if(id != null)
            admin = userService.getAdminById(id);

        if(admin == null && email != null)
            admin = userService.getAdminByEmail(email);

        return ResponseEntity.ok().body(admin);
    }

    @DeleteMapping(path = "/admin/{id}")
    public ResponseEntity removeAdmin(@PathVariable int id){
        try {
            userService.removeAdmin(id);
            return new ResponseEntity(HttpStatus.OK); }
        catch (Exception e){
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Admin>> getListOfAdmins(){
        return ResponseEntity.ok().body(userService.getListOfAdmins());
    }

    // ------------ Expert Methods ------------

    @PostMapping("/expert")
    public ResponseEntity addExpert(@RequestBody Expert expert){
        try{ return ResponseEntity.ok().body(userService.addExpert(expert)); }
        catch (Exception e){
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/expert")
    public ResponseEntity getExpert(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "email", required = false) String email){
        if(id == null && email == null)
            return ResponseEntity.badRequest().body(new AbstractMap.SimpleEntry<>("error","An email or id is required!"));

        Expert expert = null;
        if(id != null)
            expert = userService.getExpertById(id);

        if(expert == null && email != null)
            expert = userService.getExpertByEmail(email);

        return ResponseEntity.ok().body(expert);
    }

    @DeleteMapping(path = "/expert/{id}")
    public ResponseEntity removeExpert(@PathVariable int id){
        try {
            userService.removeExpert(id);
            return new ResponseEntity(HttpStatus.OK); }
        catch (Exception e){
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/expert/all")
    public ResponseEntity<List<Expert>> getListOfExperts(){
        return ResponseEntity.ok().body(userService.getListOfExperts());
    }

    // ------------ Shared Methods ------------

    /**
     * Save an expert to table
     *
     * @param  email      email of the account
     * @param  password   password that corresponds to the email account
     * @return            -1 unsuccessful logIn, 0 Gambler, 1 Admin, 2 Expert
     */
    //todo add mapping
    public int logIn(String email,String password){
        return userService.logIn(email,password);
    }

    public User getUserByEmail(String email){
        return userService.getUserByEmail(email);
    }
}
