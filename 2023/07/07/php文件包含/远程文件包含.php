<?php
error_reporting(0);
highlight_file(__FILE__);
if(isset($_GET['file'])){
	$file=$_GET['file'];
	include($file);
}
?>