package ras.adlrr.RASBet.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ras.adlrr.RASBet.api.auxiliar.ResponseEntityBadRequest;
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
    public ResponseEntity<Gambler> addGambler(@RequestBody Gambler gambler){
        try{ return ResponseEntity.ok().body(userService.addGambler(gambler)); }
        catch (Exception e){
            return new ResponseEntityBadRequest<Gambler>().createBadRequest(e.getMessage());
        }
    }

    @PutMapping("/gambler/update")
    public ResponseEntity<Gambler> updateGambler(@RequestParam(value = "id") int gambler_id,
                                                 @RequestParam(value = "name", required = false) String name,
                                                 @RequestParam(value = "email", required = false) String email,
                                                 @RequestParam(value = "password", required = false) String password,
                                                 @RequestParam(value = "phone_number", required = false) Integer phoneNumber,
                                                 @RequestParam(value = "nationality", required = false) String nationality,
                                                 @RequestParam(value = "city", required = false) String city,
                                                 @RequestParam(value = "address", required = false) String address,
                                                 @RequestParam(value = "postal_code", required = false) String postal_code,
                                                 @RequestParam(value = "occupation", required = false) String occupation){
        try{ return ResponseEntity.ok().body(userService.updateGambler(gambler_id, name, email, password, phoneNumber,nationality, city, address, postal_code, occupation)); }
        catch (Exception e){
            return new ResponseEntityBadRequest<Gambler>().createBadRequest(e.getMessage());
        }
    }

    @GetMapping(path = "/gambler")
    public ResponseEntity<Gambler> getGambler(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "email", required = false) String email){
        if(id == null && email == null)
            return new ResponseEntityBadRequest<Gambler>().createBadRequest("An email or id is required!");

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
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }

    @GetMapping("/gambler/all")
    public ResponseEntity<List<Gambler>> getListOfGamblers(){
        return ResponseEntity.ok().body(userService.getListOfGamblers());
    }

    // ------------ Admin Methods ------------

    @PostMapping("/admin")
    public ResponseEntity<Admin> addAdmin(@RequestBody Admin admin){
        try{ return ResponseEntity.ok().body(userService.addAdmin(admin)); }
        catch (Exception e){
            return new ResponseEntityBadRequest<Admin>().createBadRequest(e.getMessage());
        }
    }

    @GetMapping(path = "/admin")
    public ResponseEntity<Admin> getAdmin(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "email", required = false) String email){
        if(id == null && email == null)
            return new ResponseEntityBadRequest<Admin>().createBadRequest("An email or id is required!");

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
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Admin>> getListOfAdmins(){
        return ResponseEntity.ok().body(userService.getListOfAdmins());
    }

    // ------------ Expert Methods ------------

    @PostMapping("/expert")
    public ResponseEntity<Expert> addExpert(@RequestBody Expert expert){
        try{ return ResponseEntity.ok().body(userService.addExpert(expert)); }
        catch (Exception e){
            return new ResponseEntityBadRequest<Expert>().createBadRequest(e.getMessage());
        }
    }

    @GetMapping(path = "/expert")
    public ResponseEntity<Expert> getExpert(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "email", required = false) String email){
        if(id == null && email == null)
            return new ResponseEntityBadRequest<Expert>().createBadRequest("An email or id is required!");

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
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
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
    @GetMapping
    public int logIn(@RequestParam("email") String email, @RequestParam("password") String password){
        return userService.logIn(email,password);
    }

    public User getUserByEmail(String email){
        return userService.getUserByEmail(email);
    }
}