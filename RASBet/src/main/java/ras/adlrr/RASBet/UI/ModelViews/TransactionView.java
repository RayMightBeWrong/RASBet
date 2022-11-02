package ras.adlrr.RASBet.UI.ModelViews;

import ras.adlrr.RASBet.model.Transaction;

public class TransactionView {
    private Transaction transaction;

    public TransactionView(Transaction transaction) {
        this.transaction = transaction;
    }

    public String toString(){
        String s = "Transação\n";
        s += "Descrição : " + transaction.getDescription() + "\n";
        s += "Data : " + transaction.getDate() + "\n";
        WalletView walletView = new WalletView(transaction.getWallet());
        s += walletView.toString() + "\n";
        s += "Quantidade gasta: " + transaction.getValue() + "\n";
        s += "Quantidade final: " + transaction.getBalance_after_mov() + "\n";
        return s;
    }
}
