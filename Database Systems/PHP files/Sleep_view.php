<?php
 include('DB.php');
  $select=mysql_query("SELECT * FROM sleep_event order by user_id desc");
  $i=1;
  while($userrow=mysql_fetch_array($select))
  {
  $sleepid=$userrow['sleep_event_id'];
  $userid=$userrow['user_id'];
  $starttime=$userrow['start_time'];
$stoptime=$userrow['stop_time'];
$duration=$userrow['duration'];
  $quality=$userrow['quality'];
$location=$userrow['location'];
?>

<div class="display">
  <p>SLEEP EVENT ID : <span><?php echo $sleepid; ?></span>
  </p>
  <br />
  <p> USER ID : <span><?php echo $userid; ?></span>
  </p>
  <br />
    <p> START TIME : <span><?php echo $starttime; ?></span>
  </p>
  <br />
  <p> STOP TIME : <span><?php echo $stoptime; ?></span>
  </p>
  <br />
  <p> DURATION : <span><?php echo $duration; ?></span>
  </p>
  <br />
  <p> QUALITY : <span><?php echo $quality; ?></span>
  </p>
  <br />
  <p> LOCATION : <span><?php echo $location; ?></span>
  </p>
  <br />
</div>
<?php } ?>