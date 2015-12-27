<!DOCTYPE>
<html>
<head>
<script type="text/javascript"
	src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/js/MetroJs.min.js"></script>
<!--Import Google Icon Font-->
<link href="http://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<!--Import materialize.css-->
<link type="text/css" rel="stylesheet" href="/css/MetroJs.min.css"
	media="screen,projection" />
	<link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<!--Import materialize.css-->
<script type="text/javascript">
	$(document).ready(function() {
		$(".live-tile").liveTile();
	});
</script>
<style type="text/css">
body {
	background-repeat: no-repeat;
	background-attachment: scroll;
	background-position: 0% 0%;
	background-size: cover;
}

.mydiv {
	top: 50%;
	left: 50%;
	transform: translate3d(-50%, -50%, 0);
	position: absolute;
	width: 400px;
}
</style>
</head>
<body background="/images/bg.jpg">
	<div class="mydiv">
		<div class="tiles blue tile-group four-wide">
			<!-- Green Static Tile -->
			<div onclick="window.location.href='Food_Index.php?user_id=<?php if(isset($_GET['userid'])){echo $_GET['userid'];}?>'"  class="live-tile accent red exclude">
				<span class="tile-title"><center>
						<h1>Food Event</h1>
					</center></span>
				<p>Click to insert your Food event</p>
			</div>
			<div onclick="window.location.href='Sleep_index.php?user_id=<?php if(isset($_GET['userid'])){echo $_GET['userid'];}?>'" class="live-tile accent blue exclude">
				<span class="tile-title"><center>
						<h1>Sleep Event</h1>
					</center></span>
				<p>Click to insert your Sleep event</p>
			</div>
			<div onclick="window.location.href='Exercise_Index.php?user_id=<?php if(isset($_GET['userid'])){echo $_GET['userid'];}?>'" class="live-tile accent green exclude">
				<span class="tile-title"><center>
						<h1>Exercise Event</h1>
					</center></span>
				<p>Click to insert your Exercise event</p>
			</div>
		</div>
		<div class="tiles blue tile-group four-wide">
			<!-- Green Static Tile -->
			<div onclick="window.location.href='Physical_Mood_Index.php?user_id=<?php if(isset($_GET['userid'])){echo $_GET['userid'];}?>';" class="live-tile accent orange exclude">
				<span class="tile-title"><center>
						<h1>Physical Mood</h1>
					</center></span>
				<p>Click to insert your Physical Mood</p>
			</div>
			<div onclick="window.location.href='Mental_Mood_Index.php?user_id=<?php if(isset($_GET['userid'])){echo $_GET['userid'];}?>';" class="live-tile accent purple exclude">
				<span class="tile-title"><center>
						<h1>Mental Mood</h1>
					</center></span>
				<p>Click to insert your Mental Mood</p>
			</div>
			<div onclick="window.location.href='History.php?user_id=<?php if(isset($_GET['userid'])){echo $_GET['userid'];}?>';" class="live-tile accent magenta exclude">
				<span class="tile-title"><center>
						<h1>History</h1>
					</center></span>
				<p>View your past events and Moods here</p>
			</div>
		</div>
		<br>
		<br>
		<div align="center">
		<button class="btn waves-effect waves-light" type="button" name="logout" onclick="window.location.href='Login.php';">Logout</button>
		</div>
	</div>
	<!-- Activate live tiles -->
	<script type="text/javascript">
		// apply regular slide universally unless .exclude class is applied 
		// NOTE: The default options for each liveTile are being pulled from the 'data-' attributes
		$(".live-tile, .flip-list").not(".exclude").liveTile();
	</script>
</body>
</html>