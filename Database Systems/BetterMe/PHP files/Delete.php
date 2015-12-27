<?php
  ob_start();
  include("DB.php");
  if(isset($_GET['id'])!="")
  {
  $delete=$_GET['id'];
  $delete=mysql_query("DELETE FROM user WHERE user_id='$delete'");
  if($delete)
  {
      header("Location:index.php");
  }
  else
  {
      echo mysql_error();
  }
  }
  ob_end_flush();
?>