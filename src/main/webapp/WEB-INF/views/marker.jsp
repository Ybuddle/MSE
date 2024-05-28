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

<title>돌발상황</title>
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
	width: 450px;
	height: 350px;
	padding: 15px 10px;
}

#containerWrapper th[scope="row"] {
	width: 100px;
}

#poptable {
	width: 450px;
	height: 350px;
}
#pop-head{
	display: flex;
	justify-content: space-between;
}
table {
	/* table-layout : fixed */
	
}

#poptable td {
 	max-width: 0;
	white-space: pre-wrap;

	/* word-wrap: break-word; */
}
/*accList 테이블  */
#accList{
width:100%; 
height:250px; 
overflow:auto;
}
#accList table{
}
#accList thead{
position : sticky; 
top: 0; 
}
.add-time-td {
	width: 180px;
}
</style>

</head>
<body>

	<nav class="navbar navbar-expand-lg navbar-dark bg-dark"
		aria-label="Ninth navbar example">
		<div class="container-xl">
			<a class="navbar-brand" href="marker">My SeoulEvent</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarsExample07XL"
				aria-controls="navbarsExample07XL" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>

			<div class="collapse navbar-collapse" id="navbarsExample07XL">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" href="marker">돌발상황</a></li>
					<li class="nav-item"><a class="nav-link" href="cctv">CCTV</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container-xl mb-4">
		<!-- 로딩 화면 -->
		<div class="mask">
			<img class="loadingImg" src='https://i.ibb.co/20zw80q/1487.gif'>
		</div> 

		<!--kakaomap  -->
		<div id="map" style="width: 100%; height: 37.5rem;"></div>

		<!--탭바  -->
		<ul class="nav nav-tabs" role="tablist">
			<li class="nav-item" role="presentation"><a
				class="nav-link active" data-bs-toggle="tab" href="#accContainer"
				aria-selected="true" role="tab">사고 및 통제 <span class="badge bg-primary">0건</span>
				</a></li>
<!-- 			<li class="nav-item" role="presentation"><a class="nav-link"
				data-bs-toggle="tab" href="#profile" aria-selected="false"
				tabindex="-1" role="tab">Profile</a></li>
			<li class="nav-item" role="presentation"><a
				class="nav-link disabled" href="#" aria-selected="false"
				tabindex="-1" role="tab">Disabled</a></li> -->
		</ul>

		<div id="myTabContent" class="tab-content">
			<!--첫번째탭 accContainer-->
			<div class="tab-pane fade show active" id="accContainer"
				role="tabpanel">
				<!-- 돌발 리스트 -->
				<div id="accList">
					<table class="table table-hover">
				<colgroup>
            <col class="col-md-10">
            <col class="col-md-2">
        </colgroup>
						<thead>
							<tr>
								<th scope="col">발생내용</th>
								<th class="acc-time-td" scope="col">발생 및 완료예정일</th>
							</tr>
						</thead>
						<tbody>
							<!-- <tr>
								<td>Column content</td>
								<td class="add-time-td"><div>2024-05-16 11:00</div>
									<div>2024-05-16 14:00</div></td>
							</tr> -->
						</tbody>
					</table>
				</div>
			</div>
				<!--첫번째탭accContainer 끝-->

		</div>
	</div>
	<!--content container div 끝 -->
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
		            dataType: "json",
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
		};
		
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
  
	// 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
	var bounds = new kakao.maps.LatLngBounds();    
	function setBounds() {
	    // LatLngBounds 객체에 추가된 좌표들을 기준으로 지도의 범위를 재설정합니다
	    // 이때 지도의 중심좌표와 레벨이 변경될 수 있습니다
	    map.setBounds(bounds);
	};
 	async function getdata(){
			let accDtoList = await getAccInfoAjax();
			//dto 마다 정제된 변수들 list
			let positions = [];
			//탭바에 총 갯수 추가
			addTotalAccList(accDtoList);
			
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
					lng : dto.grs80tm_x,
					acc_road_code : dto.acc_road_code
				});
				
				//dto 마다 기본 marker생성
				var marker = displayMarker(positions[i]);
				//해당 marker의 오버레이와 오버레이 띄우는 클릭이벤트
				var content = getMakerOverlayContents(marker,positions[i]);
				
			    // LatLngBounds 객체에 좌표를 추가
			    bounds.extend(marker.getPosition());
				//리스트 생성(marker,인포 윈도우 컨텐츠)+클릭이벤트
				addAccList(positions[i],marker,content);
			}
			//console.log(positions);
		    //영역 재설정
		    setBounds();
		};
		
		//overlay 기본셋
		var overlay = new kakao.maps.CustomOverlay({
			xAnchor : 0,
			yAnchor : 0.5,
			zIndex : 3
		});
		
		//accList에 내용을 append 하는 함수
		function addAccList(data,marker,content){
			 var newtr = document.createElement('tr');
			var newRowHTML = `
                    <td class="acc_info">\${data.title}</td>
                    <td class="add-time-td">
                        <div>\${data.startTime}</div>
                        <div>\${data.endTime}</div>
                    </td>
            `;
            // 새로운 행을 추가하고 tbody를 표시합니다.
            
            $(newtr).append(newRowHTML)
            $('#accList tbody').append(newtr);
            
            if(data.acc_road_code=="부분통제"){
            	let roadCodeSpan = `<span class="badge bg-warning">\${data.acc_road_code}</span>`;
            	$(newtr).find('.acc_info').append(roadCodeSpan)
            }else{
            	let roadCodeSpan = `<span class="badge bg-danger">\${data.acc_road_code}</span>`;
            	$(newtr).find('.acc_info').append(roadCodeSpan)
            }
            //tr 마다 클릭했을때 해당 marker 좌표로 이동
            $(newtr).click(function() {
            	map.panTo(marker.getPosition());
		        overlay.setContent(content);
		        
		        overlay.setPosition(marker.getPosition());
		        overlay.setMap(map,marker);
            });
		};
		//accList 탭에 총 갯수 표시
		function addTotalAccList(accDtoList){
			let accTotalNum = accDtoList.length + '건';
			$('.badge.bg-primary').text(accTotalNum);
		};
		
		//마커에 대한 overlay contents 설정 함수
		function getMakerOverlayContents(marker,data){
			// HTML 컨텐츠를 백틱을 이용한 문자열 템플릿으로 생성
		    var contentHTML = `
		        <div id="containerWrapper">
		            <table class="table" id="poptable">
		                <thead>
		                    <tr>
		                        <th scope="col" colspan="2" style="position: relative;">
		                        	<div id="pop-head">		                            
		                        		<button type="button" id="popclose" class="btn btn-outline-secondary btn-sm">Close</button>
		                            <button type="button" id="popCctv" class="btn btn-outline-primary" value="\${data.lat},\${data.lng}">가까운CCTV</button>
		                          </div>
		                        </th>
		                    </tr>
		                </thead>
		                <tbody class="table-group-divider">
		                    <tr>
		                        <th scope="row">기간</th>
		                        <td>\${data.startTime} ~ \${data.endTime}</td>
		                    </tr>
		                    <tr>
		                        <th scope="row">돌발유형</th>
		                        <td>\${data.acc_type} (\${data.acc_dtype})</td>
		                    </tr>
		                    <tr>
		                        <th scope="row">통제여부</th>
		                        <td>\${data.acc_road_code}</td>
		                    </tr>
		                    <tr>
		                        <td colspan="2">\${data.title}</td>
		                    </tr>
		                </tbody>
		            </table>
		        </div>
		    `;

		    // content div에 해당 html 넣기
		    var content = document.createElement('div');
		    content.innerHTML = contentHTML;
				
		    kakao.maps.event.addListener(marker, 'click', function() {
		        overlay.setContent(content);
		        overlay.setPosition(new kakao.maps.LatLng(data.lat, data.lng));
		        overlay.setMap(map,marker);
		       // console.log(overlay);
		    });

		    console.log(content);
		    
		    return content;
		};

		// 지도에 마커를 표시하는 함수입니다    
		function displayMarker(data) {
			let latlng = new kakao.maps.LatLng(data.lat, data.lng);
		    var marker = new kakao.maps.Marker({
		        map: map,
		        position: latlng,
		        title: data.title // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
		    });

		    
		    return marker;
		};
		//실행
		getdata();
    
		$(document).on("click","#popclose",function() {
			overlay.setMap(null);
		});
		$(document).on("click","#popCctv",function() {
			//alert($(this).val());
			// 원본 문자열
			var coordinates = $(this).val();

			// 문자열을 쉼표로 분리
			var parts = coordinates.split(",");

			// 분리된 값을 변수에 대입
			var latitude = parts[0];
			var longitude = parts[1];
			
			function getCctvData(latitude,longitude) {
			    return new Promise(function(resolve, reject) {
			        $.ajax({
			            method: "GET",
			            url: "cctvDataAjax",
			            dataType: "json",
			            data : { lat : latitude , lng : longitude}
			        })
			        .done((data) => {
			            console.log("데이터가 있나요? : " + data);
			            resolve(data); // 데이터가 성공적으로 도착했을 때 resolve 호출
			        })
			        .fail((error) => {
			            console.error("AJAX 요청 실패:", error);
			            reject(null); // 요청이 실패했을 때 reject 호출
			        });
			    });
			};
        // AJAX 요청 실행
        getCctvData(latitude, longitude).then((data) => {
            // 데이터를 성공적으로 받았을 때 처리
            console.log("받은 데이터:", data);
            console.log("받은 데이터:", data[0].CCTVUTICDTO);
           var streamCctv =  new StreamCctv(data[0], null, function(streamCctv){
                var width = 355;
                var height = 345;
                var top = (window.screen.height - height) / 2;
                var left = (window.screen.width - width) / 2;
                var key = "qokfUPkT7O2DIrbm6j0JaWLWRfx1bjQYbEbegfHIhKCj8jdTPwhmvJ18JoxScyLYrJg7fbg5s7mGkXmqwcgg";
                  console.log(streamCctv.gCctvId+" 스트르");
                if(streamCctv.gCctvId.indexOf('E60') != -1 || streamCctv.gCctvId.indexOf('E62') != -1){
                    streamCctv.gId = encodeURIComponent(streamCctv.gId);
                  }

                // 추가된 디버깅 로그
                console.log("gCctvId: ", streamCctv.gCctvId);
                console.log("gCctvName: ", streamCctv.gCctvName);
                console.log("gKind: ", streamCctv.gKind);
                console.log("gCctvIp: ", streamCctv.gCctvIp);
                console.log("gCh: ", streamCctv.gCh);
                console.log("gId: ", streamCctv.gId);
                console.log("gPasswd: ", streamCctv.gPasswd);
                console.log("gPort: ", streamCctv.gPort);
                var pathUrl = 'http://www.utic.go.kr/view/map/openDataCctvStream.jsp?key=' + key+ '&cctvid=' + streamCctv.gCctvId + '&cctvName=' + encodeURI(encodeURIComponent(streamCctv.gCctvName)) + '&kind=' + streamCctv.gKind + '&cctvip=' + streamCctv.gCctvIp + '&cctvch=' + streamCctv.gCh + '&id=' + streamCctv.gId + '&cctvpasswd=' + streamCctv.gPasswd + '&cctvport=' + streamCctv.gPort;
           console.log(pathUrl);
                window.open('http://www.utic.go.kr/view/map/openDataCctvStream.jsp?key=' + key+ 
                		'&cctvid=' + streamCctv.gCctvId+ '&cctvName=' + encodeURI(encodeURIComponent(streamCctv.gCctvName)) 
                		+ '&kind=' + streamCctv.gKind + '&cctvip=' + streamCctv.gCctvIp + '&cctvch=' + streamCctv.gCh 
                		+ '&id=' + streamCctv.gId + '&cctvpasswd=' + streamCctv.gPasswd + '&cctvport=' + streamCctv.gPort, 
                		'PopupCctv',"top=" + top + "px, left=" + left + "px, width=" + width + "px, height=" 
                		+ height+ "px, menubar=no, location=no, toolbar=no, scrollbars=no, status=no, resizable=no");

              });
        }).catch((error) => {
            // 오류가 발생했을 때 처리
            console.error("오류:", error);
        });
		});


	</script>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>

<script type="text/javascript">
function getValueOrNull(value) {
    return value !== undefined ? value : "null";
}
	 function StreamCctv(data, mapType, callback){

			  var cctvObj;
			      cctvObj = data.CCTVUTICDTO;
			  
			    this.gWidth = "320";
			    this.gHeight = "245";
			    this.gCctvId = getValueOrNull(cctvObj.CCTVID);
			    this.gCctvName = getValueOrNull(cctvObj.CCTVNAME);
			    this.gCenterName = getValueOrNull(cctvObj.CENTERNAME);
			    this.gDx = getValueOrNull(cctvObj.XCOORD);
			    this.gDy = getValueOrNull(cctvObj.YCOORD);
			    this.gLocate = getValueOrNull(cctvObj.LOCATE);
			    
			    if (cctvObj.CCTVID.substring(0, 3) === "L01") {
			        this.gCctvIp = cctvObj.CCTVID.slice(-3);
			    }else{this.gCctvIp = getValueOrNull(cctvObj.CCTVIP);}
			    
			    this.gPort = getValueOrNull(cctvObj.PORT);
			    this.gCh = getValueOrNull(cctvObj.CH);
			    this.gId = getValueOrNull(cctvObj.ID);
			    this.gPasswd = getValueOrNull(cctvObj.PASSWD);
			    this.gMovie = getValueOrNull(cctvObj.MOVIE);
				console.log(this.gCctvId);
				console.log(this);
			  if(this.gCctvId.substring(0, 3) == 'L01'){
			    this.gKind = 'Seoul';
			  } else if(this.gCctvId.substring(0, 3) == 'L02'){
			    this.gKind = 'N';
			  } else if(this.gCctvId.substring(0, 3) == 'L03'){
			    this.gKind = 'O';
			  } else if(this.gCctvId.substring(0, 3) == 'L04'){
			    this.gKind = 'P';
			  } else if(this.gCctvId.substring(0, 3) == 'L08'){
			    this.gKind = 'd';
			  } else{
			    this.gKind = cctvObj.KIND;
			  }
              if(callback){
                  callback(this);
                }
			 }
</script>
</body>
</html>