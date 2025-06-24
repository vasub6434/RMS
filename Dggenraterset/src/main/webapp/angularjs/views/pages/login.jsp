<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="false"%>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>Log in</title>
  <style>
.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}
</style>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.5 -->
  <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="font-awesome-4.5.0/css/font-awesome.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/AdminLTE.min.css">
  <!-- iCheck -->
  <link rel="stylesheet" href="plugins/iCheck/square/blue.css">
  <!-- jQuery 2.1.4 -->
<script src="plugins/jQuery/jQuery-2.1.4.min.js"></script>
      <!-- Sweet Alert -->
    <link href="css/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body class="hold-transition login-page" onload='document.loginForm.username.focus();'>
<div class="login-box">
  <div class="login-logo">
   <b>DG Generator Application</b>	
  </div>
  <!-- /.login-logo -->
  <div class="login-box-body">	
  		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:if>
		
		
 <form id="form-horizontal" name='loginForm' class="form-horizontal" role="form" action="<c:url value='/login' />"   method="post">
      <div class="form-group">
           <input type="text" class="form-control" name="username" id="username" placeholder="Username" />
      </div>
      <div class="form-group">
       <input type="password" class="form-control" name="password" id="password" placeholder="Password" />
      </div>
          <input type="submit" class="btn btn-primary btn-block btn-flat" value="Sign In" />  
          <input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
</form>
  </div>
  <!-- /.login-box-body -->
</div>
<!-- /.login-box -->


<!-- Bootstrap 3.3.5 -->
<script src="bootstrap/js/bootstrap.min.js"></script>
<!-- Sweet alert -->
    <script src="js/sweetalert/sweetalert.min.js"></script>
<script src="js/jquery.validate.min.js"></script>
<script src="js/JSONAjax.js"></script>

</body>
</html>
