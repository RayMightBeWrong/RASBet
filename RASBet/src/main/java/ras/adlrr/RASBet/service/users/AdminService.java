package ras.adlrr.RASBet.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.AdminRepository;
import ras.adlrr.RASBet.model.Admin;
import ras.adlrr.RASBet.service.interfaces.users.IAdminService;

import java.util.List;

@Service
public class AdminService implements IAdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

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
        String attributesError = UserAuxiliarMethods.validateUserAttributes(admin);
        if(attributesError != null)
            throw new Exception(attributesError);
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
     * Checks for the existence of a admin with the given email
     * @param email Identification of the admin
     * @return true if a admin exists with the given identification
     */
    public boolean adminExistsByEmail(String email){
        return adminRepository.existsByEmail(email);
    }

    /**
     * Validates all the attributes of an admin.
     * @param admin Object that extends the class User, which contains the attributes.
     * @return "null" if all attributes are valid or string mentioning error
     */
    private String validateAdminAttributes(Admin admin){
        return UserAuxiliarMethods.validateUserAttributes(admin);
    }

}
