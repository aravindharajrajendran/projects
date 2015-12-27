<?php
ob_start();
  include("DB.php");
  if(isset($_POST['send'])!="")
  {
  	$user_email=mysql_real_escape_string($_POST['usermail']);
  	$password=mysql_real_escape_string($_POST['password']);
  	$query = "select user_id, user_fname, user_lname from `user` where user_email='$user_email' and password='$password'";
  	$result = mysql_query($query) or die(mysql_error());
  	$count = mysql_num_rows($result);
  	if ($count == 1){
		session_start();
		$row = mysql_fetch_assoc($result);
  		$_SESSION['usermail'] = $user_email;
		$_SESSION['userid'] = $row['user_id'];
		$_SESSION['username'] = $row['user_fname']." ".$row['user_lname'];
  	}else{
  		echo "Invalid Login Credentials.";
  	}
  }
  if (isset($_SESSION['usermail'])){
  	$user_email=$_SESSION['usermail'];
	$user_name =$_SESSION['username'];
	$user_id=$_SESSION['userid'];
  	header("Location:Home.php?userid=".$user_id);
	}
  else { echo "Retry"; }

?>