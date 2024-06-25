package it.polimi.tiw.project.beans;

import java.math.BigDecimal;

public class BankAccount {

    private int ID;
    private BigDecimal amount;
    private String user;

    public int getID() {

        return ID;
    }

    public void setID(int ID) {

        this.ID = ID;
    }

    public BigDecimal getAmount() {

        return amount;
    }

    public void setAmount(BigDecimal amount) {

        this.amount = amount;
    }

    public String getUser() {

        return user;
    }

    public void setUser(String user) {

        this.user = user;
    }
}
