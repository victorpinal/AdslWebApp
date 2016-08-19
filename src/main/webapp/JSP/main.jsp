<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<s:head />
<link rel="stylesheet" href="/CSS/main.css" type="text/css" />
<script src="https://code.jquery.com/jquery-3.1.0.min.js" type="text/javascript"></script>
<script src="/JS/main.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.2.1/Chart.bundle.min.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="refresh" content="60">
<title><s:text name="titulo_principal" /></title>
</head>
<body>
    <div class="wrapper">
        <div class="left-side">
            <s:form id="FRM_ips">
                <s:select key="idSelected" list="datosIp" listKey="id" onchange="$('#FRM_ips').submit();" />
                <s:submit />
            </s:form>
            <s:textarea name="resumen"></s:textarea>
        </div>
        <div class="right-side">
        <canvas id="myChart" width="1200" height="800"></canvas>
        <script>
            var ctx = document.getElementById("myChart");
            var myLineChart = new Chart(ctx, {
                type: 'line',
                data: {
                    datasets:[{                    	
                        label: 'Down',
                        fill:false,
                        data:[                              
                            <s:iterator value="datos" var="dato" status="incr">                            
                            //{x:<s:property value="%{#incr.index}"/>,y:<s:property value="#dato[1]" />},                                
                            {x:'<s:property value="#dato[0]"/>',y:<s:property value="#dato[1]" />},
                            </s:iterator>
                            ]
                    },
                    /* {                     
                        label: 'Up',
                        fill:false,
                        data:[                              
                            <s:iterator value="datos" var="dato" status="incr">
                            {x:<s:property value="%{#incr.index}"/>,y:<s:property value="#dato[2]" />},                                
                            </s:iterator>
                            ]
                    } */
                    ]
                },
                options: {
                    responsive: false,
                    showLines: false,
                    scales: {
                        xAxes: [{
                            type: 'time', 
                            displayFormats: {
                                minute: 'HH:mm'
                            }
                        }]
                    }
                }
            });
        </script>
            <table border="1">
                <tr>
                    <th>Hora</th><th>Down</th><th>Up</th><th>Att.Down</th><th>Att.Up</th>
                </tr>
                <s:iterator value="datos" var="dato">                
                    <tr>
                        <td class="tdLabel"><s:property value="#dato[0]" /></td>
                        <td class="tdLabel"><s:property value="#dato[1]" /></td>
                        <td class="tdLabel"><s:property value="#dato[2]" /></td>
                        <td class="tdLabel"><s:property value="#dato[3]" /></td>
                        <td class="tdLabel"><s:property value="#dato[4]" /></td>
                    </tr>
                </s:iterator>
            </table>            
        </div>
    </div>
    <div class="footer">
        <a href="<s:url action="index" namespace="config-browser" />">Launch the configuration browser</a>
    </div>
</body>
</html>