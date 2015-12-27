<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Insert form</title>
<link type="text/css" media="all" rel="stylesheet" href="style.css">
</head>

<body>
<div class="display">
<form action="E_Sign.php" method="post" name="insertform">
<p>
  <label  for="email" id="preinput"> EMAIL ID : </label>
  <input type="email" id="usermail" name="usermail" required placeholder="Enter your Email" />
</p>
<p>
  <label for="password" id="preinput"> PASSWORD : </label>
  <input type="password" id="password" name="password" required placeholder="Enter your password" />
</p>
<p>
  <input type="submit" name="send" value="Submit" id="inputid1"  />
</p>
</form>
</div>
<?php echo " "; ?>
</body>
</html>