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
BITTE ALLES NOCHMALS KONTROLLIEREN!!! <br>
KEINE LEERZEICHEN benutzen!<br>
Umlaute können problemlos verarbeitet werden, aber bitte darauf achten, dass Namen nicht falsch geschrieben werden (oder wenn falsch, dann einheitlich falsch)<br>
Zu einem Vornamen gehört IMMER ein Nachname, sonst geht die Welt unter!<br>
<br>
<?php 
//alle variablen speichern
$klasse = $_POST['klasse'];
//k1 in session speichern
$k1 = $_POST['k1'];

$sportartenName = array(
  0 => "fb1_v[]",
  1 => "fb1_n[]",
  2 => "fb2_v[]", 
  3 => "fb2_n[]",
  4 => "bb_v[]",
  5 => "bb_n[]", 
  6 => "ft_v[]", 
  7 => "ft_n[]"
     );

 /*    alt
$sportarten1['fb1_v[]'] = $_POST['fb1_v'];
//echo $sportarten1['fb1_v[]']['0'];
$sportarten1['fb1_n[]'] = $_POST['fb1_n'];

$sportarten1['fb2_v[]'] = $_POST['fb2_v'];
$sportarten1['fb2_n[]'] = $_POST['fb2_n'];

$sportarten1['bb_v[]'] = $_POST['bb_v'];
$sportarten1['bb_n[]'] = $_POST['bb_n'];

$sportarten1['ft_v[]'] = $_POST['ft_v'];
$sportarten1['ft_n[]'] = $_POST['ft_n'];

$sportarten1['bm_v[]'] = $_POST['bm_v'];
$sportarten1['bm_n[]'] = $_POST['bm_n'];

$sportarten1['tt_v[]'] = $_POST['tt_v'];
$sportarten1['tt_n[]'] = $_POST['tt_n'];

$sportarten1['vb_v[]'] = $_POST['vb_v'];
$sportarten1['vb_n[]'] = $_POST['vb_n'];

$sportarten1['st_v[]'] = $_POST['st_v'];
$sportarten1['st_n[]'] = $_POST['st_n'];
*/
//neu:
//echo $sportarten1[0];
$sportarten1[0] = $_POST['fb1_v'];
/*echo $sportarten1['fb1_v']['0'];
 echo $sportarten1[0];
 echo $sportarten1['fb1_v'];
 echo $sportarten1[$sportarten1[0]];
echo $sportarten1[$sportarten1[0]][0]; */
 
$sportarten1[1] = $_POST['fb1_n'];

$sportarten1[2] = $_POST['fb2_v'];
$sportarten1[3] = $_POST['fb2_n'];

$sportarten1[4] = $_POST['bb_v'];
$sportarten1[5] = $_POST['bb_n'];

$sportarten1[6] = $_POST['ft_v'];
$sportarten1[7] = $_POST['ft_n'];

$sportarten1[8] = $_POST['bm_v'];
$sportarten1[9] = $_POST['bm_n'];

$sportarten1[10] = $_POST['tt_v'];
$sportarten1[11] = $_POST['tt_n'];

$sportarten1[12] = $_POST['vb_v'];
$sportarten1[13] = $_POST['vb_n'];

$sportarten1[14] = $_POST['st_v'];
$sportarten1[15] = $_POST['st_n'];

?>

  

Klasse eingeben:
<form action="submit.php" method="post">
<input type="checkbox" name="k1" value="k1"> K1  <br>
<input type="text" name="klasse" required="required" value="<?= $klasse ?> " />

<table>
	<!-- Fußball 1 + 2 & Fahrradtour -->
	<tr>
		<td colspan="2"> Fußball 1
		<td colspan="2"> Fußball 2		
		<td colspan="2"> Basketball
		<td colspan="2"> Fahrradtour
	</tr>
	<tr>
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
	</tr>

 <?php 

  $tabindex = array(1, 2, 19, 20, 37, 38, 55, 56);
  
  echo $tabindex[1];
 //die länge hängt mit der länge von fb1_v zusammen
  
  for($zeile = 0; $zeile<9; $zeile++) {
    echo "<tr> \n";
    for($i = 0; $i<8; $i++)  {
      //echo "in der for schleife";
      echo "<td> <input type=\"text\" name=\"" . $sportartenName[$i] . "\"  value=\"" .  $sportarten1[$i][$zeile] .  "\" tabindex=" . $tabindex[$i] . "/> \n"; 
      $tabindex[$i]+=2; //tabindexwert um 2 erhöhen für schönes eintippen  
    }
    echo "</tr> \n"; 
  }
 
  
?> 
    
	  
  

	<!-- Badminton & Tischtennis & Volleyball -->
	
	<tr>
		<td colspan="8">  	
	</tr>
		<td colspan="2"> Badminton
		<td colspan="2"> Tischtennis
		<td colspan="2"> Volleyball
		<td colspan="2"> Staffellauf
	</tr>
	
	<tr>
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
	</tr>
	
	<!-- Team1 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 1
		<td colspan="2" style="text-align:center"> Team 1
		
		<td> <input type="text" name="vb_v[]"/>
		<td> <input type="text" name="vb_n[]"/>
		<td> <input type="text" name="st_v[]"/>
		<td> <input type="text" name="st_n[]"/>
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
		
		<td> <input type="text" name="vb_v[]"/>
		<td> <input type="text" name="vb_n[]"/>
		<td> <input type="text" name="st_v[]"/>
		<td> <input type="text" name="st_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
		
		<td> <input type="text" name="vb_v[]"/>
		<td> <input type="text" name="vb_n[]"/>
		<td> <input type="text" name="st_v[]"/>
		<td> <input type="text" name="st_n[]"/>
	<tr>
	
	<!-- Team2 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 2
		<td colspan="2" style="text-align:center"> Team 2
		
		<td> <input type="text" name="vb_v[]"/>
		<td> <input type="text" name="vb_n[]"/>
		<td> <input type="text" name="st_v[]"/>
		<td> <input type="text" name="st_n[]"/>
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
		
		<td> <input type="text" name="vb_v[]"/>
		<td> <input type="text" name="vb_n[]"/>
		<td> <input type="text" name="st_v[]"/>
		<td> <input type="text" name="st_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
		
		<td> <input type="text" name="vb_v[]"/>
		<td> <input type="text" name="vb_n[]"/>
		<td> <input type="text" name="st_v[]"/>
		<td> <input type="text" name="st_n[]"/>
	<tr>
	
	<!-- Team3 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 3
		<td colspan="2" style="text-align:center"> Team 3
		
		<td> <input type="text" name="vb_v[]"/>
		<td> <input type="text" name="vb_n[]"/>
		<td> <input type="text" name="st_v[]"/>
		<td> <input type="text" name="st_n[]"/>
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
		
		<td> <input type="text" name="vb_v[]"/>
		<td> <input type="text" name="vb_n[]"/>
		<td> <input type="text" name="st_v[]"/>
		<td> <input type="text" name="st_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
		
		<td> <input type="text" name="vb_v[]"/>
		<td> <input type="text" name="vb_n[]"/>
		<td> <input type="text" name="st_v[]"/>
		<td> <input type="text" name="st_n[]"/>
	<tr>
		
	<!-- Team4 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 4
		<td colspan="2" style="text-align:center"> Team 4
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
		
	<!-- Team5 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 5
		<td colspan="2" style="text-align:center"> Team 5
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
	
	
		
	<!-- Team6 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 6
		<td colspan="2" style="text-align:center"> Team 6
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
  
  	<!-- Team7 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 7
		<td colspan="2" style="text-align:center"> Team 7
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
  
  	<!-- Team8 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 8
		<td colspan="2" style="text-align:center"> Team 8
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
  
  	<!-- Team9 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 9
		<td colspan="2" style="text-align:center"> Team 9
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
  
  	<!-- Team10 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 10
		<td colspan="2" style="text-align:center"> Team 10
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
  
  	<!-- Team11 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 11
		<td colspan="2" style="text-align:center"> Team 11
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
  
  	<!-- Team12 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 12
		<td colspan="2" style="text-align:center"> Team 12
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
  
  	<!-- Team13 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 13
		<td colspan="2" style="text-align:center"> Team 13
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
	
    	<!-- Team14 -->
	<tr>
		<td colspan="2" style="text-align:center"> Team 14
		<td colspan="2" style="text-align:center"> Team 14
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
	<tr>
	
	</tr>
		<td> <input type="text" name="bm_v[]"/>
		<td> <input type="text" name="bm_n[]"/>
		<td> <input type="text" name="tt_v[]"/>
		<td> <input type="text" name="tt_n[]"/>
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
  //ini_set("session.use_trans_sid",true);
	//session_start();
	$conn = new mysqli($servername, $username, $password, $dbname);
	
	if($conn->connect_error){
		die('Connection failed:\n' . $conn->connect_error);
	}
?>
</body>
</html>

