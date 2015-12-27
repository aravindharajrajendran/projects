<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Register</title>
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
<script>
	$(document).ready(function() {
		$('.datepicker').pickadate({
			selectMonths : true,
			selectYears : 100
		});

		$(document).ready(function() {
			$('select').material_select();
		});
	});
</script>
</head>

<body background="images/bg.jpg">
	<div class="display">
		<form class="col s6" action="Insert.php" method="post"
			name="insertform">
			<div class="row">
				<div class="input-field col s6">
					<input type="text" name="username" class="validate" id="inputid" />
					<label for="name" id="preinput"><font size="4">Name</font></label>
				</div>
				<div class="input-field col s6">
					<input type="email" name="usermail" class="validate" id="inputid" />
					<label for="email" id="preinput"><font size="4">Email address</font></label>
				</div>
			</div>
			<div class="row">
				<div class="input-field col s6">
					<input type="date" class="datepicker" name="userbdate"
						class="validate" id="inputid" /> <label for="userbdate"
						id="preinput"><font size="4">Birthdate</font></label>
				</div>
				<div class="input-field col s6">
					<p style="padding-top: 20px;">
						<input name="group1" type="radio" id="male" value="M" /> <label for="male">Male</label>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
							name="group1" type="radio" id="female" value="F"/> <label for="female">Female</label>
					</p>
					<label for="group1"><font size="4">Sex</font></label>
				</div>
			</div>
			<div class="row">
				<div class="input-field col s4">
					<input type="number" name="userheight" class="validate"
						id="inputid" /> <label for="userheight" id="preinput"><font size="4">Height (in cms)</font></label>
				</div>
				<!-- </div>
			<div class="row"> -->
				<div class="input-field col s4">
					<input type="number" name="userweight" class="validate"
						id="inputid" /> <label for="userweight" id="preinput"><font size="4">
						Weight (in lbs)</font></label>
				</div>
				<div class="input-field col s4">
					<input type="password" name="password" class="validate"
						id="inputid" /> <label for="password" id="preinput"><font size="4">
						Password</font></label>
				</div>
			</div>
			<div align="center">
				<button class="btn waves-effect waves-light" type="submit"
					name="send" id="inputid1">Sign Up</button>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<button class="btn waves-effect waves-light" type="button"
					name="cancel" onclick="history.go(-1);">Cancel</button>
			</div>
		</form>
	</div>
</body>
</html>