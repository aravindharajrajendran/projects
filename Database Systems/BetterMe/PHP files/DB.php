<?php
ob_start();
define('DB_SERVER', "avengers.cuf8wkmtkpy1.us-east-1.rds.amazonaws.com");
define('DB_USERNAME', "admin");
define('DB_PASSWORD', "password");
define('DB_DATABASE', "BetterMeMain");
//$connection = mysql_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD) or die(mysql_error());
$connection = mysql_connect("avengers.cuf8wkmtkpy1.us-east-1.rds.amazonaws.com", "admin", "password", "BetterMeMain") or 
die(mysql_error());
$database = mysql_select_db(DB_DATABASE) or die(mysql_error());
?>