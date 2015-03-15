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
Bitte die K1-Kurse so eingeben: Box neben "K1" anklicken und dann den Kurs (D1,D2,D3 etc.) in das Feld eingeben <br>
KEINE LEERZEICHEN benutzen!<br>
Umlaute können problemlos verarbeitet werden, aber bitte darauf achten, dass Namen nicht falsch geschrieben werden (oder wenn falsch, dann einheitlich falsch)<br>
Zu einem Vornamen gehört IMMER ein Nachname, sonst geht die Welt unter!<br>
<br>
Klasse eingeben:
<form action="check.php" method="post">
<input type="checkbox" name="k1" value="k1"> K1  <br>
<input type="text" name="klasse"/>

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
	
	<tr>
		<td> <input type="text" name="fb1_v[]" tabindex=1/>
		<td> <input type="text" name="fb1_n[]" tabindex=2/>
		<td> <input type="text" name="fb2_v[]" tabindex=19/>
		<td> <input type="text" name="fb2_n[]" tabindex=20/>
		<td> <input type="text" name="bb_v[]"/>
		<td> <input type="text" name="bb_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]" tabindex=3/>
		<td> <input type="text" name="fb1_n[]" tabindex=4/>
		<td> <input type="text" name="fb2_v[]" tabindex=21/>
		<td> <input type="text" name="fb2_n[]" tabindex=22/>
		<td> <input type="text" name="bb_v[]"/>
		<td> <input type="text" name="bb_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]" tabindex=5/>
		<td> <input type="text" name="fb1_n[]" tabindex=6/>
		<td> <input type="text" name="fb2_v[]" tabindex=23/>
		<td> <input type="text" name="fb2_n[]" tabindex=24/>
		<td> <input type="text" name="bb_v[]"/>
		<td> <input type="text" name="bb_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]" tabindex=7/>
		<td> <input type="text" name="fb1_n[]" tabindex=8/>
		<td> <input type="text" name="fb2_v[]" tabindex=25/>
		<td> <input type="text" name="fb2_n[]" tabindex=26/>
		<td> <input type="text" name="bb_v[]"/>
		<td> <input type="text" name="bb_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]" tabindex=9/>
		<td> <input type="text" name="fb1_n[]" tabindex=10/>
		<td> <input type="text" name="fb2_v[]" tabindex=27/>
		<td> <input type="text" name="fb2_n[]" tabindex=28/>
		<td> <input type="text" name="bb_v[]"/>
		<td> <input type="text" name="bb_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]" tabindex=11/>
		<td> <input type="text" name="fb1_n[]" tabindex=12/>
		<td> <input type="text" name="fb2_v[]" tabindex=29/>
		<td> <input type="text" name="fb2_n[]" tabindex=30/>
		<td> <input type="text" name="bb_v[]"/>
		<td> <input type="text" name="bb_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]" tabindex=13/>
		<td> <input type="text" name="fb1_n[]" tabindex=14/>
		<td> <input type="text" name="fb2_v[]" tabindex=31/>
		<td> <input type="text" name="fb2_n[]" tabindex=32/>
		<td> <input type="text" name="bb_v[]"/>
		<td> <input type="text" name="bb_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]" tabindex=15/>
		<td> <input type="text" name="fb1_n[]" tabindex=16/>
		<td> <input type="text" name="fb2_v[]" tabindex=33/>
		<td> <input type="text" name="fb2_n[]" tabindex=34/>
		<td> <input type="text" name="bb_v[]"/>
		<td> <input type="text" name="bb_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
	<tr>
		<td> <input type="text" name="fb1_v[]" tabindex=17/>
		<td> <input type="text" name="fb1_n[]" tabindex=18/>
		<td> <input type="text" name="fb2_v[]" tabindex=35/>
		<td> <input type="text" name="fb2_n[]" tabindex=36/>
		<td> <input type="text" name="bb_v[]"/>
		<td> <input type="text" name="bb_n[]"/>
		<td> <input type="text" name="ft_v[]"/>
		<td> <input type="text" name="ft_n[]"/>
	</tr>
	
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
	session_start();
	$conn = new mysqli($servername, $username, $password, $dbname);
	
	if($conn->connect_error){
		die('Connection failed:\n' . $conn->connect_error);
	}
?>
</body>
</html>

