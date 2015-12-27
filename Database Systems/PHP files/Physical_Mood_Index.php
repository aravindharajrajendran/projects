<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Physical Mood</title>
<link type="text/css" media="all" rel="stylesheet" href="style.css">
<!--Import Google Icon Font-->
<link href="http://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<!--Import materialize.css-->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.1/css/materialize.min.css">
<script type="text/javascript"
	src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.1/js/materialize.min.js"></script>
<style type="text/css">
body {
	background-repeat: no-repeat;
	background-attachment: scroll;
	background-position: 0% 0%;
	background-size: cover;
}

.display {
	top: 50%;
	left: 50%;
	transform: translate3d(-50%, -50%, 0);
	position: absolute;
	width: 440px;
}
</style>
</head>

<body background="images/bg.jpg">
	<div class="display">
		<form class="col s12" action="PhysicalMood.php" method="post"
			name="insertform">
			<div class="row">
				<div class="input-field col s12">
					<input type="text" name="userid" class="validate" id="inputid" readonly value="<?php if(isset($_GET['user_id'])){echo $_GET['user_id'];}?>" />
					<label for="userid" id="preinput"><font size="4">User ID</font></label>
				</div>
			</div>
			<div class="row">
				<div class="input-field col s12">
					<p style="padding-top: 20px;">
						<input name="group1" type="radio" id="exhausted" value="1" /> <label for="exhausted">Exhausted</label>
						&nbsp;&nbsp;&nbsp; 
							<input
							name="group1" type="radio" id="tired" value="2"/> <label for="tired">Tired</label> <input
							name="group1" type="radio" id="normal" value="3"/> <label for="normal">Normal</label>&nbsp; <input
							name="group1" type="radio" id="active" value="4"/> <label for="active">Active</label> <input
							name="group1" type="radio" id="energetic" value="5"/> <label for="excited">Energetic</label>
					</p>
					<label for="group1"><font size="4">Mood Condition</font></label>
					</div>
				</div>
				<div class="row">
				<div class="input-field col s12">
					<h7> Time </h7>
					<input type="datetime-local" name="moodtime"/> <label for="moodtime"><font size="4"> </font></label>
				</div>
				</div>
				<div align="center">
					<button class="btn waves-effect waves-light" type="submit"
					name="send" id="inputid1">Submit</button>
			</div>
		</form>
		</div>
	</div>
</body>
</html>