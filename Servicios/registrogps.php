<?php
include_once './db_functions.php';
//Create Object for DB_Functions clas
$db = new DB_Functions(); 

//Get JSON posted by Android Application
$json = $_POST["gpsJSON"];
//Remove Slashes
if (get_magic_quotes_gpc()){
$json = stripslashes($json);
}
//Decode JSON into an Array
$data = json_decode($json);
//Util arrays to create response JSON
$a=array();
$b=array();
//Loop through an Array and insert data read from JSON into MySQL DB
for($i=0; $i<count($data) ; $i++)
{
//Store User into MySQL DB
$res = $db->storegps($data[$i]->dat_id,$data[$i]->usu_id,$data[$i]->dat_latitud,$data[$i]->dat_longitud,$data[$i]->dat_precision,$data[$i]->dat_altitud,$data[$i]->dat_velocidad,$data[$i]->dat_proveedor,$data[$i]->dat_fechahora_lectura);

	//Based on inserttion, create JSON response
	if($res){
	    $b["usu_id"] = $data[$i]->usu_id;
		$b["dat_fechahora_lectura"] = $data[$i]->dat_fechahora_lectura;
		$b["status"] = 'yes';
		array_push($a,$b);
	}else{
	    $b["usu_id"] = $data[$i]->usu_id;
		$b["dat_fechahora_lectura"] = $data[$i]->dat_fechahora_lectura;
		$b["status"] = 'no';
		array_push($a,$b);
	}
}
//Post JSON response back to Android Application
echo json_encode($a);

?>
