<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="../includes/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자전거 고장신고 현황</title>
	<!-- Bootstrap 4 -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">	
</head>

<body>
<div class="panel-body">
	<table id="breakdownList" width="100%" class="table table-sriped table-boardered table-hover text-center">
		<thead>
			<tr>
				<th>No.</th>
				<th style="display:none;">게시번호</th>
				<th>제목</th>
				<th>정류소 ID</th>
				<th>자전거 번호</th>
				<th>고중부품</th>
				<th>내용</th>
				<th>작성자</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="list" items="${breakdownReport}" varStatus="vs">
				<tr class="bikeParkInfo${vs.index}">
					<td class="index"><c:out value="${vs.count}"/></td>
					<td class="bnum" style="display:none;"><c:out value="${list.bnum}"/></td>
					<td class="btitle"><c:out value="${list.btitle}"/></td>
					<td class="stationid"><c:out value="${list.stationid}"/></td>
					<td class="bikenum"><c:out value="${list.bikenum}"/></td>
					<td class="brokenparts"><c:out value="${list.brokenparts}"/></td>
					<td class="content"><c:out value="${list.content}"/></td>
					<td class="writer"><c:out value="${list.writer}"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pull-right">
		<form id="searchPostForm" class="searchPostForm" action="/board/breakdownList" method="get">
			<input id="keyword" name="keyword" type="text" placeHolder="Search..." />
			<input id="btnSearch" class="btnSearch" type="button" value="검색어"/>
		</form>
		<button class="btnWrite">작성</button>
	</div>
	<div class="pull-left">
		<ul class="pagination">
			<c:if test="${pageMarker.prev}">
				<li class="page-item"><a class="page-link" href="${pageMarker.startPage-1}">«</a></li>
			</c:if>
			<c:forEach var="num" begin="${pageMarker.startPage}" end="${pageMarker.endPage}">
				<li class="page-item"><a class="page-link ${pageMarker.cri.pageNum == num ? "active":""}" href="${num}">${num}</a></li>
			</c:forEach>
			<c:if test="${pageMarker.next}">
				<li class="page-item"><a class="page-link" href="${pageMarker.endPage+1}">»</a></li>
			</c:if>
		</ul>
	</div>
	<!-- end pagenation -->
	<form id="actionForm" action="/board/breakdownList" method='get'>
		<input type='hidden' name='pageNum' value='${pageMarker.cri.pageNum}'>
		<input type='hidden' name='amount'  value='${pageMarker.cri.amount}'>
		<input type='hidden' name='keyword' value='${pageMarker.cri.keyword}'>
	</form>
</div>
</body>
<!-- JQuery -->
<script src="//code.jquery.com/jquery-3.3.1.min.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	var actionForm = $("#actionForm");
	
	$(".page-item a").on("click",function(e){
		e.preventDefault();
		console.log("click");
		actionForm.find('input[name=pageNum]').val($(this).attr("href"));
		actionForm.submit();
	});
	
	
	$("#breakdownList tr").click(function(){
		console.log(this);
		
		var bnum = this.children[1].textContent;
		/*
		$.ajax({
			url:'/board/getBreakdownReport',
			data : {bnum : bnum},
			type : 'GET',
			error: function(){
				alert("통신 실패");
			},
			success : function(result){
				console.log(result);
				//window.location.href = '/board/detailBreakdownReport';
				//location.replace('/board/detailBreakdownReport');
				location.href='/board/detailBreakdownReport';
			}
		});
		*/
		
		actionForm.append("<input type='hidden' name='bnum' value='"+bnum+"'>");
		actionForm.attr("action","/board/getBreakdownReport");
		actionForm.submit();
		
	});
	
	
	$(".btnSearch").on("click",function(e){
    	//if(!$(".searchPostForm").find("#keyword").val()){
    	if(!$("#keyword").val()){
    		alert("검색어를 입력해주세요.");
    		return false;
    	}
    	
    	e.preventDefault();
    	
    	
    	$.ajax({
			url : '/board/breakdownList',
			data : {keyword:$("#keyword").val()},					
			type : 'GET',
			error : function(){
				alert("통신 실패");
			},
			success : function(result){
				console.log("Success post");
				console.log(result);
			}
		}).done(function(body){
			// 새로운 정보로 새로고침.
			location.relaod();
		}); //$.ajax
    });

	$(".btnWrite").on("click",function(e){
		e.preventDefault();
		$.ajax({
			url:'/board/registerBreakdown',
			type:'GET',
			error : function(){
				alert("통신 실패");
			},
			success : function(result){
				window.location.href = '/board/registerBreakdown';
			}
		});
	});
});
</script>
</html>