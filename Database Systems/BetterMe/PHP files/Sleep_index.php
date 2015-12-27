
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Sleep Event</title>
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
		<form class="col s12" action="Sleepevent.php" method="post"
			name="insertform">
						<div class="row">
				<div class="input-field col s12">
				<INPUT TYPE="text" NAME="userid" readonly value="<?php if(isset($_GET['user_id'])){echo $_GET['user_id'];}?>" /> <label for="userid">UserID</label>
				<!--<INPUT TYPE="text" NAME="user_id" value="user_id" type="hidden" style="display:none">-->
				</div>
			</div>
			<div class="row">
			<div class="input-field col s6">
				<h7>Start time:</h7>
				<input type="datetime-local" name="starttime"/>
			</div>
			<div class="input-field col s6">
			<h7>End time:</h7>
				<input type="datetime-local" name="endtime"/>
			</div>
			</div>
			<div class="row">
				<div class="input-field col s12">
				<INPUT TYPE="text" NAME="location" /> <label for="location">Location</label>
				<!--<INPUT TYPE="text" NAME="location" value="location" type="hidden" style="display:none">-->
				</div>
			</div>
			<div class="row">
				<div class="input-field col s12">
				<INPUT TYPE="text" NAME="quality" /> <label for="quality">Quality (1-5)</label>
				<!--<INPUT TYPE="text" NAME="location" value="location" type="hidden" style="display:none">-->
				</div>
			</div>
			<div align="center">
				<button class="btn waves-effect waves-light" type="submit"
				name="send" id="inputid1">Submit</button>
			</div>
			
		</form>
	</div>
</body>
</html>