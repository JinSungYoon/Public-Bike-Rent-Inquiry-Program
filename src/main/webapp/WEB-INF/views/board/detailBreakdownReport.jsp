<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="../includes/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자전거 고장신고 상세</title>
	<!-- Bootstrap 4 -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">	
</head>
<body>
	<div class="row">
         <div class="col-lg-12">
             <div class="panel panel-default">
                 <div class="panel-heading">
                     	고장신고 상세내용
                     	<button type="list" class="btnList">목록</button>
                 </div>
                 <!-- /.panel-heading -->
                 <div class="panel-body">
                   	   <div class="form-group" style="display:none">
                           <label>게시글번호</label>
                           <span class="btitle"></span>
                           <input class="form-control" name="bnum" value='<c:out value="${post.bnum}"/>' readonly="readonly">
                       </div>
                   	   <div class="form-group">
                           <label>제목</label>
                           <span class="btitle"></span>
                           <input class="form-control" name="btitle" value='<c:out value="${post.btitle}"/>' readonly="readonly">
                       </div>
                       <div class="form-group">
                           <label>자전거 정류장</label>
                           <input class="form-control" name="stationid" value='<c:out value="${post.stationid}"/>' readonly="readonly">
                       </div>
                       <div class="form-group">
                           <label>자전거 번호</label>
                           <input class="form-control" name="bikenum" value='<c:out value="${post.bikenum}"/>' readonly="readonly">
                       </div>
                       <div class="form-group">
                           <label>고장부품</label>
                           <input class="form-control" name="brokenparts" value='<c:out value="${post.brokenparts}"/>' readonly="readonly">
                       </div>
                       <div class="form-group">
                           <label>작성자</label>
                           <input class="form-control" name="writer" value='<c:out value="${post.writer}"/>' readonly="readonly">
                       </div>
                       <div class="form-group">
                           <label>내용</label>
                           <textarea class="form-control" rows="5" cols="50" name="content" readonly="readonly"><c:out value="${post.content}"/></textarea>
                       </div>
                   	<button type="modify" class="btnSubmit">수정</button>
                   	<button type="delete" class="btnDelete">삭제</button>
                 </div>
                 <!-- /.panel-body -->
             </div>
             <!-- /.panel -->
         </div>
         <!-- /.col-lg-12 -->
     </div>
     <!--  /.row -->
</body>
<script type="text/javascript">
$(document).ready(function(){
	$(".btnList").on("click",function(e){
		e.preventDefault();
		$.ajax({
			url:'/board/breakdownList',
			type : 'GET',
			error : function(){
				
			},
			success : function(){
				window.location.href = '/board/breakdownList';
			}
		});
	});
	
	$(".btnDelete").on("click",function(e){
		e.preventDefault();
		$.ajax({
			url:'/board/deleteBreakdownReport',
			data : {bnum:$("input[name=bnum]").val()},
			type:'POST',
			error : function(e){
				alert("통신 실패 : "+e);
			},
			success : function(result){
				console.log("통신 성공");
			}
			
		});
	});
});
</script>
</html>