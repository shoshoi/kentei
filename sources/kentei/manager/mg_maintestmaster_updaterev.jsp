
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setCharacterEncoding("UTF-8"); %>

<html>
<head>
<title>検定取得状況管理システム - 学生マスタメンテナンス</title>
	<link rel="stylesheet" href="style.css" type="text/css" />
	<script language="javascript" type="text/javascript" src="js/jquery-1.3.1.min.js"></script>	
	<script language="javascript" type="text/javascript" src="js/jquery.dropdownPlain.js"></script>
	<script language="javascript" type="text/javascript" src="js/jquery.validate.js"></script>
	<script language="javascript" type="text/javascript" src="js/messages_ja.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#form").validate();
			$("#form").pass.value=$("#form").passselect.checked ? 'true' : 'false'
		});
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
		<h2>学生マスタメンテナンス</h2>
		
			<c:if test="${!empty msg}">
			<div id="msg"><p>${msg}</p></div>
			</c:if>
		
		<c:if test="${st_data.st_no != null}">
		
		
		<form method="POST" action="mg_maintestmaster_check" id="form">
		<table class="inputs">
			<tr><td>学籍番号</td><td>：<input type="text" value="${param.st_no}" disabled></td></tr>
			<tr><td>氏名（必須）</td><td>：<input type="text"  value="${st_data.st_name}" disabled/></td></tr>
			<tr><td>フリガナ（必須）</td><td>：<input type="text" name="st_kana" id="cst_kana" value="${param.st_kana}" class="required" maxlength="40" ></td></tr>
			<tr><td>学科</td><td>：
				<select name="class">
			<c:forEach var="item" items="${classlist}">
			<c:if test="${param.class_no == item.class_no}">
				<option value="${item.class_no}" selected>${item.class_name}</option>
			</c:if>
			<c:if test="${param.class_no != item.class_no}">
				<option value="${item.class_no}">${item.class_name}</option>
			</c:if>
			</c:forEach>
		</select></td></tr>
			<tr><td>学年</td><td>：
			<select name="year">
			<c:forEach begin="1" end="4" step="1" varStatus="status">
			<c:if test="${param.year == status.index}">
				<option value="${status.index}" selected>${status.index}年</option>
			</c:if>
			<c:if test="${param.year != status.index}">
				<option value="${status.index}">${status.index}年</option>
			</c:if>
			</c:forEach>
		</select></td></tr>
			<tr><td colspan="2">

			<c:if test="${param.pass == 'false'}">
			<input type="checkbox" name="passselect" onclick="this.form.pass.value=this.checked ? 'true' : 'false'" />パスワードを初期化する
			</c:if>
			<c:if test="${param.pass == 'true'}">
			<input type="checkbox" name="passselect" onclick="this.form.pass.value=this.checked ? 'true' : 'false'" checked />パスワードを初期化する
			</c:if>
		
			</td></tr>
			<tr><td colspan="2"><input type="submit" value="送信" /></td></tr>
		</table>
		<input type="hidden" name="st_no" value="${param.st_no}">
		<input type="hidden" name="st_name" id="cst_name" value="${st_data.st_name}" />
		<input type="hidden" name="pass" value="false">
		</form>

		</c:if>
		<c:if test="${param.st_no == null}">
			<p>存在しない学籍番号です。</p>
		</c:if>
		<a href="./mg_maintestmaster">戻る</a>
	</div>
</div>
</body>
</html>



