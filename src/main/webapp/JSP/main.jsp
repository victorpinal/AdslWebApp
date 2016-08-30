<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="/CSS/main.css" />
<script src="https://code.jquery.com/jquery-3.1.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.2.1/Chart.bundle.min.js"></script>
<script src="/JS/main.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- meta http-equiv="refresh" content="60"-->
<title><s:text name="titulo_principal" /></title>
<s:head />
</head>
<body>
    <section>
        <div id="left-side">
            <div id="form">
                <s:form id="FRM_ips" theme="simple">
                    <s:select key="idSelected" list="datosIp" listKey="id" onchange="$('#FRM_ips').submit();" />
                    <s:submit></s:submit>                               
                    <s:textfield type="date" key="fechaIni"></s:textfield>                                
                    <s:textfield type="date" key="fechaFin"></s:textfield>                                     
                </s:form>
            </div>            
            <textarea><s:property value="resumen"></s:property></textarea>    
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
        <div id="right-side">
            <canvas id="myChart" width="800" height="600"></canvas>
        </div>
    </section>
    <footer>
        <a href="<s:url action="index" namespace="config-browser" />">Launch the configuration browser</a>
    </footer>
</body>
</html>