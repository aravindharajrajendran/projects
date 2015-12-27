<?php
 ob_start();
  include("DB.php");
  if(isset($_POST['send'])!="")
  {
  $userid=mysql_real_escape_string($_POST['userid']);
  $starttime=mysql_real_escape_string($_POST['starttime']);
  date_default_timezone_set('UTC');
  $date = new DateTime($starttime);
  $start = $date->format('Y-m-d H:i:s');  
  $endtime=mysql_real_escape_string($_POST['endtime']);
    $datee = new DateTime($endtime);
  $end = $datee->format('Y-m-d H:i:s');
  $location=mysql_real_escape_string($_POST['location']);
  $quality=mysql_real_escape_string($_POST['quality']);
  $update=mysql_query("INSERT INTO sleep_event (user_id,start_time,stop_time,quality,location)VALUES
                                      ('$userid','$start','$end','$quality','$location');");
  if($update)
  {
      $msg="Successfully Updated!!";
	  header('Location:Home.php?user_id='.$userid);
  }
  else
  {
     $errormsg="Something went wrong, Try again ";
      echo "<script type='text/javascript'>alert('$errormsg');</script>";
      
  }
  }
 ob_end_flush();
?>