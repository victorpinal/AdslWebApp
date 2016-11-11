<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet" href="CSS/main.css" />
    <script src="https://code.jquery.com/jquery-3.1.0.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.2.1/Chart.bundle.min.js"></script>
    <!--<script src="JS/main.js"></script>-->
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <!-- meta http-equiv="refresh" content="60"-->
    <title><s:text name="titulo_principal" /></title>
    <s:head />
</head>

<body>
    <section>
        <div id="left_side">
            <s:form id="FRM_ips" theme="simple">
                <div id="search_bar">
                    <s:select key="idSelected" list="datosIp" listKey="id" onchange="$('#FRM_ips').submit();" />
                    <s:submit></s:submit>
                </div>
                <s:textfield type="date" key="fechaIni"></s:textfield>
                <s:textfield type="date" key="fechaFin"></s:textfield>
            </s:form>
            <textarea>
                <s:property value="resumen"></s:property>
            </textarea>
            <div id="table">
            <table border=1>
                <tr>
                    <th>Hora</th>
                    <th>Down</th>
                    <th>Up</th>
                    <th>Att.Down</th>
                    <th>Att.Up</th>
                </tr>
                <s:iterator value="datos" var="dato">
                    <tr>
                        <td><s:property value="#dato[0]" /></td>
                        <td><s:property value="#dato[1]" /></td>
                        <td><s:property value="#dato[2]" /></td>
                        <td><s:property value="#dato[3]" /></td>
                        <td><s:property value="#dato[4]" /></td>
                    </tr>
                </s:iterator>
            </table>
            </div>
        </div>
        <div id="right_side">
            <canvas id="myChart" width="320" height="240"></canvas>
        </div>
    </section>
    <footer>
        <a href="<s:url action='index' namespace='config-browser'/>">Launch the configuration browser</a>
    </footer>
    <script>
    $(document).ready(function() {
        
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
    </script>
</body>

</html>