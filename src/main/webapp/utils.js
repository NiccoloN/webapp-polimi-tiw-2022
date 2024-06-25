/**
 * AJAX call management
 */
function makeCall(method, url, formElement, cback, reset = true) {

	makeCallFormData(method, url, formElement === null ? null : new FormData(formElement), cback)

	if (formElement !== null && reset === true) formElement.reset();
}

function makeCallFormData(method, url, formData, cback) {

	const req = new XMLHttpRequest(); // visible by closure
	req.onreadystatechange = function() {

		cback(req)
	}; // closure

	req.open(method, url);

	if (formData == null) req.send();
	else req.send(formData);
}