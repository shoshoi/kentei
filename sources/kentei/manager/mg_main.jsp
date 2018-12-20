

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
		<h2>トップページ</h2>
		<div id="sitemap">
		<h3>サイトマップ</h3>
		<dl>
		<dt><a href="mg_stsearchtest">学生→検定検索</a></dt><dd>選択した学生が取得している検定の一覧を表示します。</dd>
		<dt><a href="mg_testsearchst">検定→学生検索</a></dt><dd>選択した検定を取得している学生の一覧を表示します。</dd>
		<dt><a href="mg_maintestmaster">学生マスタメンテナンス</a></dt><dd>学生情報を更新します。</dd>
		<dt><a href="mg_maintetestmaster">検定マスタメンテナンス</a></dt><dd>検定情報を登録、更新、削除します。</dd>
		<dt><a href="mg_mainteassocimaster">実施団体メンテナンス</a></dt><dd>実施団体情報を登録、削除します。</dd>
		<dt><a href="mg_testdate">検定実施日メンテナンス</a></dt><dd>検定の実施日を登録、削除します。</dd>
		<dt><a href="mg_mainteuser">パスワード変更</a></dt><dd>パスワードの変更を行います。</dd>
		</dl>
		</div>
	</div>
</div>
</body>
</html>



