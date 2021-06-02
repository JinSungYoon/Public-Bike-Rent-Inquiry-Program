<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/views/globalVariable.jsp"%>

<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>서울시 따릉이 자전거</title>

	<!-- Bootstrap 4 -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
</head>
<body>
<%= application.getContextPath() %><br>
<%= request.getSession().getServletContext().getRealPath("/resources/img") %><br>
<%= application.getRealPath("/resources/img") %><br>
<h1>실시간 공공자전거 대여현황</h1>
<div id="map" style="width:100%;height:350px;">
</div>
<div class ="search" style="">
	<input id="address" type="text" placeHolder="검색할 주소"/>
	<input id="submit" type="button" value="주소검색"/>
</div>

<div class="searchPlaceDiv">
	<form id="searchPlaceForm" class="searchPlaceForm" action="/board/searchPlace" method="post">
		<input id="placeName" name="placeName" type="text" placeHolder="검색할 지역" />
		<input id="dataSubmit" type="button" value="지역명검색"/>
	</form>
</div>
<div>
	<c:if test="${locationInfo!=null}">
		<span class="title">${locationInfo[0].title}</span>
		<span class="placeAddress">${locationInfo[0].address}</span>
	</c:if>
</div>

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
<script type="text/javascript" src='https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=<%=naverMapApiKey%>&submodules=geocoder'></script>

<script type="text/javascript">

var map = "";			// map 전역
var marker = "";		//marker 전역
var markers = [];		// marker 위치를 저장하기 위한 배열
var infoWindows = [];	// information을 저장하기 위한 배열
var parkingList;

window.onload = function(){
	
};

$(document).ready(function(){
    
	// 화면 Load 이후에 공공 자전거 데이터 조회
	$.ajax({
		url : '/board/publicBikeParkingList',
		dataType : "json",
		type : 'POST',
		error : function(){
			alert("통신 실패");
		},
		success : function(result){
			console.log("Success laod public bike parking list");
			parkingList = result;
			setupMarker(parkingList);
		}
		,beforeSend:function(){
			LoadingWithMask();
		},
		complete:function(){
			closeLoadingWithMask();
		}
	});
	
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

            if (infoWindow.getMap()) {
                infoWindow.close();
            } else {
                infoWindow.open(map, marker);
            }
        }
    }
    
	
	function setupMarker(data){
		// 조회된 10개의 자전거 대여소 위치를 지도에 표시하는 부분
		for(var loop=0;loop<data.length;loop++){
	    	if(loop==0){
	    		setMarker(data[loop].stationLatitude,data[loop].stationLongitude,data[loop].stationName);
	    	}else{
	    		addMarker(data[loop].stationLatitude,data[loop].stationLongitude,data[loop].stationName);
	    		naver.maps.Event.addListener(markers[loop], 'click', getClickHandler(loop));
	    	}
	    }
	}
	
    map = new naver.maps.Map("map", {
        center: new naver.maps.LatLng(37.3595316, 127.1052133),
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
			
            map.setCenter(point);
            infoWindow.open(map, point);
            
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
			
            map.setCenter(point);
            infoWindow.open(map, point);
            
        });
    }
	
    // 지역 이름으로 검색하는 함수 CLick 이벤트
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
			error : function(){
				alert("통신 실패");
			},
			success : function(result){
				console.log("Success search place");
				searchPlacenameToCoordinate(result[0]);
			}
		}); //$.ajax
    	
    });
 	
    // 지역 이름으로 검색하는 함수 Enter 이벤트
    $("#dataSubmit").keydown(function(e){
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
			error : function(){
				alert("통신 실패");
			},
			success : function(result){
				console.log("Success search place");
				searchPlacenameToCoordinate(result[0]);
			}
		}); //$.ajax
    	
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
        
		var latlng = {y:stationLatitude,x:stationLongitude,_lnt:stationLatitude,_lng:stationLongitude,locationNm:stationName}
		
		searchCoordinateToAddress(latlng);
		
		/*
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
        */
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
          
        loadingImg +="<img src='/resources/img/loading.gif' style='position: absolute; display: block; margin: 0px auto;'/>";
     
        //화면에 레이어 추가
        $('body')
            .append(mask)
     
        //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채웁니다.
        $('#mask').css({
                'width' : maskWidth,
                'height': maskHeight,
                'opacity' :'0.3'
        });
      
        //마스크 표시
        $('#mask').show();
      
        //로딩중 이미지 표시
        $('#loadingImg').append(loadingImg);
        $('#loadingImg').show();
    }
     
    function closeLoadingWithMask() {
        $('#mask, #loadingImg').hide();
        $('#mask, #loadingImg').empty(); 
    }
    
});
	
</script>
</body>
</html>