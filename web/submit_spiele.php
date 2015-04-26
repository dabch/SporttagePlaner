<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width" />
<title>Eingabe Spielpläne @ Sporttage-Kepler</title>
</head>



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
<?php
$inifile = parse_ini_file("db.ini", false);
define('NL', '<br>', true); // define Newline (<br>)
if($_POST['submit'] != '') {
	$stufe = $_POST['stufe'];
	$sportart = $_POST['sportart'];
	$tag = $_POST['tag'];
	
	$team1s = $_POST['team1'];
	$team2s = $_POST['team2'];
	$schiris = $_POST['schiri'];
	
	$anzahlFelder = $_POST['anzahlFelder'];
	if($anzahlFelder == '') {
		echo "UNBEKANNTER FEHLER!";
		exit();
	}
	
	echo 'Stufe: ' . $stufe . ', Sportart: '. $sportart . ', Tag: '. $tag . NL;	
	
	$conn = new mysqli($inifile['ip'], $inifile['username'], $inifile['password'], $inifile['dbname']);

	if($conn->connect_error){
		die('Connection failed:\n' . $conn->connect_error);
	}
	
	// Umlaute fixen
	$conn->query('SET NAMES "utf8"');
	
	// Transaktion beginnen
	$conn->autocommit(false);
	//$conn->beginTransaction();
	
	for($feld = 0; $feld < $anzahlFelder; $feld++) {
		echo NL;
		echo 'Feld ' . ($feld + 1) . NL;
		$t1 = $team1s[$feld];
		$t2 = $team2s[$feld];
		
		for($spiel = 0; $spiel < count($t1); $spiel++) {	
			if($t1[$spiel] == '' ^ $t2[$spiel] == '') { // Abbruch wenn ein Spielpartner fehlt (beide Null -> nicht abbrechen, sondern weitermachen)
				echo '<u>FEHLER:</u> Zu einem Spiel gehören immer zwei Spielpartner! Feld ' . ($feld + 1) . ', Spiel Nr ' . ($spiel + 1) .NL;
				echo '<b>Der gesamte Spielplan muss erneut eigegeben werden</b> (also alles, was hier drüber als "Erfolgreich eingefügt" steht)' . NL;
				$conn->rollback();
				exit();
			} else if(chop($t1[$spiel]) == '' || chop($t2[$spiel]) == '') { // eintragung skippen falls beide leer
				continue;
			}
				
			$timeid = $spiel + 1;
			
			$paarung = chop($t1[$spiel]) . ' : ' . chop($t2[$spiel]); // choppen um überflüssige Lehrzeichen zu verhindern

			//if($schiri == '') { // Schiri: falscher Datentyp in DB
			$sql = 'INSERT INTO ' . $stufe . '_' . $sportart . '_' . $tag . '_spiele (TimeID, Feld, Paarung) VALUES (' . $timeid . ', ' . ($feld + 1) . ', "' . $paarung . '")';
			/*} else {
				$sql = 'INSERT INTO ' . $stufe . '_' . $sportart . '_' . $tag . '_spiele (TimeID, Feld, Paarung, Schiri) VALUES (' . $timeid . ', ' . ($feld + 1) . ', "' . $paarung . '", "' . $schiri . '")';
			}*/
			
			if($conn->query($sql) === TRUE) {
				echo 'Erfolgreich hinzugefügt: ' . $t1[$spiel] . ' : ' . $t2[$spiel] . NL;
			} else {
				echo 'Fehler beim Hinzufügen:<br>';
				$errmesg = $conn->error;
				echo $errmesg; // Fehlermeldung ausgeben
				exit();
			}
		}
	}
	
	$conn->commit();
	echo 'Erfolgreich';
} else {
	echo '<b>FEHLER: Keine Daten!';
}
?>
<br>
</body>
</html>

