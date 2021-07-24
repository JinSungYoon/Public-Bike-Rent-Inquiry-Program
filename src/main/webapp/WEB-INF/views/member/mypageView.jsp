<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@include file="../includes/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
	<!-- Bootstrap 4 -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
</head>
<body>
	 <div class="panel-body">
         <form id="postForm">
         	<div class="form-group">
                 <label>아이디(이메일)</label>
                 <sec:authorize access="isAuthenticated()">
                 	<sec:authentication property="principal.username" var="memberId"/>
                   	<input class="form-control" name="memberId" value="${memberId}" readonly>
                 </sec:authorize>
             </div>
             <div class="form-group">
                 <label>비밀번호</label>
                 <input type="password" class="form-control" name="memberPw">
             </div>
             <div class="form-group">
                 <label>이름</label>
                 <sec:authorize access="isAuthenticated()">
                 	<sec:authentication property="principal.membername" var="memberName"/>
                   	<input class="form-control" name="memberName" value="${memberName}" readonly>
                 </sec:authorize>
             </div>
             <sec:authorize access="isAuthenticated()">
                 <sec:authentication property="principal.memberphone" var="memberPhone"/>
                   	<input class="form-control" name="memberPhone" value="${memberPhone}">
             </sec:authorize>
             <div class="form-group">
                 <label>주소</label>
                 <input type="text" id="postCode" placeholder="우편번호">
 				 <input type="button" onclick="execDaumPostcode()" value="우편번호 찾기"><br>
				 <input type="text" id="roadAddress" placeholder="도로명주소" size="60" ><br>
				 <input type="hidden" id="jibunAddress" placeholder="지번주소"  size="60">
				 <span id="guide" style="color:#999;display:none"></span>
				 <input type="text" id="detailAddress" placeholder="상세주소"  size="60"><br>
				 <input type="text" id="extraAddress" placeholder="참고주소"  size="60"><br>
             </div>
             <div class="form-group">
                 <label>생년월일</label>
                 <sec:authorize access="isAuthenticated()">
                 <sec:authentication property="principal.memberbirth" var="memberBirth"/>
                   	<input class="form-control" name="memberBirth" value="${memberBirth}">
             	 </sec:authorize>
             </div>
             <div class="form-group">
                 <label>관리자코드</label>
                 <input class="form-control" name="memberRoleCode">
             </div>
         </form>
         <button type="update" class="btnSubmit">업데이트</button>
         <button type="delete" class="btnCancel">취소</button>
     </div>
     <!-- /.panel-body -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!-- 주소록 api 연동을 위한 -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script type="text/javascript">
function execDaumPostcode(){
	new daum.Postcode({
        oncomplete: function(data) {
        	// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            var extraRoadAddr = ''; // 참고 항목 변수

            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                extraRoadAddr += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if(data.buildingName !== '' && data.apartment === 'Y'){
               extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            if(extraRoadAddr !== ''){
                extraRoadAddr = ' (' + extraRoadAddr + ')';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            $('#postCode').val(data.zonecode);
            $("#roadAddress").val(data.roadAddress);
            $("#jibunAddress").val(data.autoJibunAddress);
            
            // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
            if(roadAddr !== ''){
            	$("#extraAddress").val(extraRoadAddr);
            } else {
            	$("#extraAddress").val('');
            }

            var guideTextBox = document.getElementById("guide");
            // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
            if(data.autoRoadAddress) {
                var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                guideTextBox.style.display = 'block';

            } else if(data.autoJibunAddress) {
                var expJibunAddr = data.autoJibunAddress;
                guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                guideTextBox.style.display = 'block';
            } else {
                guideTextBox.innerHTML = '';
                guideTextBox.style.display = 'none';
            }
        }
 	}).open();	
}
$(document).ready(function(){	
	
	$(".btnSubmit").on("click",function(){
		
		var memberId = $("input[name=memberId]").val();
		var memberPw = $("input[name=memberPw]").val();
		var memberName = $("input[name=memberName]").val();
		var memberGender = $("input[name=gender]:checked").val();
		var memberAddress = $("#roadAddress").val().trim()+" "+$("#detailAddress").val().trim()+" "+$("#extraAddress").val().trim();
		var memberPhone = $("input[name=memberPhone]").val();
		var memberBerth = $("input[name=memberBerth]").val();
		var memberRole = $("input[name=memberRoleCode]").val();
		
		var params = {memberId : memberId,memberPw : memberPw, memberName : memberName,memberGender : memberGender,memberAddress : memberAddress,memberPhone : memberPhone, memberBerth : memberBerth,memberRole : memberRole};
		
		$.ajax({
			url:'/member/updateUser',
			data : JSON.stringify(params),
			type : "POST",
			contentType : 'application/json',
			beforeSend : function(xhr)
            {   //데이터를 전송하기 전에 헤더에 csrf값을 설정한다
                xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
            },
			error : function(e){
				alert("Error : "+e);
			},
			success : function(result){
				alert(result);
			}
		});
	});
	
});
</script>
</body>
</html>