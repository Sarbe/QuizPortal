<%@page import="bean.QuizEmpDetails"%>
<%@page import="bean.QuizDetails"%>
<%@page import="bean.EmpDetails"%>
<%@page import="util.CommonFns"%>
<%@page import="bean.QuestionBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Set Quiz</title>
<jsp:include page="headerInfo.jsp"></jsp:include>
<%
	String msg = (String)request.getAttribute("MSG");

	EmpDetails ed = (EmpDetails)session.getAttribute("EMP_DETAIL");
	List qbList = (List)session.getAttribute("QUESTION_LIST_EDIT");
	QuestionBean qb = (QuestionBean)session.getAttribute("QUESTION_TO_EDIT");
	QuizEmpDetails qed = (QuizEmpDetails)session.getAttribute("QUIZ_DETAILS");
	String ans = qb.getAnswer();
	
%>
<script type="text/javascript">
$(document).ready(function(){
	
	<% 
	if(msg!=null && !msg.equals("")){%>
		alertX('<%=msg%>');
	<% }
	%>
	
	
	
	  $(".editQuestion").click(function(){
			$("#method").val("showQuestion");
			$("#qstn_nbr").val(this.id);
			document.frm.submit();
	  });
	  
	 <%--  $(".viewQuestion").click(function(){
			$("#method").val("showQuestion");
			$("#quizNo").val("<%=qb.getQuiz_nbr()%>");
			$("#qstn_nbr").val(this.id);
			$("#mode").val("V");
			document.frm.submit();
	  }); --%>
	  
	  $("#addQuestion").click(function(){
			$("#method").val("addQuestion");
			$("#quizNo").val("<%=qb.getQuiz_nbr()%>");
			//$("#qstn_nbr").val(this.id);
			document.frm.submit();
	  });
	  
	  $("#clearFlds").click(function(){
			 clearFields();
		 })
	  $("#saveQuestion").click(function(){
		  var errMsg = validation();
		  if(errMsg ==""){
			$("#method").val("saveQuestion");
			document.frm.submit();
		  }else{
			  alert(errMsg);
		  }
	  });
	  
	  $(".statusOfQuiz").click(function(){
		  if($(this).id = "PUBLISH"){
			  var r = confirm("Be sure to save the details first, then publish. Continue to Publish?");
			  if(r){
				$("#method").val("alterStatusOfQuiz");
				document.frm.submit();
		  		}
	 	 	}else{
	 	 		$("#method").val("alterStatusOfQuiz");
				document.frm.submit();
	 	 	}
	  });
	
	  $("#changeDetails").click(function(){
		  
		  if(new Date($("#start_dt").val())<=new Date($("#end_dt").val())){
			  alert(new Date($("#start_dt").val())-new Date($("#end_dt").val()))
		  }
		  
		  
			$.ajax({
				type : "POST",
				url : "QuizAction",
				data : {
					method :'changeQuizDate',
					start_dt:$("#start_dt").val(),
					end_dt:$("#end_dt").val()
				},
				success : function(msg) {
						alertX(msg);
				}
			});
		  
			
	  });
	  
	  
	  $("#qstnFromDump").click(function(){
		  if($(this).is(':checked')){
			  $.ajax({
					type : "POST",
					url : "QuizAction",
					data : {
						method :'getQstnFromDump'
						
					},
					success : function(data) {
						if(data=="NQ"){
							alertX("No "+"<%=qed.getQuizDtl().getQuizType()%> question present in Dump");
							$("#qstnFromDump").prop("checked",false);
						}else{
							var data = jQuery.parseJSON(data);
							$("[name=question]").val(data.question_Txt);
							$("[name=option1]").val(data.option1);
							$("[name=option2]").val(data.option2);
							$("[name=option3]").val(data.option3);
							$("[name=option4]").val(data.option4);
							$("[name=comments]").val(data.comments);
							$("[name=chk_ans]").each(function(){
								//alert($(this).val());
								if($(this).val()==data.answer){
									$(this).prop("checked",true);
								}
							});//val(data.answer);
							$("[name=dump_question_Nbr]").val(data.question_Nbr);
							
							
							alertX("One "+"<%=qed.getQuizDtl().getQuizType()%> question Selected from Dump");
						}
					}
				});
		  }else{
			  clearFields();
		  }
	  });
	
	
	  
	  function clearFields(){
		  $("[name=question]").val("");
			$("[name=option1]").val("");
			$("[name=option2]").val("");
			$("[name=option3]").val("");
			$("[name=option4]").val("");
			$("[name=comments]").val("");
			$("[name=chk_ans]:checked").prop("checked",false);
			$("[name=dump_question_Nbr]").val();
	  }
	  
	  //$( "#start_dt" ).datepicker();
	 // $( "#end_dt" ).datepicker();
	  $("#start_dt").datepicker({
		  minDate: 1,
		  maxDate: "+90D",
	      numberOfMonths: 2,
	      onSelect: function(selected) {
	          $("#end_dt").datepicker("option","minDate", selected)
	        }
	    });
	    $("#end_dt").datepicker({ 
	    	minDate: 1,
			maxDate: "+90D",
	        numberOfMonths: 2,
	        onSelect: function(selected) {
	           $("#start_dt").datepicker("option","maxDate", selected)
	        }
	    });  

	    //validation
	    function validation(){
	    	
	    	var errMsg ="";
	    	if($("#question").val()=="")
	    		errMsg += "\n Enter Question";
	    		
	    	$(".ansOption").each(function(n){
	    		if($(this).val()=="")
	    			errMsg += "\n Enter Choice :"+ (n+1);
	    	});

	    	if($(".radioOption:checked").length==0){
	    		errMsg += "\n Choose Answer";
	    	}
	    	return errMsg;
	    }
	    
  		$("#uploadImg").click(function(){
  			
  			if($("#browseImage").val()!=""){
  				$("#method").val("uploadImg");
  		    	document.frm.submit();
  			}else{
  				alertX("Choose image first...");
  			}
	    	
	    });
	    
 		$("#removeImg").click(function(){
	    	$("#qstnImage").val("");
	    	$("#image").attr("src","");
	    	
	    	$("#imgDiv").hide();
	    	$(this).hide();
	    	
	    });
 		
 		
 	/* 	
	$(".backLink").click(function(){
	    	
	    	$("#method").val("backToHome");
	    	document.frm.submit();
	    });
	 */
	
	
});	

</script>
<style type="text/css">


.qstnHeader{
font-weight: bold;
font-size: 20px;
color: maroon;
}





#imgDiv{
margin: 10px auto;
width: 600px;height: 150px;overflow: auto;border: 1px solid #DADADA;
}



#qstnMainDiv {
	width: 800px;
	height: 520px;
	border: 1px solid #AAA;
	border-bottom: 1px solid #888;
	box-shadow: 0px 2px 2px #AAA;
	background-color: white;
	margin: 0 auto;
	overflow-y: auto;
	padding: 0px 10px;
}




.selectedQstn {
	color: #fff;
	border-radius: 50px;
	font-weight: bold;
	font-size: 15px;
}


.usrqstnBox {
	margin: 20px auto;
	width: 900px;
	background-color: white;
	padding: 20px 0px;
	box-shadow: 0 2px 2px #AAA;
}
.testDetails{
	position: fixed;
	top:65px;
	left: 0;
	right: 0;
	margin: 0px auto;
}

#addQuestion{
	width: 50px;
	height: 50px;
	line-height: 50px;
	background-image: url('../images/add_qstn.png');
	background-repeat: no-repeat;
	background-position: center;
	
}



</style>
</head>
<body>
<form action="QuizAction" name="frm" method="post" id="frm" enctype="multipart/form-data">
<input type="hidden" name="method" id="method">
<input type="hidden" name="quizNo" id="quizNo" value="">
<input type="hidden" name="qstn_nbr" id="qstn_nbr" value="">

<jsp:include page="header.jsp"></jsp:include>
<div id="content">
			
				<% boolean qsts = qed.getQuizDtl().getIsPublished().equals("N"); %>
			
					<!-- <div style="width: 150px;"><a class="aLink backLink" style="margin-left: 20px;">Back toHome</a></div> -->
					
					<div class="bk backLink" style="cursor: pointer;" title="Backk to Home"></div>
					
					 <div class="usrqstnBox testDetails" style="width: 910px;" >
							<table width="95%">
								<tbody>
														
									<tr>
									<td align="center" colspan="2">
										<span class="qstnHeader"><%=qed.getQuizDtl().getQuizType()+" :: " %> Quiz No:<%=qed.getQuiz_nbr()%>
										<%=qed.getQuizDtl().getShow_quiz_nbr().equals("")?"":"( Test No :"+qed.getQuizDtl().getShow_quiz_nbr()+" )" %></span></td>
									</tr>
									<tr>
									<td align="center" colspan="2">
									<span style="font-weight: bold;margin: 0px 10px;">Start Date:</span><input type="text" id="start_dt" name="start_dt" <%=qsts?"":"disabled=disabled" %> readonly="readonly" size="10"
												value="<%=CommonFns.changeDateFormat(qed.getQuizDtl().getStart_Dt(),"dd-MMM-yyyy hh:mm:ss a","dd-MMM-yyyy")%>" style="text-align: center;">
											
										<span style="font-weight: bold;margin: 0px 10px;">End Date:</span><input type="text" id="end_dt" name="end_dt" <%=qsts?"":"disabled=disabled" %> readonly="readonly" size="10"
												  value="<%=CommonFns.changeDateFormat(qed.getQuizDtl().getEnd_Dt(),"dd-MMM-yyyy hh:mm:ss a","dd-MMM-yyyy")%>" style="text-align: center;">
										
										<%if(qsts){%>
										 <a class="aLink" id="changeDetails" >Change Date</a>
										 <%} %>
									 
									 </td>
									</tr>
								</tbody>
							</table>
						</div>	 
					
					
					
					<div class="usrqstnBox" style="margin-top: 60px;">
					
						<table width="90%" align="center">
		
						<tr>
						<th align="left" colspan="2"><span style="font-size: 20px;">Question No:<%=qb.getQuestion_Nbr()%></span>
							<input type="hidden" name="question_Nbr" value="<%=qb.getQuestion_Nbr()%>">
							<input type="hidden" name="dump_question_Nbr">
						<% if(qsts){ %>
						 <input type="checkbox" id="qstnFromDump"><span style="font-style: italic;font-weight: bold;">Pick from Dump</span>
						 <%} %>
						   </th>
						</tr>
						<tr align="left">
							<th valign="top">Question</th>
							<td>
								<textarea rows="6" cols="90" name="question" id="question" tabindex="1" <%=qsts?"":"disabled=disabled"%>><%=qb.getQuestion_Txt()%></textarea> <br/>
								</td>
							</tr>
							<tr  align="left">
							<th valign="top">Image(Optional)</th>
							<td>
								<% if(qsts){ %>
								<input type="file" name="fileBtn" id="browseImage" value="<%=qb.getQuestion_Img() %>">
								<input type="button" value="Upload" id="uploadImg">
								<%} %>
								
								<input type="hidden" name="qstnImage" id="qstnImage" value="<%=qb.getQuestion_Img() %>">	
								<% if(qsts){ %>
									<%if(!qb.getQuestion_Img().equals("")){ %>
									<a id="removeImg" class="aLink">Remove Image</a>
									<%} %>
								<%} %>
								<br/>
								<%if(!qb.getQuestion_Img().equals("")){ %>
									<div id="imgDiv">
										<img alt="" id="image" src="../img/<%=qb.getQuestion_Img() %>" >
									</div>				
								<%} %>
							</td>
						</tr>
						<tr  align="left">
							<th align="right"> <input class="radioOption" type="radio" value="1" name="chk_ans" <%=ans.equals("1")?"checked='checked'":"" %> <%=qsts?"":"disabled=disabled"%>>
								</th>
							<td>
							<textarea rows="2" cols="90" name="option1" autocomplete="off"  tabindex="6" <%=qsts?"":"disabled=disabled"%>><%=qb.getOption1() %></textarea>
								<%-- <input type="text" class="ansOption" name="option1" value="<%=qb.getOption1()%>" 
											autocomplete="off" tabindex="2" style="width: 1000px;" <%=qsts?"":"disabled=disabled"%>>  --%>
							</td>
						</tr>
						<tr  align="left">
							<th align="right"><input class="radioOption" type="radio" value="2" name="chk_ans" <%=ans.equals("2")?"checked='checked'":"" %> <%=qsts?"":"disabled=disabled"%>>
							</th>
							<td>
							<textarea rows="2" cols="90" name="option2" autocomplete="off"  tabindex="6" <%=qsts?"":"disabled=disabled"%>><%=qb.getOption2() %></textarea>
								<%-- <input type="text" class="ansOption" name="option2"  value="<%=qb.getOption2()%>" 
											autocomplete="off" tabindex="3" style="width: 1000px;" <%=qsts?"":"disabled=disabled"%>>  --%>
							</td>
						</tr>
						<tr  align="left">
							<th align="right"><input class="radioOption" type="radio" value="3" name="chk_ans" <%=ans.equals("3")?"checked='checked'":"" %> <%=qsts?"":"disabled=disabled"%>>
							
							</th>
							<td>
							<textarea rows="2" cols="90" name="option3" autocomplete="off"  tabindex="6" <%=qsts?"":"disabled=disabled"%>><%=qb.getOption3() %></textarea>
								<%-- <input type="text" class="ansOption" name="option3" value="<%=qb.getOption3()%>" 
											autocomplete="off" tabindex="4" style="width: 1000px;" <%=qsts?"":"disabled=disabled"%>>  --%>
							
							 </td>
						</tr>
						<tr  align="left">
							<th align="right"><input class="radioOption" type="radio" value="4" name="chk_ans" <%=ans.equals("4")?"checked='checked'":"" %> <%=qsts?"":"disabled=disabled"%>>
							
							</th>
							<td>
							<textarea rows="2" cols="90" name="option4" autocomplete="off"  tabindex="6" <%=qsts?"":"disabled=disabled"%>><%=qb.getOption4() %></textarea>
								<%-- <input type="text" class="ansOption" name="option4" value="<%=qb.getOption4()%>" 
											autocomplete="off"  tabindex="5" style="width: 1000px;" <%=qsts?"":"disabled=disabled"%>>  --%>
							
							  </td>
						</tr>
						<tr  align="left">
							<th align="right">Comments</th>
							<td>
							<textarea rows="2" cols="90" name="comments" autocomplete="off"
															tabindex="6" <%=qsts?"":"disabled=disabled"%>><%=qb.getComments() %></textarea>
														<%-- 	<input type="text" name="comments" value="<%=qb.getComments() %>" 
											autocomplete="off"  tabindex="6" style="width: 300px;" <%=qsts?"":"disabled=disabled"%>>  --%>
							
							   </td>
						</tr>
						
						<tr  align="left">
							<td colspan="2" align="center" style="padding-top: 20px;">
							
								<% if(qsts){ %>
								<input type="button" value="Save" id="saveQuestion" tabindex="7"> 
								<!-- <input type="button" value="Clear" id="clearFlds" tabindex="8">  -->
									<%if(qbList.size()!=0){ %>
									<input type="button" value="Publish" id="PUBLISH" class="statusOfQuiz"  tabindex="9">
									<%} %>
								<%}else{ %>
								<input type="button" value="Unpublish" id="UNPUBLISH" class="statusOfQuiz" tabindex="9">
								<%} %>
							</td>
						</tr>
					</table>
					</div>
					
					
					
					
					
					<div style="" id="qstnNbrDiv">
					<table style="width: 100px;" id="qstnNbrTbl">
						<% if( qsts && (qbList.size()!=0 && qbList.size()<20)){ %>
						<tr align="center"><td colspan="2" ><div class="qstnNbrTblDiv selectedQstn gE" id="addQuestion" style="float: none;" title="Add New Question"></div> </td></tr>
						<%} %>
						<tr>
							<%for(int i=0;i<qbList.size();i++){ 
								QuestionBean qbTemp = (QuestionBean)qbList.get(i);
							%>
								<td>
								
								<%if(qbTemp.getQuestion_Nbr()!=qb.getQuestion_Nbr()){ %>
								<div class="qstnNbrTblDiv editQuestion" id="<%=qbTemp.getQuestion_Nbr() %>">
								<%=qbTemp.getQuestion_Nbr() %>
								</div>
							<%}else{ %>
								<div class="qstnNbrTblDiv selectedQstn"><%=qbTemp.getQuestion_Nbr() %></div>
							<%} %>
								
							</td>
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
</div>

</form>
</body>
</html>