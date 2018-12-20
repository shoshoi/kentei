
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>検定取得状況管理システム - 検定→学生検索</title>
	<link rel="stylesheet" href="style.css" type="text/css" />
	<script type="text/javascript" src="js/jquery-1.3.1.min.js"></script>	
	<script type="text/javascript" language="javascript" src="js/jquery.dropdownPlain.js"></script>
	<script type="text/javascript" language="javascript" src="js/etc.js"></script>
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
		<h2>検定→学生検索</h2>
		
			<c:if test="${!empty msg}">
			<div id="msg"><p>${msg}</p></div>
			</c:if>
		
		
		<form method="get" action="mg_testsearchst_get">
		<p>実施日：
		<select name="date">
			<option value="0">指定なし</option>
			<c:forEach var="item" items="${datelist}">
			<c:if test="${limit_date == item.test_perform_date}">
				<option value="${item.test_perform_date}" selected>${item.test_perform_date}</option>
			</c:if>
			<c:if test="${limit_date != item.test_perform_date}">
				<option value="${item.test_perform_date}">${item.test_perform_date}</option>
			</c:if>
			</c:forEach>
		</select>
		<input type="hidden" name="test_no" value="${test_no}">
		<input type="submit" value="絞込み" />
		<input type="button" value="リセット" onClick="location.href='mg_testsearchst_get?test_no=${test_no}'" />
		</p>
		</form>
		<c:if test="${!empty list}">
		
		<p><c:if test="${param.date != null && param.date != 0}"><strong>${param.date}</strong>実施の</c:if><strong>${test_name}</strong>を取得した学生は以下の通りです。</p>
		
		<form method="post" action="st_remtest">
		<table border="3" class="hitable">
			<tr><th>学籍番号</th><th>氏名</th><th>学科</th><th>学年</th><th>実施日</th><th>取得日</th></tr>
			<c:forEach var="item" items="${list}" varStatus="status">
				<tr><td>${item.st_no}</td><td>${item.st_name}</td><td>${item.class_name}</td><td>${item.year}</td><td>${item.test_perform_date}</td><td>${item.test_get_date}</td></tr>
			</c:forEach>
			</table>
		</form>
		</c:if>
		<%-- 取得検定が登録されていないとき  --%>
		<c:if test="${empty list}">
			<c:if test="${!empty test_name}">
				<p><c:if test="${param.date != null && param.date != 0}"><strong>${param.date}</strong>実施の</c:if><strong>${test_name}</strong>を取得した学生はいません。</p>
			</c:if>
			<c:if test="${empty test_name}">
				<p>存在しない検定番号です。</p>
			</c:if>
		</c:if>
		<p><a href="./mg_testsearchst">戻る</a></p>
	</div>
</body>
</html>



