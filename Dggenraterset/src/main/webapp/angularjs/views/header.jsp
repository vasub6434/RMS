 <%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  <script src="js/jquery.cookie.js"></script> 
<sec:authorize access="hasRole('ROLE_USER')">

<header class="main-header">
    <nav class="navbar navbar-static-top">
      <div class="container">
        <div class="navbar-header">
          <a href="../../index2.html" class="navbar-brand"><b>Delta </b>PowerPlant</a>
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse">
            <i class="fa fa-bars"></i>
          </button>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse pull-left" id="navbar-collapse">
          <ul class="nav navbar-nav">
           <li><a href="#newdashboard"><i class="fa fa-tachometer"></i>&nbsp;&nbsp;<span>Dashboard</span></a></li>
      <li><a href="#digitalData"><i class="fa fa-industry"></i>&nbsp;&nbsp;<span>Digital Data</span></a></li>
         <li><a href="#EnegeryMeter"><i class="fa fa-industry"></i>&nbsp;&nbsp;<span>EnergyMeter Dashboard</span></a></li>
            <li><a href="<c:url value="/logout" />"><i class="fa fa-sign-out"></i> <span>Logout</span></a></li>
            
            <!-- <li class="active"><a href="#">Link <span class="sr-only">(current)</span></a></li>
            <li><a href="#">Link</a></li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
                <li><a href="#">Action</a></li>
                <li><a href="#">Another action</a></li>
                <li><a href="#">Something else here</a></li>
                <li class="divider"></li>
                <li><a href="#">Separated link</a></li>
                <li class="divider"></li>
                <li><a href="#">One more separated link</a></li>
              </ul>
            </li> -->
          </ul>
         <!--  <form class="navbar-form navbar-left" role="search">
            <div class="form-group">
              <input type="text" class="form-control" id="navbar-search-input" placeholder="Search">
            </div>
          </form> -->
        </div>
        
        <!-- /.navbar-custom-menu -->
      </div>
      <!-- /.container-fluid -->
    </nav>
  </header>
  	<!-- For login user -->
		<c:url value="/logout" var="logoutUrl" />
		<form action="${logoutUrl}" method="post" id="logoutForm">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		</form>
		<script>
			function formSubmit() {
				document.getElementById("logoutForm").submit();
			}
		</script>
		
  <!-- Left side column. contains the logo and sidebar -->
 <%--  <aside class="main-sidebar">
    <section class="sidebar">
      <!-- Sidebar user panel -->
      <div class="user-panel">
        <div class="pull-left image">
          <img src="images/logo.png" class="img-circle" alt="User Image">
        </div>
        <div class="pull-left info">
          <p>Delta PowerPlant</p>
        </div>
      </div>
      <ul class="sidebar-menu">
      <li><a href="#newdashboard"><i class="fa fa-camera-retro"></i><span>Dashboard</span></a></li>
      <li><a href="#digitalData"><i class="fa fa-camera-retro"></i><span>Digital Data</span></a></li>
            <li><a href="<c:url value="/logout" />"><i class="fa fa-sign-out"></i> <span>Logout</span></a></li>
      </ul>
    </section>
  </aside> --%>
  </sec:authorize>
  <sec:authorize access="hasRole('ROLE_ADMIN')">
  <header class="main-header">
    <nav class="navbar navbar-static-top">
      <div class="container">
        <div class="navbar-header">
          <a href="../../index2.html" class="navbar-brand"><b>Delta </b>PowerPlant</a>
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse">
            <i class="fa fa-bars"></i>
          </button>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse pull-left" id="navbar-collapse">
          <ul class="nav navbar-nav">
          <li><a href="#addimage"><i class="fa fa-picture-o"></i><span>Image Upload</span></a></li>
          
           <li class="dropdown">
              <a  class="dropdown-toggle" data-toggle="dropdown">Device <span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
              <li><a href="#componentpage"><i class="fa fa-map-o"></i><span>Component Details</span></a></li>
                      <li><a href="#deviceprofilelist"><i class="fa fa-sliders"></i><span>Device Profile</span></a></li>
            <li><a href="#deviceinfo"><i class="fa fa-info-circle"></i><span>Device Info</span></a></li>
              <li><a href="#analogdevicesetting"><i class="fa fa-cog"></i><span>Analog Device Setting</span></a></li>
              </ul>
            </li>
            
			<%-- <ul class="dropdown-menu" role="menu">
						
			
			           
			
			</ul> --%>
           <!--  <li><a href="#devicesetup"><i class="fa fa-camera-retro"></i><span>Image Builder</span></a></li> -->
            <li><a href="#parameterlist"><i class="fa fa-pencil-square-o"></i><span>Parameter Details</span></a></li>
    
            <li><a href="#dashboardlist"><i class="fa fa-ioxhost" aria-hidden="true"></i><span>Dash Board Details</span></a></li>
          <!--   <li><a href="#devicemaster"><i class="fa fa-ioxhost" aria-hidden="true"></i><span>Device Master</span></a></li> -->
          
             <li><a href="#digital"><i class="fa fa-camera-retro"></i><span>Digital Data</span></a></li>
              <li><a href="<c:url value="/logout" />"><i class="fa fa-sign-out"></i> <span>Logout</span></a></li>
          </ul>
         <!--  <form class="navbar-form navbar-left" role="search">
            <div class="form-group">
              <input type="text" class="form-control" id="navbar-search-input" placeholder="Search">
            </div>
          </form> -->
        </div>
        
        <!-- /.navbar-custom-menu -->
      </div>
      <!-- /.container-fluid -->
    </nav>
  </header>
  
  <%--  <header class="main-header">
    <!-- Logo -->
    <a href="loginlist.jsp" class="logo">
      <!-- mini logo for sidebar mini 50x50 pixels -->
      <span class="logo-mini"><b>D</b>G</span>
      <!-- logo for regular state and mobile devices -->
      <span class="logo-lg"><b>DG Generator</b></span>
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top" role="navigation">
      <!-- Sidebar toggle button-->
      <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
        <span class="sr-only">Toggle navigation</span>
      </a>
       <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
            <li class="dropdown user user-menu dropdown-toggle" style="display: block;">
            <c:if test="${pageContext.request.userPrincipal.name != null}">
				Welcome  ${pageContext.request.userPrincipal.name} 
			</c:if>
          </li>
          <!-- Control Sidebar Toggle Button -->
        </ul>
        </div>
    </nav>
  </header> --%>
  	<!-- For login user -->
		<c:url value="/logout" var="logoutUrl" />
		<form action="${logoutUrl}" method="post" id="logoutForm">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		</form>
		<script>
			function formSubmit() {
				document.getElementById("logoutForm").submit();
			}
		</script>
		
  <!-- Left side column. contains the logo and sidebar -->
  <%-- <aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <!-- Sidebar user panel -->
      <div class="user-panel">
        <div class="pull-left image">
          <img src="images/logo.png" class="img-circle" alt="User Image">
        </div>
        <div class="pull-left info">
          <p>DG GenraterSet APP</p>
        </div>
      </div>
      <ul class="sidebar-menu">
            <li><a href="#addimage"><i class="fa fa-picture-o"></i><span>Image Upload</span></a></li>
			<li><a href="#componentpage"><i class="fa fa-map-o"></i><span>Component Details</span></a></li>
           <!--  <li><a href="#devicesetup"><i class="fa fa-camera-retro"></i><span>Image Builder</span></a></li> -->
            <li><a href="#parameterlist"><i class="fa fa-pencil-square-o"></i><span>Parameter Details</span></a></li>
            <li><a href="#deviceprofilelist"><i class="fa fa-sliders"></i><span>Device Profile</span></a></li>
            <li><a href="#deviceinfo"><i class="fa fa-info-circle"></i><span>Device Info</span></a></li>
            <li><a href="#dashboardlist"><i class="fa fa-ioxhost" aria-hidden="true"></i><span>Dash Board Details</span></a></li>
          <!--   <li><a href="#devicemaster"><i class="fa fa-ioxhost" aria-hidden="true"></i><span>Device Master</span></a></li> -->
            <li><a href="#analogdevicesetting"><i class="fa fa-cog"></i><span>Analog Device Setting</span></a></li>
             <li><a href="#digital"><i class="fa fa-camera-retro"></i><span>Digital Data</span></a></li>
            <li><a href="<c:url value="/logout" />"><i class="fa fa-sign-out"></i> <span>Logout</span></a></li>
      </ul>
    </section>
    <!-- /.sidebar -->
  </aside> --%>
  	<!-- <script type="text/javascript">
		$.cookie("redirected",window.location.pathname);
		window.location ="login";
		</script> -->
  </sec:authorize> 
