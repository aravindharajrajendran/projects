<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Food</title>
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
	width: 540px;
}
</style>
</head>

<body background="images/bg.jpg">
	<div class="display">
		<form class="col s30" action="Food.php" method="post"
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
					<input type="datetime-local" name="foodtime"/> <label for="foodtime"><font size="4"> </font></label>
				</p>
<label for="foodtime"><font size="4"> Time</font> </label>
</div></div>
<div class="row">
<div class="input-field col s50">
 <p style="padding-top: 20px;">
<?php
include("DB.php");
$cat = mysql_query("SELECT * FROM food ORDER BY category") or die(mysql_error());
while($row = mysql_fetch_array($cat))
{
echo '<input name="category" type="radio" id="'.$row['category'].'" value="'.$row['category'].'"/> <label for="'.$row['category'].'">'.$row['category'].'</label>&nbsp;';


}
?>

</p>
<label for="exercise_type" id="preinput"><font size="4">Food Type:</font></label></div>
			</div>
			<div class="row">
				<div class="input-field col s12">
					<p style="padding-top: 20px;">
						<input name="quality" type="radio" id="poor" value="1" /> <label for="poor">Poor</label>
							<input
							name="quality" type="radio" id="fair" value="2"/> <label for="fair">Fair</label> <input
							name="quality" type="radio" id="good" value="3"/> <label for="good">Good</label>
<input name="quality" type="radio" id="verygood" value="5"/> <label for="verygood">Very Good</label>
<input name="quality" type="radio" id="excelent" value="4"/> <label for="excelent">Excelent</label> 
					</p>
					<label for="quality"><font size="4">Quality</font></label>
					</div>
					</div>

<div class="row">
				<div class="input-field col s12">
					<p style="padding-top: 20px;">
						 <input	name="size" type="radio" id="small" value="1"/> <label for="small">Small</label>
 						 <input	name="size" type="radio" id="medium" value="2"/> <label for="medium">Medium</label>
						 <input	name="size" type="radio" id="large" value="3"/> <label for="large">Large</label>
					</p>
					<label for="size"><font size="4">Quantity</font></label>
					</div>
				</div>
		<div class="input-field col s12">
					<p style="padding-top: 20px;">
						 <input
							name="source" type="radio" id="home" value="home"/> <label for="home">Home Food</label>&nbsp; <input
							name="source" type="radio" id="outside" value="outside"/> <label for="outside">Outside Food</label>					</p>
					<label for="sizsourcee"><font size="4">Source</font></label>
					</div>
				<br>
				<br>
			
				<div align="center">
					<button class="btn waves-effect waves-light" type="submit"
					name="send" id="inputid1">Submit</button>
			</div>
		</form>
		</div>
	</div>
</body>
</html>