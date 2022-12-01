package ras.adlrr.RASBet.service.interfaces;

import ras.adlrr.RASBet.model.Transaction;

public interface IUserTransactions {
    Transaction deposit(int wallet_id, float valueToDeposit) throws Exception;
    Transaction withdraw(int wallet_id, float valueToWithdraw) throws Exception;
    Transaction claimBalancePromotion(int wallet_id, String coupon) throws Exception;
}
