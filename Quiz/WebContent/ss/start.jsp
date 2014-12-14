<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz Portal</title>
<jsp:include page="headerInfo.jsp"></jsp:include>
<link rel="stylesheet" href="./../css/master.css" />
<%String msg = (String)request.getAttribute("MSG"); %>
<script type="text/javascript">

$(document).ready(function(){
	<% 
	if(msg!=null && !msg.equals("")){%>
		alertX('<%=msg%>');
	<% }
	%>

	$("#proceed").click(function(){
		processLogin();
	});
	
	function processLogin(){
		if($.trim($("#empId").val())=="" ||$.trim($("#passPhrase").val())=="" ){
			alertX("Enter correct details");
		}else{
			if(isNaN($("#empId").val())){
				alertX("EmpId should be numeric");
			}else{
				$("#method").val("login");
				document.frm.submit();
			}
		}
	}
	
	 $("body").keypress(function (e){
		    if(e && e.keyCode == 13){
		    	processLogin();
		    }
		 });
		 
});	


</script>

</head>
<body>
<form action="LoginAction" name="frm" method="post" >
	<input type="hidden" name="method" id="method">
	<div id="content">
	
	<div style="height: 100px;background-color: transparent; border: 0px solid white;margin: 150px 0;">
			<table height="40px" width="90%" border="0" align="center" bordercolor="white" >
			<tr><td align="center" colspan="7"><span style="font-variant: small-caps;font-size: 50px;color:#1876e8;">Automated Quiz Portal</span> </td></tr>
			<tr>
				<td width="20%"></td>
				<td width="10%" align="right">Emp ID</td>
				<td width="15%"><input type="text" name="empId" id="empId" tabindex="1" class="numOnly"> </td>
				
				<td width="10%" align="right">Pass Phrase</td>
				<td width="15%"><input type="password" name="passPhrase" id="passPhrase" tabindex="2"></td>
				<td width="10%"><input type="button" value="Go" id="proceed" tabindex="3"></td>
				<td width="20%"></td>
			</tr>
		</table>
	</div>
</div>
</form>
</body>
</html>