<%@page import="java.util.Set"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
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
	EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");

	String msg = (String) request.getAttribute("MSG");
%>
<script type="text/javascript">
$(document).ready(function(){
	
	<%if (msg != null && !msg.equals("")) {%>
				alertX('<%=msg%>');
	<%}%>
	// $( "#accordion" ).accordion({
				// heightStyle: "content"
				//});

				$("#start_dt").datepicker({
					minDate : 0,
					maxDate : "+90D",
					numberOfMonths : 2,
					onSelect : function(selected) {
						$("#end_dt").datepicker("option", "minDate", selected);
					}
				});
				$("#end_dt").datepicker(
						{
							minDate : 0,
							maxDate : "+90D",
							numberOfMonths : 2,
							onSelect : function(selected) {
								$("#start_dt").datepicker("option", "maxDate", selected);
							}
						});

				$("#d_start_dt").datepicker(
						{
							minDate : 0,
							maxDate : "+90D",
							numberOfMonths : 2,
							onSelect : function(selected) {
								$("#d_end_dt").datepicker("option", "minDate", selected);
							}
						});
				$("#d_end_dt").datepicker(
						{
							minDate : 0,
							maxDate : "+90D",
							numberOfMonths : 2,
							onSelect : function(selected) {
								$("#d_start_dt").datepicker("option", "maxDate", selected);
							}
						});

				/* $("#d_noOfQstn").keyup(function() {
					this.value = this.value.replace(/[^0-9\.]/g, '');

				});
 */
				$(".editQuizSet").click(function() {
					$("#method").val("editQuizSet");
					$("#quizNo").val(this.id);
					document.frm.submit();
				});

				$(".goToResultPage").click(function() {
					$("#method").val("goToResultPage");
					$("#quizNo").val(this.id);
					document.frm.submit();
				});

				$("#ntype").hide();
				//$("#createFromDumpDiv").hide();
				$("#paramTbl").hide();

				/* $("#createNew").click(function() {
					$("#createNewDiv").show();
					$("#createFromDumpDiv").hide();
				}); */

			 	/* $("#createFromDump").click(function() {
					$("#createFromDumpDiv").show();
					//$("#createNewDiv").hide();
				}); 
 */
				//  Button Action

				$("#create").click(function() {
					var qType = $("#quizType").val();
					if(qType=="NEW")
						qType = $("#ntype").val();
					
					var sdt = $("#start_dt").val();
					var edt = $("#end_dt").val();
					if(qType=="" ||sdt=="" ||edt==""){
						alertX("All details are mandatory.");
					}
					else{
						$("#method").val("addQuizSet");
						document.frm.submit();
					}
				});

				$("#fromDump").click(function() {
					var noOfQstn = $("#d_noOfQstn").val() == "" ? 0: $("#d_noOfQstn").val();
					
					var sdt = $("#d_start_dt").val();
					var edt = $("#d_end_dt").val();
					if(d_noOfQstn==0 ||sdt=="" ||edt==""){
						alertX("All details are mandatory.");
					}
					else{
					
						if (noOfQstn<=20 && noOfQstn >0) {
							$("#method").val("createSetFromDump");
							document.frm.submit();
						} else {
							alertX("No of Question should be within 1-20");
						}
					}
				});

				$(".createSet").click(function() {
					showX($("#paramTbl"));
					$("#d_quizType").val(this.id);
				});

				$("#quizType").change(function() {
					if ($(this).val() == "NEW")
						showX($("#ntype"));
					else
						hideX($("#ntype"));
				});

				$(".logout").click(function() {
					$("#method").val("logout");
					document.frm.submit();
				});

				$("#uploadExl").click(function() {
					$("#method").val("uploadExcelFile");
					document.frm.submit();
				});
				

				//$("#qstn_count").hide();
				$(".quizNoLink").mouseover(function() {
					/* $("#qstn_count").show();
					var ht = $(this).position().top;
					$("#qstn_count").offset({
						left : $(this).offset().left + $(this).width(),
						top : $(this).offset().top + 15
					}); */
					var ele = $(this);
					 $.ajax({
						type : "POST",
						url : "QuizAction",
						data : {
							method :'getNoofQstn',
							quizNo:$(this).attr("id")
						},
						success : function(msg) {
							//$("#qstn_count").text("Total No question :: "+msg);
							msg ="Total No question :: "+msg;
							toolTip(ele,msg);
						}
					});
					 
				}) .mouseout(function() {
					$("#qstn_count").hide();
				});
 
				
				
				
				
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
				
				
		
				
			// Manage User	
				function displayData(dataList){
					
					$("#userDtls tbody tr").remove();
						var data = jQuery.parseJSON(dataList);
						var str = "";
						if (data.length > 0) {
							$.each(data,function(row) {
								
								str = '<tr align="center">'
								+ '<td><div style="width: 5px; height: 25px;background-color: '+(this.userLoginStaus=='OFFLINE'?'grey':'#24b260')+'; "  title="'+this.userLoginStaus+'"></div> '
								+ '</td><td> <span title="Session ID : '+ this.sessionId+'">'+ this.empId+'</span>'
								+ '</td><td> <input '+(this.userLoginStaus=='OFFLINE'?'':'readonly=readonly')+' type="text" oldValue="'+this.empName+'" class="trasparentInp" value="'+ this.empName+'" id="empName_'+this.empId+'">'
								+'</td><td><input '+(this.userLoginStaus=='OFFLINE'?'':'readonly=readonly')+' type="text" oldValue="'+this.empEmail+'" class="trasparentInp" value="'+ this.empEmail+'" id="empEmail_'+this.empId+'">'
								+'</td><td><input '+(this.userLoginStaus=='OFFLINE'?'':'readonly=readonly')+' type="password" oldValue="'+this.passPhrase+'" class="trasparentInp" value="'+ this.passPhrase+'" id="passPhrase_'+this.empId+'">'
								+ '</td><td><select '+(this.userLoginStaus=='OFFLINE'?'':'readonly=readonly')+' oldValue="'+this.empType+'" class="trasparentInp" id="empType_'+this.empId+'">' 
								+'<option value="A" '+ (this.empType=='A'?'selected=selected':'')+ '>ADMIN</option>'
								+'<option value="N" '+(this.empType=='N'?'selected=selected':'')+ '>NORMAL</option>'
								+'</select>'
								+ '</td><td><select '+(this.userLoginStaus=='OFFLINE'?'':'readonly=readonly')+' oldValue="'+this.prefered_quizType+'" class="trasparentInp" id="prefered_quizType_'+this.empId+'">';
							
								<% for(int i =0 ;i<ed.getQuizTypes().size();i++){
								%>
								str += '<option value="'+'<%=ed.getQuizTypes().get(i)%>'+'" '+
								(this.prefered_quizType=='<%=ed.getQuizTypes().get(i)%>'?'selected=selected':'')+ '>'+'<%=ed.getQuizTypes().get(i)%>'+'</option>';
								<%}%>
								
								str +='</select>'
								+ '</td><td><input type="checkbox" class="trasparentInp" value="'+this.isChangeRequired+'" oldValue="'+this.isChangeRequired+'" id="isChangeRequired_'+this.empId+'" '+(this.isChangeRequired == 'Y' ? 'checked=checked' : '') +  '>'
								+'</td><td>';
								if(this.userLoginStaus=='OFFLINE'){
									str+='<span class="save-icon actnBtn" id="Savebtn_'+this.empId+'"></span>'
									+'<span class="delete-icon actnBtn" id="Dltbtn_'+this.empId+'"></span>';
								}
								+'</td></tr>';
								
								$("#userDtls tbody").append(str);
				
							});
						} else {
							str = '<tr><td colspan="7" align="center">No records Found</td></tr>';
							$("#userDtls tbody").append(str);
						}
					}
				
			//Save Details
				$(document.body).on("click",'.save-icon',function(e) {
					
					var idval = this.id;
					
					var eId = idval.split("_")[1];
					var eName = $("#empName_"+eId).val();
					var eMail = $("#empEmail_"+eId).val();
					var ePass = $("#passPhrase_"+eId).val();
					var eType = $("#empType_"+eId).val();
					var qType = $("#prefered_quizType_"+eId).val();
					var chngReqd = $("#isChangeRequired_"+eId).is(":checked")?"Y":"N";
					//alert(chngReqd);
					 $.ajax({
						type : "POST",
						url : "QuizAction",
						data : {
							method :'updateUser',
							empId : eId,
							empName : eName,
							empMail : eMail,
							empPass : ePass,
							empType : eType,
							prfrdType : qType,
							changeReqd : chngReqd
						},
						success : function(data) {
							if(data!=""){
								displayData(data);
								$("#searchKey").val("");
								alertX("User Details - Updated Successfully");
							}else{
								alertX("There is a error. Contact Suuport Team.");
							}
						}
					}); 
				});
				
				//Remove User
				$(document.body).on("click",'.delete-icon',function(e) {
					
					var r = confirm("Are you sure to Delete the User. \nAll the Details related to User will be Deleted.");
					
						if(r==true){
							var idval = this.id;
							var eId = idval.split("_")[1];
							$.ajax({
								type : "POST",
								url : "QuizAction",
								data : {
									method :'removeUser',
									empId : eId
								},
								success : function(data) {
									if(data!=""){
										displayData(data);
										$("#searchKey").val("");
										alertX("User Details - Removed Successfully");
									}else{
										alertX("There is a error. Contact Suuport Team.");
									}
								}
							});
						}
						
					});
				
				
					
					$("#mngUser").click(function(){
						$("#searchKey").val("");
							$.ajax({
							type : "POST",
							url : "QuizAction",
							data : {
								method :'manageUser'
							},
							success : function(data) {
								displayData(data);
		
							}
						});
					});
		
					
					$("#searchKey").keyup(function(){
						$.ajax({
						type : "POST",
						url : "QuizAction",
						data : {
							method :'searchUser',
							searchKey : $("#searchKey").val()
						},
						success : function(data) {
							displayData(data);
	
						}
					});
				});
					
					$(document.body).on("change",'.trasparentInp',function(e) {
						var oldVal =$(this).attr("oldValue");
						var newVal = $(this).val();
						
						if(oldVal!=newVal){
							$(this).css("border-color","red");
						}else{
							$(this).css("border-color","#DADADA");
						}
					});
					
					
				/* 	$('[id*=dataTable]').dataTable( {
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
								"paging": false
							} );
						}
						
					})
					
		});
			
</script>
<style type="text/css">
a {
	text-decoration: underline;
	cursor: pointer;
}

.dataTbls tbody tr:hover {
	background-color: #eee;
}

.infoTbl {
	border-collapse: collapse;
}

.infoTbl th {
	background-color: #eee;
}

#qstn_count {
	background-color: white;
	height: 40px;
	width: 150px;
	position: absolute;
	text-align: center;
	line-height: 40px;
	font-weight: bold;
	font-size: 13px;
}

.delete-icon {
	background: url("../images/qSprite.png");
	cursor: pointer;
	background-position: -0px -167px;
	width: 24px;
	height: 24px
}

.save-icon {
	background: url("../images/qSprite.png");
	cursor: pointer;
	background-position: -0px -78px;
	width: 21px;
	height: 21px;
}

.refresh {
	background: url("../images/qSprite.png");
	background-position: -0px -201px;
	width: 16px;
	height: 16px;
	float: right;
	margin-left: 10px;
}

.trasparentInp {
	background-color: transparent;
	width: 80px;
}
.actnBtn{
border: 1px solid transparent;
}
.actnBtn:hover{
border: 1px solid red;
}
</style>
</head>
<body>
	<form action="QuizAction" name="frm" method="post" enctype="multipart/form-data">
		<input type="hidden" name="method" id="method"> 
		<input type="hidden" name="quizNo" id="quizNo"> 
		<input type="hidden" name="themeImg" id="themeImg">
		
		<div id="content">
			<jsp:include page="header.jsp"></jsp:include>
			
			<%
				
				if (ed != null) {
					List<QuizEmpDetails> quizDetails = ed.getQuizSetDetails();
			%>
			
			<div class="menuContent">
				<div class="menuItem" style="float: left;">
					<ul>
						<li id="menuList_1" class="menuListClass"><span>Quiz In Progress</span></li>
						<li id="menuList_2" class="menuListClass"><span>Quiz in Queue</span></li>
						<li id="menuList_3" class="menuListClass"><span>Declare Result of Old Quiz</span></li>
						<li id="menuList_4" class="menuListClass"><span>Add a New Quiz</span></li>
						<li id="menuList_5" class="menuListClass"><span>Create Set From Question Dump</span></li>
						<li id="menuList_6" class="menuListClass"><span>Upload Question Excel</span></li>
						<%
							if (ed.getIsDeveloper().equals("Y")) {
						%>
							<li id="menuList_7" class="menuListClass"><span id="mngUser">Manage User
							<!-- <img class="refresh"/> -->
							</span>
							</li>
							
						<%
							}
						%>
					</ul>
				</div>
				<div class="menuItemDetails">
					<div id="menuList_Details_1" class="menuListDetailsClass">
						<div style="overflow: auto;height: 400px;">
							<table border="0" class="dataTbls chkRowNo" id="dataTable1">
							<thead>
								<tr>
									<th><span>Quiz Type</span></th>
									<th><span>Quiz No.</span></th>
									<th><span>Start Date</span></th>
									<th><span>End Date</span></th>
									<th><span>Published</span></th>
									<th><span>Test No.</span></th>
								</tr>
								</thead>
								<tbody>
								<%
									if (null != quizDetails && quizDetails.size() != 0) {
											for (int i = 0; i < quizDetails.size(); i++) {
												QuizEmpDetails qed = quizDetails.get(i);
												if (qed.getQuizDtl().getIsPublished().equals("Y")
														&& CommonFns.compareWithToday(qed.getQuizDtl()
																.getStart_Dt()) >= 0
														&& CommonFns.compareWithToday(qed.getQuizDtl()
																.getEnd_Dt()) <= 0) {
								%>
									<tr>
										<td>
													<span> <%=qed.getQuizDtl().getQuizType()%></span>
										</td>
										<td>
											<span><a class="editQuizSet quizNoLink" id="<%=qed.getQuiz_nbr()%>" >QUIZ <%=qed.getQuiz_nbr()%></a></span>
													
										</td>
										<td><span>
											<%=CommonFns.changeDateFormat(qed
									.getQuizDtl().getStart_Dt(),
									"dd-MMM-yyyy hh:mm:ss a", "dd-MMM-yyyy")%></span>
										</td>
										<td><span>
											<%=CommonFns.changeDateFormat(qed
									.getQuizDtl().getEnd_Dt(),
									"dd-MMM-yyyy hh:mm:ss a", "dd-MMM-yyyy")%></span>
										</td>
										<td align="center">
										<span>
											<%=qed.getQuizDtl().getIsPublished()
									.equals("Y") ? "YES" : "NO"%>
											</span>
										</td>
										<td>
													<span><%=qed.getQuizDtl().getShow_quiz_nbr()%></span>
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
					
					
					<div  id="menuList_Details_2" class="menuListDetailsClass" >
						<div style="overflow: auto;height: 400px;">
							<table border="0" class="dataTbls chkRowNo" id="dataTable2">
							<thead>
								<tr>
									<th><span>Quiz Type</span></th>
									<th><span>Quiz No.</span></th>
									<th><span>Start Date</span></th>
									<th><span>End Date</span></th>
									<th><span>Published</span></th>
									<th><span>Test No.</span></th>
								</tr>
								</thead>
								<tbody>
								<%
									if (null != quizDetails && quizDetails.size() != 0) {
											for (int i = 0; i < quizDetails.size(); i++) {
												QuizEmpDetails qed = quizDetails.get(i);
												if (qed.getQuizDtl().getIsPublished().equals("N")
														|| CommonFns.compareWithToday(qed.getQuizDtl()
																.getStart_Dt()) < 0) {
								%>
									<tr>
										<td>
													<%=qed.getQuizDtl().getQuizType()%>
										</td>
										<td>
											<span><a class="editQuizSet quizNoLink" id="<%=qed.getQuiz_nbr()%>" >QUIZ <%=qed.getQuiz_nbr()%></a></span>
													
										</td>
										<td>
											<%=CommonFns.changeDateFormat(qed
									.getQuizDtl().getStart_Dt(),
									"dd-MMM-yyyy hh:mm:ss a", "dd-MMM-yyyy")%>
										</td>
										<td>
											<%=CommonFns.changeDateFormat(qed
									.getQuizDtl().getEnd_Dt(),
									"dd-MMM-yyyy hh:mm:ss a", "dd-MMM-yyyy")%>
										</td>
										<td align="center">
											<%=qed.getQuizDtl().getIsPublished()
									.equals("Y") ? "YES" : "NO"%>
										</td>
										<td>
													<%=qed.getQuizDtl().getShow_quiz_nbr()%>
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
					
					
					<div  id="menuList_Details_3" class="menuListDetailsClass" >
						<div style="overflow: auto;height: 400px;">
							<table border="0" class="dataTbls chkRowNo" id="dataTable3">
							<thead>
								<tr>
									<th><span>Quiz Type</span></th>
									<th><span>Quiz No.</span></th>
									<th><span>Start Date</span></th>
									<th><span>End Date</span></th>
									<th><span>Published</span></th>
									<th><span>Test No.</span></th>
									<th><span>Declared</span></th>
								</tr>
								</thead>
								<tbody>
								
									<%
										if (null != quizDetails && quizDetails.size() != 0) {
												for (int i = 0; i < quizDetails.size(); i++) {
													QuizEmpDetails qed = quizDetails.get(i);
													if (qed.getQuizDtl().getIsPublished().equals("Y")
															&& CommonFns.compareWithToday(qed.getQuizDtl()
																	.getEnd_Dt()) > 0) {
									%>
									<tr>
										<td>
													<%=qed.getQuizDtl().getQuizType()%>
										</td>
										<td>
												<span><a class="goToResultPage quizNoLink" id="<%=qed.getQuiz_nbr()%>" >QUIZ <%=qed.getQuiz_nbr()%></a></span>
										</td>
										<td>
											<%=CommonFns.changeDateFormat(qed.getQuizDtl().getStart_Dt(),"dd-MMM-yyyy hh:mm:ss a", "dd-MMM-yyyy")%>
										</td>
										<td>
											<%=CommonFns.changeDateFormat(qed.getQuizDtl().getEnd_Dt(),"dd-MMM-yyyy hh:mm:ss a", "dd-MMM-yyyy")%>
										</td>
										<td align="center">
												<%=qed.getQuizDtl().getIsPublished().equals("Y") ? "YES" : "NO"%>
										</td>
										<td>
													Test <%=qed.getQuizDtl().getShow_quiz_nbr()%>
										</td>
										<td>
													<%=qed.getQuizDtl().getIsResultDeclared().equals("Y") ? "YES" : "NO"%>
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
					
					
					
					<div id="menuList_Details_4" class="menuListDetailsClass">
						<div style="overflow: auto;height: 400px;">
							<table border="0" class="infoTbl">
								<tr>
									<th>Quiz Type</th>
									<td>
										<%
											List<String> quizTypes = ed.getQuizTypes();
										%>
										 <select name="quizType" id="quizType">
											<option value="" selected="selected">--- Select ---</option>
											<%
												for (int i = 0; i < quizTypes.size(); i++) {
											%>
											<option value="<%=quizTypes.get(i)%>"><%=quizTypes.get(i)%></option>
											<%
												}
											%>
											<option value="NEW">New...</option>
									</select> 
									</td>
									<td colspan="2"><input id="ntype" type="text" name="newType" size="10" style="visibility: hidden;"></td>
								</tr>
								<tr>
									<th>Start Date</th>
									<td><input type="text" name="start_dt" id="start_dt" size="10" readonly="readonly"></td>
									<th>End Date</th>
									<td><input type="text" name="end_dt" id="end_dt" size="10" readonly="readonly"></td>
								</tr>
								<tr>
									<td colspan="4" align="center" style="padding-top: 30px;"><input type="button" value="Create" name="go" id="create"></td>
								</tr>
							</table>
						</div>
					</div>
					
					
					
					
					
					<div id="menuList_Details_5" class="menuListDetailsClass">
						<div style="overflow: auto;height: 400px;">
						
							<%
								List<String> dumpSets = ed.getDumpSets();
							%>
							<table border="0" class="dataTbls">
								<thead>
									<tr>
									<th width="90px"><span>Quiz Type</span></th>
									<th width="90px"><span>No Of Qstn</span></th>
									<th width="180px"><span>Action</span></th>
									</tr>
								</thead>
								<tbody>
								<%
									for (int i = 0; i < dumpSets.size(); i++) {
											String rcrd = (String) dumpSets.get(i);
								%>
									<tr>
										<td><%=rcrd.split("\\|")[0]%></td>
										<td><%=rcrd.split("\\|")[1]%></td>
										<td><a class="createSet aLink" id="<%=rcrd.split("\\|")[0]%>"> <%="Create " + rcrd.split("\\|")[0] + " Set"%> </a></td>
									</tr>
									<%
										}
									%>
									</tbody>
								</table>
								<br><br><br><br>
								<table border="0" bgcolor="white" id="paramTbl" class="infoTbl" style="visibility: hidden;">
									<tr>
										<th>Quiz Type</th>
										<td><input type="text" name="d_quizType" id="d_quizType" size="10" readonly="readonly"></td>
										<th>NO of Qstn</th>
										<td><input type="text" name="d_noOfQstn" class="numOnly" id="d_noOfQstn" value="0" size="2"></td>
										</tr>
									<tr>
										<th>Start Date</th>
										<td><input type="text" name="d_start_dt" id="d_start_dt" value="" size="10" readonly="readonly"></td>
										<th>End Date</th>
										<td><input type="text" name="d_end_dt" id="d_end_dt" value="" size="10" readonly="readonly"></td>
										</tr>
										<tfoot>
									<tr>
										<td colspan="4" align="center" style="padding-top: 30px;"><input type="button" value="Create Set" id="fromDump"></td>
									</tr>
									</tfoot>
							</table>
						</div>
					</div>
					
					<div id="menuList_Details_6" class="menuListDetailsClass">
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
					
					
					
						<div  id="menuList_Details_7" class="menuListDetailsClass" >
						<div style="overflow: auto;height: 400px;">
						<div style="width: 400px;float: right;padding: 10px 0px;"><span style="font-weight: bold;">Search by Details</span>
						 <input type="text" id="searchKey" name="searchKey" autocomplete="off" >
						</div>
							<table border="0" class="dataTbls" id="userDtls">
								<thead>
								
									<tr>
										<th><span></span></th>
										<th><span>Emp Id</span></th>
										<th><span>Emp Name</span></th>
										<th><span>Emp Email</span></th>
										<th><span>pass Phrase</span></th>
										<th><span>Emp Type</span></th>
										<th><span>Quiz Type</span></th>
										<th><span>Changes Reqd.</span></th>
										<th><span>Action</span></th>
									</tr>
								</thead>
								<tbody>
								
								</tbody>
							</table>
						</div>
					</div>
					
				
					
					
					
				</div>
			</div>
			

		</div>
		<%
			} else {
		%>
		<h1>No user has logged in</h1>
		
		<%
					}
				%>

		<div id="qstn_count1"></div> 
		
	

	</form>
</body>
</html>