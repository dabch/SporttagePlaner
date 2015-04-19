<!DOCTYPE html>
<?php 
?>
<html>
<head>
<meta charset='UTF-8' name='viewport' content='width=device-width' />
<title>Eingabe Spielpläne @ Sporttage-Kepler</title>
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
<h1>Bitte hier die Spiele eingeben:</h1>
<i>Alle</i> Felder, außer Schiedsrichter, müssen ausgefüllt sein. Die Kombination aus Spielnummer und Feld muss eindeutig sein (d.h. es dürfen nicht auf einem Feld zeitgleich zwei Spiele stattfinden)<br>
<br>
<form method="post">
<table>
	<tr>
	<td>Tag
	<td>Stufe
	<td>Sportart
	<td>Spielnummer (Zeit)
	<td>Feld
	<td>Mannnschaft 1
	<td>Mannschaft 2
	<td>Schiri (optional)
	</tr>
	

<br>
<?php
// Variablen aus POST extrahieren
$sportart = $_POST['sportart'];
$stufe = $_POST['stufe'];
$tag = $_POST['tag'];

$timeid = $_POST['timeid'];
$feld = $_POST['feld'];
$paarung = chop($_POST['team1']) . ' : ' . chop($_POST['team2']); // choppen um überflüssige Lehrzeichen zu verhindern
$schiri = chop($_POST['schiri']);


// ### <form> anpassen, um die vorherigen Einstellungen bei TAG, Sportart und Stufe zu übernehmen (man muss ja meistens mehrere Spiele eingeben ###
echo '<tr>';

// Mo / Di
if($tag == 'DI') {
	echo '<td><input type="radio" name="tag" value="MO">Montag <br>
	<input type="radio" name="tag" value="DI" checked>Dienstag';
} else {
	echo '<td><input type="radio" name="tag" value="MO" checked>Montag <br>
	<input type="radio" name="tag" value="DI">Dienstag'; // im Normalzustand (else{}) den Montag checken
}
// Stufe
if($stufe == 'US') {
	echo '<td><select name="stufe">
		<option value="US" selected>US</option>
		<option value="MS">MS</option>
		<option value="OS">OS</option>
	</select>';
} elseif($stufe == 'OS') {
	echo '<td><select name="stufe">
		<option value="US">US</option>
		<option value="MS">MS</option>
		<option value="OS" selected>OS</option>
	</select>';
} else { // MS soll default sein
	echo '<td><select name="stufe">
		<option value="US">US</option>
		<option value="MS" selected>MS</option>
		<option value="OS">OS</option>
	</select>';
}
echo $sportart;
// Sportart
if ($sportart == 'BM') {
	echo '<td><select name="sportart">
		<option value="BB">BB</option>
		<option value="BM" selected>BM</option>
		<option value="FB">FB </option>
		<option value="TT">TT</option>
		<option value="VB">VB</option>
	</select>';
} elseif ($sportart == 'FB') {
	echo '<td><select name="sportart">
		<option value="BB">BB</option>
		<option value="BM">BM</option>
		<option value="FB" selected>FB </option>
		<option value="TT">TT</option>
		<option value="VB">VB</option>
	</select>';
} elseif ($sportart == 'TT') {
	echo '<td><select name="sportart">
		<option value="BB">BB</option>
		<option value="BM">BM</option>
		<option value="FB">FB </option>
		<option value="TT" selected>TT</option>
		<option value="VB">VB</option>
	</select>';
} elseif ($sportart == 'VB') {
	echo '<td><select name="sportart">
		<option value="BB">BB</option>
		<option value="BM">BM</option>
		<option value="FB">FB </option>
		<option value="TT">TT</option>
		<option value="VB" selected>VB</option>
	</select>';
} else { // Default: BB
	echo '<td><select name="sportart">
		<option value="BB" selected>BB</option>
		<option value="BM">BM</option>
		<option value="FB">FB </option>
		<option value="TT">TT</option>
		<option value="VB">VB</option>
	</select>';
}
?>
		
	
<td><input type="number" name="timeid" min="1" style="width: 50px" required>
<td><input type="number" name="feld" min="1" style="width: 50px" required>
<td><input type="text" name="team1" maxlength="5" size="5" required>
<td><input type="text" name="team2" maxlength="5" size="5" required>
<td><input type="text" name="schiri">
</tr>

</table>
<input type="submit" value="Spiel eintragen">
</form>
<br>
<?php
// Nur weitermachen wenn ein POST-String vorliegt (getestet an $tag, weil das immer gesetzt ist)
if($tag != '') {
	// ### Eintragen in DB ###
	$inifile = parse_ini_file("db.ini", false);

	$conn = new mysqli($inifile['ip'], $inifile['username'], $inifile['password'], $inifile['dbname']);

	if($conn->connect_error){
		die('Connection failed:\n' . $conn->connect_error);
	}

	// Umlaute fixen
	$conn->query('SET NAMES "utf8"');
	$sportart = $_POST['sportart'];
	$stufe = $_POST['stufe'];
	$tag = $_POST['tag'];

	$timeid = $_POST['timeid'];
	$feld = $_POST['feld'];
	$paarung = chop($_POST['team1']) . ' : ' . chop($_POST['team2']); // choppen um überflüssige Lehrzeichen zu verhindern
	$schiri = chop($_POST['schiri']);

	$spielHinzufuegen = $conn->prepare('INSERT INTO ' . $stufe . '_' . $sportart . '_' . $tag . '_spiele (TimeID, Feld, Paarung) VALUES (?, ?, ?)');

	$sql = 'INSERT INTO ' . $stufe . '_' . $sportart . '_' . $tag . '_spiele (TimeID, Feld, Paarung) VALUES (' . $timeid . ', ' . $feld . ', "' . $paarung . '")';

	if($conn->query($sql) === TRUE) {
		echo 'Erfolgreich hinzugefügt!<br>';
	} else {
		echo 'Fehler beim Hinzufügen:<br>';
		$errmesg = $conn->error;
		echo $errmesg; // Fehlermeldung ausgeben
	}

	// Prepared Statement funktionieren aus irgendeinem grund nicht :/  by Sandesh: liegt am query, das is ne funktion die nen string und kein objekt will glaub
}

?>
<br>
<br>
<iframe src="info.php" width="500" height="800" style="border:none"></iframe>

</body>
</html>

