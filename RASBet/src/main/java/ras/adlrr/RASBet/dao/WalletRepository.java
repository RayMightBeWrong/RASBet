package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ras.adlrr.RASBet.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet,Integer> {
}
