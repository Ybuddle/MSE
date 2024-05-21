<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>다음 지도 API</title>


<!-- 부트스트랩 css 사용 -->
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<script type="text/javascript"
	src="resources/jquery/jquery-3.7.1.min.js"></script>

<style>
.bd-placeholder-img {
	font-size: 1.125rem;
	text-anchor: middle;
	-webkit-user-select: none;
	-moz-user-select: none;
	user-select: none;
}

@media ( min-width : 768px) {
	.bd-placeholder-img-lg {
		font-size: 3.5rem;
	}
}

.b-example-divider {
	width: 100%;
	height: 3rem;
	background-color: rgba(0, 0, 0, .1);
	border: solid rgba(0, 0, 0, .15);
	border-width: 1px 0;
	box-shadow: inset 0 .5em 1.5em rgba(0, 0, 0, .1), inset 0 .125em .5em
		rgba(0, 0, 0, .15);
}

.b-example-vr {
	flex-shrink: 0;
	width: 1.5rem;
	height: 100vh;
}

.bi {
	vertical-align: -.125em;
	fill: currentColor;
}

.nav-scroller {
	position: relative;
	z-index: 2;
	height: 2.75rem;
	overflow-y: hidden;
}

.nav-scroller .nav {
	display: flex;
	flex-wrap: nowrap;
	padding-bottom: 1rem;
	margin-top: -1px;
	overflow-x: auto;
	text-align: center;
	white-space: nowrap;
	-webkit-overflow-scrolling: touch;
}

.btn-bd-primary { -
	-bd-violet-bg: #712cf9; -
	-bd-violet-rgb: 112.520718, 44.062154, 249.437846; -
	-bs-btn-font-weight: 600; -
	-bs-btn-color: var(- -bs-white); -
	-bs-btn-bg: var(- -bd-violet-bg); -
	-bs-btn-border-color: var(- -bd-violet-bg); -
	-bs-btn-hover-color: var(- -bs-white); -
	-bs-btn-hover-bg: #6528e0; -
	-bs-btn-hover-border-color: #6528e0; -
	-bs-btn-focus-shadow-rgb: var(- -bd-violet-rgb); -
	-bs-btn-active-color: var(- -bs-btn-hover-color); -
	-bs-btn-active-bg: #5a23c8; -
	-bs-btn-active-border-color: #5a23c8;
}

.bd-mode-toggle {
	z-index: 1500;
}

.bd-mode-toggle .dropdown-menu .active .bi {
	display: block !important;
}
/*overlay css  */
#containerWrapper {position:relative;width:900px;height:350px;padding:15px 10px;}
#containerWrapper th[scope="row"] { width: 100px; }
#poptable { width:900px;height:350px; }
/* 로딩창 */
.mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0);
  z-index: 9999;
  opacity: .5;
  transition: 0.5s ease;
}
</style>

</head>
<body>
<!-- 로딩 화면 -->
<div class="mask">
  <img class="loadingImg" src='https://i.ibb.co/20zw80q/1487.gif'>
</div>
<script>
const mask = document.querySelector('.mask');
const html = document.querySelector('html');

html.style.overflow = 'hidden'; //로딩 중 스크롤 방지
window.addEventListener('load', function () {
  //아래 setTimeout은 로딩되는 과정을 임의로 생성하기 위해 사용. 실제 적용 시에는 삭제 후 적용해야함.
    mask.style.opacity = '0'; //서서히 사라지는 효과
    html.style.overflow = 'auto'; //스크롤 방지 해제
    mask.style.display = 'none';

})
</script>
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

	<div class="container-xl mb-4">
		<div id="map" style="width: 100%; height: 37.5rem;"></div>
	</div>
	<div class="container-xl mb-4" id="accList">
	
	</div>

	<!-- <script  type="text/javascript" src="resources/js/bootstrap.min.js"></script> -->

	<script
		src="//dapi.kakao.com/v2/maps/sdk.js?appkey=95a120f4136142a682c524ab0736667c"></script>
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
//div accList 선택

var accListDiv = $('#accList');

	    // 마커를 표시할 위치와 title을 accDtoList에서 가져와 설정합니다
var inPos = [];
<c:forEach items="${accDtoList}" var="accDto" varStatus="status">
//시간 출력
//accDto의 occr_date와 occr_time
var occr_date = `${accDto.occr_date}`; // 예: '20240516'
var occr_time = `${accDto.occr_time}`;   // 예: '164700'
var exp_clr_date = `${accDto.exp_clr_date}`; // 예: '20240516'
var exp_clr_time = `${accDto.exp_clr_time}`;   // 예: '164700'

//시작날짜와 시간을 나눠서 가져오기
var year = occr_date.substring(0, 4);
var month = occr_date.substring(4, 6);
var day = occr_date.substring(6, 8);
var hour = occr_time.substring(0, 2);
var minute = occr_time.substring(2, 4);
//종료날짜와 시간을 나눠서 가져오기
var year2 = exp_clr_date.substring(0, 4);
var month2 = exp_clr_date.substring(4, 6);
var day2 = exp_clr_date.substring(6, 8);
var hour2 = exp_clr_time.substring(0, 2);
var minute2 = exp_clr_time.substring(2, 4);

//시작 시간과 끝 시간 계산
var startTime = year + '-' + month + '-' + day + ' ' + hour + ':' + minute;
var endTime = year2 + '-' + month2 + '-' + day2 + ' ' + hour2 + ':' + minute2;
//결과 출력
console.log(startTime + ' ~ ' + endTime); // 예: "2024-05-16 16:47 ~ 2024-05-16 19:47"

var acc_type = `${accDto.acc_type}`;
var acc_dtype = `${accDto.acc_dtype}`;
	//inPos
    inPos.push({
        title: `${accDto.acc_info}`,
        acc_type: acc_type,
        acc_dtype: acc_dtype,
        startTime: startTime,
        endTime: endTime,
        latlng: new kakao.maps.LatLng(<c:out value="${accDto.grs80tm_y}" />, <c:out value="${accDto.grs80tm_x}" />)
    });
    <c:if test="${not status.last}">
        inPos.push(',');
    </c:if>
/* 
 //tableHtml 생성
 var tableHTML = `
<table class="table">
    <thead>
      <tr>
        <th colspan="2" scope="col" style="position: relative;">[ 덕수궁길 - 집회및행사 ]
        </th>
      </tr>
    </thead>
    <tbody class="table-group-divider">
      <tr>
        <th scope="row">기간</th>
        <td>` + startTime + `~ `+ endTime+`</td>
      </tr>
      <tr>
        <th scope="row">돌발유형</th>
        <td>집회및행사(행사)</td>
      </tr>
      <tr>
        <th scope="row">도로</th>
        <td>[덕수궁길]덕수궁->정동제일교회[덕수궁길]덕수궁->정동제일교회[덕수궁길]덕수궁->정동제일교회[덕수궁길]덕수궁->정동제일교회[덕수궁길]덕수궁->정동제일교회</td>
      </tr>
      <tr>
        <th scope="row">통제여부</th>
        <td>전체 통제</td>
      </tr>
      <tr>
        <td colspan="2"> ${accDto.acc_info}</td>
      </tr>
    </tbody>
</table>
`;
 // accList라는 id를 가진 div에 innerHTML 추가
    if (accListDiv.length) {
        accListDiv.append(tableHTML);
    } else {
        console.error("Element with id 'accList' not found.");
    } */
</c:forEach>
var positions = inPos;
// 데이터마다 마커 생성 function
console.log(positions.length);
for(let i=0; i < positions.length; i++){
    var data = positions[i];
    displayMarker(data);
}

//overlay 기본셋
 var overlay = new kakao.maps.CustomOverlay({
	    xAnchor: 0.5,
	    yAnchor: 1,
	    zIndex: 3
 });
 
//지도에 마커를 표시하는 함수입니다    
function displayMarker(data) { 
 var marker = new kakao.maps.Marker({
     map: map,
     position: data.latlng,
     title: data.title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
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
td1.textContent = data.startTime+  ` ~ ` + data.endTime;
tr1.appendChild(td1);
tbody.appendChild(tr1);

// 두 번째 데이터 행 생성
var tr2 = document.createElement('tr');
var th2 = document.createElement('th');
th2.scope = 'row';
th2.textContent = '돌발유형';
tr2.appendChild(th2);
var td2 = document.createElement('td');
td2.textContent = '집회및행사(행사)';
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
 closeBtn.onclick = function () {
     overlay.setMap(null);
 };
 
/*  content.appendChild(closeBtn); */
 content.appendChild(container);
 
 kakao.maps.event.addListener(marker, 'click', function() {
	 	alert("클릭");
		 overlay.setContent(content);
     overlay.setPosition(data.latlng);
     overlay.setMap(map);
/*      document.body.appendChild(container); */
     console.log(overlay);
 }); 
console.log(container);
}
	</script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
</body>
</html>