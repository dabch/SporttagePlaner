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
	   // echo $name;
	    //echo $vorname;
	    //echo $klasse;
	    $GLOBALS['abfrageSchueler']->bind_param('sss', $vorname, $name, $klasse);
	    $GLOBALS['abfrageSchueler']->execute();
	    $GLOBALS['abfrageSchueler']->bind_result($id);
	    if($GLOBALS['abfrageSchueler']->fetch()) {
	    	return $id;
	    } else {
	    	$GLOBALS['schuelerHinzufuegen']->bind_param('sss', $vorname, $name, $klasse);
	    	$GLOBALS['schuelerHinzufuegen']->execute();
	    	$GLOBALS['abfrageSchueler']->execute();
	    	$GLOBALS['abfrageSchueler']->bind_result($id);
	    	if($GLOBALS['abfrageSchueler']->fetch()) {
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
	
	for($i = 0; $i < count($vornamen); $i++) {
    if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
    getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
		echo $vornamen[$i] . ' ';
		echo $nachnamen[$i] . '<br>';
    }

		
	}
 
 $conn->close();
 
	?>
</body>
</html>
