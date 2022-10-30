package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Admin;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {
    @Query("SELECT r.id FROM Admin r where r.email = :email")
    List<Integer> findIdByEmail(@Param("email") String email);
}
