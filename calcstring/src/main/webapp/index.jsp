<%@ page import = "com.bass.calcstring.Main" language="java" contentType="text/html; charset=Cp1251" pageEncoding="Cp1251"%>

<jsp:useBean id="calc" class="com.bass.calcstring.Main" scope="session"/>
<jsp:setProperty name="calc" property="*"/>


<html xmlns="http://www.w3.org/1999/xhtml">
    <link rel="stylesheet" href="dist/themes/default/style.min.css" />
    <body>
        <head>
            <title> Calc string </title>
        </head>
	<% if (calc.getResult() == 0) { %>
		Please input numbers. Sum = <%= calc.getResult() %>
	<% } else {%>
		Sum of this numbers = <%= calc.getResult()%>
	<% } %>
	<form method=get>
		Input numbers here <input type=text name=sum>
	    <input type=submit value="Sum">
    </form>

    <script></script>

</body>
</html>
