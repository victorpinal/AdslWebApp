<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<s:head/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<table border="1">
<s:iterator value="datos" var="dato">
<tr>
<td class="tdLabel"><s:property value="#dato[0]"/></td>
<td class="tdLabel"><s:property value="#dato[1]"/></td>
<td class="tdLabel"><s:property value="#dato[2]"/></td>
<td class="tdLabel"><s:property value="#dato[3]"/></td>
<td class="tdLabel"><s:property value="#dato[4]"/></td>
</tr>
</s:iterator>
</table>
</body>
</html>