<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>다음 지도 API</title>
</head>
<body>
	<div id="map" style="width:750px;height:350px;"></div>

	<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=95a120f4136142a682c524ab0736667c"></script>
	<script>
		var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
		    mapOption = {
		        center: new kakao.maps.LatLng(37.561403970095625, 127.03547621232234), // 지도의 중심좌표
		        level: 3, // 지도의 확대 레벨
		        mapTypeId : kakao.maps.MapTypeId.ROADMAP // 지도종류
		    }; 

		// 지도를 생성한다 
		var map = new kakao.maps.Map(mapContainer, mapOption); 

		// 실시간교통 타일 이미지 추가
		map.addOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC); 



	    // 마커를 표시할 위치와 title을 accDtoList에서 가져와 설정합니다
var inPos = [];
<c:forEach items="${accDtoList}" var="accDto" varStatus="status">
    inPos.push({
        title: `${accDto.acc_info}`,
        latlng: new kakao.maps.LatLng(<c:out value="${accDto.grs80tm_y}" />, <c:out value="${accDto.grs80tm_x}" />)
    });
    <c:if test="${not status.last}">
        inPos.push(',');
    </c:if>
</c:forEach>
var positions = inPos;

	    // 마커 이미지의 이미지 주소입니다
	    var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"; 

	    // 마커를 표시할 위치와 title 객체 배열을 순회하면서 마커를 생성합니다.
	    for (var i = 0; i < positions.length; i++) {
	        var marker = new kakao.maps.Marker({
	            map: map, // 마커를 표시할 지도
	            position: positions[i].latlng, // 마커를 표시할 위치
	            title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
	            image: new kakao.maps.MarkerImage(imageSrc, new kakao.maps.Size(24, 35)) // 마커 이미지 
	        });
	    }
	</script>
</body>
</html>