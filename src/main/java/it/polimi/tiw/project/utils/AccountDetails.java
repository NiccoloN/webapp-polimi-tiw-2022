package it.polimi.tiw.project.utils;

import java.util.List;

import it.polimi.tiw.project.beans.BankAccount;
import it.polimi.tiw.project.beans.Movement;

public class AccountDetails {
	
	private final BankAccount bankAccount;
	private final List<Movement> movements;
	
	public AccountDetails(BankAccount bankAccount, List<Movement> movements) {
		
		this.bankAccount = bankAccount;
		this.movements = movements;
	}
}
