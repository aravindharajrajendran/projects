<?php 

$user_email = $_POST['usermail']; 
$password = $_POST['password']; 

*/ 
function SignIn() 
{ 
session_start(); //starting the session for user profile page 
if(!empty($_POST['user_email'])) //checking the 'user' name which is from Sign-In.html, is it empty or have some text 
{ 
	$query = mysql_query("SELECT * FROM user where user_email = '$_POST[usermail]' AND password = '$_POST[password]'") 
	or die(mysql_error()); 
	$row = mysql_fetch_array($query) or die(mysql_error()); 
	if(!empty($row['user_email']) AND !empty($row['password'])) 
	{ 
		$_SESSION['user_email'] = $row['password']; 
		echo "SUCCESSFULLY LOGIN TO USER PROFILE PAGE..."; 
	} 
	else 
	{ 
		echo "SORRY... YOU ENTERD WRONG ID AND PASSWORD... PLEASE RETRY..."; 
	} 
} 
} 
if(isset($_POST['submit'])) 
{ 	
	SignIn(); 
} 

?>