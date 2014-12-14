<%@page import="util.ConfigFile"%>
<input type="hidden" name="sessiodnId" id="sessiodnId" value="<%=request.getAttribute("SESSIONID")%>" size="100">
<!-- To retain Menu list selected item -->
<input type="hidden" name="linkNo" id="linkNo" value="<%=request.getAttribute("LINK_NO") != null ? request
					.getAttribute("LINK_NO") : "1"%>">

<%@page import="bean.EmpDetails"%>
<%
	EmpDetails ed = (EmpDetails) session.getAttribute("EMP_DETAIL");
%>

	<div class="siteHeader">
		<div style="padding: 10px;">
			<table style="width: 100%;">
				<tr>
					<td width="40%"><span style="font-weight: bolder;font-size: 30px;color: white;">
						<a class="backLink" style="margin-left: 20px;text-decoration: none;font-variant: small-caps;">Automated Quiz Portal</a>
						</span></td>

					<td width="30%"><span style="font-size: 15px;">Welcome
							<%=ed.getEmpId() + " (" + ed.getEmpName() + ")"%>
					</span>
					<!-- Do Not remove the below line. This is developer mail address used  for Admin  user's
					'Contact admin for issue' functionality. Mail address is configured inside config.properties file  -->
					<input type="hidden" name="adminMailAdd" id="adminMailAdd" value="<%=ConfigFile.getKey("key.developerMailAdd")%>">
					</td>
					<td width="10%" align="center"><a id="themeSel_link">Theme</a></td>
					<td width="10%" align="center">
					</td>
					<td width="10%" align="center"><span><a class="logout">Logout</a></span></td>
				</tr>
			</table>
		</div>
	</div>
<div style="height: 80px;">
	<div style="margin: 0px auto; text-align: center;">
		<img alt="" id="proessImg" src="../images/ProgressBar.gif" style="visibility: hidden;">
	</div>

</div>

<div
	style="background-color: white; height: 200px; width: 300px; position: fixed; border: 1px solid black;visibility: hidden;z-index: 9;"
	id="themeSel_popup">
	<input type="hidden" name="themeSelector" id="themeSelector">
	<table style="background-color: transparent; width: 100%">
		<tr>
			<td colspan="2" align="right"><div title="Close"
					style="background: url('../images/qSprite.png'); cursor: pointer; background-position: 0px -227px; width: 20px; height: 17px;"
					id="closepopup"></div></td>
		</tr>
		<tr>
			<td id="bckgrnd" align="center"><span
				style="text-decoration: underline; color: blue;">Theme Color</span></td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<table>
					<tr>
						<td><div
								style="border: 1px solid #1876e8; background-color: #1876e8;"
								id="1" class="imgCls"></div></td>
						<td><div
								style="border: 1px solid #f66; background-color: #f66;"
								id="2" class="imgCls"></div></td>
						<td><div
								style="border: 1px solid #639; background-color: #639"
								id="3" class="imgCls"></div></td>
						<td><div
								style="border: 1px solid #fc0; background-color: #fc0;"
								id="4" class="imgCls"></div></td>
						<td><div
								style="border: 1px solid #44595a; background-color: #44595a;"
								id="5" class="imgCls"></div></td>
					</tr>
					<tr>
						<td><div
								style="border: 1px solid #24b260; background-color: #24b260;"
								id="6" class="imgCls"></div></td>
						<td><div
								style="border: 1px solid #F692F1; background-color: #F692F1;"
								id="7" class="imgCls"></div></td>
						<td><div
								style="border: 1px solid #4857B3; background-color: #4857B3;"
								id="8" class="imgCls"></div></td>
						<td><div
								style="border: 1px solid #800000; background-color: #800000;"
								id="9" class="imgCls"></div></td>
						<td><div
								style="border: 1px solid #E47833; background-color: #E47833;"
								id="10" class="imgCls"></div></td>
					</tr>
				</table>



			</td>
		</tr>
		<tr>
			<td colspan="1"></td>
		</tr>
	</table>
</div>


<!-- Contact Us -->

<div style="right: 0; bottom: 0; position: fixed;color: black;z-index: 10;" id="contactusMainDiv" >
	<div id="contactus" >
	
		<div style="width: 130px; float: left;"><%=(!ed.getEmpType().equals("A")) ? "Send Feedback / Issue"	: "See Feedbacks"%></div>
		<div id="arrowDir" class="UP" ></div>
	</div>

	<%
		if (!ed.getEmpType().equals("A")) {
	%>
	<div id="sendfdbk" class="feedback">
		<table style="width: 100%;">
			<tr>
				<td><textarea rows="5" cols="65" id="feedbackTxt" spellcheck="true"></textarea></td>
			</tr>
			<tr>
				<td><input type="button" value="Send" id="sendCmnt"></td>
			</tr>
		</table>
		</div>
	<%
		} else {
	%>
	
	<div id="rtrvfdbk" class="feedback" >
	<div style="margin:  auto;"><input type="button" value="Retrive" id="retriveFeedback" style="border: 2px solid white;"></div>
	<div style="overflow-y: auto;height: inherit;height: 340px;">
		<table style="width: 100%;" id="fbTbl" class="dataTbls">
			<thead>
			<tr style="width: 1px;">
				<td width="5%"></td>
				<td width="85%"></td>
				<td width="10%"></td>
			<tr>
				
			</tr>
			</thead>
			<tbody>
			
			</tbody>
		</table>
		</div>
	</div>
	<%
		}
	%>
	
</div>
