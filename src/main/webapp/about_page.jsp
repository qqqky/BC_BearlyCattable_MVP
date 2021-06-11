<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
          <head>
            <meta charset="utf-8">
          
            <title>About BearlyCattable</title>
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
		<a href="index.html"><img class="logo" src="images/catlogo.svg" alt="BC_cat_main_logo"> </a>
		</div>
		<div id="divAboveNav"> <img class="title_img" src="images/BearlyCattable_Writing.svg" alt="Wall_Writing_Barely_Catable"> </div>
	</div>

<!--end of header -->
	<div class="middle-div-about-page"> 
		<div id="divLeftNav"></div>
<!--menu start -->
		<div id="nav" class="about">
			<div class="button-menu">
				<form name="home" method="get" action="index.html">
				<input type="submit" value="Home" /> </form>
			</div>
		</div>
<!--menu end -->
	</div>
<!--end of middle -->
	<div class="bottom-div-about-page">
		<div>
			<div class="about-page-heading"><h1> About the project </h1></div>
			<div class="about-page-text"><p>This is a simulation of a live customer service queue.<br/><br/>
				A day starts in customer service office and customers can register themselves by providing their details. For the sake of simulation, customers can also be spawned by clicking the spawn button. In all cases, best time slot available will be offered. If more than one specialist has the same best time slot available, a specialist will be chosen randomly (fair).<br/>
				<br/>
				<strong>Customers:</strong><br/>
				Customers <strong>must remember the code received</strong> upon a successful registration. They can do the following actions: <br/>
					1. Make a reservation<br/>
					2. Cancel their reservation<br/>
					3. See how much time is left to wait until their appointment starts<br/>
	
				<br/>
				<strong>Specialists:</strong>
				<br/>
				1. Currently, 5 specialists are simulated (specialists cannot be added or removed yet). They can manage their own clients in the specialist management page (requires login).<br/>
				2. <strong>Specialists cannot register clients directly, only change their status.</strong><br/>
				3. All names of current specialists are also their usernames and are as follows: <strong>Amy, Betty, Edward, Emma, Gary</strong>.<br/>
				4. All specialist passwords currently are: <strong>12345</strong><br/>
				5. Admin password for resetting the database (to play around) is: <strong>54321</strong><br/>
				6. Some management rules are enforced to simulate real-world environment. In most cases, <strong>margin of error allowed is +/-10mins.</strong><br/>
				Explanation: a customer might be a bit early or late, or a specialist might make a mistake and want to fix the field, etc.)<br/>
				<br/><strong>Customer management rules are as follows: </strong><br/>
					&nbsp;&nbsp;&bull; <strong>Specialist can only have 1 "ONGOING" appointment</strong> at one time and only "ONGOING" appointments can be marked "SERVICED". This means the window of this functionality will be narrow as it also has to fall around the supported error margin from the intended start of appointment.<br/>
					&nbsp;&nbsp;&bull; Behind the scenes, <strong>all successfully "SERVICED" customers will be saved to a separate log table</strong> (reset does not affect that table).<br/>
					&nbsp;&nbsp;&bull; If an existing appointment (or any unoccupied time) is already over, its status cannot be changed manually (usually indicated by status NONE). <br/>
					&nbsp;&nbsp;&bull; <strong>Status NONE represents that a particular time has already passed and no customers had been serviced.  </strong>This status is assigned automatically only if a time slot beforehand was of status FREE or RESERVED (indicating that either the time was FREE at
					some point or a customer had registered, but failed to show up). <strong>Status NONE is immutable.</strong><br/>
					&nbsp;&nbsp;&bull; <strong>A specialist can mark any time CANCELLED, unless it is of status NONE or status SERVICED</strong>.<br/>
					&nbsp;&nbsp;&bull; <strong>Only FREE time slots are eligible for the registrations</strong>. This means upon "CANCELLING" a visit, a specialist should then decide whether to mark the time slot "FREE" (so a new customer can potentially register) or keep it "CANCELLED".
					Visits cancelled by customers themselves will, however, be marked FREE (if time slot is still valid).<br/>
					
					&nbsp;&nbsp;&bull; Most operations cannot be performed if the office is CLOSED.
			</p></div>	
		</div>
	</div>
</body>
</html>