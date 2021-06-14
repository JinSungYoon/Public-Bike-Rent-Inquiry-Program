<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="../includes/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자전거 고장신고 접수</title>
	<!-- Bootstrap 4 -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">	
</head>
<body>
	 <!-- div class="row">
         <div class="col-lg-12">
             <h1 class="page-header">고장현황 등록</h1>
         </div>
         <!-- /.col-lg-12 -->
     </div -->
     <!-- /.row -->
     <div class="row">
         <div class="col-lg-12">
             <div class="panel panel-default">
                 <div class="panel-heading">
                     	고장신고
                     	<button type="reset" class="btnReset">초기화</button>
                     	<button type="list" class="btnList">목록</button>
                 </div>
                 <!-- /.panel-heading -->
                 <div class="panel-body">
                     <form action="/board/registerBreakdown" method="post">
                     	<div class="form-group">
                             <label>제목</label>
                             <input class="form-control" name="btitle">
                         </div>
                         <div class="form-group">
                             <label>자전거 정류장</label>
                             <input class="form-control" name="stationid">
                         </div>
                         <div class="form-group">
                             <label>자전거 번호</label>
                             <input class="form-control" name="bikenum">
                         </div>
                         <div class="form-group">
                             <label>고장부품</label>
                             <input class="form-control" name="brokenparts">
                         </div>
                         <div class="form-group">
                             <label>작성자</label>
                             <input class="form-control" name="writer">
                         </div>
                         <div class="form-group">
                             <label>내용</label>
                             <textarea class="form-control" rows="5" cols="50" name="content"></textarea>
                         </div>
                     	<button type="register" class="btnSubmit">생성</button>
                     	<button type="delete" class="btnCancel">취소</button>
                     </form>
                 </div>
                 <!-- /.panel-body -->
             </div>
             <!-- /.panel -->
         </div>
         <!-- /.col-lg-12 -->
     </div>
     <!--  /.row -->
<!-- JQuery -->
<script src="//code.jquery.com/jquery-3.3.1.min.js"></script>

<script type="text/javascript">
$(document).ready(function(){	
	
	// 리스트로 돌아가기
	$(".btnList").on("click",function(e){
		e.preventDefault();
		
		$.ajax({
			url:'/board/breakdownList',
			type:'GET',
			error : function(){
				alert("통신 실패");
			},
			success : function(result){
				window.location.href = '/board/breakdownList';
			}
		});
		
	});
	
	$(".btnReset").on("click",function(){
		
		if(confirm("초기화 하시겠습니까?")){
			$("input[name=btitle]").val("")
			$("input[name=stationid]").val("")
			$("input[name=bikenum]").val("")
			$("input[name=brokenparts]").val("")
			$("input[name=writer]").val("")
			$("textarea[name=content]").val("")	
		}
		
	});
});
</script>

</body>
</html>