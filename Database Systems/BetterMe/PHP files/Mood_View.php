<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Mental Mood</title>
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

<?php
 include('DB.php');
  $select=mysql_query("SELECT * FROM mental_mood order by mental_mood_id desc");
  $i=1;
  while($userrow=mysql_fetch_array($select))
  {
  $mentalid=$userrow['mental_mood_id'];
  $userid=$userrow['user_id'];
  $moodcond=$userrow['mood_condition'];
  $moodtime=$userrow['time_stamp'];
?>

<div class="display">
  <p> MOOD ID : <span><?php echo $mentalid; ?></span>
  </p>
  <br />
  <p> USER ID : <span><?php echo $userid; ?></span>
  </p>
  <br />
  <p> MOOD CONDITION : <span><?php echo $moodcond; ?></span>
  </p>
  <br />
  <p> MOOD TIME : <span><?php echo $moodtime; ?></span>
  </p>
  <br />
</div>
<?php } ?>
</body>
</html>