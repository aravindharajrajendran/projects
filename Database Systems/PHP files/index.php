<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Insert form</title>
<link type="text/css" media="all" rel="stylesheet" href="style.css">
</head>

<body>
<div class="display">
<form action="insert.php" method="post" name="insertform">
<p>
  <label for="usertype" id="preinput"> USER TYPE : </label>
  <input type="text" name="usertype" required placeholder="Enter your type" id="inputid"/>
</p>
<p>
  <label for="name" id="preinput"> USER NAME : </label>
  <input type="text" name="username" required placeholder="Enter your name" id="inputid"/>
</p>
<p>
  <label  for="email" id="preinput"> EMAIL ID : </label>
  <input type="email" name="usermail" required placeholder="Enter your Email" id="inputid" />
</p>
<p>
  <label for="userbdate" id="preinput"> USER BIRTHDAY : </label>
  <input type="text" name="userbdate" required placeholder="Enter your Birthday" id="inputid"/>
</p>
<p>
  <label for="usersex" id="preinput"> USER SEX : </label>
  <input type="text" name="usersex" required placeholder="Enter your Sex" id="inputid"/>
</p>
<p>
  <label for="userheight" id="preinput"> USER HEIGHT : </label>
  <input type="text" name="userheight" required placeholder="Enter your Height" id="inputid"/>
</p>
<p>
  <label for="userweight" id="preinput"> USER WEIGHT : </label>
  <input type="text" name="userweight" required placeholder="Enter your Weight" id="inputid"/>
</p>
<p>
  <label for="password" id="preinput"> PASSWORD : </label>
  <input type="password" name="password" required placeholder="Enter your password" id="inputid" />
</p>
<p>
  <input type="submit" name="send" value="Submit" id="inputid1"  />
</p>
</form>
</div>
<?php include('View.php'); ?>
</body>
</html>