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
	
	var ctx = document.getElementById("myChart");
    var myLineChart = new Chart(ctx, {
        type: 'line',
        data: {
            datasets:[{                    	
                label: 'Down',
                fill:false,
                pointBackgroundColor:'#0f0',
                data:[                              
                    <s:iterator value="datos" var="dato" status="incr">                            
                    {x:'2016-01-01 <s:property value="%{#dato[0].split(' ')[1]}"/>',y:<s:property value="#dato[1]" />},
                    </s:iterator>
                    ]
            },
            {                     
                label: 'Up',
                fill:false,
                pointBackgroundColor:'#f00',
                data:[                              
                    <s:iterator value="datos" var="dato" status="incr">
                    //{x:<s:property value="%{#incr.index}"/>,y:<s:property value="#dato[2]" />},   
                    {x:'2016-01-01 <s:property value="%{#dato[0].split(' ')[1]}"/>',y:<s:property value="#dato[2]" />},
                    </s:iterator>
                    ]
            }
            ]
        },
        options: {
            responsive: false,
            showLines: false,
            scales: {
                xAxes: [{
                    type: 'time', 
                    time: {displayFormats:{ hour:'HH:mm'}},
                }]
            }
        }
    });
    
});