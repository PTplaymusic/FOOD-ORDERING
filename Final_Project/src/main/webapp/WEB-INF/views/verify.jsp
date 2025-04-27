<%-- 
    Document   : verify-google
    Created on : Apr 27, 2025, 12:39:31 AM
    Author     : DELL
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Verify Google Account</title>
</head>
<body>
    <h2>Enter Verification Code</h2>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <form action="verify" method="POST">
        <input type="text" name="code" placeholder="Enter 6-digit code" required/>
        <button type="submit">Verify</button>
    </form>
</body>
</html>
