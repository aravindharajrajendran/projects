<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Better Me!</title>
<!--Import Google Icon Font-->
<link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
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

.mydiv
{
	top:50%;
	left: 50%;
	transform: translate3d(-50%,-50%, 0);
	position: absolute;
	width: 400px;
}
</style>
<script type="text/javascript">
function register()
{
	window.location.href="Register.php";
}

function dialog()
{
	 Materialize.toast('Account has been created successfully!', 5000);
}
</script>
</head>
<body background="/images/bg.jpg">
<?php
if(isset($_GET['message'])){
	$msg = $_GET['message'];
	echo '<script type="text/javascript"> dialog(); </script>';
}
?>
<div class="mydiv" style="display: table; margin-right: auto; margin-left: auto;">
<form class="col s6" action="E_Sign.php" method="post" name="insertform">
<div class="row">
<div class="input-field col s12">
<input type="email" id="usermail" name="usermail" class="validate" />
<label for="email"><font size="4">E-mail address</font></label>
</div>
</div>
<div class="row">
<div class="input-field col s12">
<input type="password" id="password" name="password" class="validate" />
<label for="password"><font size="4">Password</font></label>
</div>
</div>
<br>
<br>
<div align="center">
<button class="btn waves-effect waves-light" type="submit" name="send">Sign In</button>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<button class="btn waves-effect waves-light" type="button" name="send" onclick="register()">Register</button>
</form>
</div>
</div>
</body>
</html>