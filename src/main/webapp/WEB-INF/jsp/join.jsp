<%--
  Created by IntelliJ IDEA.
  User: kys0522
  Date: 2020-12-28
  Time: 오후 4:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hello World</title>

    <link href="css/main.css" rel="stylesheet" >

    <%--
    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet" />
    <link rel="stylesheet" href="/WEB-INF/css/main.css">
    --%>
    <script src="https://kit.fontawesome.com/51db22a717.js" crossorigin="anonymous"></script>
</head>

<body>
<div class="main-container">
    <div class="main-wrap">
        <header>
            <div class="logo-wrap">
                <H1>
                    HELLO WORLD
                </H1>
            </div>
        </header>
        <form action="/join/create" method="POST">
            <section class="login-input-section-wrap">
                <div class="login-input-wrap">
                    <input placeholder="Id" type="text" id="userid" name="userid"></input>
                </div>
                <div class="login-input-wrap password-wrap">
                    <input placeholder="Password" type="password" id="pw" name="pw"></input>
                </div>
                <div class="login-input-wrap">
                    <input placeholder="Name" type="text" id="name" name="name"></input>
                </div>
                <div class="login-input-wrap">
                    <input placeholder="Role" type="text" id="role" name="role"></input>
                </div>
                <div class="login-button-wrap">
                    <button type="submit">Register</button>
                </div>
            </section>
        </form>
        <footer>
            <div class="copyright-wrap">
                <span>	Copyright © MIDASIT. All Rights Reserved.</span>
            </div>
        </footer>
    </div>
</div>
</body>
</html>