
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setCharacterEncoding("UTF-8"); %>

<html>
<head>
<title>検定取得状況管理システム - 検定マスタメンテナンス</title>
	<link rel="stylesheet" href="style.css" type="text/css" />
	<script language="javascript" type="text/javascript" src="js/jquery-1.3.1.min.js"></script>	
	<script language="javascript" type="text/javascript" src="js/jquery.dropdownPlain.js"></script>
	<script language="javascript" type="text/javascript" src="js/jquery.validate.js"></script>
	<script language="javascript" type="text/javascript" src="js/messages_ja.js"></script>
	<script type="text/javascript"><!--
		$(document).ready(function(){
			$("#form").validate();
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
		<h2>実施団体メンテナンス</h2>
		
			<c:if test="${!empty msg}">
			<div id="msg"><p>${msg}</p></div>
			</c:if>
		
		<form method="POST" action="mg_mainteassocimaster_delete">
			<select name="associ_no" size="1">
			<c:forEach var="item" items="${associlist}" varStatus="status">
					<option value="${item.associ_no}">${item.associ_no} : ${item.associ_name}</option>
			</c:forEach>
			</select>
			<input type="hidden" name="flag" value="delete">
		<input type="submit" value="削除" />
		</form>
		
		<form method="POST" id="form" action="mg_mainteassocimaster_update">
			<table class="inputs">
			<tr><td>団体番号（必須，8桁）</td><td>：<input type="text" name="associ_no" id="cassoci_noo" class="required number" minlength="8" maxlength="8" style="ime-mode: disabled;"/></td></tr>
			<tr><td>団体名（必須）</td><td>：<input type="text" name="associ_name" id="cassoci_name" class="required" maxlength="40" /></td></tr>
			<tr><td>フリガナ（必須）</td><td>：<input type="text" name="associ_kana" id="cassoci_kana" class="required" maxlength="80" /></td></tr>
			<tr><td colspan="2"><input type="submit" value="登録" />　<input type="reset" value="リセット" /></td></tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>



