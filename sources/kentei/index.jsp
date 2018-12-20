
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>検定取得状況管理システム - ログイン</title>
	<link rel="stylesheet" href="style.css" type="text/css" />
</head>
<body>
<div id="login">
<h2>検定取得状況管理システム - ログイン</h2>

<form method="POST" action="./login">
	<table class="inputs" style="height:150px;">
		<tr><td>ユーザID</td><td>：<input type="text" name="id" size="25" maxlength="20" style="ime-mode: disabled;"/></td></tr>
		<tr><td>パスワード</td><td>：<input type="password" name="pass" size="20" maxlength="20" style="ime-mode: disabled;"/></td></tr>
		<tr><td colspan="2"><input type="submit" value="ログイン" />　<input type="reset" value="リセット" /></td></tr>
	</table>

</form>

<%-- メッセージ表示  --%>
<c:if test="${!empty msg}">
	<p><font color="red" >${msg}</font></p>
</c:if>
</div>
</body>
</html>



