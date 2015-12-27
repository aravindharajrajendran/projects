<?php
 ob_start();
  include("DB.php");
  if(isset($_POST['send'])!="")
  {
  $userid=mysql_real_escape_string($_POST['userid']);
  $exertype=mysql_real_escape_string($_POST['exercise_type']);
 $intensity=mysql_real_escape_string($_POST['intensity']);
  $sdatetime=mysql_real_escape_string($_POST['startmoodtime']);
 $edatetime=mysql_real_escape_string($_POST['endmoodtime']);
  date_default_timezone_set('UTC');
 $sdate = new DateTime($sdatetime);
  $sresult = $sdate->format('Y-m-d H:i:s');
  $edate = new DateTime($edatetime);
  $eresult = $edate->format('Y-m-d H:i:s');
echo $userid;
echo $exertype;
echo $intensity;
echo $sdatetime;
echo  $edatetime;
echo $sresult;
echo $eresult;
 $update=mysql_query("INSERT INTO exercise_event(user_id,exercise_type,intensity,start_time,stop_time)VALUES
                                      ('$userid','$exertype','$intensity','$sresult','$eresult');");
  
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