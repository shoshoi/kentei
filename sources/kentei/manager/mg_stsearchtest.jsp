
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
		
		
		${class}
		<form method="get" action="mg_stsearchtest">
		<p>学科：
		<select name="class">
			<option value="0">指定なし</option>
			<c:forEach var="item" items="${classlist}">
			<c:if test="${limit_class == item.class_no}">
				<option value="${item.class_no}" selected>${item.class_name}</option>
			</c:if>
			<c:if test="${limit_class != item.class_no}">
				<option value="${item.class_no}">${item.class_name}</option>
			</c:if>
			</c:forEach>
		</select>
		　学年：
		<select name="year">
			<option value="0">指定なし</option>
			<c:forEach begin="1" end="4" step="1" varStatus="status">
			<c:if test="${limit_year == status.index}">
				<option value="${status.index}" selected>${status.index}年</option>
			</c:if>
			<c:if test="${limit_year != status.index}">
				<option value="${status.index}">${status.index}年</option>
			</c:if>
			</c:forEach>
		</select>
		<input type="submit" value="絞込み" />
		<input type="button" value="リセット" onClick="location.href='mg_stsearchtest'" />
		</p>
		</form>
		
		<form name="limitno" method="get" action="mg_stsearchtest_get">
		<p>学籍番号：<input type="text" size="10" name="st_no">
		　<input type="submit" value="送信" /></p>
		</form>
		
		<c:if test="${!empty list}">
		<form method="get" action="st_remtest">
		<table border="3" class="hitable">
			<tr><th>学籍番号</th><th>氏名</th><th>学科</th><th>学年</th></tr>
			<c:forEach var="item" items="${list}" varStatus="status">
				<tr><td><a href="mg_stsearchtest_get?st_no=${item.st_no}">${item.st_no}</a></td><td>${item.st_name}</td><td>${item.class_name}</td><td>${item.year}</td></tr>
			</c:forEach>
			</table>
		</form>
		</c:if>
		<%-- 取得検定が登録されていないとき  --%>
		<c:if test="${empty list}">
		<p>該当するレコードがありません。</p>
		</c:if>
	</div>
</div>
</body>
</html>



