<!DOCTYPE html>
<?php 
?>
<html>
<head>
<meta charset='UTF-8' name='viewport' content='width=device-width' />
<title>Eingabe Klassenlisten @ Sporttage-Kepler</title>
</head>

<body>
<?php


$servername = 'mysql20.1blu.de';
$username = 's228201_2224606';
$password = '%&SporTTage14@!';
$dbname = 'db228201x2224606';

$klasseA = $_POST['klasse'];
$klasse = chop($klasseA);
$k1 = $_POST['k1'];
echo $_POST['klasse'];
//echo $klasse;
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


 $sportartenName = array(          //reihenfolge der Sportarten bestimmen für erstellen der tabelle
  0 => 'fb1_v',
  1 => 'fb1_n',
  2 => 'fb2_v', 
  3 => 'fb2_n',
  4 => 'bb_v',
  5 => 'bb_n', 
  6 => 'ft_v', 
  7 => 'ft_n',
  //2. teil
  8 => 'vb_v',
  9 => 'vb_n',
  10 => 'st_v', 
  11 => 'st_n',
  12 => 'bm_v',
  13 => 'bm_n', 
  14 => 'tt_v', 
  15 => 'tt_n',
     );   
     
$sportartenVollerName = array(          //reihenfolge der Sportarten bestimmen für erstellen der tabelle
  0 => 'Fußball Team 1',
  1 => 'Fußball Team 1',
  2 => 'Fußball Team 2', 
  3 => 'Fußball Team 2',
  4 => 'Basketball Team',
  5 => 'Basketball Team', 
  6 => 'Fahrradtour', 
  7 => 'Fahrradtour',
  //2. teil
  8 => 'Volleyball Team',
  9 => 'Volleyball Team',
  10 => 'Staffellauf Team', 
  11 => 'Staffellauf Team',
  12 => 'Badminton Teams',
  13 => 'Badminton Teams', 
  14 => 'Tischtennis Teams', 
  15 => 'Tischtennis Teams',
     ); 
     
$sportartenKurzerName = array(          //reihenfolge der Sportarten bestimmen für erstellen der tabelle
  0 => 'FB',
  1 => 'FB',
  2 => 'FB', 
  3 => 'FB',
  4 => 'BB',
  5 => 'BB', 
  6 => 'FT', 
  7 => 'FT',
  //2. teil
  8 => 'VB',
  9 => 'VB',
  10 => 'ST', 
  11 => 'ST',
  12 => 'BM',
  13 => 'BM', 
  14 => 'TT', 
  15 => 'TT',
     );          

//läd die werte aus $_POST von sporttage.php in sportarten1 an der richtigen stelle
for ($i=0; $i < count($sportartenName); $i++ )  {   
      $sportarten1[$i] = $_POST[$sportartenName[$i]];
}


	//Fußball1
//variablen wird der wert zugewiesen	
//$vornamen = $_POST['fb1_v'];
//$nachnamen = $_POST['fb1_n'];
for ($sportart=0; $sportart<12; $sportart+=2) {

$id = 0;
 // echo $vornamen[0];
//manschaftname besteht aus klasse + zahl, z.B. 9a1 9a2, es geht hier um mannschaft 1
 if (strpos($sportartenName[$sportart],'2') !== false) {
  $mannschaftNummer=2;
 } else {
  $mannschaftNummer=1;
 }
$mannschaft = $klasse .  '_' . $mannschaftNummer;
//für die SQL Anweisung muss die Mannschaft in Anführungszeichen sein:
$mannschaft= '"' . $mannschaft .  '"';
   //für alle eingetragenen namen
for($i = 0; $i < count($sportarten1[$sportart]); $i++) {
	if ($sportarten1[$sportart][$i] != '' && $sportarten1[$sportart+1][$i] != '')  { //wenn etwas eingetragen ist
    $id = getSchuelerID($sportarten1[$sportart+1][$i], $sportarten1[$sportart][$i], $klasse);   //id holen
		$addSportartToSchueler = "UPDATE " . $stufenListe . " SET " . $sportartenKurzerName[$sportart] . " = " . $mannschaft . " WHERE ID = ". $id . " "; // string erstellenn
	//	echo $addSportartToSchueler;
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
	
     //tt
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
           $mannschaftTT = $klasse . '_' . $mannschaftsNummer;
           $mannschaftTT= '"' . $mannschaftTT .  '"';
   
            echo $id1;
            echo $id2;
            echo $mannschaftTT;
            $addSportartToSchueler1 = "UPDATE " . $stufenListe . " SET TT = " . $mannschaftTT . " WHERE ID = ". $id1 . " ";
            $addSportartToSchueler2 = "UPDATE " . $stufenListe . " SET TT = " . $mannschaftTT . " WHERE ID = ". $id2 . " ";
              if ($conn->query($addSportartToSchueler1) === TRUE) {
                  echo "TT update erfolgreich bei: ";
              } else {
                  echo "error: " . $conn->error . " bei: ";
              }
              echo $vornamen[$i] . ' ';
		          echo $nachnamen[$i] . '<br>';
              if ($conn->query($addSportartToSchueler2) === TRUE) {
                  echo "TT update erfolgreich bei: ";
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
           $mannschaftBM = $klasse . '_' . $mannschaftsNummer;
           $mannschaftBM= '"' . $mannschaftBM .  '"';
   
            echo $id1;
            echo $id2;
            echo $mannschaftBM . "<br>";
            $addSportartToSchueler1 = "UPDATE " . $stufenListe . " SET BM = " . $mannschaftBM . " WHERE ID = ". $id1 . " ";
            $addSportartToSchueler2 = "UPDATE " . $stufenListe . " SET BM = " . $mannschaftBM . " WHERE ID = ". $id2 . " ";
              if ($conn->query($addSportartToSchueler1) === TRUE) {
                  echo "BM update erfolgreich bei: ";
              } else {
                  echo "error: " . $conn->error . " bei: ";
              }
              echo $vornamen[$i] . ' ';
		          echo $nachnamen[$i] . '<br>';
              if ($conn->query($addSportartToSchueler2) === TRUE) {
                  echo "BM update erfolgreich bei: ";
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
 
	?>
  
 Wenn oben ein Fehler steht, bitte bei Daniel/Sandesh melden und Fehlermeldung kopieren (Screenshot)! <br>
 Wenn alles erfolgreich war geht es <a href="http://sporttage.smv-kepler.de/">hier</a> wieder zum Anfang.

</body>
</html>
