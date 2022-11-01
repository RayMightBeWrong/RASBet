package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Sport;

@Repository
public interface SportRepository extends JpaRepository<Sport,Integer> {
    Sport findByName(String name);
    boolean existsByName(String name);
}
