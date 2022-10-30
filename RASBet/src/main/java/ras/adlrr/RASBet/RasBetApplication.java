package ras.adlrr.RASBet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ras.adlrr.RASBet.api.*;

@SpringBootApplication
public class RasBetApplication {

	public static void main(String[] args) {
		SpringApplication.run(RasBetApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(BetController betController, GameController gameController,
								  SportController sportController, TransactionController transactionController,
								  UserController userController, WalletController walletController){
		return (args -> {

		});
	}

}
