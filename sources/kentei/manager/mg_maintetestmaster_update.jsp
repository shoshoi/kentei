
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
		<h2>検定マスタメンテナンス</h2>
		
			<c:if test="${!empty msg}">
			<div id="msg"><p>${msg}</p></div>
			</c:if>
		
		<form method="POST" id="form" action="mg_maintetestmaster_check">
		<table class="inputs">
		<c:if test="${test_data.test_no != null || newflg != null }">
			<c:if test="${test_data.test_no != null}">
				<tr><td>検定番号</td><td>：<input type="text" value="${test_data.test_no}" disabled></td></tr>
				<input type="hidden" name="test_no" value="${test_data.test_no}">
				<input type="hidden" name="insertflg" value="false">
			</c:if>
			<c:if test="${test_data.test_no == null}">
				<tr><td>検定番号（必須，8桁）</td><td>：<input type="text" name="test_no" id="ctest_no" value="" class="required number" minlength="8" maxlength="8" /></td></tr>
				<input type="hidden" name="insertflg" value="true">
			</c:if>
			<tr><td>検定名（必須）</td><td>：<input type="text" name="test_name" id="ctest_name" value="${test_data.test_name}" class="required" minlength="1" maxlength="50" /></td></tr>
			<tr><td>実施団体</td>
				<td>：
					<select name="associ_no"">
						<c:forEach var="item" items="${associlist}">
						<c:if test="${test_data.associ_no == item.associ_no}">
							<option value="${item.associ_no}" selected>${item.associ_name}</option>
						</c:if>
						<c:if test="${test_data.associ_no != item.associ_no}">
							<option value="${item.associ_no}">${item.associ_name}</option>
						</c:if>
						</c:forEach>
					</select>（<a href="mg_mainteassocimaster">新規登録</a>）
				</td>
			</tr>
		<tr><td colspan="2"><input type="submit" value="送信" /></td></tr>
		</table>
		</form>
		</c:if>
		<c:if test="${test_data.test_no == null && newflg == null }">
			<p>存在しない検定番号です。</p>
		</c:if>
		<a href="mg_maintetestmaster">戻る</a>
	</div>
</div>
</body>
</html>



