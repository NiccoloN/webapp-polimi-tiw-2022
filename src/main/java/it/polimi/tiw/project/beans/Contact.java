package it.polimi.tiw.project.beans;

public class Contact {

	private String ownerUsername, contactUsername;
	private int contactBankAccountID;
	
	public String getOwnerUsername() {
		
		return ownerUsername;
	}
	
	public void setOwnerUsername(String ownerUsername) {
		
		this.ownerUsername = ownerUsername;
	}
	
	public String getContactUsername() {
		
		return contactUsername;
	}
	
	public void setContactUsername(String contactUsername) {
		
		this.contactUsername = contactUsername;
	}
	
	public int getContactBankAccountID() {
		
		return contactBankAccountID;
	}
	
	public void setContactBankAccountID(int contactBankAccountID) {
		
		this.contactBankAccountID = contactBankAccountID;
	}
}
