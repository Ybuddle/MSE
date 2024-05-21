<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<!-- 부트스트랩 css 사용 -->
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<script type="text/javascript"
	src="resources/jquery/jquery-3.7.1.min.js"></script>

<title>이미지 지도에 마커 표시하기</title>
<style>
.mask {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background-color: rgba(255, 255, 255, 0.8);
	display: flex;
	justify-content: center;
	align-items: center;
	z-index: 9999;
	transition: opacity 0.5s ease;
}

.loadingImg {
	width: 50px;
	height: 50px;
}
</style>

</head>
<body>

	<nav class="navbar navbar-expand-lg navbar-dark bg-dark"
		aria-label="Ninth navbar example">
		<div class="container-xl">
			<a class="navbar-brand" href="#">Container XL</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarsExample07XL"
				aria-controls="navbarsExample07XL" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>

			<div class="collapse navbar-collapse" id="navbarsExample07XL">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" href="#">Home</a></li>
					<li class="nav-item"><a class="nav-link" href="#">Link</a></li>
					<li class="nav-item"><a class="nav-link disabled"
						aria-disabled="true">Disabled</a></li>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#"
						data-bs-toggle="dropdown" aria-expanded="false">Dropdown</a>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item" href="#">Action</a></li>
							<li><a class="dropdown-item" href="#">Another action</a></li>
							<li><a class="dropdown-item" href="#">Something else
									here</a></li>
						</ul></li>
				</ul>
				<form role="search">
					<input class="form-control" type="search" placeholder="Search"
						aria-label="Search">
				</form>
			</div>
		</div>
	</nav>

	<div class="container-xl mb-4" id="accList">
		<!-- 로딩 화면 -->
		<div class="mask">
			<img class="loadingImg" src='https://i.ibb.co/20zw80q/1487.gif'>
		</div>
		<div id="map" style="width: 100%; height: 37.5rem;"></div>
	</div>

	<script>
		const mask = document.querySelector('.mask');
		const html = document.querySelector('html');

		html.style.overflow = 'hidden'; // 로딩 중 스크롤 방지
/* 
		$.ajax({
			type : "GET",
			url : "accInfoAjax",
			success : function(data) {
				// 데이터를 받아와서 사용
				console.log(data);

				// 예시: JSON 데이터를 파싱하여 배열로 변환
				var accDtoList = JSON.parse(data);

				accDtoList.forEach(function(accDto) {
					console.log(accDto.occr_date);
					console.log(accDto.occr_time);
					// 여기서부터는 원하는 대로 데이터 활용
				});

				// 데이터 로드 완료 후 로딩 화면 제거
				mask.style.opacity = '0'; // 서서히 사라지는 효과
				setTimeout(function() {
					mask.style.display = 'none';
					html.style.overflow = 'auto'; // 스크롤 방지 해제
				}, 500); // transition과 동일한 시간으로 설정
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.error("Error:", textStatus, errorThrown);
				// 에러 발생 시에도 로딩 화면 제거
				mask.style.opacity = '0';
				setTimeout(function() {
					mask.style.display = 'none';
					html.style.overflow = 'auto';
				}, 500);
			}
		}); */
	</script>
<script>
const accInfoAjax = () => {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: "GET",
            url: "accInfoAjax", // 실제 요청할 엔드포인트 URL
            success: function(data) {
                // 데이터를 받아와서 resolve로 반환
                resolve(data);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                // 실패 시 에러를 reject로 반환
                reject(errorThrown);
            }
        });
    });
};

(async () => {
    try{
        for(const [i,acc] of accInfoAjax.entries()) {
            const mapContainer = document.getElementById('map'+i), // 지도를 표시할 div
    		mapOption = {
    			center : new kakao.maps.LatLng(37.561403970095625,
    					127.03547621232234), // 지도의 중심좌표
    			level : 3, // 지도의 확대 레벨
    			mapTypeId : kakao.maps.MapTypeId.ROADMAP
    		// 지도종류
    		};


            // 지도를 생성합니다
            const map = new kakao.maps.Map(mapContainer, mapOption);

            const data = await accInfoAjax();
            coords = new kakao.maps.LatLng(data.result[0].y, data.result[0].x);
            // 결과값으로 받은 위치를 마커로 표시합니다
            var marker = new kakao.maps.Marker({
                map: map,
                position: coords
            });

            // 인포윈도우로 장소에 대한 설명을 표시합니다
            var infowindow = new kakao.maps.InfoWindow({
                position: coords,
                content: '<div style="width:150px;text-align:center;padding:5px;font-size:12px;">'+addr+'</div>'
            });

            infowindow.open(map, marker);
            map.setCenter(coords);

            //주소 좌표와 생성한 지도 객체를 배열에 담아줍니다.
            position.push(coords);
            maps.push(map);
        }
    } catch(e) {
        console.error(e);
    }
})();
</script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
</body>
</html>