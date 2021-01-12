<%--
  Created by IntelliJ IDEA.
  User: kys0522
  Date: 2020-12-28
  Time: 오후 4:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
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

        <section class="login-input-section-wrap">
            <p> Welcome!! ${name}</p>
        </section>

        <section class="login-input-section-wrap">
            <a href="/mod">정보변경</a>
            <sec:authorize access="hasRole('ADMIN')">
            <br>
            <a href="/admin">관리자</a>
            </sec:authorize>
            <form action="/sign/logout" method="POST">
                <div class="login-button-wrap">
                    <button type="submit">Log Out</button>
                </div>
            </form>
        </section>
        <footer>
            <div class="copyright-wrap">
                <span>	Copyright © MIDASIT. All Rights Reserved.</span>
            </div>
        </footer>
    </div>
</div>
</body>
</html>