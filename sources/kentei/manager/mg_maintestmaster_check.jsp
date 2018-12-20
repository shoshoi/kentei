<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setCharacterEncoding("UTF-8"); %>

<%
	//ユーザー情報を取得できない場合、セッションタイムアウト
	if(session.getAttribute("user") == null){
		request.setAttribute("msg","セッションがタイムアウトしました。");
%>
		<jsp:forward page="/" />
<%
	}
%>

<html>
<head>
<title>検定取得状況管理システム - 学生マスタメンテナンス</title>
	<link rel="stylesheet" href="style.css" type="text/css" />
	<script type="text/javascript" src="js/jquery-1.3.1.min.js"></script>	
	<script type="text/javascript" language="javascript" src="js/jquery.dropdownPlain.js"></script>
	<script type="text/javascript">
	<!--
		function rev(){
			var form = document.getElementById("form");
			form.action = "mg_maintestmaster_updaterev";
		}
	// -->
	</script>
</head>

<body>

	<div id="conteiner">
	<div id="header">
		<strong>ようこそ ${user.name} 様</strong>　<a href="./logout">ログアウト</a>
	</div>
	<h1>検定取得状況管理システム</h1>
	<ul class="dropdown">
        	<li><a href="mg_main">トップページ</a></li>
        	<li><a href="#">検索機能</a>
        		<ul class="sub_menu">
					<li><a href="mg_stsearchtest">学生→検定検索</a></li>
					<li><a href="mg_testsearchst">検定→学生検索</a></li>
        		</ul>
        	</li>
        	<li><a href="#">マスタメンテナンス</a>
        		<ul class="sub_menu">
        			<li><a href="mg_maintestmaster">学生マスタメンテナンス</a></li>
					<li><a href="mg_maintetestmaster">検定マスタメンテナンス</a></li>
					<li><a href="mg_mainteassocimaster">実施団体メンテナンス</a></li>
					<li><a href="mg_testdate">検定実施日メンテナンス</a></li>
        		</ul>
        	</li>
        	<li><a href="mg_mainteuser">パスワード変更</a></li>
    </ul>
    <div class="clearleft"></div> 
	<div id="main">
		<h2>学生マスタメンテナンス</h2>
		
			<c:if test="${!empty msg}">
			<div id="msg"><p>${msg}</p></div>
			</c:if>
		
		<p>これでよろしいですか？</p>
		<form id="form" method="POST" action="mg_maintestmaster_update">
		<table class="inputs">
		<tr><td>学籍番号</td><td>：${param.st_no}</td></tr>
		<tr><td>氏名</td><td>：${param.st_name}</td></tr>
		<tr><td>フリガナ</td><td>：${param.st_kana}</td></tr>
		<tr><td>学科名</td>
			<td>：<c:forEach var="item" items="${classlist}">
					<c:if test="${param.class == item.class_no}">
						学科名：${item.class_name}
					</c:if>
				</c:forEach>
			</td>
		</tr>
		<tr><td>学年</td><td>：${param.year}年</td></tr>
		<tr><td colspan="2">
				<c:if test="${param.pass == 'true'}">
				パスワードを初期化する
				<input type="hidden" name="pass" value="true">
				</c:if>
				<c:if test="${param.pass == 'false'}">
				パスワードを初期化しない
				<input type="hidden" name="pass" value="false">
				</c:if>
			</td>
		</tr>
		</table>
		<p><input type="submit" value="修正" onclick="rev()" /><input type="submit" value="送信" /></p>
		<input type="hidden" name="st_no" value="${param.st_no}">
		<input type="hidden" name="st_name" value="${param.st_name}">
		<input type="hidden" name="st_kana" value="${param.st_kana}">
		<input type="hidden" name="class_no" value="${param.class}">
		<input type="hidden" name="newclass_no" value="${param.newclass_no}">
		<input type="hidden" name="newclass_name" value="${param.newclass_name}">
		<input type="hidden" name="year" value="${param.year}">
		</form>
	</div>
</div>
</body>
</html>



