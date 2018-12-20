<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
	<title>検定取得状況管理システム - メインメニュー</title>
	<link rel="stylesheet" href="style.css" type="text/css" />
		<script type="text/javascript" src="js/jquery-1.3.1.min.js"></script>	
	<script type="text/javascript" language="javascript" src="js/jquery.dropdownPlain.js"></script>
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
		<h2>トップページ</h2>
		<div id="sitemap">
		<h3>サイトマップ</h3>
		<dl>
		<dt><a href="st_gettest">検定取得状況参照</a></dt><dd>取得している検定の一覧を表示します。</dd>
		<dt><a href="st_addtest">検定取得状況更新</a></dt><dd>新たに取得した検定を追加します。</dd>
		<dt><a href="st_mainteuser">パスワード変更</a></dt><dd>パスワードの変更を行います。</dd>
		</dl>
		</div>
	</div>
</div>
</body>
</html>



