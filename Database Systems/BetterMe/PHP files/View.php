<?php
 include('DB.php');
  $select=mysql_query("SELECT * FROM user order by user_id desc");
  $i=1;
  while($userrow=mysql_fetch_array($select))
  {
  $usertype=$userrow['user_type'];
  $username=$userrow['user_name'];
  $usermail=$userrow['user_email'];
  $userbdate=$userrow['user_birthdate'];
  $usersex=$userrow['user_sex'];
  $userheight=$userrow['user_height'];
  $userweight=$userrow['user_weight'];
?>

<div class="display">
  <p> USER NAME : <span><?php echo $username; ?></span>
    <a href="Delete.php?id=<?php echo $userid; ?>" 
    onclick="return confirm('Are you sure you wish to delete this Record?');">
            <span class="delete" title="Delete"> X </span></a>
  </p>
  <br />
  <p> USER TYPE : <span><?php echo $usertype; ?></span>
  </p>
  <br />
  <p> EMAIL ID : <span><?php echo $usermail; ?></span>
    <a href="Edit.php?id=<?php echo $userid; ?>"><span class="edit" title="Edit"> E </span></a>
  </p>
  <br />
  <p> USER BIRTHDATE : <span><?php echo $userbdate; ?></span>
  </p>
  <br />
  <p> USER SEX : <span><?php echo $usersex; ?></span>
  </p>
  <br />
  <p> USER HEIGHT : <span><?php echo $userheight; ?></span>
  </p>
  <br />
  <p> USER WEIGHT : <span><?php echo $userweight; ?></span>
  </p>
  <br />
</div>
<?php } ?>