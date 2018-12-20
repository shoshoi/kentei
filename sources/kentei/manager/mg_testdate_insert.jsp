
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setCharacterEncoding("UTF-8"); %>

<html>
<head>
<title>検定取得状況管理システム - 検定実施日登録・削除</title>
	<link rel="stylesheet" href="style.css" type="text/css" />
	<link type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/ui-lightness/jquery-ui.css" rel="stylesheet" />
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/jquery-ui.min.js"></script>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/i18n/jquery.ui.datepicker-ja.min.js"></script>
	<script type="text/javascript" language="javascript" src="js/jquery.dropdownPlain.js"></script>
	<script language="javascript" type="text/javascript" src="js/jquery.validate.js"></script>
	<script language="javascript" type="text/javascript" src="js/messages_ja.js"></script>
	<script type="text/javascript" language="javascript" src="js/etc.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#form").validate({
                rules : {
                    remdate: {
                        required: true
                    }
                },
    		    errorLabelContainer: "#verror"
            });
            $("#dates").validate();
            $("#datepicker").datepicker({
            	dateFormat: 'yy/mm/dd',
            	showOn: 'both', 
            	changeMonth: true,
        		changeYear: true,
        		beforeShow : function(){
           			 $('#ui-datepicker-div').css('font-size', '12px');
           			 $('#ui-datepicker-div').css('line-height', '12px');
      			}
            });
            $("#datepicker2").datepicker({
            	dateFormat: 'yy/mm/dd',
            	showOn: 'both', 
            	changeMonth: true,
        		changeYear: true,
        		beforeShow : function(){
           			 $('#ui-datepicker-div').css('font-size', '12px');
           			 $('#ui-datepicker-div').css('line-height', '12px');
      			}
            }); 
		});
		function datecheck(){
			var dt1 = $("#datepicker")[0].value
			var dt2 = $("#datepicker2")[0].value
			if(dt1 != "" && dt2 != "") {
				var date1 = new Date(dt1);
				var date2 = new Date(dt2 );
				if(date1.getTime() <= date2.getTime()){
					return true;
				}else{
					$('#datemsg').html('取得日は実施日以降の年月日を指定してください。');
					return false;
				}
			} else {
 			   return false;
			}
		}
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
		<h2>検定実施日登録・削除</h2>
		
			<c:if test="${!empty msg}">
			<div id="msg"><p>${msg}</p></div>
			</c:if>
			

		<c:if test="${!empty test_data}">
			<p>検定名：${test_data.test_name}　主催：${test_data.associ_name}</p>
			<form method="POST" id="dates" action="mg_testdate_add" onSubmit="return datecheck()">
			
			<p>実施日：<input type="text" id="datepicker" name="perform_date" value="" class="required" readonly /></p>
			
			<p>取得日：<input type="text" id="datepicker2" name="get_date" value="" class="required" readonly /></p>
			
			<input type="hidden" value="${param.test_no}" name="test_no">
			<div id="datemsg"></div>
			<p><input type="submit" value="登録" /></p>
			</form>

			<c:if test="${!empty datelist}">
			<form method="POST" id="form" action="mg_testdate_delete">
				<input type="hidden" name="test_no" value="${param.test_no}">
				<table border="3" class="hitable">
					<tr><th>削除</th><th>実施日</th><th>取得日</th></tr>
					<c:forEach var="item" items="${datelist}" varStatus="status">
					<tr><td><input type="radio" name="remdate" value="${item.test_perform_date}"></td><td>${item.test_perform_date}</td><td>${item.test_get_date}</td>
					</c:forEach>
				</table>
				<p><input type="submit" value="削除" /></p>
			</form>
			<p><div id="verror"></div></p>
			</c:if>
			<c:if test="${empty datelist}">
				<p>検定実施日が登録されていません。</p>
			</c:if>
		</c:if>
		<c:if test="${empty test_data}">
			<p>存在しない検定番号です。</p>
		</c:if>
		<a href="./mg_testdate">戻る</a>
	</div>
</div>
</body>
</html>



