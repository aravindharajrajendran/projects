<?php
$link = mysqli_connect("avengers.cuf8wkmtkpy1.us-east-1.rds.amazonaws.com", "admin", "password", "BetterMeMain");

/* check connection */
if (mysqli_connect_errno()) {
    printf("Connect failed: %s\n", mysqli_connect_error());
    exit();
}

if (!mysqli_query($link, "SET a=1")) {
    printf("Errormessage: %s\n", mysqli_error($link));
}

/* close connection */
mysqli_close($link);
?>