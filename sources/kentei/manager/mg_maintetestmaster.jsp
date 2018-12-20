
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>検定取得状況管理システム - 検定マスタメンテナンス</title>
	<link rel="stylesheet" href="style.css" type="text/css" />
	<script type="text/javascript" src="js/jquery-1.3.1.min.js"></script>	
	<script type="text/javascript" language="javascript" src="js/jquery.dropdownPlain.js"></script>
	<script language="javascript" type="text/javascript" src="js/jquery.validate.js"></script>
	<script language="javascript" type="text/javascript" src="js/messages_ja.js"></script>
	<script type="text/javascript" language="javascript" src="js/etc.js"></script>
	<script type="text/javascript"><!--
		$(document).ready(function(){
			$("#form").validate({
                rules : {
                    remtest: {
                        required: true
                    }
                },
    		    errorLabelContainer: "#verror"

            });
		});
	// --></script>

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
		
		
		<p><a href="mg_maintetestmaster_get?newflg=1">・新規検定登録</a></p>
		<form method="get" action="mg_maintetestmaster">
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
		<input type="button" value="リセット" onClick="location.href='mg_maintetestmaster'" />
		</p>
		</form>
		
		<form name="limitno" method="get" action="mg_maintetestmaster_get">
		<p>検定番号：<input type="text" size="10" name="test_no">
		　<input type="submit" value="送信" /></p>
		</form>
		<p><div id="verror"></div></p>
		<c:if test="${!empty list}">
		<form method="post" action="mg_maintetestmaster_delete" id="form">
		<table border="3" class="hitable">
			<tr><th>削除</th><th>検定番号</th><th>検定名</th><th>実施団体</th></tr>
			<c:forEach var="item" items="${list}" varStatus="status">
				<tr><td><input type="radio" name="remtest" value="${item.test_no}" /></td><td><a href="mg_maintetestmaster_get?test_no=${item.test_no}">${item.test_no}</a></td><td>${item.test_name}</td><td>${item.associ_name}</td></tr>
			</c:forEach>
		</table>
		<p><input type="submit" value="削除" /></p>
		<p><div id="verror"></div></p>
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



