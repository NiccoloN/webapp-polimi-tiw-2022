(function() {
    document.getElementById("loginForm").addEventListener("submit", (e) => {

        e.preventDefault();
        const form = e.target;
        if (form.checkValidity()) {

            makeCall("POST", "CheckLogin", form, function (request) {

                if(request.readyState === XMLHttpRequest.DONE) {

                    const message = request.responseText;
                    switch (request.status) {

                        case 200:
                            sessionStorage.setItem("user", message);
                            window.location.href = "HomePage.html";
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