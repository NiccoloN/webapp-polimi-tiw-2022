(function() {

    //load home
    makeCall("GET", "GetBankAccounts", null, function (request) {

        if(request.readyState === XMLHttpRequest.DONE) {

            const message = request.responseText;
            switch (request.status) {

                case 200:
                    const bankAccounts = JSON.parse(message);
                    showBankAccounts(bankAccounts);
                    break;
                case 400: // bad request
                    document.getElementById("errorMessage").textContent = message;
                    break;
                case 401: // unauthorized
                    document.getElementById("errorMessage").textContent = message;
                    break;
                case 500: // server error
                    document.getElementById("errorMessage").textContent = message;
                    break;
            }
        }
    });

    //load contacts
    makeCall("GET", "GetContacts", null, function (request) {

        if(request.readyState === XMLHttpRequest.DONE) {

            const message = request.responseText;
            switch (request.status) {

                case 200:
                    sessionStorage.setItem("contacts", message);
                    updateUsernameHints(JSON.parse(message));
                    break;
                case 400: // bad request
                    document.getElementById("errorMessage").textContent = message;
                    break;
                case 401: // unauthorized
                    document.getElementById("errorMessage").textContent = message;
                    break;
                case 500: // server error
                    document.getElementById("errorMessage").textContent = message;
                    break;
            }
        }
    });
}) ();

function showBankAccounts(bankAccounts) {

    const user = JSON.parse(sessionStorage.getItem("user"));
    document.getElementById("bankAccountsParagraph").textContent =
        "These are " + user.name + " " + user.surname + "'s bank accounts:";

    const accountsTable = createTable(2, bankAccounts.length, ["Bank Account ID", ""]);
    for (let n = 0; n < bankAccounts.length; n++) {

        accountsTable.rows[n + 1].cells[0].textContent = bankAccounts[n];

        const details = accountsTable.rows[n + 1].cells[1].firstChild;
        details.textContent = "Details";
        details.style.textDecoration = "underline";
        details.addEventListener("click", (e) => { getAccountDetails(bankAccounts[n]); }, false);
    }
    document.getElementById("bankAccountsParagraph").insertAdjacentElement("afterend", accountsTable);
}

function getAccountDetails(bankAccountId) {

    const formData = new FormData();
	formData.append("bankAccount", bankAccountId);
	makeCallFormData("POST", "GetAccountDetails", formData, function(request) {

		if(request.readyState === XMLHttpRequest.DONE) {

			const message = request.responseText;
            switch (request.status) {

            case 200:
                sessionStorage.setItem("bankAccountID", bankAccountId);
                const accountDetails = JSON.parse(message);
                showAccountDetails(accountDetails);
                break;
            case 400: // bad request
                document.getElementById("errorMessage").textContent = message;
                break;
            case 401: // unauthorized
                document.getElementById("errorMessage").textContent = message;
                break;
            case 500: // server error
                document.getElementById("errorMessage").textContent = message;
                break;
        	}
		}
	});

    const form = document.getElementById("movementForm");
    form.onsubmit = (e) => {

        e.preventDefault();
        makeMovement(form);
    };
    
    const contacts = JSON.parse(sessionStorage.getItem("contacts"));
    document.getElementById("usernameField").onkeyup = () => { updateBankAccountHints(contacts) };
}

function showAccountDetails(accountDetails) {

    const bankAccountID = JSON.parse(sessionStorage.getItem("bankAccountID"));
    const bankAccount = accountDetails.bankAccount;

    document.getElementById("accountDetailsHeader").textContent = "These are bank account " + bankAccountID + " details:";
    document.getElementById("accountDetailsID").textContent = "Bank Account ID: " + bankAccount.ID;
    document.getElementById("accountDetailsAmount").textContent = "Amount: " + bankAccount.amount;
    document.getElementById("accountDetailsOwner").textContent = "Owner: " + bankAccount.user;

    const div = document.getElementById("movementsTableDiv");
    const oldMovementsTable = div.children[0];
    if (oldMovementsTable != null) oldMovementsTable.remove();

    const movements = accountDetails.movements;
    if (movements !== null) {

        const movementsTable = createTable(5, movements.length, ["Date", "Amount", "Origin", "Destination", "Causal"]);
        for (let n = 0; n < movements.length; n++) {

            const rowCells = movementsTable.rows[n + 1].cells;
            rowCells[0].textContent = movements[n].date;
            rowCells[1].textContent = movements[n].amount;
            rowCells[2].textContent = movements[n].fromBankAccountID;
            rowCells[3].textContent = movements[n].toBankAccountID;
            rowCells[4].textContent = movements[n].causal;
        }
        div.appendChild(movementsTable);
    }

    document.getElementById("accountDetails").hidden = false;
    document.getElementById("movementSuccessDiv").hidden = true;
}

function makeMovement(form) {

    makeCall("POST", "MakeMovement", form, function (request) {

        if(request.readyState === XMLHttpRequest.DONE) {

            const message = request.responseText;
            switch (request.status) {

                case 200:
                    const movementInformation = JSON.parse(message);
                    showMovementSuccess(movementInformation);
                    break;
                case 400: // bad request
                    document.getElementById("errorMessage").textContent = message;
                    break;
                case 401: // unauthorized
                    document.getElementById("errorMessage").textContent = message;
                    break;
                case 500: // server error
                    document.getElementById("errorMessage").textContent = message;
                    break;
            }
        }
    });
}

function showMovementSuccess(movementInformation) {

    const fromBankAccount = movementInformation.fromBankAccount;
    const toBankAccount = movementInformation.toBankAccount;
    const movement = movementInformation.movement;

    const width = 3;
    const height = 4;
    const movementTable = createTable(width, height, ["From bank account", "Movement details", "To bank account"]);

    const row1 = movementTable.rows[1];
    row1.cells[0].textContent = "ID: " + fromBankAccount.ID;
    row1.cells[1].textContent = "ID: " + movement.ID;
    row1.cells[2].textContent = "ID: " + toBankAccount.ID;

    const row2 = movementTable.rows[2];
    row2.cells[0].textContent = "User: " + fromBankAccount.user;
    row2.cells[1].textContent = "Date: " + movement.date;
    row2.cells[2].textContent = "User: " + toBankAccount.user;

    const row3 = movementTable.rows[3];
    row3.cells[0].textContent = "Previous amount: " + movementInformation.fromPreviousAmount;
    row3.cells[1].textContent = "Amount: " + movement.amount;
    row3.cells[2].textContent = "Previous amount: " + movementInformation.toPreviousAmount;

    const row4 = movementTable.rows[4];
    row4.cells[0].textContent = "New amount: " + fromBankAccount.amount;
    row4.cells[1].textContent = "Causal: " + movement.causal;
    row4.cells[2].textContent = "New amount: " + toBankAccount.amount;

    const div = document.getElementById("movementSuccessTableDiv");
    const oldMovementTable = div.children[0];
    if (oldMovementTable !== null) oldMovementTable.remove();

    div.appendChild(movementTable);

    const saveContactButton = document.getElementById("saveContactButton");
    if(alreadySaved(toBankAccount.user, toBankAccount.ID)) saveContactButton.hidden = true;
    else {

        saveContactButton.hidden = false;
        saveContactButton.onclick = () => { saveContact(toBankAccount.user, toBankAccount.ID) };
    }

    document.getElementById("movementSuccessDiv").hidden = false;
    document.getElementById("accountDetails").hidden = true;
}

function saveContact(contactUsername, contactBankAccountID) {

    const formData = new FormData();
    formData.append("contactUsername", contactUsername);
    formData.append("contactBankAccountID", contactBankAccountID);
    makeCallFormData("POST", "AddContact", formData, (request) => {

        if(request.readyState === XMLHttpRequest.DONE) {

            const message = request.responseText;
            switch (request.status) {

                case 200:
                    sessionStorage.setItem("contacts", message);
                    document.getElementById("saveContactButton").hidden = true;
                    updateUsernameHints(JSON.parse(message));
                    break;
                case 400: // bad request
                    document.getElementById("errorMessage").textContent = message;
                    break;
                case 401: // unauthorized
                    document.getElementById("errorMessage").textContent = message;
                    break;
                case 500: // server error
                    document.getElementById("errorMessage").textContent = message;
                    break;
            }
        }
    });
}

function alreadySaved(contactUsername, contactBankAccountID) {

    const contacts = JSON.parse(sessionStorage.getItem("contacts"));
    if (contacts === null) return false;
    for (let n = 0; n < contacts.length; n++) {

        const contact = contacts[n];
        if (contact.contactUsername === contactUsername &&
            contact.contactBankAccountID === contactBankAccountID) return true;
    }

    return false;
}

function updateBankAccountHints(contacts) {
	
	let username = document.getElementById("usernameField").value;
	
	let dataList = document.getElementById("contactBankAccounts");
	dataList.innerHTML = "";
	
	for(let i = 0; i < contacts.length; i++) {
		
		if(username === contacts[i].contactUsername) {
			
			let option = document.createElement("option");
			option.value = contacts[i].contactBankAccountID;
			dataList.appendChild(option);
		}
	}
}

function updateUsernameHints(contacts) {
	
	let dataList = document.getElementById("contactUsernames");
	dataList.innerHTML = "";
	
	for(let i = 0; i < contacts.length; i++) {

        const contactUsername = contacts[i].contactUsername;

        if (dataList.options.namedItem(contactUsername) === null) {

            let option = document.createElement("option");
            option.value = contactUsername;
            option.id = contactUsername;
            dataList.appendChild(option);
        }
	}
}

function createTable(width, height, columnNames) {

    const table = document.createElement("table");
    for (let n = 0; n < height + 1; n++) {

        const tr = table.insertRow();
        for (let i = 0; i < width; i++) {

            let td;
            let span = document.createElement("span");

            if (n === 0) {

                td = tr.appendChild(document.createElement("th"));
                span.textContent = columnNames[i];
            }
            else {

                td = tr.insertCell();
                span.textContent = "Cell " + n + ", " + i;
            }

            td.appendChild(span);
        }
    }

    return table;
}
