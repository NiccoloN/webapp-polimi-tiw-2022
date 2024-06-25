package it.polimi.tiw.project.utils;

import it.polimi.tiw.project.beans.BankAccount;
import it.polimi.tiw.project.beans.Movement;

import java.math.BigDecimal;

public class MovementInformation {
	
	private final BigDecimal fromPreviousAmount, toPreviousAmount;
	private final BankAccount fromBankAccount, toBankAccount;
	private final Movement movement;
	
	public MovementInformation(BankAccount fromBankAccount, BankAccount toBankAccount, Movement movement, BigDecimal fromPreviousAmount, BigDecimal toPreviousAmount) {
		
		this.fromPreviousAmount = fromPreviousAmount;
		this.toPreviousAmount = toPreviousAmount;
		this.fromBankAccount = fromBankAccount;
		this.toBankAccount = toBankAccount;
		this.movement = movement;
	}
}
