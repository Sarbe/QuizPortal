<%@page import="util.CommonFns"%>
<%@page import="bean.QuizEmpDetails"%>
<%@page import="bean.EmpDetails"%>
<%@page import="bean.QuizDetails"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home Page</title>
<jsp:include page="headerInfo.jsp"></jsp:include>
<%
EmpDetails ed =  (EmpDetails)session.getAttribute("EMP_DETAIL");
String msg = (String) request.getAttribute("MSG");
%>
<script type="text/javascript">
$(document).ready(function(){
	
	<%if (msg != null && !msg.equals("")) {%>
	alertX('<%=msg%>');
	<%}%>
	  
	  $(".giveTest").click(function(){
			$("#method").val("giveTest");
			$("#quizNo").val(this.id);
			document.frm.submit();
	  });
	  
	  $(".viewResult").click(function(){
			$("#method").val("viewResult");
			$("#quizNo").val(this.id);
			document.frm.submit();
	  });
	  
	  
	  
	  $(".logout").click(function(){
			$("#method").val("logout");
			document.frm.submit();
	  });
	  
	  $('div#popup').hide();
	  
	  <% if(ed.getIsChangeRequired().equals("Y")){ %>
	    function showBusy(){
			//if ($('div.menuContent >div.child').length == 0)
			//	$('div.menuContent').append('<div class="child"></div>');
			$('div.menuContent').hide();
			$('div#popup').show();
		}
		 showBusy();
	<%}%>
		  
	  $("#saveUserDetails").click(function(){
		  var emailAdd = $("#emailAdd").val();
		  var empName = $("#empName").val();
		  var eId = <%=ed.getEmpId()%>;
		  
		  
		  if($.trim(emailAdd)==""|| $.trim(empName)==""){
			  alertX("Enter all details");
		  }else{
			  if(validateEmail(emailAdd)){

				  	$.ajax({
				 		 type : "POST",
				 		 url : "QuizAction",
				  		 data : {
				  			method :'uniqueEmailAddress',
				 			emailAdd:$("#emailAdd").val(),
				 			empId : eId
				  		},
				  		success : function(data) {
				  			var count = jQuery.parseJSON(data);
				 			if(count== 0){
				  				$("#method").val("saveUserDetails");
				  				document.frm.submit();
				  			}else{
				  				alertX("Email Address already Exists.");
				  			}
				  		}
					  });
			  	}else{
			  		alertX("Invalid Email Address");
			 	 }
			}
	  });
		  
		function validateEmail(email){
			var status = false;
			var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	           if( emailReg.test( email ) ) {
	           	status = true;
	           } 
	
	           return status;
		}
		  
		//Menu Script
		
			var linkKey = $("#linkNo").val();
			$(".menuListDetailsClass").hide();
			$("#menuList_Details_"+linkKey).show();
			$("#menuList_"+linkKey).addClass("menuItemSelected");
				
			$(".menuListClass").click(function(){
				var id = this.id;
				var idKey = id.split("_")[1];
				$("#linkNo").val(idKey);
				
				$(".menuListClass").removeClass("menuItemSelected");
				$(this).addClass("menuItemSelected");
			
				$(".menuListDetailsClass").hide();
				
				$("#menuList_Details_"+idKey).show();
			});
			
			
			$("#uploadExl").click(function() {
				$("#method").val("uploadExcelFile");
				document.frm.submit();
			});
			
			
		
			/* $('[id*=dataTable]').dataTable( {
				"paging":         false
			} );
			$(".chkRowNo").each(function(n){
				var tblId = $(this).attr("id");
				//str+=ids;
				 var str = "";
				if($("#"+tblId+" tbody tr").length==0){
					str = '<tr><td colspan="6">No Quiz Details Present</td></tr>';
						$("#"+tblId+" tbody").append(str);
				} 
			}); */
			
			$('[id*=dataTable]').each(function(){
				var tblId = $(this).attr("id");
				var str = "";
				if($("#"+tblId+" tbody tr").length==0){
					str = '<tr><td colspan="6">No Quiz Details Present</td></tr>';
						$("#"+tblId+" tbody").append(str);
				} else{
					$("#"+tblId).dataTable( {
						"paging": false,
						 "search": {"search": "<%=ed.getPrefered_quizType()%>"}
					} );
				}
				
			})
		  
});	


</script>
<style type="text/css">
div.child {
	top: 0;
	right: 0;
	bottom: 0;
	left: 0;
	opacity: .8;
	/* background-image: url("../images/gwt.gif"); */
	background-size: 200px 200px;
	background-position: center;
	background-repeat: no-repeat;
	filter: alpha(opacity =         50);
	-moz-opacity: 0.5;
	position: absolute;
	background-color: white;
}

div.parent {
	position: relative;
	width: 100%;
	height: 100%;
}

#popup {
	background-color: white;
	width: 400px;
	height: 150px;
	 top:0;
    bottom: 0;
    left: 0;
    right: 0;
	margin:  auto;
	border: 1px solid #ccc;
	border-color: rgba(0, 0, 0, .2);
	box-shadow: 0 2px 10px rgba(0, 0, 0, .2);
		position: absolute;
		padding: 20px 0px;
		
}

a {
	text-decoration: underline;
	cursor: pointer;
}
</style>
</head>
<body>
	<form action="QuizAction" name="frm" method="post" enctype="multipart/form-data">
	<input type="hidden" name="method" id="method">
	<input type="hidden" name="quizNo" id="quizNo">
	<input type="hidden" name="themeImg" id="themeImg">
	<%
		if(ed!=null){
	%>
	<div id="content">
	
	<div class="parent">
		<jsp:include page="header.jsp"></jsp:include>
				<div class="menuContent">
					<div class="menuItem" style="float: left; border: 0px solid red;">
						<ul>
							<li id="menuList_1" class="menuListClass"><span>Test In Progress</span></li>
							<li id="menuList_2" class="menuListClass"><span>View Result of Old Test</span></li>
							<li id="menuList_3" class="menuListClass"><span>Upload Question</span></li>
						</ul>
					</div>
					<div class="menuItemDetails">
					
					<%
						List<QuizEmpDetails> quizDetails = ed.getQuizSetDetails();
					%>
						<div id="menuList_Details_1" class="menuListDetailsClass">
							<div style="overflow: auto;height: 400px;">
								<table class="dataTbls chkRowNo" id="dataTable1">
									<thead>
										<tr>
											<th><span>Quiz Type</span></th>
											<th><span>Test No.</span></th>
											<th><span>Start Date</span></th>
											<th><span>End Date</span></th>
											<!-- <th><span>Published</span></th> -->
											<th><span>Visited ?</span></th>
											<th><span>Appeared On</span></th>
										</tr>
										</thead>
										<tbody>
										<%
										if(null!=quizDetails && quizDetails.size()!=0){
											for (int i = 0; i < quizDetails.size(); i++) {
												QuizEmpDetails qed = quizDetails.get(i);
												if(qed.getQuizDtl().getIsPublished().equals("Y") && CommonFns.compareWithToday(qed.getQuizDtl().getStart_Dt())>=0
														&& CommonFns.compareWithToday(qed.getQuizDtl().getEnd_Dt())<=0 ){
										%>
										<tr>
											<td>
														<%=qed.getQuizDtl().getQuizType() %>
											</td>
											<td>
														<span><a class="giveTest quizNoLink" id="<%=qed.getQuiz_nbr() %>" >TEST <%=qed.getQuizDtl().getShow_quiz_nbr() %></a></span>
											</td>
											<td><span>
											<%=CommonFns.changeDateFormat(qed.getQuizDtl().getStart_Dt(), "dd-MMM-yyyy hh:mm:ss a", "dd-MMM-yyyy")%></span>
											</td>
											<td><span>
											<%=CommonFns.changeDateFormat(qed.getQuizDtl().getEnd_Dt(), "dd-MMM-yyyy hh:mm:ss a", "dd-MMM-yyyy")%></span>
											</td>
											<%-- <td align="center">
													<%=qed.getQuizDtl().getIsPublished().equals("Y")?"YES":"NO" %>
											</td> --%>
											<td>
														<%=qed.getIsVisited() %>
											</td>
											<td>
														<%=qed.getAppear_Dt() %>
											</td>
										
										</tr>
									<%}
											}
										}
									%>
									</tbody>
								</table>
							</div>
						</div>
						
						<div id="menuList_Details_2" class="menuListDetailsClass">
						
							<div style="overflow: auto;height: 400px;">
								<table class="dataTbls chkRowNo" id="dataTable2">
									<thead>
										<tr>
											<th><span>Quiz Type</span></th>
											<th><span>Test No.</span></th>
											<th><span>Start Date</span></th>
											<th><span>End Date</span></th>
											<!-- <th><span>Published</span></th> -->
											<th><span>Visited?</span></th>
											<th><span>Appeared On</span></th>
										</tr>
									</thead>
									<tbody>
										<%
										if(null!=quizDetails && quizDetails.size()!=0){
											for (int i = 0; i < quizDetails.size(); i++) {
												QuizEmpDetails qed = quizDetails.get(i);
												if(qed.getQuizDtl().getIsPublished().equals("Y") && CommonFns.compareWithToday(qed.getQuizDtl().getEnd_Dt())>0
														){
										%>
										<tr>
											<td>
														<%=qed.getQuizDtl().getQuizType() %>
											</td>
											<td>
														<span><a class="viewResult quizNoLink" id="<%=qed.getQuiz_nbr() %>" >TEST <%=qed.getQuizDtl().getShow_quiz_nbr() %></a></span>
											</td>
											<td><span>
											<%=CommonFns.changeDateFormat(qed.getQuizDtl().getStart_Dt(), "dd-MMM-yyyy hh:mm:ss a", "dd-MMM-yyyy")%></span>
											</td>
											<td><span>
											<%=CommonFns.changeDateFormat(qed.getQuizDtl().getEnd_Dt(), "dd-MMM-yyyy hh:mm:ss a", "dd-MMM-yyyy")%></span>
											</td>
											<%-- <td align="center">
												<%=qed.getQuizDtl().getIsPublished().equals("Y")?"YES":"NO" %>
											</td> --%>
											<td>
														<%=qed.getIsVisited() %>
											</td>
											<td>
														<%=qed.getAppear_Dt() %>
											</td>
										
										</tr>
										<%
													}
												}
											}
										%>
									</tbody>
								</table>
							</div>
						
						</div>
						
						
						<div id="menuList_Details_3" class="menuListDetailsClass">
						<div style="overflow: auto;height: 400px;">
				 				File: <input type="file" name="file" id="file" /> <br /> <br/> 
				 				<input type="button" value="Upload" name="upload" id="uploadExl" />
				 				
				 				<a href="../resource/Template.xls">Download template</a>
				 				<br><br><br>
				 				<span>sdsdsdsa a afafwdawdfa fa fsaf </span><br>
				 				<span>sdsdsdsa a afafwdawdfa fa fsaf </span><br>
				 				<%
				 					List errList = (List) request.getAttribute("ERROR");
				 						if (errList != null && errList.size() > 0) {
				 							for (int i = 0; i < errList.size(); i++) {
				 								String err = (String) errList.get(i);
				 				%>
												<span><%=err%></span><br>
								
								<%
									}
								}
								%>
				 				
						</div>
					</div>
				</div>
			</div>
		</div>

		</div>
		<%} %>
		<div style="" id="popup">
		<div style="text-align: center;padding: 5px 0px;"><span style="font-size: 15px;font-weight: bold;">Please Enter Additional Details.</span></div>
		<div style="text-align: center;padding: 5px 0px;">Email: <input id="emailAdd" name="emailAdd" value="<%=ed.getEmpEmail() %>" autocomplete = "off"> </div>
		<div style="text-align: center;padding: 5px 0px;">Name: <input id="empName" name="empName" value="<%=ed.getEmpName() %>" autocomplete = "off"> </div>
		<div style="text-align: center;padding: 5px 0px;">Default Quiz Type: 
			<%
				List<String> quizTypes = ed.getQuizTypes();
			%>
			 <select name="quizType" id="quizType">
				<option value="" selected="selected">--- Select ---</option>
				<%
					for (int i = 0; i < quizTypes.size(); i++) {
				%>
				<option value="<%=quizTypes.get(i)%>" <%=ed.getPrefered_quizType().equals(quizTypes.get(i))?"selected=selected":"" %> ><%=quizTypes.get(i)%></option>
				<%
					}
				%>
			</select>
		
		 </div>
		<div style="text-align: center;padding: 5px 0px;"> <input type="button" value="Save" id="saveUserDetails"> </div>
		</div>

	</form>
</body>
</html>