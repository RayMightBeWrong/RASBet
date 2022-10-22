package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ras.adlrr.RASBet.model.User;

public interface UserRepository extends JpaRepository<User,Integer> {
}

