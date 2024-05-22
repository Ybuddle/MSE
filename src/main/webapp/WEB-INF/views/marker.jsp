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
/*overlay css  */
#containerWrapper {
	position: relative;
	width: 900px;
	height: 350px;
	padding: 15px 10px;
}

#containerWrapper th[scope="row"] {
	width: 100px;
}

#poptable {
	width: 900px;
	height: 350px;
}
/* table {
table-layout : fixed } */
td
{
 max-width: 0;
white-space: pre-line;

/* word-wrap: break-word; */}
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
<script
		src="//dapi.kakao.com/v2/maps/sdk.js?appkey=95a120f4136142a682c524ab0736667c"></script>
	<script>
	// 로딩화면 위한 변수
		const mask = document.querySelector('.mask');
		const html = document.querySelector('html');
		html.style.overflow = 'hidden'; // 로딩 중 스크롤 방지
		
		
	// promise이용한 ajax통신	
		function getAccInfoAjax() {
		    return new Promise(function(resolve, reject) {
		        $.ajax({
		            method: "GET",
		            url: "accInfoAjax",
		            dataType: "json", // 'json'이 맞는 표현입니다.
		        })
		        .done((data) => {
		            console.log("데이터가 있나요? : " + data);
		            resolve(data); // 데이터가 성공적으로 도착했을 때 resolve 호출
		        })
		        .fail((error) => {
		            console.error("AJAX 요청 실패:", error);
		            reject(null); // 요청이 실패했을 때 reject 호출
		        })
		        .always(() => {
		            mask.style.opacity = '0';
		            setTimeout(function() {
		                mask.style.display = 'none';
		                html.style.overflow = 'auto';
		            }, 500);
		        });
		    });
		}
		
  // 출력형식에 맞게 time형식 고치기
 function resolveDateTime(accdate, acctime){
	var occr_date = accdate; // 예: '20240516'
	var occr_time = acctime; // 예: '164700'
	
	//시작날짜와 시간을 나눠서 가져오기
	var year = occr_date.substring(0, 4);
	var month = occr_date.substring(4, 6);
	var day = occr_date.substring(6, 8);
	var hour = occr_time.substring(0, 2);
	var minute = occr_time.substring(2, 4);
	
	var resolvedTime = year + '-' + month + '-' + day + ' ' + hour + ':'
	+ minute;
	
	return resolvedTime;
 }
  
//kakao 지도 생성 시작
	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	mapOption = {
		center : new kakao.maps.LatLng(37.561403970095625,
				127.03547621232234), // 지도의 중심좌표
		level : 4, // 지도의 확대 레벨
		mapTypeId : kakao.maps.MapTypeId.ROADMAP // 지도종류
	};
  
	// 지도를 생성한다 
	var map = new kakao.maps.Map(mapContainer, mapOption);
	// 실시간교통 타일 이미지 추가
	map.addOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC);
  
	
	
 	async function getdata(){
			let accDtoList = await getAccInfoAjax();
			let positions = [];
			for(const [i,dto] of accDtoList.entries()){
				let startTime = resolveDateTime(dto.occr_date,dto.occr_time);
				let endTime = resolveDateTime(dto.exp_clr_date,dto.exp_clr_time);
				positions.push({
					title : dto.acc_info,
					acc_type : dto.acc_type,
					acc_dtype : dto.acc_dtype,
					startTime : startTime,
					endTime : endTime,
					lat : dto.grs80tm_y,
					lng : dto.grs80tm_x
				});
				displayMarker(positions[i]);
			}
			console.log(positions);
		};
		
		//overlay 기본셋
		var overlay = new kakao.maps.CustomOverlay({
			xAnchor : 0.5,
			yAnchor : 1,
			zIndex : 3
		});
		
		//지도에 마커를 표시하는 함수입니다    
		function displayMarker(data) {
			var marker = new kakao.maps.Marker({
				map : map,
				position : new kakao.maps.LatLng(data.lat,data.lng),
				title : data.title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
			});
			//-------
			// 컨테이너 div 생성 및 클래스 추가
			var container = document.createElement('div');
			container.id = 'containerWrapper';
			// 테이블 생성 및 클래스 추가
			var table = document.createElement('table');
			table.className = 'table';
			table.id = 'poptable';

			// 테이블 헤드 생성
			var thead = document.createElement('thead');

			// 테이블 헤드의 행 생성
			var trHead = document.createElement('tr');

			// 첫 번째 헤드 셀 생성 및 설정
			var thHead = document.createElement('th');
			thHead.colSpan = 2;
			thHead.scope = 'col';
			thHead.style.position = 'relative';
			thHead.textContent = '[ 덕수궁길 - 집회및행사 ]';

			// "Close" 버튼 생성 및 설정
			var closeButton = document.createElement('button');
			closeButton.type = 'button';
			closeButton.id = 'popclose';
			closeButton.className = 'btn btn-outline-secondary btn-sm';
			closeButton.style.position = 'absolute';
			closeButton.style.top = '5px';
			closeButton.style.right = '10px';
			closeButton.textContent = 'Close';

			// "Close" 버튼을 thHead에 추가
			thHead.appendChild(closeButton);

			// thHead를 trHead에 추가
			trHead.appendChild(thHead);
			thead.appendChild(trHead);
			table.appendChild(thead);

			// 테이블 바디 생성 및 클래스 추가
			var tbody = document.createElement('tbody');
			tbody.className = 'table-group-divider';

			// 첫 번째 데이터 행 생성
			var tr1 = document.createElement('tr');
			var th1 = document.createElement('th');
			th1.scope = 'row';
			th1.textContent = '기간';
			tr1.appendChild(th1);
			var td1 = document.createElement('td');
			td1.textContent = data.startTime + ` ~ ` + data.endTime;
			tr1.appendChild(td1);
			tbody.appendChild(tr1);

			// 두 번째 데이터 행 생성
			var tr2 = document.createElement('tr');
			var th2 = document.createElement('th');
			th2.scope = 'row';
			th2.textContent = '돌발유형';
			tr2.appendChild(th2);
			var td2 = document.createElement('td');
			td2.textContent = data.acc_type +'('+ data.acc_dtype+')';
			tr2.appendChild(td2);
			tbody.appendChild(tr2);

			// 세 번째 데이터 행 생성
			var tr3 = document.createElement('tr');
			var th3 = document.createElement('th');
			th3.scope = 'row';
			th3.textContent = '도로';
			tr3.appendChild(th3);
			var td3 = document.createElement('td');
			td3.textContent = '[덕수궁길]덕수궁->정동제일교회[덕수궁길]덕수궁->정동제일교회[덕수궁길]덕수궁->정동제일교회[덕수궁길]덕수궁->정동제일교회[덕수궁길]덕수궁->정동제일교회';
			tr3.appendChild(td3);
			tbody.appendChild(tr3);

			// 네 번째 데이터 행 생성
			var tr4 = document.createElement('tr');
			var th4 = document.createElement('th');
			th4.scope = 'row';
			th4.textContent = '통제여부';
			tr4.appendChild(th4);
			var td4 = document.createElement('td');
			td4.textContent = '전체 통제';
			tr4.appendChild(td4);
			tbody.appendChild(tr4);

			// 다섯 번째 데이터 행 생성 (colspan이 2인 셀)
			var tr5 = document.createElement('tr');
			var td5 = document.createElement('td');
			td5.colSpan = 2;
			td5.textContent = data.title;
			tr5.appendChild(td5);
			tbody.appendChild(tr5);

			// 테이블에 tbody 추가
			table.appendChild(tbody);

			// 컨테이너에 테이블 추가
			container.appendChild(table);
			/* //문서의 body에 컨테이너 추가 (또는 원하는 다른 위치에 추가)
			 document.body.appendChild(container); */
			// "Close" 버튼 클릭 이벤트 리스너 추가
			closeButton.addEventListener('click', function() {
				overlay.setMap(null);
			});

			//-------
			var content = document.createElement('div');
			//컨테이너의 가로폭과 세로높이 설정
			/*
			 content.innerHTML =  data.title;
			 content.style.cssText = 'background: white; border: 1px solid black'; */

			var closeBtn = document.createElement('button');
			closeBtn.innerHTML = '닫기';
			closeBtn.onclick = function() {
				overlay.setMap(null);
			};

			/*  content.appendChild(closeBtn); */
			content.appendChild(container);

			kakao.maps.event.addListener(marker, 'click', function() {
				alert("클릭");
				overlay.setContent(content);
				overlay.setPosition(new kakao.maps.LatLng(data.lat,data.lng));
				overlay.setMap(map);
				/*      document.body.appendChild(container); */
				console.log(overlay);
			});
			console.log(container);
		}
		getdata();
	</script>
<script>
/* const accInfoAjax = () => {
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
        
    } catch(e) {
        console.error(e);
    }
})(); */
</script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
</body>
</html>