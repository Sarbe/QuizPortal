<%@page import="bean.QuizEmpDetails"%>
<%@page import="util.CommonFns"%>
<%@page import="bean.EmpDetails"%>
<%@page import="bean.QuizDetails"%>
<%@page import="bean.QuestionBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Appear Quiz</title>
<jsp:include page="headerInfo.jsp"></jsp:include>
<%
	EmpDetails empDtls = (EmpDetails) session
			.getAttribute("EMP_DETAIL");
	List qbList = (List) session.getAttribute("QUESTION_LIST");
	QuizEmpDetails qed = (QuizEmpDetails) session
			.getAttribute("QUIZ_DETAILS");

	String msg = (String) request.getAttribute("MSG");
%>

<script type="text/javascript">
$(document).ready(function(){
	
	<%if (msg != null && !msg.equals("")) {%>
		alertX('<%=msg%>');
	<%}%>

	$("#saveQuizAnswer").click(function() {
		$("#method").val("saveQuizAnswer");
		document.frm.submit();
	});

	$("#finalSubmit").click(function() {
		var confrm = confirm("Are you sure to Submit??");
		if (confrm == true) {
			$("#method").val("finalSubmitAnswer");
			document.frm.submit();
		}
	});
/* 
	$(".backLink").click(function() {
		$("#method").val("backToHome");
		document.frm.submit();
	}); */
	
	$(".radioChoice").click(function() {
		var qstnNbr = $(this).attr("name").split("_")[3];
		$("#qstn_"+qstnNbr).addClass("attempted");
		$("#qstn_"+qstnNbr).attr("title","Attended");
	});
	
	$(".clearAnswer").click(function() {
		var qstnNbr = $(this).attr("id").split("_")[1];
		if($("[name=answer_chkbx_qstn_"+qstnNbr+"]:checked").val()!==undefined){
			$("[name=answer_chkbx_qstn_"+qstnNbr+"]").attr("checked", false);
			$("#qstn_"+qstnNbr).removeClass("attempted");
			$("#qstn_"+qstnNbr).attr("title","Not Attended");
		}
		
	});
	
	
	

	// setInterval(function(){
	  
	  var curTm = new Date().format("dd/MM/yyyy hh:mm TT");
	  var endTm = '<%=qed.getQuizDtl().getEnd_Dt()%>';
	  
	 // $("#timer").html(new Date().toLocaleTimeString());
	 alert(curTm+"oo"+endTm);
	  $("#timer").html('hi');

	// },1000);

});
</script>
<style type="text/css">
#qstnMainDiv { /* height: 480px; */
	border: 1px solid #AAA;
	border-bottom: 1px solid #888;
	box-shadow: 0px 2px 2px #AAA;
	background-color: white;
	margin: 0 auto;
	width: 800px;
	/* overflow-y: auto; */
	padding: 0px 10px;
}

.qstnHeader {
	font-weight: bold;
	font-size: 20px;
	color: maroon;
}

.qustn {
	font-weight: bold;
	font-size: 15px;
	color: blue;
	/* background: #eee; */
}

.qstnSection {
	width: 750px;
	/* white-space: pre-wrap; */
	margin:5px 3px;
}


.clearAnswer {
	height: 15px;
	width: 15px;
	cursor: pointer;
	font-weight: normal;
	color: black;
	font-size: small;
	/* background-color: red; */
	box-shadow: 0 2px 2px #AAA;
	cursor: pointer;
	border-radius: 50px;
	text-align: center;
	float: right;
}

.red {
	background: url("../images/qSprite.png");
	background-position: -0px -52px;
	width: 16px;
	height: 16px;
	float: left;
}

.blue {
	background: url("../images/qSprite.png");
	background-position: -0px -0px;
	width: 16px;
	height: 16px;
	float: left;
}

.grn {
	background: url("../images/qSprite.png");
	background-position: -0px -26px;
	width: 16px;
	height: 16px;
	float: left;
}

.usrqstnBox {
	margin: 30px auto;
	width: 900px;
	background-color: white;
	padding: 20px 0px;
	box-shadow: 0 2px 2px #AAA;
}

.usrqstnTbl {
	margin: 0px auto;
}
.usrqstnTbl td{
	border-bottom: 1px solid #eee;
}



.attempted {
	box-shadow: 0 2px 2px #AAA;
	color: white;
	border: 0px;
	font-weight: bold;
	border-radius:50px;
}

</style>
</head>

<body>
	<form action="QuizAction" name="frm" method="post"
		enctype="multipart/form-data">
		<input type="hidden" name="method" id="method">
		<jsp:include page="header.jsp"></jsp:include>
		<div id="content">
			<div style="height: 550px; width: 100%;">
				
					<!-- <div style="width: 150px;"><a class="aLink backLink" style="margin-left: 20px;">Back toHome</a></div> -->
					<div class="bk backLink" style="cursor: pointer;"></div>
					
						<div class="usrqstnBox testDetails" style="width: 910px;">
							<table width="95%" class="">
								<tbody>
														
									<tr>
										<%
											if (!qed.getMode().equals("E")) {
										%>
									
									<tr>
										<td colspan="2"><span style="font-size:15px;">
										<%=qed.getAppear_Dt().equals("")?"You have not appear this Test":"You Appeared this exam on:"+qed.getAppear_Dt()%>
										<%-- You Appeared this exam on: <span
											style="font-weight: bold;"><%=qed.getAppear_Dt()%></span></span> --%>
											</span>
											</td>
									<tr>
										<%
											String score = qed.getScore(); //(String) request.getAttribute("SCORE");
												if (null != score && !score.equals("")) {
										%>
									
									<tr>
										<td colspan="2"><span style="font-size:15px;">You scored <span 
											style="font-weight: bold;"><%=score%>%</span></span></td>
									<tr>
										<%
											}
											}
										%>
										<td align="center" colspan="2"><span class="qstnHeader"><%=qed.getQuizDtl().getQuizType() %> - Test
												No:<%=qed.getQuizDtl().getShow_quiz_nbr() + "(Q-" + qed.getQuiz_nbr() + ")"%></span></td>
									</tr>
								</tbody>
							</table>
						</div>

						
						<div style="height: 40px;"></div>


						<%
							for (int i = 0; i < qbList.size(); i++) {
								QuestionBean qstnBean = (QuestionBean) qbList.get(i);
								String usrAns = qstnBean.getUser_ans();
								String ans = qstnBean.getAnswer();

								String tickClr = "red";
								if (usrAns.equals("")) {
									tickClr = "blue";
								} else if (usrAns.equals(ans))
									tickClr = "grn";
						%>
								
						<a name="anc_<%=i +1%>"></a>
						<div class="usrqstnBox">
						
							<table width="95%" class="usrqstnTbl" >
								<tr >
									<th align="left" colspan="2"> <span class="qustn">Question No:<%=i + 1%></span>
									
									
									<%
												if (qed.getMode().equals("E")) {
											%> 
									<div id="clrAnsId_<%=i + 1%>" class="clearAnswer" title="Clear Answer">x</div>
									<%
										}
									%>
									<!-- <hr class="hrCls"> -->
									</th>
								</tr>
								<tr>
									<th valign="top">Question</th>
									<td>
										<div class="qstnSection">
											<code><%=qstnBean.getQuestion_Txt()%></code>
										</div>
									</td>
								</tr>
								<tr>
									<th></th>
									<th>
										<%
											if (!qstnBean.getQuestion_Img().equals("")) {
										%>
										<div style="width: 750px; overflow-x: auto;" id="imgDiv">
											<img alt="" id="image" src="../img/<%=qstnBean.getQuestion_Img()%>">
											
										</div> 
										<%
												}
											%>
									</th>
								</tr>
								<tr>
									<th align="right">
										<%
											if (qed.getMode().equals("V") && ans.equals("1")) {
										%> <img alt="" src="./../images/<%=tickClr%>_tick.png"> 
										<%
												}
											%>
										<input type="radio" name="answer_chkbx_qstn_<%=i + 1%>" class="radioChoice"
										value="1" <%=(usrAns.equals("1")) ? "checked=checked" : ""%>
										<%=(qed.getMode().equals("E")) ? "": "disabled=disabled"%>> <!-- Option 1: -->
									</th>
									<td>
										<div class="qstnSection"><code><%=qstnBean.getOption1()%></code></div>
									</td>
								</tr>
								<tr>
									<th align="right">
										<%
											if (qed.getMode().equals("V") && ans.equals("2")) {
										%> 
										<div class="<%=tickClr%>"></div>
										<%-- <img alt="" src="./../images/<%=tickClr%>_tick.png">  --%>
										<%
											}
										%>
										<input type="radio" name="answer_chkbx_qstn_<%=i + 1%>" class="radioChoice"
										value="2" <%=(usrAns.equals("2")) ? "checked=checked" : ""%>
										<%=(qed.getMode().equals("E")) ? "" : "disabled=disabled"%>> <!-- Option 2: -->
									</th>
									<td>
										<div class="qstnSection"><code><%=qstnBean.getOption2()%></code></div>
									</td>
								</tr>
								<tr>
									<th align="right">
										<%
											if (qed.getMode().equals("V") && ans.equals("3")) {
										%> 
										<div class="<%=tickClr%>"></div>
										<%-- <img alt="" src="./../images/<%=tickClr%>_tick.png">  --%>
										<%
											}
										%>
										<input type="radio" name="answer_chkbx_qstn_<%=i + 1%>" class="radioChoice"
										value="3" <%=(usrAns.equals("3")) ? "checked=checked" : ""%>
										<%=(qed.getMode().equals("E")) ? "" : "disabled=disabled"%>> <!-- Option 3: -->
									</th>
									<td>
										<div class="qstnSection"><code><%=qstnBean.getOption3()%></code></div>
									</td>
								</tr>
								<tr>
									<th align="right">
										<%
											if (qed.getMode().equals("V") && ans.equals("4")) {
										%> 
										<div class="<%=tickClr%>"></div>
										<%-- <img alt="" src="./../images/<%=tickClr%>_tick.png">  --%>
										<%
											}
										%>
										<input type="radio" name="answer_chkbx_qstn_<%=i + 1%>" class="radioChoice"
										value="4" <%=(usrAns.equals("4")) ? "checked=checked" : ""%>
										<%=(qed.getMode().equals("E")) ? "" : "disabled=disabled"%>> <!-- Option 4: -->
									</th>
									<td>
										<div class="qstnSection"><code><%=qstnBean.getOption4()%></code></div>
									</td>
								</tr>
							</table>
						</div>
											
					<%
						}
					%>
										
						<div class="usrqstnBox" style="background-color: transparent;">
							<table width="95%" class="">
								<tbody>
									<tr>
										<td  align="center">
											<%
												if (qed.getMode().equals("E")) {
											%> 
											<input type="button" value="Save" id="saveQuizAnswer"> 
											<input type="button" value="Finish Test" id="finalSubmit"> 
											<% } %>
										</td>
									</tr>
								</tbody>
							</table>
						</div>

					<%
						if (qed.getMode().equals("E")) {
					%> 
					<div style="" id="qstnNbrDiv">
					<table border="0" style="width: 100px;" id="qstnNbrTbl">
						<tr>
							<%
								for (int i = 0; i < qbList.size(); i++) {
										QuestionBean qstnBean = (QuestionBean) qbList.get(i);
										String usrAns = qstnBean.getUser_ans();
										String ans = qstnBean.getAnswer();
							%>
								<td>
								<div class="qstnNbrTblDiv <%=!usrAns.equals("") ? "attempted": ""%> " id="qstn_<%=i + 1%>" title="Not Attended">
								<a href="#anc_<%=i + 1%>" style="text-decoration: none;color: inherit;" ><%=i + 1%></a>
								 </div></td>
							<%
								if (i != 0 && i % 2 == 1) {
							%>
							</tr>	<tr>
							<%
								}
									}
							%>
							</tr>	
														 
					</table>
					</div>	
					
					<%
						}
					%>
			</div>
		</div>
		
	</form>
</body>
</html>