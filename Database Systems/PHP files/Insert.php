<?php
 ob_start();
  include("DB.php");
  if(isset($_POST['send'])!="")
  {
  $usertype=mysql_real_escape_string($_POST['usertype']);
  $username=mysql_real_escape_string($_POST['username']);
  $usermail=mysql_real_escape_string($_POST['usermail']);
  $userbdate=mysql_real_escape_string($_POST['userbdate']);
  $usersex=mysql_real_escape_string($_POST['usersex']);
  $userheight=mysql_real_escape_string($_POST['userheight']);
  $userweight=mysql_real_escape_string($_POST['userweight']);
  $password=mysql_real_escape_string($_POST['password']);
  $update=mysql_query("INSERT INTO user(user_type,user_name,user_email,user_birthdate,user_sex,user_height,user_weight,password)VALUES
                                      ('$usertype','$username','$usermail','$userbdate','$usersex','$userheight','$userweight','$password')");
  
  if($update)
  {
      $msg="Successfully Updated!!";
      echo "<script type='text/javascript'>alert('$msg');</script>";
      header('Location:index.php');
  }
  else
  {
     $errormsg="Something went wrong, Try again ";
      echo "<script type='text/javascript'>alert('$errormsg ');</script>";
      
  }
  }
 ob_end_flush();
?>