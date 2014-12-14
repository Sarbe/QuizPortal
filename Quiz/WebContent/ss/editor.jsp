<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<jsp:include page="headerInfo.jsp"></jsp:include>
<style type="text/css">

</style>

<script type="text/javascript">


	$(document).ready(function(){
	   $(".up").click(function() {
	      $('html, body').animate({
	      scrollTop: 0
	   }, 2000);
	 });
	   
	   $("#exe").click(function(){
		   if($("#env").val()!=""){
			   if($("#qry").text()!=""){
					document.frm.action="DatabaseAction";
					$("#method").val("editor");
					document.frm.submit();
			   }else{
				   alertX("Enter Query");
			   }
		   }else{
			   alertX("Select Environment");
		   }
		});
	   $("#dwnldData").click(function(){
		   if($("#qry").text()!=""){
				document.frm.action="DatabaseAction";
				$("#method").val("dwnldData");
				document.frm.submit();
		   }else{
			   alertX("Enter Query");
		   }
		});
	   $("#queries").change(function(){
		   
		   $("#qry").text($(this).val());
	   })
	   
		$('#Datatbl').dataTable( {
			"scrollY": 200,
	        "scrollX": true,
	        "pagingType": "full_numbers"
	    } );
	   //request.setAttribute("MSG",msg);
	 
  		 <% String env = request.getAttribute("env") != null ? (String)request .getAttribute("env") : "";%>
  		$("#env").val('<%=env%>');
	});
</script>
<style type="text/css">
.tbl{
box-sizing: content-box;
clear: both;
color: rgb(0, 0, 0);
display: table;
font-size: 12px;
zoom: 1;
text-align: center;
}
.tbl tbody td {
border-bottom: 1px solid #4857B3;
}
</style>
</head>
<body>
	<form action="" name="frm" method="post">
		<input type="hidden" name="method" id="method">
		<div align="center" style="table-layout: auto; width: 100%;"
			id="content">
			<div
				style=" width: 1350px; height: 600px; background-color: white; padding-top: 15px;"
				class="parent">
				<div>
					<table>
						<tr>
							<td><select name="env" id="env">
							<option value=""> --Select Env -- </option>
								<option value="INT">INTEGRATION</option>
								<option value="STG">STAGE</option>
								<option value="PROD">PRODUCTION</option>
							</select>
							</td>
						</tr>
					</table>
				</div>
				<div style="width: 1200px; height: 150px;">
					<textarea name="qry" style="width: 1100px; height: 110px;" id="qry"> <%=request.getAttribute("qry") != null ? (String)request .getAttribute("qry") : ""%></textarea>
				</div>
				<div style="height: 50px;"><input type="button" value="RUN" id="exe" ></div>
				<!-- <div><button id="dwnldData">Download</button> </div> -->
				<%if(request.getAttribute("MSG")==null || request.getAttribute("MSG").equals("")){ %>
				
				<div
					style="width: 1200px; height: 350px; border: 0px solid green; overflow: auto;">
					<%
						List<List<String>> table = (List<List<String>>) session.getAttribute("TBL");
						if (null != table && table.size()>0) {
							List<String> header = table.get(0);
					%>
					<table class="tbl" id="Datatbl" c>
						<thead>
							<tr>
								<%
									for (int i = 0; i < header.size(); i++) {
								%>
								<td><%=header.get(i)%></td>
								<%
									}
								%>
							</tr>
						</thead>
						<tbody>
							<%
								for (int i = 1; i < table.size(); i++) {
										List<String> data = table.get(i);
							%>
							<tr>

								<%
									for (int j = 0; j < data.size(); j++) {
								%>
								<td><div class="des"><%=data.get(j)%></div></td>
								<%
									}
								%>
							</tr>
							<%
								}
							%>

						</tbody>
					</table>
					<%
						}
					%>
				</div>
				<%}else{ %>
				<div><%=request.getAttribute("MSG") %></div>
				<%} %>
			</div>
		</div>

	</form>
</body>
</html>