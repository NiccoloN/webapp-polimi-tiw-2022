package it.polimi.tiw.project.beans;

import java.math.BigDecimal;
import java.util.Date;

public class Movement {

    private long ID;
    private Date date;
    private BigDecimal amount;
    private int fromBankAccountID;
    private int toBankAccountID;
    private String causal;

    public long getID() {

        return ID;
    }

    public void setID(long ID) {

        this.ID = ID;
    }

    public Date getDate() {

        return date;
    }

    public void setDate(Date date) {

        this.date = date;
    }

    public BigDecimal getAmount() {

        return amount;
    }

    public void setAmount(BigDecimal amount) {

        this.amount = amount;
    }

    public int getFromBankAccountID() {

        return fromBankAccountID;
    }

    public void setFromBankAccountID(int fromBankAccountID) {

        this.fromBankAccountID = fromBankAccountID;
    }

    public int getToBankAccountID() {

        return toBankAccountID;
    }

    public void setToBankAccountID(int toBankAccountID) {

        this.toBankAccountID = toBankAccountID;
    }
    
    public String getCausal() {
    	
    	return causal;
    }
    
    public void setCausal(String causal) {
    	
    	this.causal = causal;
    }
    
    
}
