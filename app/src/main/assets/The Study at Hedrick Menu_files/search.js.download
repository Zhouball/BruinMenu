//DOM loaded
$(document).ready(function() {
	//"Search Site" remove after first focus
	$("#searchbox-input").focus(function() {
		if($("#searchbox-input").val() == "Search Site") { //DEFAULT SEARCH BOX VALUE IS 'SEARCH SITE'
			$("#searchbox-input").val(""); //CLEAR THE ORIGINAL VALUE
		} 
	});
	$("#searchbox-input").blur(function() {
		if($("#searchbox-input").val() == "") { //DEFAULT SEARCH BOX VALUE IS EMPTY
			$("#searchbox-input").val("Search Site"); //ADD DEFAULT TEXT TO SEARCH BOX
		} 
	});			
});
