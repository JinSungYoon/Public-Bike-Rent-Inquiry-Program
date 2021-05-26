<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/views/globalVariable.jsp"%>

<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>

	<!-- Bootstrap 4 -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
	

</head>
<body>
<h1>실시간 공공자전거 대여현황</h1>
<div id="map" style="width:100%;height:350px;"></div>
<div class="panel-body">
	<table id="bikeParkingTable" width="100%" class="table table-sriped table-boardered table-hover text-center">
		<thead>
			<tr>
				<th>No.</th>
				<th>거치대 개수</th>
				<th>대여소 이름</th>
				<th>주차대수</th>
				<th>거치율</th>
				<th>거치대 위도</th>
				<th>거치대 경도</th>
				<th>대여소 ID</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${result}" varStatus="vs">
				<tr class="bikeParkInfo${vs.index}">
					<td class="index"><c:out value="${vs.count}"/></td>
					<td class="rackTotCnt"><c:out value="${item.rackTotCnt}"/></td>
					<td class="stationName"><c:out value="${item.stationName}"/></td>
					<td class="parkingBikeTotCnt"><c:out value="${item.parkingBikeTotCnt}"/></td>
					<td class="shared"><c:out value="${item.shared}"/></td>
					<td class="stationLatitude"><c:out value="${item.stationLatitude}"/></td>
					<td class="stationLongitude"><c:out value="${item.stationLongitude}"/></td>
					<td class="stationId"><c:out value="${item.stationId}"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pull-right">
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
	<form id="actionForm" action="/board/publicBikeParking" method='get'>
		<input type='hidden' name='pageNum' value='${pageMarker.cri.pageNum}'>
		<input type='hidden' name='amount'  value='${pageMarker.cri.amount}'>
	</form>
</div>
<!-- JQuery -->
<script src="//code.jquery.com/jquery-3.3.1.min.js"></script>

<!-- Naver Maps API -->
<script type="text/javascript" src='https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=<%=naverMapApiKey%>'></script>

<script type="text/javascript">

var map = "";			// map 전역
var marker = "";		//marker 전역
var markers = [];		// marker 위치를 저장하기 위한 배열
var infoWindows = [];	// information을 저장하기 위한 배열

$(document).ready(function(){
    
    // 초기에 marker를 설정하는 함수
    function setMarker(lat,lng,stationName){
    	
    	var position = new naver.maps.LatLng(lat, lng);
    	
    	var mapOptions = {
        	    center: position,
        	    zoom: 14,
        	    mapTypeControl : true,
        	    zoomControl : true,
        };
    	
    	map = new naver.maps.Map('map',mapOptions);
    	    	
    	marker = new naver.maps.Marker({
    		position : position,
    		map : map,
    	});
    	
    	var infowindow = new naver.maps.InfoWindow({
        	content : stationName
        });
        
        infowindow.open(map,marker);
        
        markers.push(marker);
    	infoWindows.push(infowindow);
    	
    }
    
    // Marker를 추가하는 함수
    function addMarker(lat,lng,stationName){
    	var position = new naver.maps.LatLng(lat,lng);
    	    	
    	var markerOptions = {
    		position : position,
    		map : map,
    		title : stationName,
    		content : stationName,
    	};
    	
    	marker = new naver.maps.Marker(markerOptions);
    	
    	var infowindow = new naver.maps.InfoWindow({
        	content : stationName
        });
    	
    	markers.push(marker);
    	infoWindows.push(infowindow);
    	
    	marker.setMap(map);
    	
    }
    
	 // 해당 마커의 인덱스를 seq라는 클로저 변수로 저장하는 이벤트 핸들러를 반환합니다.
    function getClickHandler(seq) {
        return function(e) {
            var marker = markers[seq],
                infoWindow = infoWindows[seq];

            if (infoWindow.getMap()) {
                infoWindow.close();
            } else {
                infoWindow.open(map, marker);
            }
        }
    }
    
    for(var loop=0;loop<$("#bikeParkingTable").find('.stationLatitude').length;loop++){
    	if(loop==0){
    		setMarker($("#bikeParkingTable").find('.stationLatitude').eq(loop).text(),$("#bikeParkingTable").find('.stationLongitude').eq(loop).text(),$("#bikeParkingTable").find('.stationName').eq(loop).text());
    	}else{
    		addMarker($("#bikeParkingTable").find('.stationLatitude').eq(loop).text(),$("#bikeParkingTable").find('.stationLongitude').eq(loop).text(),$("#bikeParkingTable").find('.stationName').eq(loop).text());
    		naver.maps.Event.addListener(markers[loop], 'click', getClickHandler(loop));
    	}
    }
    
    // 공공자전거 목록 table 클릭 이벤트
	$("#bikeParkingTable tr").click(function(){
		
		var tdArr = new Array();
		
		var tr = $(this);
		var td = tr.children();
		
		// tr.text() 클릭된 Row 즉 tr에 모든 값을 가져온다.
		// console.log("클릭한 Row의 모든 데이터 : "+tr.text());
		
		// 반복문을 이용해서 배열에 값을 담아 사용할 수 도 있다.
		td.each(function(i){
			tdArr.push(td.eq(i).text());
		});
		
		// td의 모든 값들을 변수로 저장.
		var index = td.eq(0).text();
		var rackTotCnt = td.eq(1).text();
		var stationName = td.eq(2).text();
		var parkingBikeTotCnt = td.eq(3).text();
		var shared = td.eq(4).text();
		var stationLatitude = td.eq(5).text();
		var stationLongitude = td.eq(6).text();
		var stationId = td.eq(7).text();
        
		// 네이버 지도에 위경도에 맞는 지도 display
        var mapOptions = {
        	    center: new naver.maps.LatLng(stationLatitude, stationLongitude),
        	    zoom: 14,
        	    mapTypeControl : true,
        	    zoomControl : true,
        	};
        
        map = new naver.maps.Map('map', mapOptions);
        
        // Naver 지도위에 marker로 위치표시
        var marker = new naver.maps.Marker({
        	position : new naver.maps.LatLng(stationLatitude,stationLongitude),
        	map : map,
        	title : stationName,
        });
        
        var infowindow = new naver.maps.InfoWindow({
        	content : stationName
        });
        
        infowindow.open(map,marker);
        
	});
    
    var actionForm = $("#actionForm");
    
    $(".page-item a").on("click",function(e){
    	e.preventDefault();
    	console.log("click");
    	actionForm.find('input[name=pageNum]').val($(this).attr("href"));
    	actionForm.submit();
    });
    
});
	
</script>
</body>
</html>