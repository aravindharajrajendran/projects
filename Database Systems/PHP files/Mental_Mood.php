<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Mental Mood</title>
<<link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<!--Import materialize.css-->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.1/css/materialize.min.css">
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.1/js/materialize.min.js"></script>
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
		<form class="col s6" action="MentalMood.php" method="post"
			name="insertform">
			<div class="row">
			<div class="input-field col s6">
					<p style="padding-top: 20px;">
						<input name="group1" type="radio" id="upset" value="1" /> <label for="upset">Upset</label>
							<input
							name="group1" type="radio" id="sad" value="2"/> <label for="sad">Sad</label><input
							name="group1" type="radio" id="normal" value="3"/> <label for="normal">Normal</label> <input
							name="group1" type="radio" id="happy" value="4"/> <label for="happy">Happy</label> <input
							name="group1" type="radio" id="excited" value="5"/> <label for="excited">Excited</label>
					</p>
					<label for="group1"><font size="4">How are you feeling now?</font></label>
				<div align="center">
					<button class="btn waves-effect waves-light" type="submit"
					name="send" id="inputid1">Submit</button>
				</div>
		</form>
	</div>
</body>
</html>