<!doctype html>
<html>
<body>
	<?php
		if(@$_POST["Kaydet"])
		{
			require_once("config.php");
			global $con;
			$query="insert into tbl_test(il) value('".$_POST["il"]."')";
			if(mysqli_query($con,$query))
			{
				echo "kaydetti";
			}
		}
	?>
<form method="POST">
	<input name="il" type="text" />
	<input name="Kaydet" value="Kaydet" type="submit" />
</form>
</body>
</html>