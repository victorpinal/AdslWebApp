/**
 * 
 */
function loadResumen() {
	$.ajax({
		url : "index_getResumen",
		type : "POST",
		data : {
			idSelected : $('#selectIp').val()
		},
		dataType : "json",
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert('Error ' + textStatus);
			alert(errorThrown);
			alert(XMLHttpRequest.responseText);
		},
		success : function(data) {
			$("#resumen").html(data);
		}
	});
};
/*********/
function loadDatos() {
	$("#datos").attr(
			"src",
			"<s:url action='index_getDatos'></s:url>" + "?idSelected="
					+ $('#selectIp').val())
};
/********/
$(document).ready(function() {
	//loadResumen();
	//loadDatos();
});