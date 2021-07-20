<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@include file="../includes/header.jsp" %>
<% 
	String url = request.getServerName().toString();
	request.setAttribute("url", url);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>

<!--Bootsrap 4 CDN-->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    
    <!--Fontawesome CDN-->
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">

</head>
<body>
	<div class="container">
		<div class="d-flex justify-content-center h-100">
			<div class="card">
				<div class="card-header">
					<h3>로그인</h3>
					<div class="d-flex justify-content-end social_icon">
						<span><i class="fab fa-facebook-square"></i></span>
						<span><i class="fab fa-google-plus-square"></i></span>
						<span><i class="fab fa-twitter-square"></i></span>
					</div>
				</div>
				<div class="card-body">
					<form id="loginForm" method="post" action="/login">
						<div class="input-group form-group">
							<div class="input-group-prepend">
								<span class="input-group-text"><i class="fas fa-user"></i></span>
							</div>
							<input type="text" name="memberId" class="memberId" placeholder="로그인  ID">
							
						</div>
						<div class="input-group form-group">
							<div class="input-group-prepend">
								<span class="input-group-text"><i class="fas fa-key"></i></span>
							</div>
							<input type="password" name="memberPw" class="memberPw" placeholder="비밀번호">
						</div>
						<div class="row align-items-center remember">
							<input type="checkbox" name="remember-me">Remember Me
						</div>
						<div class="form-group">
							<input type="submit" value="Login" class="btnLogin">
						</div>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						<span><c:out value="${requestScope.loginFailMsg}"/></span>
					</form>
					
				</div>
				<div class="card-footer">
					<div class="d-flex justify-content-center links">
						Don't have an account?<a href="/member/registerUser">Sign Up</a>
					</div>
					<div class="d-flex justify-content-center">
						<a href="#">Forgot your password?</a>
					</div>
				</div>
			</div>
		</div>
	</div>	
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	/*
	$(".btnLogin").on("click",function(){
		var memberId = $(".memberId").val();
		var memberPw = $(".memberPw").val();
		var params = {memberId : memberId,memberPw : memberPw};
		
		$.ajax({
			url : '/login',
			data : JSON.stringify(params),
			type : 'POST',
			contentType : 'application/json',
		 	beforeSend : function(xhr)
            {   //데이터를 전송하기 전에 헤더에 csrf값을 설정한다
                xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
            },
			error : function(e){
				alert("Error : ");
				alert(e);
			},
			success : function(result){
				alert(result);
			}
		});
		
	})
	*/
});
	
</script>
</html>