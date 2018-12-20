
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setCharacterEncoding("UTF-8"); %>

<html>
<head>
<title>検定取得状況管理システム - 検定取得状況更新</title>
	<link rel="stylesheet" href="style.css" type="text/css" />
	<script type="text/javascript" src="js/jquery-1.3.1.min.js"></script>	
	<script type="text/javascript" language="javascript" src="js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" language="javascript" src="js/etc.js"></script>
	<script language="javascript" type="text/javascript" src="js/jquery.validate.js"></script>
	<script language="javascript" type="text/javascript" src="js/messages_ja.js"></script>
	<script type="text/javascript"><!--
		$(document).ready(function(){
			$("#form").validate({
                rules : {
                    test_perform_date: {
                        required: true
                    }
                },
    		    errorLabelContainer: "#verror"

            });
		});
	// --></script>
</head>

<body>

	<div id="conteiner">
	<div id="header">
		<strong>ようこそ ${user.name} 様</strong>　<a href="./logout">ログアウト</a>
	</div>
	<h1>検定取得状況管理システム</h1>
	<ul class="dropdown">
		<li><a href="st_main">トップページ</a></li>
		<li><a href="st_gettest">検定取得状況参照</a></li>
		<li><a href="st_addtest">検定取得状況更新</a></li>
		<li><a href="st_mainteuser">パスワード変更</a></li>
	</ul>
    <div class="clearleft"></div> 
	<div id="main">
		<h2>検定取得状況更新</h2>
		
			<c:if test="${!empty msg}">
			<div id="msg"><p>${msg}</p></div>
			</c:if>
		
		
		<c:if test="${empty msg}">


		<c:if test="${!empty datelist}">
		<p>実施日を選択してください。</p>
		
		<form method="POST" id="form" action="st_addtest_add">
			<input type="hidden" name="test_no" value="${param.test_no}">
			<table border="3" class="hitable">
				<tr><th>　</th><th>実施日</th><th>取得日</th></tr>
				<c:forEach var="item" items="${datelist}" varStatus="status">
				<tr><td><input type="radio" name="test_perform_date" value="${item.test_perform_date}"></td><td>${item.test_perform_date}</td><td>${item.test_get_date}</td>
				</c:forEach>
			</table>
		<p><input type="submit" value="登録" /><div id="verror"><p></p></div></p>
		</form>
		</c:if>
		
		<c:if test="${empty datelist}">
			<p>実施日が登録されていません。</p>
		</c:if>
	</c:if>
		<p><a href="st_addtest">戻る</a></p>
	</div>
</div>
</body>
</html>



