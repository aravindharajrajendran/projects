<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Exercise</title>
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
	top: 75%;
	left: 50%;
	transform: translate3d(-50%, -50%, 0);
	position: absolute;
	width: 640px;
}
</style>
</head>

<body background="images/bg.jpg">
	<div class="display">
		<form class="col s12" action="Exercise.php" method="post"
			name="insertform">
			<div class="row">
				<div class="input-field col s12">
					<input type="text" name="userid" class="validate" id="inputid" readonly value="<?php if(isset($_GET['user_id'])){echo $_GET['user_id'];}?>" />
					<label for="userid" id="preinput"><font size="4">User ID</font></label>
				</div>
			</div>
<div class="row">
				<div class="input-field col s6">
					
<p style="padding-top: 20px;">
					<input type="datetime-local" name="startmoodtime"/> <label for="startmoodtime"><font size="4"> </font></label>
				</p>
<label for="startmoodtime"><font size="4"> Start Time</font> </label>
</div>
				
				<div class="input-field col s6">
<p style="padding-top: 20px;">
					
					<input type="datetime-local" name="endmoodtime"/> <label for="endmoodtime"><font size="4"> </font></label>
				
</p>
<label for="endmoodtime"><font size="4"> End Time</font> </label>
</div>
				</div>
<div class="row">
<div class="input-field col s12">
 <p style="padding-top: 20px;">
<?php
include("DB.php");
$cat = mysql_query("SELECT * FROM exercise ORDER BY exercise_type ") or die(mysql_error());
while($row = mysql_fetch_array($cat))
{
echo '<input name="exercise_type" type="radio" id="'.$row['exercise_type'].'" value="'.$row['exercise_type'].'"/> <label for="'.$row['exercise_type'].'">'.$row['exercise_type'].'</label>&nbsp;';


}
?>

</p>
<label for="exercise_type" id="preinput"><font size="4">Exercise Type:</font></label></div>
			</div>
			<div class="row">
				<div class="input-field col s12">
					<p style="padding-top: 20px;">
						 <input
							name="intensity" type="radio" id="low" value="1"/> <label for="low">Low</label>&nbsp; <input
							name="intensity" type="radio" id="medium" value="2"/> <label for="medium">Medium</label> <input
							name="intensity" type="radio" id="high" value="3"/> <label for="high">High</label>
					</p>
					<label for="intensity"><font size="4">Intensity</font></label>
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