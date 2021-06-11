<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
          <head>
            <meta charset="utf-8">
          
            <title>Customer View BC</title>
            <meta name="author" content="qqqky">   
            <link rel="stylesheet" href="index_styles.css">
            <link rel="shortcut icon" type="image/png" href="images/favicon.png"/>
            
            <style>
              body {background: url("background10.svg"); background-size: cover; overflow-x: hidden }
            </style>
          </head>
          
<body>
	<div class="header-div"> 
		<div id="logoDiv"> 
		<a href="index.html"><img class="logo" src="images/catlogo_withBC.svg" alt="main_cat_logo_BC"> </a>
		</div>
		<div id="divAboveNav"> <img class="title_img" src="images/BearlyCattable_Writing.svg" alt="Wall_Writing_BarelyCattable"> 
		</div>
	</div>

<!--end of header -->
	<div class="middle-div"> 
		<div id="divLeftNav"> Live Queue View </div>
<!--menu start -->
		<div id="nav">
			<div class="button-menu">
				<form name="login" method="get" action="login_page.jsp">
				<input type="submit" value="Login" /> </form>
			</div>
			<div class="button-menu">
				<form name="register" method="get" action="registration_page.jsp">
   				<input type="submit" value="Register" /> </form>
			</div>
			<div class="button-menu">
				<form name="cancel" method="get" action="cancel_reservation_page.jsp">
   				<input type="submit" value="Cancel" /> </form>
			</div>
			<div class="button-menu">
				<form name="wait_time" method="get" action="waiting_time_page.jsp">
   				<input type="submit" value="Wait Time" /> </form>
			</div>	
		</div>
<!--menu end -->
	</div>
<!--end of middle -->
	<div class="bottom-div">
		<div id="divAbout">
			<div class="main-page-btn"> <div class="wrapper-div"> <a> <form name="Github" action="https://github.com/qqqky"> <input type="submit" value="Github" />  </form> </a> </div> </div>
			<div class="main-page-btn"> <div class="wrapper-div"> <form class="about-input" name="About" method="get" action="about_page.jsp"> <input class="about-input" type="submit" value="About" /> </form> </div> </div>
			<div> <img class="click_for_info_text" src="images/ClickForInfoWithArrow_small_Writing.svg" alt="Wall_Writing_ClickForInfoArrow"> </div>
 			<div> <div class ="work-hours">Current office hours:<br/>09:00 - 17:00</div> </div>
 			<div><img id="open-closed-img" class="OPEN_CLOSED_img" src="images/status_OPEN.svg" alt="image_missing"></div>	
 		</div>
		<div id="div-empty-20"> </div>
<!--table view start -->
		<div id="divMiddle">
			<div class="table-div">
				<table id="specialistTable">
				<tbody>
				<tr id="specrow">
					<th><div class="redRect">1</div></th>
					<th><div class="redRect">2</div></th>
					<th><div class="redRect">3</div></th>
					<th><div class="redRect">4</div></th>
					<th><div class="redRect">5</div></th>
				</tr>
				<tr>
					<td class="cell"><a id="X1Y1"></a></td>
					<td class="cell"><a id="X2Y1"></a></td>
					<td class="cell"><a id="X3Y1"></a></td>
					<td class="cell"><a id="X4Y1"></a></td>
					<td class="cell"><a id="X5Y1"></a></td>
				</tr>
				<tr>
					<td class="cell"><a id="X1Y2"></a></td>
					<td class="cell"><a id="X2Y2"></a></td>
					<td class="cell"><a id="X3Y2"></a></td>
					<td class="cell"><a id="X4Y2"></a></td>
					<td class="cell"><a id="X5Y2"></a></td>
				</tr>
				<tr>
					<td class="cell"><a id="X1Y3"></a></td>
					<td class="cell"><a id="X2Y3"></a></td>
					<td class="cell"><a id="X3Y3"></a></td>
					<td class="cell"><a id="X4Y3"></a></td>
					<td class="cell"><a id="X5Y3"></a></td>
				</tr>
				<tr>
					<td class="cell"><a id="X1Y4"></a></td>
					<td class="cell"><a id="X2Y4"></a></td>
					<td class="cell"><a id="X3Y4"></a></td>
					<td class="cell"><a id="X4Y4"></a></td>
					<td class="cell"><a id="X5Y4"></a></td>
				</tr>
				<tr>
					<td class="cell"><a id="X1Y5"></a></td>
					<td class="cell"><a id="X2Y5"></a></td>
					<td class="cell"><a id="X3Y5"></a></td>
					<td class="cell"><a id="X4Y5"></a></td>
					<td class="cell"><a id="X5Y5"></a></td>
				</tr>
			<!--	<tr class="lastRow">
					<td id="current-time" colspan="3">Current time: </td>
					<td id="time-value" colspan="2"></td> 
				</tr>
			-->
				</tbody>
				</table>
 			</div> <!--table view end -->

			<div class="under-table-div-flex">
				<div id="refreshBtn" class="refresh-div"> <form name="RefreshList" method="get" action="RefreshCust"> <input type="submit" value="Refresh List" /> </form> </div>
				<div id="spawnBtn" class="refresh-div"> <form name="SpawnCustomer" method="get" action="Spawn"> <input type="submit" value="Spawn Visitor" /> </form> </div>
			</div>
			<div class="empty-div-middle"></div>
		</div>
		<div id="divTime">
			<div class="time-div">
				<div class="wrapper-div"> Current Time: </div>
				<div class="wrapper-div"> <p id="current-time-value"> 00:00 </p> </div>
			</div>
			<div class="reset-div">
				<div class="delimiter-div"> </div>
				<div> (admin password required): </div>	
				<div class="delimiter-div"> </div>
				<div class="wrapper-div"> <img class="logo" src="images/exclamation_mark_reset_reflective.svg" alt="exclamation_mark_image"></div>
				<div class="table-input-fields">
					<form name="reset-password-form" method="post" action="Reset">
					<input name="pass-reset" type="password" placeholder=" password" />
					<br/>
					<input type="submit" value="Reset Database" />
					</form>
				</div>
				<div class="delimiter-div"> </div>
				<div class="delimiter-div"> </div>
			</div>
			
		</div>
<!--end of bottom -->	
</div>

<%
	
	String time = "";
	String gui = "";
	String feedbackMain = "";
	
	gui = (String)request.getAttribute("gui");
	time = (String)request.getAttribute("time");
	feedbackMain = (String)request.getAttribute("feedback");

%>
<script>

	
	var time= "<%= time%>";
	/*console.log("Time is: ", time);*/
	
	var gui = new String("<%= gui%>");
	var feedback = "<%= feedbackMain%>";
	
	
	function updateGUI(){
		var arr = gui.split(".");
		/*console.log("GUI Array is: ", arr);*/
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
		document.getElementById("current-time-value").innerText = time;
	}
	
	function updateFeedback(){
		var feedbackNode = document.getElementsByClassName("empty-div-middle")[0];
		if(feedback !== null){
			feedbackNode.innerText = feedback;
		}
		
	}
	function updateOpenClosedIndicator(){
		/*console.log("Server time is: ",time);*/
		
		var serviceHours = document.getElementsByClassName("work-hours")[0].innerText;
		var parsedOpen = serviceHours.substring(serviceHours.indexOf("-")-6, serviceHours.indexOf("-")-1);
		/*console.log("Open from: ", parsedOpen);*/
		var parsedClosed = serviceHours.substring(serviceHours.lastIndexOf(":")-2);
		/*console.log("Closed at: ", parsedClosed);*/
		var timeCurrent = new Date().setHours(time.slice(0,2), time.slice(3), 0);
		var timeOpen = new Date().setHours(parsedOpen.slice(0,2), parsedOpen.slice(3), 0);
		var timeClosed = new Date().setHours(parsedClosed.slice(0,2), parsedClosed.slice(3), 0);
		
		var openclosed = document.getElementById("open-closed-img");
		
		if(timeCurrent < timeClosed && timeCurrent > timeOpen){	
			openclosed.src = "images/status_OPEN.svg";
		}else{
			openclosed.src = "images/status_CLOSED.svg";
		}
		
	}
	
	updateGUI();
	updateFeedback();
	updateOpenClosedIndicator();
	

</script>

</body>
</html>