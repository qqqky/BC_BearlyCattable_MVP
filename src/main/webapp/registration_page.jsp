<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
          <head>
            <meta charset="utf-8">
          
            <title>Customer Registration BC</title>
            <meta name="author" content="qqqky">
            
            <link rel="stylesheet" href="helper_styles.css">
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
		<div id="divAboveNav"> <img class="title_img" src="images/MakeAReservation_Writing.svg" alt="Wall_Writing_MakeAReservation"> </div>
	</div>

<!--end of header -->
	<div class="middle-div"> 
		<div id="divLeftNav"> Registration Page </div>
<!--menu start -->
		<div id="nav">
			<div class="button-menu">
				<form name="home" method="get" action="index.html">
				<input type="submit" value="Home" /> </form>
			</div>
			<div class="button-menu">
				<form name="login" method="get" action="login_page.jsp">
   				<input type="submit" value="Login" /> </form>
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
		<div id="bottom-div-data">
			<table class="standard-2-field-input">
			<tbody>
			<tr>
			<td class="standard-input-form" colspan="2">
				<form name="registration-form" method="get" action="Register">
				<p class="table-input-fields">
				Enter your first and last name to register: <br/>
				 <br/>
				Name <input name="reg-first-name" type="text" placeholder=" first name" /> 
				Last Name <input name="reg-last-name" type="text" placeholder=" last name" /></p>
				<p class="table-button-long"><input type="submit" value="Register Me" /> </p>
				<p class="feedback-field"></p>
				</form>
				
			</td>
			</tr>
			</tbody>
			</table>
		</div>
	</div>
<!-- scriptlet -->
<%

 String regAnswer = (String)request.getAttribute("regResp");
 String isSuccessful = (String)request.getAttribute("successful");

%>
<script>
var reg = "<%= regAnswer%>";
var isSuccessful = "<%= isSuccessful%>";

if(reg !== null && reg !== "" && isSuccessful==="true"){
	
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
	reg = "Registration successful for "+reg+".\nYour code is: "+code;
}

console.log("Reg response is: ",reg);
console.log("Is successful response is: ",isSuccessful);

function updateCustLine(){
	
	var resultNode = document.getElementsByClassName("feedback-field")[0];
	if(isSuccessful !== "null" && reg !== "null" ){	
		resultNode.innerText = reg;
	}else{
		resultNode.innerText = "";
	}
	
}
updateCustLine();

</script>
</body>
</html>