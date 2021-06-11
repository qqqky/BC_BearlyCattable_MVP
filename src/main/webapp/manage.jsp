<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<style>

h2, h3, p {
text-align: center;
}

.background { 
 background: hsla(0, 0%, 75%, 1);
 opacity: 0.8;
}

#specTable {
text-align: center;
height: 80vh; width: 60vw; border: 1px solid black; margin-left: auto; margin-right: auto; 
background: hsla(0, 0%, 80%, 1);
}

.timeItem {
width: 15vw;
}

.statusItem {
border: 1px solid hsla(0, 0%, 80%, 1);
width: 20vw;
}

.codeItem {
width: 15vw;
}

.normBtn {
width: 8vw;
}



td.statusItem:hover {
 background-color: #bbb;
} 

</style>
</head>
<body>
<div class="background">
<br/>
<h2>Management client</h2>
<h3>Specialist &lt;name&gt;&nbsp;&lt;last_name&gt;</h3>
<p>&nbsp;</p>
<table id="specTable">
<tbody>
<tr>
<td id="selectInfoField" colspan="4">Click on STATUS to select item:&nbsp;</td>
<td id="idInfoField" colspan="2">Specialist&nbsp;ID:&nbsp;&lt;id&gt;</td>
<td id="timeInfoField">&nbsp;Current&nbsp;time:&nbsp;</td>
<td id="timeValueField" colspan="2">&nbsp;&lt;time&gt;</td>
</tr>
<tr>
<td class="timeItem" id="time_0900">09:00</td>
<td class="statusItem" id="status_0900" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_0900"></td>
<td rowspan="16"></td>
<td class="timeItem" id="time_1300">13:00</td>
<td class="statusItem" id="status_1300" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1300"></td>
<td rowspan="16"></td>
<td rowspan="11"></td>
</tr>
<tr>
<td class="timeItem" id="time_0915">09:15</td>
<td class="statusItem" id="status_0915" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_0915"></td>
<td class="timeItem" id="time_1315">13:15</td>
<td class="statusItem" id="status_1315" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1315"></td>
</tr>
<tr>
<td class="timeItem" id="time_0930">09:30</td>
<td class="statusItem" id="status_0930" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_0930"></td>
<td class="timeItem" id="time_1330">13:30</td>
<td class="statusItem" id="status_1330" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1330"></td>
</tr>
<tr>
<td class="timeItem" id="time_0945">09:45</td>
<td class="statusItem" id="status_0945" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_0945"></td>
<td class="timeItem" id="time_1345">13:45</td>
<td class="statusItem" id="status_1345" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1345"></td>
</tr>
<tr>
<td class="timeItem" id="time_1000">10:00</td>
<td class="statusItem" id="status_1000" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1000"></td>
<td class="timeItem" id="time_1400">14:00</td>
<td class="statusItem" id="status_1400" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1400"></td>
</tr>
<tr>
<td class="timeItem" id="time_1015">10:15</td>
<td class="statusItem" id="status_1015" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1015"></td>
<td class="timeItem" id="time_1415">14:15</td>
<td class="statusItem" id="status_1415" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1415"></td>
</tr>
<tr>
<td class="timeItem" id="time_1030">10:30</td>
<td class="statusItem" id="status_1030" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1030"></td>
<td class="timeItem" id="time_1430">14:30</td>
<td class="statusItem" id="status_1430" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1430"></td>
</tr>
<tr>
<td class="timeItem" id="time_1045">10:45</td>
<td class="statusItem" id="status_1045" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1045"></td>
<td class="timeItem" id="time_1445">14:45</td>
<td class="statusItem" id="status_1445" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1445"></td>
</tr>
<tr>
<td class="timeItem" id="time_1100">11:00</td>
<td class="statusItem" id="status_1100" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1100"></td>
<td class="timeItem" id="time_1500">15:00</td>
<td class="statusItem" id="status_1500" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1500"></td>
</tr>
<tr>
<td class="timeItem" id="time_1115">11:15</td>
<td class="statusItem" id="status_1115" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1115"></td>
<td class="timeItem" id="time_1515">15:15</td>
<td class="statusItem" id="status_1515" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1515"></td>
</tr>
<tr>
<td class="timeItem" id="time_1130">11:30</td>
<td class="statusItem" id="status_1130" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1130"></td>
<td class="timeItem" id="time_1530">15:30</td>
<td class="statusItem" id="status_1530" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1530"></td>
</tr>
<tr>
<td class="timeItem" id="time_1145">11:45</td>
<td class="statusItem" id="status_1145" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1145"></td>
<td class="timeItem" id="time_1545">15:45</td>
<td class="statusItem" id="status_1545" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1545"></td>
<td class="btnCell">
	<form action="RefreshList" method="GET">
  		<input type="hidden" name="specId" value="" id="refreshId"/>
		<input type="submit" class="normBtn" value="Refresh List" />
	</form>
</td>
</tr>
<tr>
<td class="timeItem" id="time_1200">12:00</td>
<td class="statusItem" id="status_1200" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1200"></td>
<td class="timeItem" id="time_1600">16:00</td>
<td class="statusItem" id="status_1600" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1600"></td>
<td class="btnCell">
	<form action="MarkStarted" method="GET">
  		<input type="hidden" name="time" value="" id="startedTime"/>
  		<input type="hidden" name="status" value="" id="startedStatus"/>
  		<input type="hidden" name="code" value="" id="startedCode"/>
		<input type="submit" class="normBtn" value="Mark started" />
	</form>
</td>
</tr>
<tr>
<td class="timeItem" id="time_1215">12:15</td>
<td class="statusItem" id="status_1215" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1215"></td>
<td class="timeItem" id="time_1615">16:15</td>
<td class="statusItem" id="status_1615" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1615"></td>
<td class="btnCell">
 <form action="MarkCancelled" method="GET">
  		<input type="hidden" name="time" value="" id="cancelledTime"/>
  		<input type="hidden" name="status" value="" id="cancelledStatus"/>
  		<input type="hidden" name="code" value="" id="cancelledCode"/>
		<input type="submit" class="normBtn" value="Mark cancelled" />
	</form>
</td>
</tr>
<tr>
<td class="timeItem" id="time_1230">12:30</td>
<td class="statusItem" id="status_1230" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1230"></td>
<td class="timeItem" id="time_1630">16:30</td>
<td class="statusItem" id="status_1630" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1630"></td>
<td class="btnCell">
  <form action="MarkEnded" method="GET">
  		<input type="hidden" name="time" value="" id="endedTime"/>
  		<input type="hidden" name="status" value="" id="endedStatus"/>
  		<input type="hidden" name="code" value="" id="endedCode"/>
		<input type="submit" class="normBtn" value="Mark ended" />
	</form>
</td>
</tr>
<tr>
<td class="timeItem" id="time_1245">12:45</td>
<td class="statusItem" id="status_1245" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1245"></td>
<td class="timeItem" id="time_1645">16:45</td>
<td class="statusItem" id="status_1645" onclick="getCurrent(event)"></td>
<td class="codeItem" id="code_1645"></td>
<td class="btnCell"> 
	<form action="Logout" method="POST">
		<input type="submit" class="normBtn" value="Logout" />
	</form>
</td>
</tr>
</tbody>
</table>
<br/>
</div>
<% 
	
	int id = (Integer)session.getAttribute("id");
	int size = (Integer)request.getAttribute("size");
	String time = (String)request.getAttribute("time");
	
	String statuses = "";
	String codes = "";
	String times = "";
	
	boolean cancelled = false;
	boolean ended = false;
	boolean started = false;
	boolean refresh = false;
	//flags indicating if client side update is needed
	boolean updateNeeded = Boolean.valueOf((String)request.getAttribute("updateNeeded"));
	if(updateNeeded){
		refresh = Boolean.valueOf((String)request.getAttribute("refresh"));
		cancelled = Boolean.valueOf((String)request.getAttribute("cancelled"));
		ended = Boolean.valueOf((String)request.getAttribute("ended"));
		started = Boolean.valueOf((String)request.getAttribute("started"));
		
		if(refresh){
			statuses = (String)request.getAttribute("statuses");
			codes = (String)request.getAttribute("codes");
			times = (String)request.getAttribute("times");
		}
	}
	
	
	
		
%>
<script>
        
	    var id =<%= id%>;  
	    var size=<%= size%>; 
	    var time="<%= time%>";
	    
	    if(size>0){
	    var statuses = new String("<%= statuses%>");
	    var statArray = statuses.split(',');
	    console.log(statArray);
	    var times = new String("<%= times%>");
	    var timeArray = times.split(',');
	    console.log(timeArray);
	    var codes = new String("<%= codes%>");
	    var codeArray = codes.split(',');
	    console.log(codeArray);
	    }
	    
	    var updateNeeded = <%= updateNeeded%>;
	    
	    if(updateNeeded){
	    	var refresh = <%= refresh%>;
	    	var cancelled = <%= cancelled%>;
	    	var ended = <%= ended%>;
	    	var started = <%= started%>;
	   		
	    	setIdField(id);
	    	if(refresh){
	    		refreshList();
	    	}
	    }
	    
		var selectionPostfix = "";
		var time = "";
		var status = "";
		var code = "";
		var specId = document.getElementById("idInfoField").innerText.substring(15);
		
		function setIdField(id){
			document.getElementById("idInfoField").innerText = "Specialist ID: "+id;
			document.getElementById("refreshId").value = id;
			document.getElementById("timeValueField").innerText = time;
		}
        function getCurrent(event){
        	
        	if(status !== ""){
        		document.getElementById("status_"+selectionPostfix).style.border = "";
        		document.getElementById("status_"+selectionPostfix).style.backgroundColor = "hsla(0, 0%, 80%, 1)";
        	}
        	
        	
        	selectionPostfix = event.target.id.substring(7);
        	var timeItem = document.getElementById("time_"+selectionPostfix);
        	time = timeItem.innerText;
        	var statusItem = document.getElementById("status_"+selectionPostfix);
        	statusItem.style.border = "1px solid blue";
        	statusItem.style.backgroundColor = "#bbb";
        	status = statusItem.innerText;
        	var codeItem = document.getElementById("code_"+selectionPostfix);
        	code = codeItem.innerText;
        	
        	
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
        	
        	if(document.getElementById("refreshId").value === ""){
        		document.getElementById("refreshId").value = specId;
        	}
        	
        }
       
 		function refreshList(){
        	
        	if(size!==null && size >0){
        		
        		var selectedStatus;
        		var selectedCode;
        		
        		for(let i=0; i<size; i++){
        			
        			selectedStatus = document.getElementById("status_"+timeArray[i]);
        			if(selectedStatus!== null){
        				selectedStatus.innerText = statArray[i];
        			}
        			
        			selectedCode = document.getElementById("code_"+timeArray[i]);
        			if(selectedCode!== null){
        				selectedCode.innerText = codeArray[i];
        			}
        			
        		}
        				
        	}
 		}
        	      	
        
    </script>
</body>
</html>