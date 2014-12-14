<%@page import="bean.EmpDetails"%>
<meta http-equiv="X-UA-Compatible" content="IE=9">
<meta http-equiv="cache-control" content="max-age=0" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="pragma" content="no-cache" />
<link rel="shortcut icon" type="image/ico" href="./../images/aqp.jpg">

<script type="text/javascript" src="./../js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="./../js/jquery-ui.js"></script>
<script type="text/javascript" src="./../js/extra.js"></script>
<script type="text/javascript" src="./../js/jquery.dataTables.js"></script>

<script type="text/javascript" src="./../js/jquery.tooltipster.js"></script>
<script type="text/javascript" src="./../js/countdown.js"></script>
<script type="text/javascript" src="./../js/jquery.blockUI.js"></script>

<link rel="stylesheet" href="./../css/jquery-ui.css" />
<link rel="stylesheet" href="./../css/jquery.dataTables.css" />
<link rel="stylesheet" href="./../css/master.css" />
<%
		EmpDetails ed =  (EmpDetails)session.getAttribute("EMP_DETAIL");
if(null!=ed){
	%>
<link rel="stylesheet" href="./../css/main<%=ed.getTheme().equals("")?"1":ed.getTheme() %>.css" />
<% }%>