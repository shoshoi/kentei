
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>検定取得状況管理システム - 学生→検定検索</title>
	<link rel="stylesheet" href="style.css" type="text/css" />
	<script type="text/javascript" src="js/jquery-1.3.1.min.js"></script>	
	<script type="text/javascript" language="javascript" src="js/jquery.dropdownPlain.js"></script>
	<script type="text/javascript" language="javascript" src="js/etc.js"></script>
</head>
<body>
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
	<h2>学生→検定検索</h2>
	
		<c:if test="${!empty msg}">
			<div id="msg"><p>${msg}</p></div>
		</c:if>
			
		<c:if test="${!empty list}">
		
		<p>${st_name}さんが取得している検定は以下の通りです。</p>

		<form method="post" action="st_remtest">
		<table border="3" class="hitable">
			<tr><th>検定名</th><th>実施団体</th><th>実施日</th><th>取得日</th></tr>
			<c:forEach var="item" items="${list}" varStatus="status">
				<tr><td>${item.test_name}</td><td>${item.association}</td><td>${item.test_perform_date}</td><td>${item.test_get_date}</td></tr>
			</c:forEach>
			</table>
		
		</form>
		</c:if>
		<%-- 取得検定が登録されていないとき  --%>
		<c:if test="${empty list}">
			<c:if test="${!empty st_name}">
				<p>${st_name}さんは検定を取得していません。</p>
			</c:if>
			<c:if test="${empty st_name}">
				<p>存在しない学籍番号です。</p>
			</c:if>
		</c:if>
		<p><a href="./mg_stsearchtest">戻る</a></p>
</body>
</html>



