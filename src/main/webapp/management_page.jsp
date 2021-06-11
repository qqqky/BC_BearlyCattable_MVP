<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
          <head>
            <meta charset="utf-8">
          
            <title>Management Page BC</title>
            <meta name="author" content="qqqky">
            
            <link rel="stylesheet" href="management_styles.css">
			<link rel="shortcut icon" type="image/png" href="images/favicon.png"/>
            
            <style>
              body {background: url("background10.svg"); background-size: cover; overflow-x: hidden }
            </style>
          
          </head>
          
          <body>
<div class="header-div"> 
		<div id="logoDiv"> 
		<a href="index.html"><img class="logo" src="images/pileoftrashbigger.svg" alt="gibberish_pile"> </a>
		</div>
		<div id="divAboveNav"> <img class="title_img" src="images/ManagementClient_Writing.svg" alt="Wall_Writing_ManagementClient"> </div>
	</div>

<!--end of header -->
	<div class="middle-div"> 
		<div id="divLeftNav"> Amount of daily chores...
		</div>
<!--menu start -->
		<div id="nav">
		</div>
<!--menu end -->
	</div>
<!--end of middle -->
	<div class="bottom-div"> 
		<div class="table-div">
			<table id="specTable">
			<tbody>
			<tr>
				<th id="selectInfoField" colspan="3">Click on STATUS to select item</th>
				<th id="idInfoField" colspan="2">Specialist ID: 0</th>
				<th id="timeValueField" colspan="2">Time: 00:00</th>
			</tr>
			<tr>
				<td class="timeItem"><strong>Time</strong></td>
				<td class="statusItem-noborder"><strong>Status</strong></td>
				<td class="codeItem"><strong>Code</strong></td>
				<td class="timeItem"><strong>Time</strong></td>
				<td class="statusItem-noborder"><strong>Status</strong></td>
				<td class="codeItem"><strong>Code</strong></td>
				<td class="btnCell"><strong>Action</strong></td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y1">09:00</td>
				<td class="statusItem" id="status_X1Y1" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y1"></td>
				<td class="timeItem" id="time_X2Y1">13:00</td>
				<td class="statusItem" id="status_X2Y1" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y1"></td>
				<td class="btnCell"></td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y2">09:15</td>
				<td class="statusItem" id="status_X1Y2" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y2"></td>
				<td class="timeItem" id="time_X2Y2">13:15</td>
				<td class="statusItem" id="status_X2Y2" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y2"></td>
				<td class="btnCell"> </td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y3">09:30</td>
				<td class="statusItem" id="status_X1Y3" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y3"></td>
				<td class="timeItem" id="time_X2Y3">13:30</td>
				<td class="statusItem" id="status_X2Y3" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y3"></td>
				<td class="btnCell"> </td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y4">09:45</td>
				<td class="statusItem" id="status_X1Y4" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y4"></td>
				<td class="timeItem" id="time_X2Y4">13:45</td>
				<td class="statusItem" id="status_X2Y4" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y4"></td>
				<td class="btnCell"> </td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y5">10:00</td>
				<td class="statusItem" id="status_X1Y5" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y5"></td>
				<td class="timeItem" id="time_X2Y5">14:00</td>
				<td class="statusItem" id="status_X2Y5" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y5"></td>
				<td class="btnCell" rowspan="2">
					<form action="RefreshList" method="GET">
  					<input type="hidden" name="specId" value="" id="refreshId"/>
					<input type="submit" class="normBtn" value="Refresh List" />
					</form>
				</td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y6">10:15</td>
				<td class="statusItem" id="status_X1Y6" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y6"></td>
				<td class="timeItem" id="time_X2Y6">14:15</td>
				<td class="statusItem" id="status_X2Y6" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y6"></td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y7">10:30</td>
				<td class="statusItem" id="status_X1Y7" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y7"></td>
				<td class="timeItem" id="time_X2Y7">14:30</td>
				<td class="statusItem" id="status_X2Y7" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y7"></td>
				<td class="btnCell" rowspan="2">
					<form action="MarkFree" method="GET">
  					<input type="hidden" name="time" value="" id="freeTime"/>
  					<input type="hidden" name="status" value="" id="freeStatus"/>
  					<input type="hidden" name="code" value="" id="freeCode"/>
					<input type="submit" class="normBtn" value="Mark Free" />
					</form>
				</td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y8">10:45</td>
				<td class="statusItem" id="status_X1Y8" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y8"></td>
				<td class="timeItem" id="time_X2Y8">14:45</td>
				<td class="statusItem" id="status_X2Y8" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y8"></td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y9">11:00</td>
				<td class="statusItem" id="status_X1Y9" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y9"></td>
				<td class="timeItem" id="time_X2Y9">15:00</td>
				<td class="statusItem" id="status_X2Y9" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y9"></td>
				<td class="btnCell" rowspan="2">
					<form action="MarkStarted" method="GET">
  					<input type="hidden" name="time" value="" id="startedTime"/>
  					<input type="hidden" name="status" value="" id="startedStatus"/>
  					<input type="hidden" name="code" value="" id="startedCode"/>
					<input type="submit" class="normBtn" value="Mark Ongoing" />
					</form>
				</td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y10">11:15</td>
				<td class="statusItem" id="status_X1Y10" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y10"></td>
				<td class="timeItem" id="time_X2Y10">15:15</td>
				<td class="statusItem" id="status_X2Y10" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y10"></td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y11">11:30</td>
				<td class="statusItem" id="status_X1Y11" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y11"></td>
				<td class="timeItem" id="time_X2Y11">15:30</td>
				<td class="statusItem" id="status_X2Y11" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y11"></td>
				<td class="btnCell" rowspan="2">
					<form action="MarkEnded" method="GET">
  					<input type="hidden" name="time" value="" id="endedTime"/>
  					<input type="hidden" name="status" value="" id="endedStatus"/>
  					<input type="hidden" name="code" value="" id="endedCode"/>
					<input type="submit" class="normBtn" value="Mark Serviced" />
					</form>
				</td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y12">11:45</td>
				<td class="statusItem" id="status_X1Y12" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y12"></td>
				<td class="timeItem" id="time_X2Y12">15:45</td>
				<td class="statusItem" id="status_X2Y12" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y12"></td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y13">12:00</td>
				<td class="statusItem" id="status_X1Y13" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y13"></td>
				<td class="timeItem" id="time_X2Y13">16:00</td>
				<td class="statusItem" id="status_X2Y13" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y13"></td>
				<td class="btnCell" rowspan="2">
					<form action="MarkCancelled" method="GET">
  					<input type="hidden" name="time" value="" id="cancelledTime"/>
  					<input type="hidden" name="status" value="" id="cancelledStatus"/>
  					<input type="hidden" name="code" value="" id="cancelledCode"/>
					<input type="submit" class="normBtn" value="Mark Cancelled" />
					</form>
				</td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y14">12:15</td>
				<td class="statusItem" id="status_X1Y14" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y14"></td>
				<td class="timeItem" id="time_X2Y14">16:15</td>
				<td class="statusItem" id="status_X2Y14" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y14"></td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y15">12:30</td>
				<td class="statusItem" id="status_X1Y15" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y15"></td>
				<td class="timeItem" id="time_X2Y15">16:30</td>
				<td class="statusItem" id="status_X2Y15" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y15"></td>
				<td class="btnCell" rowspan="2"> 
					<form action="Logout" method="POST">
					<input type="submit" class="normBtn" value="Logout" />
					</form>
				</td>
			</tr>
			<tr>
				<td class="timeItem" id="time_X1Y16">12:45</td>
				<td class="statusItem" id="status_X1Y16" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X1Y16"></td>
				<td class="timeItem" id="time_X2Y16">16:45</td>
				<td class="statusItem" id="status_X2Y16" onclick="getCurrent(event)"></td>
				<td class="codeItem" id="code_X2Y16"></td>
			</tr>
			</tbody>
			</table>
		</div>		
	</div>
	
<!-- scriptlet -->
<%

int id = (Integer)session.getAttribute("id");
int size = (Integer)request.getAttribute("size");
String time = (String)request.getAttribute("time");

String statuses = "";
String codes = "";
String times = "";
String feedbackMng = "";

boolean cancelled = false;
boolean ended = false;
boolean started = false;
boolean refresh = false;
boolean free = false;

//flags indicating if client side update is needed
boolean updateNeeded = Boolean.valueOf((String)request.getAttribute("updateNeeded"));
if(updateNeeded){
	refresh = Boolean.valueOf((String)request.getAttribute("refresh"));
	cancelled = Boolean.valueOf((String)request.getAttribute("cancelled"));
	ended = Boolean.valueOf((String)request.getAttribute("ended"));
	started = Boolean.valueOf((String)request.getAttribute("started"));
	free = Boolean.valueOf((String)request.getAttribute("free"));
	
	if(refresh){
		statuses = (String)request.getAttribute("statuses");
		codes = (String)request.getAttribute("codes");
		times = (String)request.getAttribute("times");
		feedbackMng = (String)request.getAttribute("feedback");
	}
}

%>
<script>

var id =<%= id%>;  
/*console.log("id: ",id);*/
var size=<%= size%>; 
/*console.log("size: ",size);*/
var time="<%= time%>";
/*console.log("time: ", time);*/
var feedback = "<%= feedbackMng%>";

if(size>0){
var statuses = new String("<%= statuses%>");
var statArray = statuses.split(',');
/*console.log(statArray);*/
var times = new String("<%= times%>");
var timeArray = times.split(',');
/*console.log(timeArray);*/
var codes = new String("<%= codes%>");
var codeArray = codes.split(',');
/*console.log(codeArray);*/
}

var updateNeeded = <%= updateNeeded%>;
/*console.log("updateNeeded: ",updateNeeded);*/

setIDandTime(id, time);

if(updateNeeded){
	var refresh = <%= refresh%>;
	var cancelled = <%= cancelled%>;
	var ended = <%= ended%>;
	var started = <%= started%>;
		
/*!*/	
	if(refresh){
		refreshList();
	}
}

var selectionPostfix = "";
var time = "";
var status = "";
var code = "";
var specId = id;

function setIDandTime(id, time){
	document.getElementById("idInfoField").innerText = "Specialist ID: "+id;
	document.getElementById("refreshId").value = id;
	document.getElementById("timeValueField").innerText = "Time: "+time;
}
var undo = [];

function getCurrent(event){

	selectionPostfix = event.target.id.substring(7);

	/*undo effects of the prev selection*/
	if(undo.length !== 0){
		undo.forEach((item, index) => {
			document.getElementById(item).style.border = "";
			document.getElementById(item).style.backgroundColor = "#C4C4C4";
		})
		
	}

	var timeItem = document.getElementById("time_"+selectionPostfix);
	time = timeItem.innerText;
	var statusItem = document.getElementById("status_"+selectionPostfix);
	status = statusItem.innerText;
	var codeItem = document.getElementById("code_"+selectionPostfix);
	code = codeItem.innerText;
	
	/*add selection effects*/
	statusItem.style.backgroundColor = "#B95B5B";
	statusItem.style.borderLeft = "1px solid #B95B5B";
	statusItem.style.borderRight = "1px solid #B95B5B"; /*was #bbb*/
	timeItem.style.backgroundColor = "#B95B5B";
	codeItem.style.backgroundColor = "#B95B5B";
	var specId = document.getElementById("idInfoField").innerText.substring(15);
	undo[0] = "time_"+selectionPostfix;
	undo[1] = "status_"+selectionPostfix;
	undo[2] = "code_"+selectionPostfix;

	setAll(time, status, code);
   	     	
}

function setAll(time, status, code){
	document.getElementById("endedTime").value = time;
	document.getElementById("endedStatus").value = status;
	document.getElementById("endedCode").value = code;

	document.getElementById("cancelledTime").value = time;
	document.getElementById("cancelledStatus").value = status;
	document.getElementById("cancelledCode").value = code;

	document.getElementById("startedTime").value = time;
	document.getElementById("startedStatus").value = status;
	document.getElementById("startedCode").value = code;
	
	document.getElementById("freeTime").value = time;
	document.getElementById("freeStatus").value = status;
	document.getElementById("freeCode").value = code;

	var specId = document.getElementById("idInfoField").innerText.substring(15);

	if(document.getElementById("refreshId").value === ""){
		document.getElementById("refreshId").value = specId;
	}

}

function refreshList(){
	
	if(size!==null && size >0){
		var selectedStatus;
		var selectedCode;
		var selectedTime;
		var translated;
		
		for(let i=0; i<size; i++){
			if(i<16){
				translated = "X1Y"+(i+1);
			}else{
				translated = "X2Y"+(i-15);
			}
			
			selectedStatus = document.getElementById("status_"+translated);
			if(selectedStatus!== null){
				selectedStatus.innerText = statArray[i];
			}
			
			selectedCode = document.getElementById("code_"+translated);
			if(selectedCode!== null){
				selectedCode.innerText = codeArray[i];
			}
			
			selectedTime = document.getElementById("time_"+translated);
			if(selectedTime!== null){
				selectedTime.innerText = timeArray[i].slice(0,2)+":"+timeArray[i].slice(2);
			}
			
		}
		if(feedback!=="null" && feedback!==""){
			window.onload = doFeedbackPrompt;
		}		
	}
}
function doFeedbackPrompt(){
	alert(feedback);
}

	      	


</script>
</body>
</html>