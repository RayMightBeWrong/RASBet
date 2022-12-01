package ras.adlrr.RASBet.service.interfaces.users;

import java.util.List;

import ras.adlrr.RASBet.model.Admin;

public interface IAdminService {
    public Admin getAdminById(int id);

    public Admin getAdminByEmail(String email);

    public Admin addAdmin(Admin admin) throws Exception;

    public void removeAdmin(int id) throws Exception;

    public List<Admin> getListOfAdmins();

    public boolean adminExistsById(int admin_id);

    boolean adminExistsByEmail(String email);
}
