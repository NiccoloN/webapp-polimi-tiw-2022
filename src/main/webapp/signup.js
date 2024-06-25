(function() {
	
	document.getElementById("email").addEventListener("keyup", () => { mailValidity() }, false);
    document.getElementById("repeatPassword").addEventListener("keyup", () => { repeatPasswordValidity() }, false);
    
    document.getElementById("signUpForm").addEventListener("submit", (e) => {

        e.preventDefault();
        const form = e.target;
        if (form.checkValidity()) {

            makeCall("POST", "CheckSignup", form, function (request) {

                if(request.readyState === XMLHttpRequest.DONE) {

                    const message = request.responseText;
                    switch (request.status) {

                        case 200:
                            window.location.href = "LoginPage.html";
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
    }, false)
}) ();

function mailValidity() {
	
	let mail = document.getElementById("email");
	let errorMessage = document.getElementById("errorMessage");
	
	let re = /\S+@\S+\.\S+/;
	
	if(re.test(mail.value)) errorMessage.textContent = "";
	else errorMessage.textContent = "Mail format is not valid.";
}

function repeatPasswordValidity() {
	
	let password = document.getElementById("password");
	let repeatPassword = document.getElementById("repeatPassword");
	
	let errorMessage = document.getElementById("errorMessage");
	
	if(repeatPassword.value !== password.value) errorMessage.textContent = "Password and repeat password are not equals.";
	else errorMessage.textContent = "";
}