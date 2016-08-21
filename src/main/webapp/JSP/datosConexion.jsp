<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Datos de conexión a BD</title>
</head>
<body>
    <s:form>
        <s:textfield key="datosConexion.mysqlserver"></s:textfield>
        <s:textfield key="datosConexion.mysqlport" type="number"></s:textfield>
        <s:textfield key="datosConexion.mysqluser" placeholder="sa"></s:textfield>
        <s:textfield key="datosConexion.mysqlpwd" type="password"></s:textfield>
        <s:submit></s:submit>
    </s:form>
</body>
</html>