<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="description" content=""/>
<meta name="keywords" content=""/>
<link rel="shortcut icon" href="${rc.contextPath}/css/common/images/favicon.ico"/>
<title>WebHawks Attendance Management System</title>
<link href="${rc.contextPath}/css/common/bootstrap.min.css" rel="stylesheet">
<link href="${rc.contextPath}/css/common/font-awesome.min.css" rel="stylesheet">
<link href="${rc.contextPath}/css/common/sumoselect.css" rel="stylesheet" />
<link rel="stylesheet" href="${rc.contextPath}/css/common/login.css" type="text/css" />
<script src="${rc.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/ajaxHelper.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/login.js"></script>
</head>
<body>
<div class="row vertical-offset-100">
 	<div class="col-md-4 col-md-offset-4 col-xs-10 col-xs-offset-1">
    	<div class="panel panel-default">
        	<div class="panel-heading">                                
            	<div class="row-fluid user-row">
                	<center><img src="${rc.contextPath}/css/common/images/WebHawksITlogo.png" alt="WAMS" height="34" width="118"><h4>Attendance Management System</h4></center>
                </div>
            </div>
            <div class="panel-body">
                <form  accept-charset="UTF-8" class="form-signin">
                    <fieldset>            					
                        <div class="form-group">
                            <div class="input-group">
            				    <span class="input-group-addon user_id">User ID</span> 
                                <input class="form-control login_input" required placeholder="Username" name="username" id="username" type="text" autofocus="autofocus">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-addon user_id">Pasword</span>
                                <input class="form-control login_input" required placeholder="Password" name="password" id="password" type="password" onkeydown="if (event.keyCode == 13) document.getElementById('login').click()">
                            </div>
                        </div>
                        <span class="error" id="errorMsg"></span>
                        <br></br>
                        <input class="btn btn-lg btn-primary btn-block" type="button" id="login" value="Sign In" onclick="loginValided('${rc.contextPath}');"/>
                    </fieldset>
                </form>
            </div>
		</div>
 	</div>
</div>

    
   