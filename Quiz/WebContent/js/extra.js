



jQuery.fn.extend({
	hideX: function() {
    $(this).css("border","1px red solid");
  },
  uncheck: function() {
    return this.each(function() {
      this.checked = false;
    });
  }
});



function hideX(ele){
	 
}




(function($){
/* showX = function(ele) {
	  ele.css("visibility","visible");
		 ele.show();
  };*/
  

	toolTip = function(parentElement,msg){
		$("body #qstn_count").remove();
		 var htmlCd = '<div id="qstn_count">'+msg+'</div>';
		 $("body").append( htmlCd);
		// var ht = parentElement.position().top;
			$("#qstn_count").offset({
				left :parentElement.offset().left + parentElement.width(),
				top : parentElement.offset().top + 15
			});
			
			$("#qstn_count").delay(3000).fadeOut(2000);
	};
  
  
})(jQuery);

/*
function toolTip(parentElement,msg){
	$("body #qstn_count").remove();
	 var htmlCd = '<div id="qstn_count">'+msg+'</div>';
	 $("body").append( htmlCd);
	 var ht = parentElement.position().top;
		$("#qstn_count").offset({
			left :parentElement.offset().left + parentElement.width(),
			top : parentElement.offset().top + 15
		});
}*/





function showX(ele){
	 ele.css("visibility","visible");
	 ele.show();
}

function alertX(msg) {
	//alert(msg);
	var len = msg.length;
	var size  = len*8;
	$("#content #msgAlert").remove();
	var str = '<div class="alertX " id="msgAlert" style="width:'+size+'px;"></div>';
	$("#content").append(str);
	$("#msgAlert").html(msg);
	$("#msgAlert").delay(7000).fadeOut(4000);
}

$(document).ready(function(){
	
	
	
	 $("a").keyup(function(e){
		 if($(this).val().length>15){
			 if($(this).width()<550){
			 var w = $(this).val().length*8;
			 $(this).width(w);
			 }
		 }
		 
	 });
	 
	 $('.numOnly').keydown(function(event) {
         // Allow special chars + arrows 
         if (event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 
             || event.keyCode == 27 || event.keyCode == 13 
             || (event.keyCode == 65 && event.ctrlKey === true) 
             || (event.keyCode >= 35 && event.keyCode <= 39)){
                 return;
         }else {
             // If it's not a number stop the keypress
             if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                 event.preventDefault(); 
             }   
         }
     });
	 
	 
	 $(".logout").click(function(){
			$("#method").val("logout");
			document.frm.action="QuizAction";
			document.frm.submit();
	  });
	 
	 
	 $(".backLink").click(function(){
	    	$("#method").val("backToHome");
	    	document.frm.action="QuizAction";
	    	document.frm.submit();
	    });
	 
// Theme selector
	 
	 $(".imgCls").click(function(){
			$("#method").val("themeSelector");
			 $("#themeSelector").val(this.id);
			document.frm.submit();
	  });
	 
	 // For tooltip
	
	 $("#themeSel_popup").hide();
	 $("#themeSel_link").click(function(e){
		 showX($("#themeSel_popup"));
		 var ht= $( this ).position().top;
			$("#themeSel_popup").offset({
				left : $( this ).offset().left-50 ,
				top :$( this ).offset().top+ht
			});
	  });
	 $("#closepopup").click(function(e){
		 hideX($("#themeSel_popup"));
	 });
	 
	 
	 ///////////////////////////////////
	 $(".feedback").hide();
	 $("#arrowDir").click(function(e){
		 if($(this).attr("class")=="UP"){
			 //$(".feedback").show();
			 showX($(".feedback"));
			 
			 $("#arrowDir").removeClass("UP").addClass("DOWN");
		 }else{
			 hideX($(".feedback"));
			 $("#arrowDir").removeClass("DOWN").addClass("UP");
		 }
		 
	 });
	 
	 $("#sendCmnt").click(function(){
			 if($.trim($("#feedbackTxt").val())!=""){
				$.ajax({
					type : "POST",
					url : "QuizAction",
					data : {
						method :'sendFeedback',
						feedback:$("#feedbackTxt").val()
					},
					success : function(msg) {
							alertX(msg);
							 hideX($(".feedback"));
							$("#arrowDir").removeClass("DOWN").addClass("UP");
							$("#feedbackTxt").val("");
					}
				});
		  
			 }else{
				 alertX("Please enter your feedback.");
			 }
	  });
	
	
	 $( "#contactusMainDiv" ).draggable({ cursor: "move", cursorAt: { top: 0, left: 0 } });
	 
	 
		 $("#retriveFeedback").click(function(){
			 $("#fbTbl tbody tr").remove();
				$.ajax({
					type : "POST",
					url : "QuizAction",
					data : {
						method :'retriveFeedback'
					},
					success : function(dataList) {
						var data = jQuery.parseJSON(dataList);
						var str = "";
						if (data.length > 0) {
							$.each(data,function(i) {
								str = '<tr><td>'+(i+1)+'</td><td align="left">Submitted By::<span id="nameLbl_'+ data[i].split("#")[0]+'">'+
								data[i].split("#")[1]+'</span> | <a class="Email_contactAdmin" id="link_'+data[i].split("#")[0]+'">Contact Admin for this issue.</a><br>'+
								'<textarea disabled="disabled" id="fb_cmnt_'+data[i].split("#")[0]+'" rows="4" cols="85">'+data[i].split("#")[2]+'</textarea></td>'+
								'<td><input type="button" value="Solved" class="resolveFeedback" id="'+
								 data[i].split("#")[0]+'"></td></tr>';
								//alert(str);
								$("#rtrvfdbk table tbody").append(str);
							});
						}else{
							str = '<tr><td colspan="3">No Feedback received from Users</td></tr>';
							$("#fbTbl tbody").append(str);
						}
					}
				});
			  
		  });
	 
	 $('body').on('click', '.resolveFeedback',function(){
		 var sn =$(this).attr("id");
		$.ajax({
			type : "POST",
			url : "QuizAction",
			data : {
				method :'resolveFeedback',
				slNo:sn
			},
			success : function(msg) {
				$("#"+sn).hide();
			}
		});
	 });
	 
	 $('body').on('click', '.Email_contactAdmin',function(){
		 var slNo = $(this).attr("id").split("_")[1];
		
		 var empName = $("#nameLbl_"+slNo).val();
		 var body = $("#fb_cmnt_"+slNo).val();
		 var admin_Email =$("#adminMailAdd").val(); //"sarbeswar_sethi@infosys.com";
		 var subj = "Feedback of "+empName;
		 var link="mailto:"+admin_Email+"?subject="+subj+"&body="+body;
		 //alert(link);
		 $(this).attr("href",link);
	 });	 
	
		
});