<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>検定取得状況管理システム - 取得検定参照</title>
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
		<li><a href="st_main">トップページ</a></li>
		<li><a href="st_gettest">検定取得状況参照</a></li>
		<li><a href="st_addtest">検定取得状況更新</a></li>
		<li><a href="st_mainteuser">パスワード変更</a></li>
	</ul>
    <div class="clearleft"></div> 
	<div id="main">
		<h2>検定取得状況更新</h2>
		
			<c:if test="${!empty msg}">
			<div id="msg"><p>${msg}</p></div>
			</c:if>
		
		<p>登録する検定を選択して下さい。</p>
		<form method="get" action="st_addtest">
		<p>実施団体：

		<select name="associ">
			<option value="0">指定なし</option>
			<c:forEach var="item" items="${associlist}">
			<c:if test="${limit_associ == item.associ_no}">
				<option value="${item.associ_no}" selected>${item.associ_name}</option>
			</c:if>
			<c:if test="${limit_associ != item.associ_no}">
				<option value="${item.associ_no}">${item.associ_name}</option>
			</c:if>
			</c:forEach>
		</select>
		
		<input type="submit" value="絞込み" />
		<input type="button" value="リセット" onClick="location.href='st_addtest'" />
		</p>
		</form>
		
		<form name="limitno" method="get" action="st_addtest_get">
		<p>検定番号：<input type="text" size="10" name="test_no" />
		　<input type="submit" value="送信" /></p>
		</form>
		
		<c:if test="${!empty list}">
		<form method="post" action="mg_maintetestmaster_delete" id="form">
		<table border="3" class="hitable">
			<tr><th>検定番号</th><th>検定名</th><th>実施団体</th><th>取得済</th></tr>
			<c:forEach var="item" items="${list}" varStatus="status">
				<tr><td><a href="st_addtest_get?test_no=${item.test_no}">${item.test_no}</a></td><td>${item.test_name}</td><td>${item.associ_name}</td>
				<td align="center">

				<c:forEach var="item2" items="${glist}">
					<c:if test="${item.test_no == item2}">○</c:if>
				</c:forEach></td>
				
				</tr>
			</c:forEach>
			</table>
		</form>
		</c:if>
		<c:if test="${empty list}">
		<p>該当するレコードがありません。</p>
		</c:if>
		
	</div>
</div>
</body>
</html>



