<?php
 ob_start();
  include("DB.php");
  if(isset($_POST['send'])!="")
  {

 $userid=mysql_real_escape_string($_POST['userid']);
 $category=mysql_real_escape_string($_POST['category']);
 $quality=mysql_real_escape_string($_POST['quality']);
 $source=mysql_real_escape_string($_POST['source']);
 $size=mysql_real_escape_string($_POST['size']);
 $foodtime=mysql_real_escape_string($_POST['foodtime']);

  date_default_timezone_set('UTC');
 $date = new DateTime($foodtime);
  $result = $date->format('Y-m-d H:i:s');


 $update=mysql_query("INSERT INTO food_event(category,user_id,time_stamp,quality,food_source,size)VALUES
                                      ('$category','$userid','$result','$quality','$source','$size');");
 

  if($update)
  {
      $msg="Successfully Updated!!";
	  header('Location:Home.php?userid='.$userid);
  }
  else
  {
     $errormsg="Something went wrong, Try again ";
      echo "<script type='text/javascript'>alert('$errormsg ');</script>";
      
  }
  }
 ob_end_flush();
?>