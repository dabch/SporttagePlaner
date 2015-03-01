<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width" />
<title>Eingabe Klassenlisten @ Sporttage-Kepler</title>
</head>

<style>
table, th, td {
	border: 1px solid black;
	border-collapse: collapse;
}

th, td {
	padding: 3px;
}
</style>

<body>
Klasse eingeben:
<form action="" method="post">
<input type="text" name="klasse"/>

<table>
	<!-- Fußball 1 + 2 & Fahrradtour -->
	<tr>
		<td colspan="2"> Fußball 1
		<td colspan="2"> Fußball 2
		<td colspan="2"> Fahrradtour
	</tr>
	<tr>
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]"/>
		<td> <input type="text" name="fb1_n[]"/>
		<td> <input type="text" name="fb2_v[]"/>
		<td> <input type="text" name="fb2_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]"/>
		<td> <input type="text" name="fb1_n[]"/>
		<td> <input type="text" name="fb2_v[]"/>
		<td> <input type="text" name="fb2_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]"/>
		<td> <input type="text" name="fb1_n[]"/>
		<td> <input type="text" name="fb2_v[]"/>
		<td> <input type="text" name="fb2_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]"/>
		<td> <input type="text" name="fb1_n[]"/>
		<td> <input type="text" name="fb2_v[]"/>
		<td> <input type="text" name="fb2_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]"/>
		<td> <input type="text" name="fb1_n[]"/>
		<td> <input type="text" name="fb2_v[]"/>
		<td> <input type="text" name="fb2_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]"/>
		<td> <input type="text" name="fb1_n[]"/>
		<td> <input type="text" name="fb2_v[]"/>
		<td> <input type="text" name="fb2_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]"/>
		<td> <input type="text" name="fb1_n[]"/>
		<td> <input type="text" name="fb2_v[]"/>
		<td> <input type="text" name="fb2_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]"/>
		<td> <input type="text" name="fb1_n[]"/>
		<td> <input type="text" name="fb2_v[]"/>
		<td> <input type="text" name="fb2_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]"/>
		<td> <input type="text" name="fb1_n[]"/>
		<td> <input type="text" name="fb2_v[]"/>
		<td> <input type="text" name="fb2_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<!-- Badminton & Tischtennis & Volleyball -->
	
	<tr>
		<td colspan="6">  	
	</tr>
		<td colspan="2"> Badminton
		<td colspan="2"> Tischtennis
		<td colspan="2"> Volleyball
	</tr>
	
	<tr>
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
	</tr>
	
	<tr>
		<td colspan="4" style="text-align:center"> Team 1
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
		<td> <input type="text" name="vb_v[]"/>
		<td> <input type="text" name="vb_n[]"/>
	<tr>
		
	
	
</table>
  <input type="submit" name="submit">
</form>
<br>
<?php
	$servername = 'wp052.webpack.hosteurope.de';
	$username = 'db1093417-sport';
	$password = '%&SporTTage14@!';
	$dbname = 'db1093417-sporttage';
	session_start();
	$conn = new mysqli($servername, $username, $password, $dbname);
	
	if($conn->connect_error){
		die('Connection failed:\n' . $conn->connect_error);
	}
?>
</body>
</html>

