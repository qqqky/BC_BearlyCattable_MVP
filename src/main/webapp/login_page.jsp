<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
          <head>
            <meta charset="utf-8">
          
            <title>Specialist Login BC</title>
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
		<div id="divAboveNav"> <img class="title_img" src="images/SpecialistLogin_Writing.svg" alt="Wall_Writing_SpecialistLogin"> </div>
	</div>

<!--end of header -->
	<div class="middle-div"> 
		<div id="divLeftNav"> Specialist Login </div>
<!--menu start -->
		<div id="nav">
			<div class="button-menu">
				<form name="home" method="get" action="index.html">
				<input type="submit" value="Home" /> </form>
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
		<div id="bottom-div-data">
			<table class="standard-2-field-input">
			<tbody>
			<tr>
			<td class="standard-input-form" colspan="2">
				<form name="spec-login-form" method="post" action="Login">
				<p class="table-input-fields">
				Enter your username and password to log in: <br/>
				 <br/>
				Username <input name="username" type="text" placeholder=" username" /> 
				Password <input name="password" type="password" placeholder=" password" /></p>
				<p class="table-button-long"><input type="submit" value="Login" /> </p>
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

String loginAnswer = (String)request.getAttribute("loginResp");

%>
<script>

var login= "<%= loginAnswer%>";
console.log("Login answer is: ", login);

function updateCustLine(){
	
	var resultNode = document.getElementsByClassName("feedback-field")[0];
	if(login !== "null" ){	
		resultNode.innerText = login;
	}else{
		resultNode.innerText = "";
	}
	
}
updateCustLine();

</script>
</body>
</html>