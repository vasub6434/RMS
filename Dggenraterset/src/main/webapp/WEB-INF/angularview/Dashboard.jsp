<!DOCTYPE html>
<html>
<head>
<style type="text/css">

table { table-layout:fixed; } td{ overflow:hidden; text-overflow: ellipsis; }
.hidden {
  display: none !important;
  visibility: hidden !important;
}
</style>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>Delta PowerPlant</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.5 -->
  <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/dataTables.jqueryui.min.css">

  <!-- Font Awesome -->
  <link rel="stylesheet" href="font-awesome-4.5.0/css/font-awesome.min.css">
  <!-- DataTables -->
  <link rel="stylesheet" href="plugins/datatables/dataTables.bootstrap.css">
  <link rel="stylesheet" href="plugins/datatables/extensions/TableTools/css/dataTables.tableTools.min.css">
  <!-- Sweet Alert -->
    <link href="css/sweetalert/sweetalert.css" rel="stylesheet">
  



  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/AdminLTE.min.css">
  <!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
  <link rel="stylesheet" href="dist/css/skins/_all-skins.min.css">
 <!-- jQuery 2.1.4 -->
<script src="plugins/jQuery/jQuery-2.1.4.min.js"></script>
<script src="js/jquery-ui.js"></script>


</head>
<body class="hold-transition skin-blue layout-top-nav" ng-app="app" >
<div ui-view="header"></div>
    <div class="wrapper" >
   
   <div class="content-wrapper">
        <section class="content">
       <div class="page-loading" ng-show="viewVisible">Loading...</div>
   <div ui-view><!-- Where your content will live --></div>
      <!-- /.row -->
    </section>
    <!-- /.content -->
  </div>
  <div ui-view="footer"></div>
  <!-- /.content-wrapper -->
  </div>
   
  
</body>
<!-- angularjs -->
<script src="angularjs/js/angular.min.js"></script>
<script src="angularjs/js/angular-ui-router.min.js"></script>
<script src="angularjs/app.js"></script>
<!--  Add Image -->
<script src="angularjs/views/images/addimage.controller.js"></script>
<script src="angularjs/views/images/addimage.service.js"></script>


<!--  Add Image -->
<script src="angularjs/views/component/component.controller.js"></script>
<script src="angularjs/views/component/component.services.js"></script>


<!--  Add Image -->
<!-- <script src="angularjs/js/angular-material.min.js"></script> -->
<script src="angularjs/js/angular-messages.min.js"></script>
<script src="angularjs/js/angular-cookies.js"></script>
<!-- <script src="angularjs/js/angular-animate.min.js"></script> -->
<!-- Bootstrap 3.3.5 -->
<script src="bootstrap/js/bootstrap.min.js"></script>
<!-- DataTables -->
<script src="plugins/datatables/jquery.dataTables.min.js"></script>
<script src="plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="plugins/datatables/extensions/TableTools/js/dataTables.tableTools.min.js"></script>
<!-- SlimScroll -->
<script src="plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="plugins/fastclick/fastclick.js"></script>
<script src="js/JSONAjax.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/app.min.js"></script>
  <!-- Sweet alert -->
    <script src="js/sweetalert/sweetalert.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css" rel="stylesheet" />
<!-- page script -->
<script>
var timerID=null;
 function inittable() 
  {
  $('#expiryreport')
  .dataTable(
  {
	              
	            
  "sPaginationType" : "full_numbers",
  "oLanguage" : {
  "sLengthMenu" : "Entries per page:<span class='lenghtMenu'> _MENU_</span><span class='lengthLabel'></span>"
  },
  //"sDom" : '<"tbl-tools-searchbox"fl<"clear">>,<"tbl_tools"CT<"clear">>,<"table_content"t>,<"widget-bottom"p<"clear">>',
   "sDom": 'T<"clear">lfrtip',
  "oTableTools" : {
  "sSwfPath" : "plugins/datatables/extensions/TableTools/swf/copy_csv_xls_pdf.swf"
  }
             
  
               
  });
  $("div.tbl-tools-searchbox select").addClass(
  'tbl_length');
  }
</script>

</html>
