<%@page import="bean.QuizEmpDetails"%>
<%@page import="bean.QuizDetails"%>
<%@page import="bean.EmpDetails"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Result Page</title>
<jsp:include page="headerInfo.jsp"></jsp:include>
<%
	List quizEmpList = (List) session.getAttribute("RESULT_LIST");
	QuizEmpDetails qed = (QuizEmpDetails)session.getAttribute("QUIZ_DETAILS");
	int quizNo =  ((Integer)request.getAttribute("QUIZ_NO")).intValue();
%>
<script type="text/javascript">
$(document).ready(function(){
	

	$("#calculateResult").click(function() {
		var r = confirm("Are you Sure to reCalculate the result. A new Mail needs to be sent to User.Please search Again to Send mail.");
		if(r==true){
			$("#method").val("calculateResult");
			document.frm.submit();
		}
	});
	
	/* $(".backLink").click(function(){
    	
    	$("#method").val("backToHome");
    	document.frm.submit();
    }); */
	
	$("#markDeclaredAndSendMail").click(function() {
		$("#proessImg").css("visibility", "visible");
		 $.ajax({
				type : "POST",
				url : "QuizAction",
				data : {
					method :'markQuizAsDeclared',
				},
				success : function(msg) {
					
					$("#proessImg").css("visibility", "hidden");
					alertX(msg);
					$("#markDeclaredAndSendMail").hide();
				}
		});
	});
	
});
</script>


</head>
<body>
	<form action="QuizAction" name="frm" method="post" enctype="multipart/form-data">
	<input type="hidden" name="method" id="method"> 
	<jsp:include page="header.jsp"></jsp:include>
	<a class="aLink backLink" style="margin-left: 20px;">Back to Home</a>	
		<div id="content">
			<div style="margin: 0 auto;background-color: transparent;height: 500px;">
				<table border="0" align="center" class="dataTbls" >
				<thead>
					<tr>
						<td colspan="6" align="center"><span style="font-size: 15px;font-weight: bold;">Result for Quiz:: <%=quizNo %></span></td>
					</tr>
					<tr>
						<th><span><span>Rank</span></span></th>
						<th><span>Emp Id</span></th>
						<th><span>Emp Name</span></th>
						<th><span>Emp Email</span></th>
						<th><span>Quiz Submit Date</span></th>
						<th><span>Score</span></th>
					</tr>
					</thead>
					<tbody>
					<%
						for (int i = 0; i < quizEmpList.size(); i++) {
							QuizEmpDetails qed1 = (QuizEmpDetails) quizEmpList.get(i);
							EmpDetails ed = qed1.getEmpDtl();
							QuizDetails qd = qed1.getQuizDtl();
					%>
					<tr>
						<td><%=qed1.getRank()%></td>
						<td><%=qed1.getEmpDtl().getEmpId()%></td>
						<td><%=qed1.getEmpDtl().getEmpName()%></td>
						<td><%=qed1.getEmpDtl().getEmpEmail()%></td>
						<td><%=qed1.getAppear_Dt()%></td>
						<td><%=qed1.getScore()%></td>
					</tr>
					<%
						}
					%>
					</tbody>
					<tfoot>
					<tr>
					<tr><td colspan="6" align="center">
					<input type="button" id="calculateResult" value="Re-Calculate"> 
					<%if(!qed.getQuizDtl().getIsResultDeclared().equals("Y")){ %>
					<a id="markDeclaredAndSendMail" class="aLink">Send Mail</a>
						<!-- <input type="button" value="Send Mail" >  -->
					<%} %>
					</td></tr>
					</tfoot>
				</table>
			</div>
		</div>
	</form>
</body>
</html>