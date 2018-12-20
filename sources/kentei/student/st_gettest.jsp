
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>検定取得状況管理システム - 取得検定参照</title>
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
                    remtest: {
                        required: true
                    }
                },
    		    errorLabelContainer: "#verror"

            });
		});
	// --></script>
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
		<h2>取得検定参照</h2>
		
			<c:if test="${!empty msg}">
			<div id="msg"><p>${msg}</p></div>
			</c:if>
		
		<c:if test="${!empty list}">
		
		<p>${user.name}さんが取得している検定は以下の通りです。</p>
		<form method="post" action="st_remtest" id="form">
		<table border="3" class="hitable">
			<tr><th>削除</th><th>検定名</th><th>実施団体</th><th>実施日</th><th>取得日</th></tr>
			<c:forEach var="item" items="${list}" varStatus="status">
				<tr><td><input type="radio" name="remtest" value="${status.index}" /></td><td>${item.test_name}</td><td>${item.association}</td><td>${item.test_perform_date}</td><td>${item.test_get_date}</td></tr>
			</c:forEach>
			</table>
		<p><input type="submit" value="削除" /><div id="verror"><p></p></div></p>
		</form>
		</c:if>
		<%-- 取得検定が登録されていないとき  --%>
		<c:if test="${empty list}">
		<p>検定が登録されていません。</p>
		</c:if>
	</div>
</div>
</body>
</html>



