<!DOCTYPE html>

<html>
<head>
<meta charset='UTF-8' name='viewport' content='width=device-width' />
<title>Eingabe Klassenlisten @ Sporttage-Kepler</title>
</head>

<style>
table, th, td {
	border: 1px solid black;
	border-collapse: collapse;
	text-align: center;
}

th, td {
	padding: 3px;
}
</style>

<body>
<?php


$servername = 'mysql20.1blu.de';
$username = 's228201_2224606';
$password = '%&SporTTage14@!';
$dbname = 'db228201x2224606';

$klasseA = $_POST['klasse'];
$klasse = chop($klasseA);
$k1 = $_POST['k1'];
 $_POST['klasse'];
echo 'Klasse: ' . $klasse . '<br>';
//echo $sessionID;
//echo "vor der stufe bla";
   
if (strpos($klasse,'10') !== false || $k1 == "k1" || strpos($klasse,'K1') !== false ||  strpos($klasse,'K2') !== false) {
	$stufenListe = "Mannschaften_OS";
} else if (strpos($klasse,'7') !== false || strpos($klasse,'8') !== false || strpos($klasse,'9') !== false || strpos($klasse,'A1') !== false) {
	$stufenListe = "Mannschaften_MS";
} else if (strpos($klasse,'5') !== false || strpos($klasse,'6') !== false) {
	$stufenListe = "Mannschaften_US";	
}


//echo $stufenListe . '<br>' ;

$conn = new mysqli($servername, $username, $password, $dbname);

if($conn->connect_error){
	die('Connection failed:\n' . $conn->connect_error);
}

  // Umlaute fixen
$conn->query('SET NAMES "utf8"');
  
$abfrageSchuelerStatement = 'SELECT ID FROM ' . $stufenListe . ' WHERE Vorname = ? AND Name = ? AND Klasse = ?';
  	
$abfrageSchueler = $conn->prepare($abfrageSchuelerStatement);
	
$schuelerHinzufuegenStatement =  'INSERT INTO ' . $stufenListe . ' (Vorname, Name, Klasse) VALUES (?, ?, ?);';
$schuelerHinzufuegen = $conn->prepare($schuelerHinzufuegenStatement);

function getSchuelerID($name, $vorname, $klasse) {
	//nun lokale variable damit man sie schließen und öffnen kann
    
	$abfrageSchueler = $GLOBALS['conn']->prepare($GLOBALS['abfrageSchuelerStatement']);	
        $abfrageSchueler->bind_param('sss', $vorname, $name, $klasse);
    	$abfrageSchueler->execute();
    	$abfrageSchueler->bind_result($id);
	if($abfrageSchueler->fetch()) {
       		$abfrageSchueler->close;
    		return $id;
	} else {
		$GLOBALS['schuelerHinzufuegen']->bind_param('sss', $vorname, $name, $klasse);
    		$GLOBALS['schuelerHinzufuegen']->execute();
    		$abfrageSchueler->execute();
    		$abfrageSchueler->bind_result($id);
    		if($abfrageSchueler->fetch()) {
       			 $abfrageSchueler->close();
		 	return $id;
    		} 
    	}	
}

	//echo getSchuelerID('Herbert', 'Idris', '9a');


 $badmintonAnzahlTeams=$_POST['badmintonAnzahlTeams']; //anzahl badminton teams
 $AnzahlSpielerMannschaft=$_POST['AnzahlSpielerMannschaft']; //spieler/mannschaftssportart team
 $anzahlTeams = $_POST['anzahlTeams']; 
/*for ($i=0; $i < count($anzahlTeams); $i++) {
	echo "<br> anzahl teams: " . $anzahlTeams[$i];
} */ 

  
 /* $anzahlTeams = array(
  0 => 2, //FB
  1 => 1, //BB
  2 => 1, //FT
  3 => 1, //VB
  4 => 1,  //ST 
  );*/
  $nameDB = array(
  0 => 'fb', //FB
  1 => 'bb', //BB
  2 => 'ft', //FT
  3 => 'vb', //VB
  4 => 'st',  //ST 
  );
  $nameVoll = array(
  0 => 'Fußball Team', //FB
  1 => 'Basketball Team', //BB
  2 => 'Fahrradtour', //FT
  3 => 'Volleyball Team', //VB
  4 => 'Staffellauf Team',  //ST 
  );
  $nameKurz = array(
  0 => 'FB', //FB
  1 => 'BB', //BB
  2 => 'FT', //FT
  3 => 'VB', //VB
  4 => 'ST',  //ST 
  ); 
  
  
$hoechsteMannschaftssportartNr = 0;
$anzahlTeamsGesamt=0;
for ($i=0; $i<count($anzahlTeams); $i++) {
	for ($a=0; $a<$anzahlTeams[$i]; $a++) {
        	$anzahlTeamsGesamt++;
      	}
} 
  
$hoechsteMannschaftssportartNr = $anzahlTeamsGesamt*2-1; //*2: 2 spalten pro team; -1: array startet bei 0
//echo ' hoechsteMannschaftssportartNr: ' . $hoechsteMannschaftssportartNr . '<br>';  
//echo $anzahlTeamsGesamt . '<br>'; 
$neueArrayLaenge=0;
for ($i=0; $i<count($anzahlTeams); $i++) { //für alle Teams
    	for ($a=1; $a<$anzahlTeams[$i]+1; $a++) {
        	$teamNummer = $a;
        	//vornamen teil
        	$sportartenName[$neueArrayLaenge] = $nameDB[$i] . $teamNummer . '_v';          //array mit fb1_v etc
        	$sportartenVollerName[$neueArrayLaenge]= $nameVoll[$i] . ' ' . $teamNummer ;   //array fürs echo, ausgeschrieben
        	$sportartenKurzerName[$neueArrayLaenge]= $nameKurz[$i];                        //array für datenbankspalte
        	$sportartenAnzahlTeams[$neueArrayLaenge] = $anzahlTeams[$i];
       		/* echo 'Nr. ' . $neueArrayLaenge . ' Team: ' . $sportartenName[$neueArrayLaenge] . '<br>';
        	echo 'Nr. ' . $neueArrayLaenge . ' Team: ' . $sportartenVollerName[$neueArrayLaenge] . '<br>';
        	echo 'Nr. ' . $neueArrayLaenge . ' Sportart: ' . $sportartenKurzerName[$neueArrayLaenge] . '<br>';    */    
        
        	$neueArrayLaenge++;
        	
        	//nachnamen teil
        	$sportartenName[$neueArrayLaenge]= $nameDB[$i] . $teamNummer . '_n';
        	$sportartenVollerName[$neueArrayLaenge]= $nameVoll[$i] . ' ' . $teamNummer;
        	$sportartenKurzerName[$neueArrayLaenge]= $nameKurz[$i];
        	$sportartenAnzahlTeams[$neueArrayLaenge] = $anzahlTeams[$i];
         
       		/* echo 'Nr. ' . $neueArrayLaenge . ' Team: ' . $sportartenName[$neueArrayLaenge] . '<br>';
        	echo 'Nr. ' . $neueArrayLaenge . ' Team: ' . $sportartenVollerName[$neueArrayLaenge] . '<br>';
        	echo 'Nr. ' . $neueArrayLaenge . ' Sportart: ' . $sportartenKurzerName[$neueArrayLaenge] . '<br>';      */
        
        	$neueArrayLaenge++;  
    	}
}     

//läd die werte aus $_POST von sporttage.php in sportarten1 an der richtigen stelle
for ($i=0; $i < count($sportartenName); $i++ )  {   
	$sportarten1[$i] = $_POST[$sportartenName[$i]];
}


//*********************************ALLE MANNSCHAFTSSPORTARTEN*************************************************************************************************************
//variablen wird der wert zugewiesen	
//$vornamen = $_POST['fb1_v'];
//$nachnamen = $_POST['fb1_n'];
for ($sportart=0; $sportart < ($anzahlTeamsGesamt*2); $sportart+=2) {
	$id = 0;
 	// echo $vornamen[0];
	//manschaftname besteht aus klasse + zahl, z.B. 9a1 9a2, es geht hier um mannschaft 
	//es gibt aktuell max 4 mannschaften/sportart!
	for ($potentielleMannschaftNummer = 1; $potentielleMannschaftNummer < $sportartenAnzahlTeams[$sportart]; $potentielleMannschaftNummer++) {
 		if (strpos($sportartenName[$sportart],"'" . $potentielleMannschaftNummer ."'") !== false) {
  			$mannschaftNummer=$potentielleMannschaftNummer;
 		}
 	}
 	/* else if (strpos($sportartenName[$sportart],'3') !== false) {
  		$mannschaftNummer=3;
 	} elseif (strpos($sportartenName[$sportart],'2') !== false) {
  		$mannschaftNummer=2;
 	} else {
  		$mannschaftNummer=1;
 	}*/
 
	if ($k1 == "k1") {
   		$mannschaft = $klasse .  '.' . $mannschaftNummer;
	} else {
  		$mannschaft = $klasse . $mannschaftNummer;
	} 
	//für die SQL Anweisung muss die Mannschaft in Anführungszeichen sein:
	$mannschaft= '"' . $mannschaft .  '"';
   	//für alle eingetragenen namen
	for($i = 0; $i < count($sportarten1[$sportart]); $i++) {
		if ($sportarten1[$sportart][$i] != '' && $sportarten1[$sportart+1][$i] != '')  { //wenn etwas eingetragen ist
    			$id = getSchuelerID($sportarten1[$sportart+1][$i], $sportarten1[$sportart][$i], $klasse);   //id holen
			$addSportartToSchueler = "UPDATE " . $stufenListe . " SET " . $sportartenKurzerName[$sportart] . " = " . $mannschaft . " WHERE ID = ". $id . " "; // string erstellenn
			//echo $addSportartToSchueler;
    			if ($conn->query($addSportartToSchueler) === TRUE) {	//string ausführen und mit rückgabewert weitermachen
				echo $sportartenVollerName[$sportart] . " update erfolgreich bei: ";		//erfolg ausgeben
			} else {
				echo "error: " . $conn->error . " bei: ";		//fehler ausgeben
			}
			echo $sportarten1[$sportart][$i] . ' ';		//jeweils den Namen des Schülers ausgeben
			echo $sportarten1[$sportart+1][$i] . '<br>';
		}
	}
}	
	
//***************************TISCHTENNIS****************************************************************************************************************
$vornamen = $_POST['tt_v'];
$nachnamen = $_POST['tt_n'];
echo '<br>';
$id = 0;
$mannschaftsNummer=0;
for($i = 1; $i < count($vornamen); $i+=2) {
   	$mannschaftsNummer++;
     	//   echo "in der for schleife";
        if ($vornamen[$i] != '' && $nachnamen[$i] != '' && $vornamen[$i-1] != '' && $nachnamen[$i-1] != '')  {
          	//  echo "in der for schleife";
           	$id1 = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
           	$id2 = getSchuelerID($nachnamen[$i-1], $vornamen[$i-1], $klasse);
           	$mannschaftTT=0;
           	if ($k1 == "k1") {
              		$mannschaftTT = $klasse .  '.' . $mannschaftNummer;
           	} else {
              		$mannschaftTT = $klasse . $mannschaftNummer;
           	} 
           	$mannschaftTT= '"' . $mannschaftTT .  '"';
   
            	//echo $id1;
            	//echo $id2;
            	//echo $mannschaftTT;
            	$addSportartToSchueler1 = "UPDATE " . $stufenListe . " SET TT = " . $mannschaftTT . " WHERE ID = ". $id1 . " ";
            	$addSportartToSchueler2 = "UPDATE " . $stufenListe . " SET TT = " . $mannschaftTT . " WHERE ID = ". $id2 . " ";
            	if ($conn->query($addSportartToSchueler1) === TRUE) {
                  	echo "Tischtennis update erfolgreich bei: ";
              	} else {
                  	echo "error: " . $conn->error . " bei: ";
              	}
              	echo $vornamen[$i] . ' ';
		echo $nachnamen[$i] . '<br>';
              	if ($conn->query($addSportartToSchueler2) === TRUE) {
                  	echo "Tischtennis update erfolgreich bei: ";
              	} else {
                  	echo "error: " . $conn->error . " bei: ";
              	}
		echo $vornamen[$i-1] . ' ';
		echo $nachnamen[$i-1] . '<br>';
       }	
}        
     
       //bm
$vornamen = $_POST['bm_v'];
$nachnamen = $_POST['bm_n'];
echo '<br>';
$id = 0;
$mannschaftsNummer=0;
for($i = 1; $i < count($vornamen); $i+=2) {
	$mannschaftsNummer++;
        if ($vornamen[$i] != '' && $nachnamen[$i] != '' && $vornamen[$i-1] != '' && $nachnamen[$i-1] != '')  {
           	$id1 = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
           	$id2 = getSchuelerID($nachnamen[$i-1], $vornamen[$i-1], $klasse);
           	$mannschaftBM=0;
           	if ($k1 == "k1") {
              		$mannschaftBM = $klasse .  '.' . $mannschaftNummer;
           	} else {
              		$mannschaftBM = $klasse . $mannschaftNummer;
           	} 
           	$mannschaftBM= '"' . $mannschaftBM .  '"';
   
            	//echo $id1;
            	//echo $id2;
            	//echo $mannschaftBM . "<br>";
            	$addSportartToSchueler1 = "UPDATE " . $stufenListe . " SET BM = " . $mannschaftBM . " WHERE ID = ". $id1 . " ";
            	$addSportartToSchueler2 = "UPDATE " . $stufenListe . " SET BM = " . $mannschaftBM . " WHERE ID = ". $id2 . " ";
              	if ($conn->query($addSportartToSchueler1) === TRUE) {
                  	echo "Badminton update erfolgreich bei: ";
              	} else {
                  	echo "error: " . $conn->error . " bei: ";
              	}
              	echo $vornamen[$i] . ' ';
		echo $nachnamen[$i] . '<br>';
              	if ($conn->query($addSportartToSchueler2) === TRUE) {
                  	echo "Badminton update erfolgreich bei: ";
              	} else {
                  	echo "error: " . $conn->error . " bei: ";
              	}
		echo $vornamen[$i-1] . ' ';
		echo $nachnamen[$i-1] . '<br>';
       }	
}        
 
 $conn->close();
 //SESSION schließen, damit leute nicht mehrfach eingetragen werden, falls man den selben PC  nutzt
 //session_destroy();
 $deinName = $_POST['schuldiger'];
 echo '<br> Ich kenne dich. du heißt ' . $deinName . ". <br><br>"
 
	?>
  
 Wenn oben ein Fehler steht, bitte bei Daniel/Sandesh melden und Fehlermeldung kopieren (Screenshot)! <br>
 Wenn alles erfolgreich war geht es <a href="http://sporttage.smv-kepler.de/klassenlisten.php">hier</a> wieder zum Anfang.

</body>
</html>
