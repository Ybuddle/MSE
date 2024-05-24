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
			<a class="navbar-brand" href="#">My SeoulEvent</a>
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
			<li class="nav-item" role="presentation"><a class="nav-link"
				data-bs-toggle="tab" href="#profile" aria-selected="false"
				tabindex="-1" role="tab">Profile</a></li>
			<li class="nav-item" role="presentation"><a
				class="nav-link disabled" href="#" aria-selected="false"
				tabindex="-1" role="tab">Disabled</a></li>
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
				<!--첫번째탭accContainer 끝-->
			</div>

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
  
	// 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
	var bounds = new kakao.maps.LatLngBounds();    
	function setBounds() {
	    // LatLngBounds 객체에 추가된 좌표들을 기준으로 지도의 범위를 재설정합니다
	    // 이때 지도의 중심좌표와 레벨이 변경될 수 있습니다
	    map.setBounds(bounds);
	}
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
			console.log(positions);
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
		}
		//accList 탭에 총 갯수 표시
		function addTotalAccList(accDtoList){
			let accTotalNum = accDtoList.length + '건';
			$('.badge.bg-primary').text(accTotalNum);
		}
		
		//마커에 대한 overlay contents 설정 함수
		function getMakerOverlayContents(marker,data){
			// HTML 컨텐츠를 백틱을 이용한 문자열 템플릿으로 생성
		    var contentHTML = `
		        <div id="containerWrapper">
		            <table class="table" id="poptable">
		                <thead>
		                    <tr>
		                        <th scope="col" colspan="2" style="position: relative;">
		                            [ 덕수궁길 - 집회및행사 ]
		                            <button type="button" id="popclose" class="btn btn-outline-secondary btn-sm" style="position: absolute; top: 5px; right: 10px;">Close</button>
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
		                        <th scope="row">도로</th>
		                        <td>[덕수궁길]덕수궁->정동제일교회</td>
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
		        console.log(overlay);
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
		        title: data.title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
		    });

		    
		    return marker;
		}
		//실행
		getdata();
    
		$(document).on("click","#popclose",function() {
			overlay.setMap(null);
		});
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

<script type="text/javascript">
/* 
$(document).ready(function(){
  cctv.loadCCTV();
});
 */
var popCctv;
function RotaCCTV2() {
  this.cctvList = [];
  this.layer;
  this.minlevel = 6;
  this.maxlevel = 8;
  this.getCctv = function(c) {
    var a = this.cctvList.length;
    for(var b = 0; b < a; b++) {
      if(this.cctvList[b].CCTVID == c){
        return this.cctvList[b];
      }
    }
    return null;
  };
  this.isLoad = function() {
    if (this.cctvList.length == 0) {
      return false;
    } else {
      return true;
    }
  };
  this.loadCCTV = function() {
    var a = "./../cctv.htm";
    a = "/map/mapcctv.do";
    var b = this;
    $.ajax({
      url : a,
      dataType : "json",
      async : false,
      success : function(c) {
        try{
          b.cctvList = c;
        }catch(e){
          
        }
      }
    });
  };
  this.getCCTVTooltipInfo = function(b) {
    var a = '<table border="0" cellspacing="0" cellpadding="0"><tr><td width="1" height="1"></td><td bgcolor="#1B4E99"></td><td width="1" height="1"></td></tr><tr><td bgcolor="#1B4E99"></td><td><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td bgcolor="#1B4E99"><table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td height="20"><table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td style="padding:3px 3px 3px 3px;color:#ffffff;font-weight:bold;font-size:11px;">'
        + b
        + '</td></tr></table></td></tr></table></td></tr></table></td><td bgcolor="#1B4E99"></td></tr><tr><td width="1" height="1"></td><td bgcolor="#1B4E99"></td><td width="1" height="1"></td></tr></table>';
    return a;
  };
  this.popupCctvImage = function(c) {
    var b = this.getCctv(c);
    if (b.LOCATE == null && b.MOVIE == "Y") {
      this.popupCctvStream(c);
      return;
    }
    var a = new StreamCctv(b);
    var d = Mando.wgs84ToMandoMap(a.gDx, a.gDy);
    cctvPopup.displayPopup(true, new UTISMap.LonLat(d.lon, d.lat), a.getImageHtml(), c);
    weblog(c, "cctv");
  };
  this.popupCctvStream = function(cctvid, key){
    if(popCctv) {
      //popCctv.close();
    }
    
    var streamCctv = new StreamCctv(cctvid, null, function(streamCctv){
      var width = 355;
      var height = 345;
      var top = (window.screen.height - height) / 2;
      var left = (window.screen.width - width) / 2;
      if(cctvid.indexOf('E60') != -1 || cctvid.indexOf('E62') != -1){
        streamCctv.gId = encodeURIComponent(streamCctv.gId);
      }
      popCctv = window.open('./../view/map/openDataCctvStream.jsp?key=' + key+ '&cctvid=' + cctvid+ '&cctvName=' + encodeURI(encodeURIComponent(streamCctv.gCctvName)) + '&kind=' + streamCctv.gKind + '&cctvip=' + streamCctv.gCctvIp + '&cctvch=' + streamCctv.gCh + '&id=' + streamCctv.gId + '&cctvpasswd=' + streamCctv.gPasswd + '&cctvport=' + streamCctv.gPort, 'PopupCctv',"top=" + top + "px, left=" + left + "px, width=" + width + "px, height=" + height+ "px, menubar=no, location=no, toolbar=no, scrollbars=no, status=no, resizable=no");
      popCctv.focus();
      weblog(cctvid, 'cctv');
    });
  };
  this.popupCctvUgo = function(cctvid){
    var width = 322;
    var height = 239;
    var top = (window.screen.height - height) / 2;
    var left = (window.screen.width - width) / 2;
    var features = 'width='+ width +',height='+ height +',top='+ top +',left='+ left +',toolbar=no,scrollbars=no,status=no,resizable=no,location=no';
    window.open('/view/map/cctvUgo.jsp', 'UTIC도시교통정보센터', features);
    weblog(cctvid, 'cctv');
  };
  this.popupLiveVideo = function(e, d) {
    var b = this.getCctv(e);
    var c = b.locate;
    var a = getCctvSteamHTML(c, b.cctvname);
    displayPopup(true, d, new UTISMap.Size(340, 298), new UTISMap.Size(0, -298 - IconType.IconCCTV.size.h), a);
  };
  this.getCctvFileName = function(b) {
    var c = b;
    if (c == undefined) {
      return "";
    }
    var a = c.lastIndexOf("/");
    if (a > -1) {
      return c.substring(a + 1, c.length);
    }
    return "";
  };
  this.getRealtimeCctvPath = function(b, c) {
    var e = networkCheck.getCctvPath();
    var a = "./../map/mapcctvinfo.do?cctvid=" + b.cctvid;
    var d = this;
    $.getJSON(a, function(j) {
      var k = null;
      if (j.length == 1) {
        k = j[0];
      }
      var h;
      if (j.length == 0 || k == null || k.locate == undefined) {
        h = "./../contents/images/map/cctv_no.gif";
      } else {
        var g = k.locate;
        var i = d.getCctvFileName(g);
        h = e + i;
      }
      var f = d.getCctvHTML(b, h, c);
      cctvPopup.displayPopup(true, c, f);
    });
  };
  this.playCCTV = function() {
    if (!confirm("해당 컨텐츠는 지속적으로 인터넷 패킷을 사용합니다.\n\n 3G 망에서 시청시 과도한 요금이 부가될 수 있습니다.\n\n동영상을 재생하시겠습니까?")) {
      return
    }
    document.getElementById("mov").play();
  };
  this.getCctvSteamHTML = function(c, e) {
    var d = c.locate;
    var b = '<video id="mov" width="500" height="224" src="' + d + '" onclick="playCCTV()"/>';
    var a = '<div class="video shad cctv"><p>'
        + c.cctvname
        + "<span><a onclick=\"javascript:cctv.popupCctv2('"
        + c.cctvid
        + "',"
        + e.lon
        + ","
        + e.lat
        + ',\'stop\');" class="pdr"><img src="../../contents/images/map/btn_stop_video.gif" alt="정지영상보기" /></a><a href="javascript:cctvPopup.hidePopup(\''
        + c.cctvid
        + '\');"><img src="../../contents/images/map/btn_cctv_clse.gif" alt="닫기" /></a></span></p><div class="cctv_area player">'
        + b + "</div></div>";
    return a;
  };
  this.getCctvHTML = function(c, b, d) {
    var a = '<div class="screenshop shad cctv" unselectable="on" style="-webkit-user-select: none;" ><p unselectable="on" style="-webkit-user-select: none;" >'
        + c.cctvname
        + "<span><a onclick=\"javascript:cctv.popupLiveVideo('"
        + c.cctvid
        + "',"
        + d.lon
        + ","
        + d.lat
        + ');" class="pdr"><img src="./../contents/images/map/btn_live_video.gif" alt="동영상보기" /></a><a onclick="javascript:cctvPopup.hidePopup(\''
        + c.cctvid
        + '\');"><img src="./../contents/images/map/btn_cctv_clse.gif" alt="닫기" /></a></span></p><div class="screenshot_area player"><img src="'
        + b
        + '" alt="" width="100%" height="100%" /></div></div>';
    return a;
  };
  this.popupCctv2 = function(b, e, d, a) {
    var c = Mando.wgs84ToMandoMap(e, d);
    this.popupCctv(b, new UTISMap.LonLat(c.lon, c.lat), a);
  };
  this.popupCctv = function(d, e, c) {
    var b = this.getCctv(d);
    var f = null;
    if (b.type == "02") {
      f = true;
    }
    if (!c && f) {
      var a = this.getCctvSteamHTML(b, e);
      cctvPopup.displayPopup(true, e, a);
    } else {
      this.getRealtimeCctvPath(b, e);
      return;
    }
  };
  this.openCctvIPhone = function(a) {
    var b = document.getElementById("mov");
    b.src = a;
  };
}
var cctv = new RotaCCTV2();
var params = getUrlParams();
var key = params.key;

function getUrlParams() {
    var params = {};
    window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(str, key, value) { params[key] = value; });
    return params;
} ;

function test(cctvId){
  cctv.popupCctvStream(cctvId, key, "cctvOpen");
}
/*---------------------------------  */
  function StreamCctv(cctvid, mapType, callback){
	  if(cctvid.CCTVID != null) {
		    cctvid = cctvid.CCTVID ;
		  }

		  var cctvObj;
		  
		  $.ajax({
		    url: 'http://www.utic.go.kr/map/getCctvInfoById.do?cctvId=' + cctvid,
		    async: false,
		    success: function(data) {
		      cctvObj = JSON.parse(data);
		    }
		  });
		  
		  this.gWidth      = "320";
		  this.gHeight     = "245";
		  this.gCctvId     = cctvObj.CCTVID;
		  this.gCctvName   = cctvObj.CCTVNAME;
		  this.gCenterName = cctvObj.CENTERNAME;
		  this.gDx         = cctvObj.XCOORD;
		  this.gDy         = cctvObj.YCOORD;
		  this.gLocate     = cctvObj.LOCATE;
		  this.gCctvIp     = cctvObj.CCTVIP;
		  this.gPort       = cctvObj.PORT;
		  this.gCh         = cctvObj.CH;
		  this.gId         = cctvObj.ID;
		  this.gPasswd     = cctvObj.PASSWD;
		  this.gMovie      = cctvObj.MOVIE;

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
		 }
		 var test123 = new StreamCctv('L933082',null,function(test123){
			 console.log(test123);
			 console.log(test123.gKind);
		 
		 });
		/*  http://www.utic.go.kr/view/map/openDataCctvStream.jsp?key=qokfUPkT7O2DIrbm6j0JaWLWRfx1bjQYbEbegfHIhKCj8jdTPwhmvJ18JoxScyLYrJg7fbg5s7mGkXmqwcgg&cctvid=L010141&cctvName=%25EC%2584%259C%25EC%259A%25B8%25EC%2584%25B8%25EA%25B4%2580%25EC%2582%25AC%25EA%25B1%25B0%25EB%25A6%25AC&kind=Seoul&cctvip=null&cctvch=52&id=185&cctvpasswd=null&cctvport=null
			 http://www.utic.go.kr/view/map/openDataCctvStream.jsp?key=qokfUPkT7O2DIrbm6j0JaWLWRfx1bjQYbEbegfHIhKCj8jdTPwhmvJ18JoxScyLYrJg7fbg5s7mGkXmqwcgg&cctvid=L010404&cctvName=%25EC%2584%25B1%25EC%258B%25A0%25EC%2597%25AC%25EB%258C%2580%25EC%259E%2585%25EA%25B5%25AC&kind=Seoul&cctvip=null&cctvch=52&id=137&cctvpasswd=null&cctvport=null
				 http://www.utic.go.kr/view/map/openDataCctvStream.jsp?key=qokfUPkT7O2DIrbm6j0JaWLWRfx1bjQYbEbegfHIhKCj8jdTPwhmvJ18JoxScyLYrJg7fbg5s7mGkXmqwcgg&cctvid=L010156&cctvName=%25EC%2584%25B1%25EC%2588%2598%25EB%258C%2580%25EA%25B5%2590%25EB%2582%25A8%25EB%258B%25A8&kind=Seoul&cctvip=null&cctvch=52&id=182&cctvpasswd=null&cctvport=null */
</script>
</body>
</html>