<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<s:head />
<link rel="stylesheet" href="/CSS/main.css" type="text/css" />
<script src="https://code.jquery.com/jquery-3.1.0.min.js" type="text/javascript"></script>
<script src="/JS/main.js" type="text/javascript"></script>
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
            <table border="1">
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