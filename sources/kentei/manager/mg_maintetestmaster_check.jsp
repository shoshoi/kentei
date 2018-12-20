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
<title>検定取得状況管理システム - 検定マスタメンテナンス</title>
	<link rel="stylesheet" href="style.css" type="text/css" />
	<script type="text/javascript" src="js/jquery-1.3.1.min.js"></script>	
	<script type="text/javascript" language="javascript" src="js/jquery.dropdownPlain.js"></script>
	<script type="text/javascript">
	<!--
		function rev(){
			var form = document.getElementById("form");
			form.action = "mg_maintetestmaster_updaterev";
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
		<h2>検定マスタメンテナンス</h2>
		
			<c:if test="${!empty msg}">
			<div id="msg"><p>${msg}</p></div>
			</c:if>
		
		<p>これでよろしいですか？</p>
		<form method="POST" action="mg_maintetestmaster_update" id="form">
		<table class="inputs">
		<tr><td>検定番号</td><td>：${param.test_no}</td></tr>
		<tr><td>検定名</td><td>：${param.test_name}</td></tr>
		<tr><td>実施団体</td>
			<td>：
				<c:forEach var="item" items="${associlist}">
					<c:if test="${param.associ_no == item.associ_no}">
						${item.associ_name}
					</c:if>
				</c:forEach>
			</td>
		</tr>

		</table>

		<p><input type="submit" value="修正"  onclick="rev()"/><input type="submit" value="送信" /></p>
		
		<input type="hidden" name="test_no" value="${param.test_no}">
		<input type="hidden" name="test_name" value="${param.test_name}">
		<input type="hidden" name="associ_no" value="${param.associ_no}">
		<input type="hidden" name="newassoci_no" value="${param.newassoci_no}">
		<input type="hidden" name="newassoci_name" value="${param.newassoci_name}">
		<input type="hidden" name="newassoci_kana" value="${param.newassoci_kana}">
		<input type="hidden" name="insertflg" value="${param.insertflg}">
		</form>
	</div>
</div>
</body>
</html>



