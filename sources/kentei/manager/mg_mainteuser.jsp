
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
			$("#form").validate({
                rules : {
                    now_pass: {
                        required: true,
                        rangelength: [8,16]
                    },
                    new_pass: {
                        required: true,
                        rangelength: [8,16]
                    },
                    new_pass2: {
                        required: true,
                        equalTo: "#cnew_pass"
                    }
                }
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
		<h2>パスワード変更</h2>
		
			<c:if test="${!empty msg}">
			<div id="msg"><p>${msg}</p></div>
			</c:if>
		

		
		<form method="POST" id="form" action="mainteuser_update">
			<table class="inputs">
			<tr><td>現在のパスワード</td><td>：<input type="password" name="now_pass" id="cnow_pass" style="ime-mode:disabled;" /></td></tr>
			<tr><td>新規パスワード（8～16桁の英数字）</td><td>：<input type="password" name="new_pass" id="cnew_pass" style="ime-mode:disabled;" /></td></tr>
			<tr><td>新規パスワード（確認）</td><td>：<input type="password" name="new_pass2" id="cnew_pass2" style="ime-mode:disabled;" /></td></tr>
			<tr><td colspan="2"><input type="submit" value="送信" />　<input type="reset" value="リセット" /></td></tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>



