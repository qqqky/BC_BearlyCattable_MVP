<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style> 

body {
background: hsla(0, 0%, 70%, 0.75);
}

.background { 
 background: hsla(0, 0%, 80%, 1);
 opacity: 0.8;
}

.cell {
width: 10vw;
}

h2 {
text-align: center;
}


#specialistTable {
border-collapse: collapse; text-align: center;
height: 37vh; width: 30vw; border: 1px solid black; margin-left: auto; margin-right: auto; 
background: hsla(0, 0%, 80%, 1);
}

#customerTable {
border-collapse: collapse; text-align: center;
height: 30vh; margin-left: auto; margin-right: auto;
background: hsla(0, 0%, 80%, 1);
}

#loginTable {
border-collapse: collapse; text-align: center;
height: 30vh; margin-left: auto; margin-right: auto; 
background: hsla(0, 0%, 80%, 1);
}


#specrow > th {
height: 7vh;
border: 3px solid #777;

}

</style>
</head>
<body>
<!-- ####### #########-->
<div class = "background">

<h2>
<br/>Customer waiting view<br/></h2>
<hr/>
<table id="specialistTable">
<tbody>
<tr id="specrow">
<th class="cell">1</th>
<th class="cell">2</th>
<th class="cell">3</th>
<th class="cell">4</th>
<th class="cell">5</th>
</tr>
<tr>
<td class="cell">&nbsp;<a id="X1Y1"></a></td>
<td class="cell">&nbsp;<a id="X2Y1"></a></td>
<td class="cell">&nbsp;<a id="X3Y1"></a></td>
<td class="cell">&nbsp;<a id="X4Y1"></a></td>
<td class="cell">&nbsp;<a id="X5Y1"></a></td>
</tr>
<tr>
<td class="cell">&nbsp;<a id="X1Y2"></a></td>
<td class="cell">&nbsp;<a id="X2Y2"></a></td>
<td class="cell">&nbsp;<a id="X3Y2"></a></td>
<td class="cell">&nbsp;<a id="X4Y2"></a></td>
<td class="cell">&nbsp;<a id="X5Y2"></a></td>
</tr>
<tr>
<td class="cell">&nbsp;<a id="X1Y3"></a></td>
<td class="cell">&nbsp;<a id="X2Y3"></a></td>
<td class="cell">&nbsp;<a id="X3Y3"></a></td>
<td class="cell">&nbsp;<a id="X4Y3"></a></td>
<td class="cell">&nbsp;<a id="X5Y3"></a></td>
</tr>
<tr>
<td class="cell">&nbsp;<a id="X1Y4"></a></td>
<td class="cell">&nbsp;<a id="X2Y4"></a></td>
<td class="cell">&nbsp;<a id="X3Y4"></a></td>
<td class="cell">&nbsp;<a id="X4Y4"></a></td>
<td class="cell">&nbsp;<a id="X5Y4"></a></td>
</tr>
<tr>
<td class="cell">&nbsp;<a id="X1Y5"></a></td>
<td class="cell">&nbsp;<a id="X2Y5"></a></td>
<td class="cell">&nbsp;<a id="X3Y5"></a></td>
<td class="cell">&nbsp;<a id="X4Y5"></a></td>
<td class="cell">&nbsp;<a id="X5Y5"></a></td>
</tr>
<tr>
<td id="refreshBtn" colspan="2">
 <form name="refresh" method="get" action="RefreshCust" >
   <input type="submit" value="Refresh view" />
   </form>
  </td>
<td id="current-time" colspan="3"></td>
 
</tr>
</tbody>
</table>
<hr/>
<p style="font-size: 1.5em; text-align: center;">Welcome to customer options:<span style="font-size: 14px;">&nbsp;</span></p>
<table id="customerTable">
<tbody>
<tr>
<td>
	<form name="registration-form" method="get" action="Register" >
	<p><strong>Customer self-registration</strong><br /> 
	<br/>First name <br />
	 <input name="reg-first-name" type="text" /> 
	<br/>Last name <br /> <input name="reg-last-name" type="text" /></p>
	<p><br /> <input type="submit" value="Register me" /></p>
	</form>
</td>
<td>
	<form name="cancel-form" method="get" action="CancelVisit">
	<p><strong>Cancel your visit</strong><br />
	<br />Reservation code <br /> <input name="cancel-code" type="text" /> 
	<br/>Last name <br /> <input name="cancel-last-name" type="text" /></p>
	<p><br /> <input type="submit" value="Cancel my visit" /></p>
	</form>
</td>
<td>
	<form name="waiting-time-form" method="get" action="WaitingTime">
	<p><strong>Get waiting time</strong><br /> 
	<br />Reservation code <br /> <input name="waiting-code" type="text" /> 
	<br/>Last name <br /> <input name="waiting-last-name" type="text" /></p>
	<p><br /> <input type="submit" value="See waiting time" /></p>
	</form>
</td>
</tr>
</tbody>
</table>
<p id="custResultLine" style="text-align: center;"><strong>----------------------</strong></p>
<table id="loginTable">
<tbody>
<tr>
<td style="width: 295.2px; border-color: black; text-align: center;">
<form id="spec-login-form" method="post" action="Login">
	Specialist Login <br /> <br /> 
	Username<br />
		<input name="username" type="text" /> <br /> 
	Password<br />
		<input name="password" type="password" /> <br /> <br />
		<input type="submit" value="Login" /></form></td>
</tr>
</tbody>
</table>
<p style="text-align: center;"><strong>-----------------------result placeholder line----------------------</strong></p>
<p style="text-align: center;">&nbsp;</p>
</div>
<%
	boolean changed = Boolean.valueOf((String)request.getAttribute("changed"));

	boolean wait = false;
	boolean reg = false;
	boolean cancel = false;
	String regAnswer = "";
	String cancAnswer = "";
	String waitAnswer = "";
	String logAnswer = "";
	String time = "";
	String gui = "";
	
	if(changed){
		reg = Boolean.valueOf((String)request.getAttribute("reg"));
		regAnswer = (String)request.getAttribute("regResp");
		wait = Boolean.valueOf((String)request.getAttribute("waiting"));
		waitAnswer = (String)request.getAttribute("waitResp");
		cancel = Boolean.valueOf((String)request.getAttribute("cancelled"));
		cancAnswer = (String)request.getAttribute("cancelResp");
	}
	gui = (String)request.getAttribute("gui");
	time = (String)request.getAttribute("time");

%>
<script>

	var isCancel = <%= cancel%>;
	var isWait = <%= wait%>;
	var isReg = <%= reg%>;
	var changed = <%= changed%>;
	
	var reg = "<%= regAnswer%>";
	var code;
	if(reg !== "" && reg.length<20){
		
		var arr = new String("<%= regAnswer%>").split(',');
		reg = "";
		for(let i=0; i<arr.length-1; i++){
			if(i<arr.length-2){
				reg = reg + arr[i] + " ";
			}else{
				reg = reg + arr[i];
			}	
		}
		code = arr[arr.length-1];
		reg = "Registration successful for "+reg+". Your code is: "+code;
	}
	console.log("Reg response is: ",reg);

	
	var wait= "<%= waitAnswer%>";
	console.log("Wait response is: ", wait);

	var cancel = "<%= cancAnswer%>";
	console.log("Cancel response is: ", cancel);
	
	var time= "<%= time%>";
	console.log("Time is: ", time);
	
	var gui = new String("<%= gui%>");
	
	if(changed){
		updateCustLine();
		updateGUI();
	}
	
	function updateCustLine(){
		
		var node = document.getElementById("custResultLine");
		node.style.fontWeight = "bold";
		if(isReg){
			node.innerText = reg;
		}else if(isWait){
			node.innerText = wait;
		}else if(isCancel){
			node.innerText = cancel;
		}
	}
	function updateGUI(){
		var arr = gui.split(".");
		console.log("GUI Array is: ", arr);
		var one = arr[0].split(",");
			for(let i=0; i<one.length; i++){
				document.getElementById("X1Y"+(i+1)).innerText = one[i];
			}
		var two = arr[1].split(",");
			for(let i=0; i<two.length; i++){
				document.getElementById("X2Y"+(i+1)).innerText = two[i];
		}
		var three = arr[2].split(",");
			for(let i=0; i<three.length; i++){
				document.getElementById("X3Y"+(i+1)).innerText = three[i];
		}
		var four = arr[3].split(",");
			for(let i=0; i<four.length; i++){
				document.getElementById("X4Y"+(i+1)).innerText = four[i];
		}
		var five = arr[4].split(",");
			for(let i=0; i<five.length; i++){
				document.getElementById("X5Y"+(i+1)).innerText = five[i];
		}
		document.getElementById("current-time").innerText = "Current time: "+time;
	}

</script>
</body>
</html>