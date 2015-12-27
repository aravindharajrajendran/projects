<?php 
ob_start();
include('DB.php');
if(isset($_GET['id']))
{
  $id=$_GET['id'];
  if(isset($_POST['update']))
  {
  $eusername=$_POST['eusername'];
  $eusermail=$_POST['eusermail'];
  $emobile=$_POST['eusermobile'];
  $updated=mysql_query("UPDATE iedu SET 
        username='$eusername', emailid='$eusermail', mobileno='$emobile' WHERE id='$id'")or die();
  if($updated)
  {
  $msg="Successfully Updated!!";
  header('Location:index.php');
  }
}
}  //update ends here
ob_end_flush();
?>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Edit form</title>
<link type="text/css" media="all" rel="stylesheet" href="style.css">
</head>

<body>
<?php 
  if(isset($_GET['id']))
  {
  $id=$_GET['id'];
  $getselect=mysql_query("SELECT * FROM iedu WHERE id='$id'");
  while($profile=mysql_fetch_array($getselect))
  {
    $username=$profile['username'];
    $usermail=$profile['emailid'];
    $usermobile=$profile['mobileno'];
?>
<div class="display">
  <form action="" method="post" name="insertform">
    <p>
      <label for="name"  id="preinput"> USER NAME : </label>
      <input type="text" name="eusername" required placeholder="Enter your name" 
      value="<?php echo $username; ?>" id="inputid" />
    </p>
    <p>
      <label  for="email"  id="preinput"> EMAIL ID : </label>
      <input type="email" name="eusermail" required placeholder="Enter your Email" 
      value="<?php echo $usermail; ?>" id="inputid" />
    </p>
    <p>
      <label for="mobile" id="preinput"> MOBILE NUMBER : </label>
      <input type="text" name="eusermobile" required placeholder="Enter your mobile number" 
      value="<?php echo $usermobile; ?>" id="inputid" />
    </p>
    <p>
      <input type="submit" name="update" value="Update" id="inputid1" />
    </p>
  </form>
</div>
<?php } } ?>
</body>
</html>