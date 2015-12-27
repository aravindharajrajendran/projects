<?php
 include('DB.php');
  $select=mysql_query("SELECT * FROM physical_mood order by user_id desc");
  $i=1;
  while($userrow=mysql_fetch_array($select))
  {
  $physicalid=$userrow['physical_mood_id'];
  $userid=$userrow['user_id'];
  $moodcond=$userrow['mood_condition'];
  $moodtime=$userrow['time_stamp'];
?>

<div class="display">
  <p> MOOD ID : <span><?php echo $physicalid; ?></span>
  </p>
  <br />
  <p> USER ID : <span><?php echo $userid; ?></span>
  </p>
  <br />
  <p> MOOD CONDITION : <span><?php echo $moodcond; ?></span>
  </p>
  <br />
  <p> MOOD TIME : <span><?php echo $moodtime; ?></span>
  </p>
  <br />
</div>
<?php } ?>