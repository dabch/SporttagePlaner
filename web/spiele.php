<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width" />
<title>Eingabe Spielpläne @ Sporttage-Kepler</title>
</head>

<?php
$anzahlFelder = $_POST['anzahlFelder'];
$spiele = $_POST['anzahlSpiele'];
// default wenn nicht gesetzt
if($anzahlFelder == '' || $spiele == '') {
	$anzahlFelder=3; //anzahl badminton teams
	$spiele=9; //spieler/mannschaftssportart team
}
?>

<style>
table, th, td {
	border: 1px solid black;
	border-collapse: collapse;
}

th, td {
	padding: 3px;
	text-align: center;
}
</style>

<body>
<h2>Tabelle konfigurieren</h2>
<b> Wenn die Tabelle so in Ordnung ist, kann dieser Teil übersprungen werden!!! </b> <br> 
Falls eine Andere Mannschaftsaufteilung gewünscht ist, bitte hier eingeben und auf "Tabelle umbauen" klicken, die Tabelle wird dann umgebaut. <br>
Es dürfen auch Felder leer gelassen werden.<br>
<br>

<form action="" method="POST">
<table>
	<tr>
	<td> <input type="number" name="anzahlFelder" value="3" required="required" min="0" max="25"> Anzahl Spielfelder
	<td> <input type="number" name="anzahlSpiele" value="9" required="required" min="0" max="25"> Anzahl Spiele im gesamten Block
	</tr>
</table>	

<input type="submit" name="Tabelle umbauen" value="Tabelle umbauen"> 

</form>



<h1>Spielplan eintragen</h1>
Die <b>Spaltenüberschriften wiederholen sich</b> der Übersichtlichkeit halber <b>alle zehn Zeilen</b>, das hat keinen Effekt auf die Eintragung in die DB!<br>
<br>
<form action="submit_spiele.php" method="POST">
<table>
	<tr>
	<th colspan="3">Dieser Spielplan ist für....
	</tr>	
	
	<tr>
	<td>Tag
	<td>Stufe
	<td>Sportart
	</tr>
	
	<tr>
		<td><input type="radio" name="tag" value="MO">Montag <br>
			<input type="radio" name="tag" value="DI" checked>Dienstag
		<td><select name="stufe">
				<option value="US">US</option>
				<option value="MS" selected>MS</option>
				<option value="OS">OS</option>
			</select>
		<td><select name="sportart">
				<option value="BB">BB</option>
				<option value="BM" selected>BM</option>
				<option value="FB">FB </option>
				<option value="TT">TT</option>
				<option value="VB">VB</option>
			</select>
	<tr>
</table>
<br>
<!-- eigentlicher Spiel-Table -->
<table>

<?php 


for($timeID = 0; $timeID < $spiele; $timeID++) {
	if($timeID % 10 == 0) { // jede 10. Zeile
		echo '<tr>';
		echo '<th>'; // erste Spalte frei (Nr)
		for($feld = 0; $feld < $anzahlFelder; $feld++) {
			echo '<th colspan="2"> Feld ' . ($feld + 1);
		}
	echo '</tr>';
		echo '<tr>
<th>Nr';
	for($feld = 0; $feld < $anzahlFelder; $feld++) {
		echo '<th>Team 1
<th>Team 2';
	}
	echo '</tr>
';
	}
	
	echo '<tr><td>' . ($timeID + 1);
	for($feld = 0; $feld < $anzahlFelder; $feld++) {
		echo '
		<td><input type="text" name="team1[' . $feld . '][]" maxlength="5" size="5"><td><input type="text" name="team2['. $feld . '][]" maxlength="5" size="5">';
	}
	echo '</tr>
';

}

echo '<input type="hidden" name="anzahlFelder" value="' . $anzahlFelder . '">';
?>
</table>
<input type="submit" name="submit" value="Eintragen">
</form>
<br>
</body>
</html>

