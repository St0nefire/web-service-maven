
console.log("ERROR MESSAGE: " + errorMessage);
if(errorMessage) {
	var alertElmt = jQ("#alert_error").get(0);
	var errElmt = jQ("#error_message").get(0);
	errElmt.innerHTML = errorMessage;
	alertElmt.style.display = "block";
}