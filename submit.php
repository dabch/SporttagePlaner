<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8' name='viewport' content='width=device-width' />
<title>Eingabe Klassenlisten @ Sporttage-Kepler</title>
</head>

<body>
	<?php
	$servername = 'wp052.webpack.hosteurope.de';
	$username = 'db1093417-sport';
	$password = '%&SporTTage14@!';
	$dbname = 'db1093417-sporttage';
	
	$klasse = $_POST['klasse'];

	$conn = new mysqli($servername, $username, $password, $dbname);

	if($conn->connect_error){
		die('Connection failed:\n' . $conn->connect_error);
	}
	
	$abfrageSchueler = $conn->prepare('SELECT ID FROM Mannschaften_MS WHERE Vorname = ? AND Name = ? AND Klasse = ?');
	
	$schuelerHinzufuegen = $conn->prepare('INSERT INTO Mannschaften_MS (Vorname, Name, Klasse) VALUES (?, ?, ?);');

	function getSchuelerID($name, $vorname, $klasse) {
	  //nun lokale variable damit man sie schließen und öffnen kann
    /*  $GLOBALS['abfrageSchueler'] = $conn->prepare('SELECT ID FROM Mannschaften_MS WHERE Vorname = ? AND Name = ? AND Klasse = ?');
	    $GLOBALS['abfrageSchueler']->bind_param('sss', $vorname, $name, $klasse);
	    $GLOBALS['abfrageSchueler']->execute();
	    $GLOBALS['abfrageSchueler']->bind_result($id);
	    if($GLOBALS['abfrageSchueler']->fetch()) {
        $GLOBALS['abfrageSchueler']->close;
	    	return $id;
	    } else {
	    	$GLOBALS['schuelerHinzufuegen']->bind_param('sss', $vorname, $name, $klasse);
	    	$GLOBALS['schuelerHinzufuegen']->execute();
	    	$GLOBALS['abfrageSchueler']->execute();
	    	$GLOBALS['abfrageSchueler']->bind_result($id);
	    	if($GLOBALS['abfrageSchueler']->fetch()) {
          $GLOBALS['abfrageSchueler']->close();
		    	return $id;
	    	} 
	    }
      */
      $abfrageSchueler = $GLOBALS['conn']->prepare('SELECT ID FROM Mannschaften_MS WHERE Vorname = ? AND Name = ? AND Klasse = ?');
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

//	$conn->close();

	echo ' conn closed';
	
	$vornamen = $_POST['fb1_v'];
	$nachnamen = $_POST['fb1_n'];
	echo '<br>';
	echo $klasse . '<br>';
	 $id = 0;
   $mannschaft = $klasse .  '1';
  //$addSportartToSchueler = $conn->prepare('UPDATE Mannschaften_MS SET FB = ? WHERE ID = ?;');
  $addSportartToSchueler = "UPDATE Mannschaften_MS SET FB = " . $mannschaft . " WHERE ID = ". $id . " ";
 // $addSportartToSchueler2 = $conn->prepare('INSERT INTO Mannschaften_MS (FB) VALUES (?) WHERE ID = ?');
 // $addSportartToSchueler->bind_param('si', $mannschaft, $id);
  //$addSportartToSchueler->bind_param('si', $mannschaft, $id);
  
   $mannschaft= '"' . $mannschaft .  '"';
   
	for($i = 0; $i < count($vornamen); $i++) {
    if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
      $id = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
   
      echo $id;
   // echo gettype($id);
  //  echo gettype($mannschaft);
      echo $mannschaft;
   //   $mannschaft= '"' . $mannschaft .  '"';
      $addSportartToSchueler = "UPDATE Mannschaften_MS SET FB = " . $mannschaft . " WHERE ID = ". $id . " ";
      echo $mannschaft;
  //  $addSportartToSchueler->bind_param("si", $mannschaft, $id);
      echo $addSportartToSchueler;
      //$conn->query($addSportartToSchueler);
      //$whatever = $conn->use_result();
      //$conn->free();
      if ($conn->query($addSportartToSchueler) === TRUE) {
        echo "update succesfully";
      } else {
        echo "error: " . $conn->error;
      }
   // echo $addSportartToSchueler->execute();
   // echo $addSportartToSchueler->rowCount() . "records UPDATED succesflly";
		  echo $vornamen[$i] . ' ';
		  echo $nachnamen[$i] . '<br>';
    
    }

		
	}
 
 $conn->close();
 
	?>
</body>
</html>
