<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ include file="../includes/header.jsp" %>
<%@ include file="/WEB-INF/views/globalVariable.jsp"%>
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>서울시 따릉이 자전거</title>
	<!-- Bootstrap 4 -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
	<!--  date timepicker를 사용하기 위한 css -->
	<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/3.0.0/css/bootstrap-datetimepicker.min.css" rel="stylesheet" />
	<!-- css -->
	<link rel="stylesheet" href="/resources/css/detailBreakdownReport.css" type="text/css">
</head>
<body>
<h1>실시간 공공자전거 대여현황</h1>
<div id="map" style="width:100%;height:350px;">
</div>
<div class ="search" style="">
	<input id="address" type="text" placeHolder="검색할 주소"/>
	<input id="submit" type="button" value="주소검색"/>
</div>

<div class="searchPlaceDiv">
	<form id="searchPlaceForm" class="searchPlaceForm" action="/board/searchPlace" method="post">
		<input id="placeName" name="placeName" type="text" placeHolder="검색할 지역"/>
		<input id="dataSubmit" type="button" value="지역명검색"/>
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>
</div>
<div class="panel-body">
	<sec:authorize access="isAuthenticated()">
	<sec:authentication property="principal.username" var="memberId"/>
		<span id="memberId" hidden="true">${memberId}</span>
		<span><button id="btnAdd">추가</button><button id="btndelete">삭제</button><button id="btnSave">저장</button></span>
		<table id="favoritesTable" width="100%" class="table table-sriped table-boardered table-hover text-center">
			<thead>
				<tr>
					<th></th>
					<th>No.</th>
					<th>대여소 ID</th>
					<th>대여소 이름</th>
					<th>알림 자전거 대수</th>
					<th>알림 범위</th>
					<th>알림 시간1</th>
					<th>알림 시간2</th>
					<th>알림 시간3</th>
					<th>알림유효 시간</th>
					<th>활성화 여부</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${favorites}" varStatus="vs">
					<tr class="bikeParkInfo${vs.index}">
						<td class="delYn"><input type="checkbox"/></td>
						<td class="index"><c:out value="${vs.count}"/></td>
						<td class="stationId"><input id="stationId" class="stationId" value = "<c:out value="${item.stationId}"/>" readonly></input></td>
						<td class="stationName">
							<input type="text" id="stationInput" list="stationComboList" value="<c:out value="${item.stationName}"/>"></input>
							<datalist id="stationComboList" class="stationComboList">
							</datalist>
						</td>
						<td class="noticeBikeNum"><input type="text" id="noticeBikeNum" value = "<c:out value="${item.noticeBikeNum}"/>"></input></td>
						<td class="noticeScope"><input type="text" id="noticeScope" value = "<c:out value="${item.noticeScope}"/>"></input></td>
						<td class="noticeTime1"><input type="time" id="noticeTime1" value = "<c:out value="${item.noticeTime1}"/>"></input></td>
						<td class="noticeTime2"><input type="time" id="noticeTime2" value = "<c:out value="${item.noticeTime2}"/>"></input></td>
						<td class="noticeTime3"><input type="time" id="noticeTime3" value= "<c:out value="${item.noticeTime2}"/>"></input></td>
						<td class="effectiveDate"><input type="datetime-local" id="effectiveDate" value="<c:out value="${item.effectiveDate}"/>"></input></td>
						<td class="activeYn">
							<input type="text" id="activeYn" list="activeCondition" value="<c:out value="${item.activeYn}"/>"></input>
							<datalist id="activeCondition">
								<option value="Y">Y</option>
								<option value="N">N</option>
							</datalist>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
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
		
	</sec:authorize>
	<sec:authorize access="isAnonymous()">
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
					<th>상세정보</th>
				</tr>
			</thead>
			<tbody id="stationList">
				<c:forEach var="item" items="${stationList}" varStatus="vs">
					<tr class="bikeParkInfo${vs.index}">
						<td class="index"><c:out value="${vs.count}"/></td>
						<td class="rackTotCnt"><c:out value="${item.rackTotCnt}"/></td>
						<td class="stationName"><c:out value="${item.stationName}"/></td>
						<td class="parkingBikeTotCnt"><c:out value="${item.parkingBikeTotCnt}"/></td>
						<td class="shared"><c:out value="${item.shared}"/></td>
						<td class="stationLatitude"><c:out value="${item.stationLatitude}"/></td>
						<td class="stationLongitude"><c:out value="${item.stationLongitude}"/></td>
						<td class="stationId"><c:out value="${item.stationId}"/></td>
						<td class="arrow"><button class="accordion">▼</button></td>
					</tr>
					<tr class="fold" style="display:none;">
						<td colspan="9">
							<c:set var="index" value="${vs.index}"/>
							<!-- c:if test="${empty breakdownReport[index].getStationid()}" -->
							<div class="fold-content">
								<h3>Hello world</h3>
								<span>${breakdownCount.get(index).get("STATIONID")}</span>
								<span>${breakdownCount.get(index).get("CNT")}</span>
							</div>
							<!-- /c:if -->
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</sec:authorize>
	<!-- end pagenation -->
	<form id="actionForm" action="/board/publicBikeParking" method='get'>
		<input type='hidden' name='pageNum' value='${pageMarker.cri.pageNum}'>
		<input type='hidden' name='amount'  value='${pageMarker.cri.amount}'>
	</form>
</div>
<!-- JQuery -->
<script src="//code.jquery.com/jquery-3.3.1.min.js"></script>

<!-- Naver Maps API -->
<script type="text/javascript" src='https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=<%=naverMapApiKey%>&submodules=geocoder'></script>

<script type="text/javascript">

var map = "";			// map 전역
var marker = "";		//marker 전역 
var markers = [];		// marker 위치를 저장하기 위한 배열
var infoWindows = [];	// information을 저장하기 위한 배열
var parkingList;
var latitude = "";
var longitude = "";
var stationList = [];
var displayNum = 10;

window.onload = function(){	//html 로딩 이후에 시작
	navigator.geolocation.getCurrentPosition(function(pos) {
		latitude = pos.coords.latitude;
		longitude = pos.coords.longitude;
	});
};

$(document).ready(function(){	// 브라우저 트리를 생성한 직후 생성
    
	// 화면 Load 이후에 공공 자전거 데이터 조회
	$.ajax({
		url : '/board/publicBikeParkingList',
		dataType : "json",
		type : 'POST',
		error : function(){
			alert("통신 실패");
		},
		success : function(result){
			parkingList = result;
			setupMarker(parkingList,latitude,longitude);
			setupStationList();
			<sec:authorize access="isAuthenticated()">
			// 대여소 이름 Combobox 추가
			var stationComboList = $(".stationComboList");
			
			var comboOption = '';
			for(var index in parkingList){
				comboOption += '<option value="'+parkingList[index].stationName+'" />'; // Storing options in variable
			}
			
			for(var idx = 0;idx<stationComboList.length;idx++){
				stationComboList[idx].innerHTML = comboOption;
			}
			</sec:authorize>
			
		}
		,beforeSend:function(xhr){
			xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
			LoadingWithMask();
		},
		complete:function(){
			closeLoadingWithMask();
		}
	});
	
	function setupStationList(){
		// 미 로그인시 화면에 보여지는 좌표 정보 Table에 Display
		var stationList = $("#stationList");
		
		// Table내용 모두 지우기
		stationList.empty();
		
		for(var index = 0;index<displayNum; index++){
			var tableRow = '';
			var comboOption = '';
			tableRow += '<tr>';
			tableRow += '<td class="index">';
			tableRow += index+1;
			tableRow += '</td>';
			tableRow += '<td class="rackTotCnt">';
			tableRow += parkingList[index].rackTotCnt;
			tableRow += '</td>';
			tableRow += '<td class="stationName">';
			tableRow += parkingList[index].stationName;
			tableRow += '</td>';
			tableRow += '<td class="parkingBikeTotCnt">';
			tableRow += parkingList[index].parkingBikeTotCnt;
			tableRow += '</td>';
			tableRow += '<td class="shared">';
			tableRow += parkingList[index].shared;
			tableRow += '</td>';
			tableRow += '<td class="stationLatitude">';
			tableRow += parkingList[index].stationLatitude;
			tableRow += '</td>';
			tableRow += '<td class="stationLongitude">';
			tableRow += parkingList[index].stationLongitude;
			tableRow += '</td>';
			tableRow += '<td class="stationId">';
			tableRow += parkingList[index].stationId;
			tableRow += '</td>';
			tableRow += '<td class="arrow">';
			tableRow += '▼';
			tableRow += '</td>';
			tableRow += '</tr>';
			stationList.append(tableRow);
		}
	}
	
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
        marker.setMap(map);
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

    	marker.setMap(map);
    	
    	infoWindows.push(infowindow);
    	
    }
    
	 // 해당 마커의 인덱스를 seq라는 클로저 변수로 저장하는 이벤트 핸들러를 반환합니다.
    function getClickHandler(seq) {
        return function(e) {
            var marker = markers[seq],
                infoWindow = infoWindows[seq];
            <sec:authorize access="isAuthenticated()">
            if(confirm(parkingList[seq].stationName+"를 즐겨찾기에 추가하시겠습니까?")){
            	var memberId = document.getElementById("memberId").innerText;
            	var stationId   = parkingList[seq].stationId;
            	var stationName = parkingList[seq].stationName;
            	var params = {memberId:memberId,stationId:stationId,stationName:stationName};
            	$.ajax({
            		url:'/member/registerFavorites',
            		data:JSON.stringify(params),
            		contentType : 'application/json',
            		type:'POST',
            		beforeSend : function(xhr)
                    {   //데이터를 전송하기 전에 헤더에 csrf값을 설정한다
                        xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
                    },
            		error:function(error){
            			console.log(error);
            		},
            		success:function(result){
            			console.log(result);
            			self.location = "/board/publicBikeParking";
            		}
            	})
            	console.log(parkingList[seq]);
            }
            </sec:authorize>
            <sec:authorize access="isAnonymous()">
            if(confirm("로그인을 하면 즐겨찾기 등록이 가능합니다 로그인 하시겠습니까?")){
            	$.ajax({
            		url:'/member/loginView',
            		type:'GET',
            		error:function(result){
            			
            		},
            		success:function(result){
            			window.location.href = '/member/loginView';
            		},
            	})// $.ajax
            }
            </sec:authorize>
            if (infoWindow.getMap()) {
                infoWindow.close();
            } else {
                infoWindow.open(map, marker);
            }
        }
    }
    
	function setupMarker(data,lat,lng){
		// 현재 위치 기준으로 정렬
		sortJSON(data,"asc",lat,lng);
		
		markers.length = 0;
		marker = {};
		
		// 조회된 10개의 자전거 대여소 위치를 지도에 표시하는 부분	
		for(var loop=0;loop<displayNum;loop++){
			if(loop==0){
	    		setMarker(data[loop].stationLatitude,data[loop].stationLongitude,data[loop].stationName);
	    	}else{
	    		addMarker(data[loop].stationLatitude,data[loop].stationLongitude,data[loop].stationName);
	    		naver.maps.Event.addListener(markers[loop], 'click', getClickHandler(loop));
	    	}
	    }
	}
	
	var sortJSON = function(data,type,lat,lng) {
	  if (type == undefined) {
	    type = "asc";
	  }
	  return data.sort(function(a, b) {
	    
		var x = getDistanceFromLatLonInKm(lat,lng,a.stationLatitude,a.stationLongitude);
		var y = getDistanceFromLatLonInKm(lat,lng,b.stationLatitude,b.stationLongitude);
		  
	    //var x = Math.sqrt(Math.pow(parseFloat(lng)-parseFloat(a.stationLongitude),2)+Math.pow(parseFloat(lat)-parseFloat(a.stationLatitude),2));
		//var y = Math.sqrt(Math.pow(parseFloat(lng)-parseFloat(b.stationLongitude),2)+Math.pow(parseFloat(lat)-parseFloat(b.stationLatitude),2))
	    
	    if (type == "desc") {
	      return x > y ? -1 : x < y ? 1 : 0;
	    } else if (type == "asc") {
	      return x < y ? -1 : x > y ? 1 : 0;
	    }
	  });
	};
	
	// 위/경도간 거리 구하는 함수
	function getDistanceFromLatLonInKm(lat1,lng1,lat2,lng2) { 
		function deg2rad(deg) { return deg * (Math.PI/180) } 
		var R = 6371; // Radius of the earth in km 
		var dLat = deg2rad(lat2-lat1); // deg2rad below 
		var dLon = deg2rad(lng2-lng1); 
		var a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2); 
		var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		var d = R * c; // Distance in km 
		return d; 
	}

	
	
    map = new naver.maps.Map("map", {
        center: new naver.maps.LatLng(latitude, longitude),
        zoom: 15,
        mapTypeControl: true
    });

    infoWindow = new naver.maps.InfoWindow({
        anchorSkew: true
    });
	
    map.setCursor('pointer');

    // 좌표로 주소 검색 API
    function searchCoordinateToAddress(latlng) {

        infoWindow.close();
		// 좌표 -> 주소 검색 API
        naver.maps.Service.reverseGeocode({
            coords: latlng,
            orders: [
                naver.maps.Service.OrderType.ADDR,
                naver.maps.Service.OrderType.ROAD_ADDR
            ].join(',')
        }, function(status, response) {
            if (status === naver.maps.Service.Status.ERROR) {
                return alert('Something Wrong!');
            }

            var items = response.v2.results,
                address = '',
                htmlAddresses = [];

            for (var i=0, ii=items.length, item, addrType; i<ii; i++) {
                item = items[i];
                address = makeAddress(item) || '';
                addrType = item.name === 'roadaddr' ? '[도로명 주소]' : '[지번 주소]';

                htmlAddresses.push((i+1) +'. '+ addrType +' '+ address);
            }

            if(latlng.locationNm==null){
	            infoWindow.setContent([
	                '<div style="padding:10px;min-width:200px;line-height:150%;">',
						'<h4 style="margin-top:5px;">검색 좌표</h4><br />',	
	                htmlAddresses.join('<br />'),
	                '</div>'
	            ].join('\n'));
        	}else{
        		infoWindow.setContent([
                    '<div style="padding:10px;min-width:200px;line-height:150%;">',
                    '<h4 style="margin-top:5px;">'+ latlng.locationNm +'</h4><br />',		
                    htmlAddresses.join('<br />'),
                    '</div>'
                ].join('\n'));
        	}
            infoWindow.open(map, latlng);
        });
    }

    // 주소로 위치 찾기 API
    function searchAddressToCoordinate(address) {
        naver.maps.Service.geocode({
            query: address
        }, function(status, response) {
            if (status === naver.maps.Service.Status.ERROR) {
                return alert('Something Wrong!');
            }

            if (response.v2.meta.totalCount === 0) {
                return alert('totalCount' + response.v2.meta.totalCount);
            }

            var htmlAddresses = [],
                item = response.v2.addresses[0],
                point = new naver.maps.Point(item.x, item.y);

            if (item.roadAddress) {
                htmlAddresses.push('[도로명 주소] ' + item.roadAddress);
            }

            if (item.jibunAddress) {
                htmlAddresses.push('[지번 주소] ' + item.jibunAddress);
            }

            if (item.englishAddress) {
                htmlAddresses.push('[영문명 주소] ' + item.englishAddress);
            }

            infoWindow.setContent([
                '<div style="padding:10px;min-width:200px;line-height:150%;">',
                '<h4 style="margin-top:5px;">검색 주소 : '+ address +'</h4><br />',
                htmlAddresses.join('<br />'),
                '</div>'
            ].join('\n'));
			
         	// 위도 변경
            latitude  = parseFloat(item.y);
            // 경도 변경
            longitude = parseFloat(item.x);
            
            map.setCenter(point);
            //infoWindow.open(map, point);
            
        });
    }
    
 	// 지역명로 위치 찾기 API
    function searchPlacenameToCoordinate(placeInfo) {
        naver.maps.Service.geocode({
            query: placeInfo.address
        }, function(status, response) {
            if (status === naver.maps.Service.Status.ERROR) {
                return alert('Something Wrong!');
            }

            if (response.v2.meta.totalCount === 0) {
                return alert('totalCount' + response.v2.meta.totalCount);
            }

            var htmlAddresses = [],
                item = response.v2.addresses[0],
                point = new naver.maps.Point(item.x, item.y);

            if (item.roadAddress) {
                htmlAddresses.push('[도로명 주소] ' + item.roadAddress);
            }

            if (item.jibunAddress) {
                htmlAddresses.push('[지번 주소] ' + item.jibunAddress);
            }

            if (item.englishAddress) {
                htmlAddresses.push('[영문명 주소] ' + item.englishAddress);
            }

            infoWindow.setContent([
                '<div style="padding:10px;min-width:200px;line-height:150%;">',
                '<h4 style="margin-top:5px;">검색지역 : '+ placeInfo.title +'</h4><br />',
                htmlAddresses.join('<br />'),
                '</div>'
            ].join('\n'));
			
            // 위도 변경
            latitude  = parseFloat(item.y);
            // 경도 변경
            longitude = parseFloat(item.x);
    
            map.setCenter(point);
            //infoWindow.open(map, point);
            
        });
    }
	
    // 주소로 검색하는 함수 CLick 이벤트
    var searchPlaceFormObj = $("#searchPlaceForm");
    
    $("#dataSubmit").on("click",function(e){
    	if(!searchPlaceFormObj.find("#placeName").val()){
    		alert("검색할 지명을 입력해주세요.");
    		return false;
    	}
    	
    	e.preventDefault();
    	
    	//searchPlaceFormObj.submit();
    	
    	$.ajax({
			url : '/board/searchPlace',
			data : {placeName:searchPlaceFormObj.find("#placeName").val()},		
			dataType : "json",
			type : 'POST',
			beforeSend : function(xhr)
            {   //데이터를 전송하기 전에 헤더에 csrf값을 설정한다
                xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
            },
			error : function(){
				alert("통신 실패");
			},
			success : function(result){
				console.log("Success search place");
				searchPlacenameToCoordinate(result[0]);
				setupMarker(parkingList,latitude,longitude);
				setupStationList();
			}
		}); //$.ajax
    	
    });
    
    // 지역 이름으로 검색하는 함수 Enter 이벤트
    $("#placeName").keydown(function(e){
    	// EnterKey를 눌렀을때만
    	if(e.keyCode==13){
	    	if(!searchPlaceFormObj.find("#placeName").val()){
	    		alert("검색할 지명을 입력해주세요.");
	    		return false;
	    	}
	    	
	    	e.preventDefault();
	    	
	    	$.ajax({
				url : '/board/searchPlace',
				data : {placeName:searchPlaceFormObj.find("#placeName").val()},		
				dataType : "json",
				type : 'POST',
				beforeSend : function(xhr)
	            {   //데이터를 전송하기 전에 헤더에 csrf값을 설정한다
	                xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
	            },
				error : function(){
					alert("통신 실패");
				},
				success : function(result){
					console.log("Success search place");
					searchPlacenameToCoordinate(result[0]);
					setupMarker(parkingList,latitude,longitude);
					setupStationList();
				}
			}); //$.ajax
    	}
    });
    	
        
    // 초기에 위치 셋팅하는 함수
    function initGeocoder() {
        if (!map.isStyleMapReady) {
            return;
        }

        map.addListener('click', function(e) {
            searchCoordinateToAddress(e.coord);
        });

        $('#address').on('keydown', function(e) {
            var keyCode = e.which;
            if (keyCode === 13) { // Enter Key
                searchAddressToCoordinate($('#address').val());
            }
        });

        $('#submit').on('click', function(e) {
            e.preventDefault();
            searchAddressToCoordinate($('#address').val());
        });
		
        if($(".placeAddress").text()!=""){
        	searchAddressToCoordinate($(".placeAddress").text());
        }else{
        	searchAddressToCoordinate('월드컵북로 396');	
        }
        
    }
	
    // 주소정보 셋팅하는 함수
    function makeAddress(item) {
        if (!item) {
            return;
        }

        var name = item.name,
            region = item.region,
            land = item.land,
            isRoadAddress = name === 'roadaddr';

        var sido = '', sigugun = '', dongmyun = '', ri = '', rest = '';

        if (hasArea(region.area1)) {
            sido = region.area1.name;
        }

        if (hasArea(region.area2)) {
            sigugun = region.area2.name;
        }

        if (hasArea(region.area3)) {
            dongmyun = region.area3.name;
        }

        if (hasArea(region.area4)) {
            ri = region.area4.name;
        }

        if (land) {
            if (hasData(land.number1)) {
                if (hasData(land.type) && land.type === '2') {
                    rest += '산';
                }

                rest += land.number1;

                if (hasData(land.number2)) {
                    rest += ('-' + land.number2);
                }
            }

            if (isRoadAddress === true) {
                if (checkLastString(dongmyun, '면')) {
                    ri = land.name;
                } else {
                    dongmyun = land.name;
                    ri = '';
                }

                if (hasAddition(land.addition0)) {
                    rest += ' ' + land.addition0.value;
                }
            }
        }

        return [sido, sigugun, dongmyun, ri, rest].join(' ');
    }

    function hasArea(area) {
        return !!(area && area.name && area.name !== '');
    }

    function hasData(data) {
        return !!(data && data !== '');
    }

    function checkLastString (word, lastString) {
        return new RegExp(lastString + '$').test(word);
    }

    function hasAddition (addition) {
        return !!(addition && addition.value);
    }
    
    // 초기 지도 위치 셋팅
    naver.maps.onJSContentLoaded = initGeocoder;
    naver.maps.Event.once(map, 'init_stylemap', initGeocoder);
    
    // 공공자전거 목록 table 클릭 이벤트
	$("#bikeParkingTable tr").click(function(){
		
		var tdArr = new Array();
		
		var tr = $(this);
		var td = tr.children();
		
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
        
		var latlng = {y:stationLatitude,x:stationLongitude,_lnt:stationLatitude,_lng:stationLongitude,locationNm:stationName}
		
		searchCoordinateToAddress(latlng);
		
	});
    
    var actionForm = $("#actionForm");
    
    $(".page-item a").on("click",function(e){
    	e.preventDefault();
    	console.log("click");
    	actionForm.find('input[name=pageNum]').val($(this).attr("href"));
    	actionForm.submit();
    });
    
    function LoadingWithMask() {
        //화면의 높이와 너비를 구합니다.
        var maskHeight = $(document).height();
        var maskWidth  = window.document.body.clientWidth;
         
        //화면에 출력할 마스크를 설정해줍니다.
        var mask       ="<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
        var loadingImg ='';
        
        loadingImg +="<div id='loadingImg'>";
        loadingImg +=" <img src='/resources/img/loading.gif' style='position: relative; display: block; margin: 0px auto; width:150px; height:150px;'/>";
        loadingImg +="</div>";        
     
        //화면에 레이어 추가
        $('body').append(mask)
        	
     
        $("#mask").append(loadingImg)
        
        //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채웁니다.
        $('#mask').css({
                'width' : maskWidth,
                'height': maskHeight,
                'opacity' :'0.3'
        });
      	
        $('#loadingImg').css({
            'width' : '500px',
            'height': '500px',
            'padding' : '500px 800px',
            'text-align':'center',
        	//'opacity' :'0.1'
    	});
        
        //마스크 표시
        $('#mask').show();
      
        //로딩중 이미지 표시
        $('#loadingImg').show();
    }
     
    function closeLoadingWithMask() {
        $('#mask, #loadingImg').hide();
        $('#mask, #loadingImg').empty(); 
    }
    
    
    var acc = $(".accordion");
    
   	for (i = 0; i < acc.length; i++) {
   		acc[i].addEventListener("click", function() {
  		var idx = Number(this.parentElement.parentElement.children[0].textContent)-1;
		if($(".fold")[idx].style.display=="none"){
 			 $(".fold")[idx].style.display = "block";
 		  }else{
 			 $(".fold")[idx].style.display = "none";
 		  }
 	  });
 	} 
   	
   	$(".stationName input").on("change",function(){
   		// 정류장 명이 변경된 row의 Index 찾기
   		var rowIdx = $(this).parents().parents().index();
   		// 정류장 목록중에서 변경된 이름과 같은 정류장 찾기
   		var station = parkingList.filter(data => data.stationName==$(this)[0].value);
   
   		if(station.length>0){
   			// 정류장 이름에 해당하는 ID값으로 변경하기
   	   		$(".stationId")[rowIdx].children[0].value = station[0].stationId;;
   		}else{
   			alert("정확한 정류장 명을 입력 및 선택해주세요");
   		}
   		
   		 
   	});
   	
   	$("#btnSave").on("click",function(){
   		var favoritesList = new Array();
   		for(var index = 0; index < $("#favoritesTable")[0].children[1].children.length;index++){
   			var obj = new Object();
   			obj.memberId = document.getElementById("memberId").innerText;
   	   		
	   	   	for(var loop=2; loop<$("#favoritesTable")[0].children[1].children[0].children.length;loop++){
		   	     obj[$("#favoritesTable")[0].children[1].children[index].children[loop].children[0].id] = $("#favoritesTable")[0].children[1].children[index].children[loop].children[0].value; 
		   	 }
   			favoritesList.push(obj);
   		}	
   		
   		$.ajax({
   			url : '/member/registerFavoritesList',
   			data : JSON.stringify(favoritesList),
   			contentType : 'application/json',
   			type : "POST",
   			beforeSend : function(xhr){
   			 	xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
   			},
   			error:function(error){
    			console.log(error);
    		},
    		success:function(result){
    			if(result>0){
    				alert("성공적으로 업데이트 되었습니다.");
    			}
    			
    		}
   		});
   		
   	});
   	
   	
});
</script>
</body>
</html>