<?php
 ob_start();
  include("DB.php");
  if(isset($_POST['send'])!="")
  {
  $userid=mysql_real_escape_string($_POST['userid']);
  $moodcond=mysql_real_escape_string($_POST['group1']);
  $mdatetime=mysql_real_escape_string($_POST['moodtime']);
  date_default_timezone_set('UTC');
  $date = new DateTime($mdatetime);
  $result = $date->format('Y-m-d H:i:s');
  $update=mysql_query("INSERT INTO mental_mood(user_id,mood_condition,time_stamp)VALUES
                                      ('$userid','$moodcond','$result')");
  
  if($update)
  {
      $msg="Successfully Updated!!";
	  header('Location:Home.php?user_id='.$userid);
  }
  else
  {
     $errormsg="Something went wrong, Try again ";
      echo "<script type='text/javascript'>alert('$errormsg ');</script>";
      
  }
  }
 ob_end_flush();
?>