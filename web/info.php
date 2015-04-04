<!DOCTYPE html>
<?php 
?>
<html>
<head>
<meta charset='UTF-8' name='viewport' content='width=device-width' />
<title>Zeiten- und Mannschaftsanzeige @ Sporttage-Kepler</title>
</head>

<style>
table, th, td {
	border: 0px;
	text-align: center;
}

th, td {
	padding: 3px;
}
</style>

<body>
<h1>Mannschaften und Zeiten anzeigen</h1>
<form method="post">
	<select name="stufe">
		<option value="US">US</option>
		<option value="MS">MS</option>
		<option value="OS" selected>OS</option>
	    </select> Stufe<br>
	<select name="sportart">
		<option value="BB">BB</option>
		<option value="BM" selected>BM</option>
		<option value="FB">FB </option>
		<option value="TT">TT</option>
		<option value="VB">VB</option>
	    </select> Sportart<br>
	<td rowspan = "2"><input type="submit" value="Infos anzeigen">
</form>
<br>
<?php
$stufe = $_POST['stufe'];
$sportart = $_POST['sportart'];

// Nur weitermachen wenn ein POST-String vorliegt (getestet an $stufe, weil das immer gesetzt ist)
if($stufe != '') {

        echo '<h3>Mannschaften und Zeiten für: ' . $stufe . ' ' . $sportart . '</h3>';
        
	// ### Eintragen in DB ###
	
	$servername = 'mysql20.1blu.de';
	$username = 's228201_2224606';
	$password = '%&SporTTage14@!';
	$dbname = 'db228201x2224606';

	$conn = new mysqli($servername, $username, $password, $dbname);

	if($conn->connect_error){
		die('Connection failed:\n' . $conn->connect_error);
	}

	// Umlaute fixen
	$conn->query('SET NAMES "utf8"');
	
	// Um alle Mannschaften zu holen
	$getTeamsStmt = $conn->prepare('SELECT DISTINCT ' . $sportart . ' FROM Mannschaften_' . $stufe . ' WHERE ' . $sportart . ' != \'\'');
	
	// Zeiten am mo
	//$getMoZeiten = $conn->prepare('SELECT ID, Spielbeginn Beginn, Spielende Ende FROM ' . $stufe . "_" . $sportart ." _MO_zeiten');
	
	
	$getTeamsStmt->execute();
	//$getMoZeiten->execute();
	//$getDiZeiten->execute();
	
	$getTeamsStmt->bind_result($teamname);
	
	while($getTeamsStmt->fetch()) {
	        echo $teamname . '<br>';
	}
	
        $conn->close();
}
?>


</body>
</html>

