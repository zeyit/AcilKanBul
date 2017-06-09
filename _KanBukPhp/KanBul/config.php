<?php
	define("host","localhost");
	define("user","user");
	define("pass","şifre");
	define("db_sec","db");

	$con = @mysqli_connect(host,user,pass,db_sec) or die("Veri tabanı bağlanti hatası");
	
?>
