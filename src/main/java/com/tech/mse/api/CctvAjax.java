package com.tech.mse.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tech.mse.dto.AccInfoDto;
import com.tech.mse.dto.CctvDto;
import com.tech.mse.dto.CctvUticDto;
import com.tech.mse.forenum.AccMainCodeE;
import com.tech.mse.forenum.AccSubCodeE;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CctvAjax {
	static String accURL = "http://openapi.seoul.go.kr:8088";
	static String seoulKey = "546f564c636b7962313131574a784546";
	static String accURLwithKeyXML = "http://openapi.seoul.go.kr:8088/" + seoulKey + "/xml";

	static String cctvURL = "https://openapi.its.go.kr:9443/cctvInfo";
	static String itsKey = "7f0a52103752426f924c3f687099d167";

	
	static String transURL = "https://dapi.kakao.com/v2/local/geo/transcoord";
	static String restKey = "8044e14fa2240bb8b13af47f524ae9aa";
	static String auth = "KakaoAK " + restKey;
	public CctvAjax() {
		// TODO Auto-generated constructor stub
	}
	public String accDTOListToJson(List<CctvDto> accDTOList) {
	    // Gson 객체 생성
	    Gson gson = new Gson();
	    // List<accDTO>를 JSON 형식으로 직렬화하여 문자열로 변환
	    String json = gson.toJson(accDTOList);
	    return json;
	}
	// Haversine 공식을 사용하여 두 지점 사이의 거리를 계산하는 함수(km단위 + 기준은 wgs84의 지구 반지름)
	public double getDistanceInKilometerByHaversine(String wgs84x, String wgs84y, String cctvWgs84x,
			String cctvWgs84y) {
		// X 좌표값, 경위도인 경우 longitude(경도) 세로경 -> 세로로 그러진 선을 기준으로 표시된 것 => x값
		// 정확히는 그리니치 천문대를 기준으로 잘라 정반대편을 180도로 하여 각이 몇도인지표시(x)
		// =>본초 자오선으로부터 동쪽이나 서쪽으로의 거리
		// Y 좌표값, 경위도인 경우 latitude(위도) 가로위 -> 가로로 그러진 선을 기준으로 표시된 것 => y값
		// 정확히는 적도를 반으로갈라 북극과 남극을 90도로 하여 각이 몇도인지 표시됨(y)
		// =>적도로부터 북쪽이나 남쪽으로의 거리

		double distance; // 변환될 거리값 km
//	    double radius = 6378.137; // 지구 반지름(WGS84 기준, 단위: km)
		double radius = 6371; // 6371로 변경함 (구글맵과 동일) 지구 반지름(WGS84 기준, 단위: km)
		double toRadian = Math.PI / 180; // 각도에 이것을 곱하면 라디안으로 변환 [각도 *toRadian]

		double lng1x = Double.parseDouble(wgs84x);
		double lng2x = Double.parseDouble(cctvWgs84x);
		double lat1y = Double.parseDouble(wgs84y);
		double lat2y = Double.parseDouble(cctvWgs84y);

		double dLong = Math.abs(lng2x - lng1x) * toRadian; // 두 지점의 경도 차이를 라디안으로 변환
		double dLat = Math.abs(lat2y - lat1y) * toRadian; // 두 지점의 위도 차이를 라디안으로 변환

		// Haversine 공식을 사용하여 두 지점 사이의 거리를 계산
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(lat1y * toRadian) * Math.cos(lat2y * toRadian) * Math.sin(dLong / 2) * Math.sin(dLong / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		// 지구의 반지름을 곱하여 두 점 사이의 거리를 계산
		distance = radius * c;

		// 결과를 소수점 세 자리까지 반올림하여 반환
		return Math.round(distance * 1000.0) / 1000.0;
	}

//	public static void main(String[] args) {
//		String x1 = "126.997128";
//		String y1 = "37.547889";
//		String x2 = "129.043846";
//		String y2 = "35.158874";
//
//		double distance = getDistanceInKilometerByHaversine(x1, y1, x2, y2);
//		Map<String,String> MaxMinXYMap = getMaxMinXYMap(x1,y1);
//		System.out.println("java 거리 결과값 : " + distance);
//		System.out.println("구글맵의 거리재기로 잰 거리 : " + 322.72);
//		
//		StringBuilder urlbuilder = null;
//		CctvAjax tc = new CctvAjax();
//		try {
////			urlbuilder = getURLCctv(MaxMinXYMap);
//			urlbuilder = getURLCctvObject("L933095");
//			System.out.println("URL : "+ urlbuilder.toString());
//			tc.useOnlyDocSeoul(urlbuilder);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	public CctvUticDto getCctvUticDto(String cctvid) throws JsonParseException, JsonMappingException, IOException {

		CctvUticDto cctvDto = null;
		try {
            // URL 생성
            StringBuilder urlBuilder = getURLCctvObject(cctvid);

            // URL에 연결
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 응답 데이터 읽기
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                conn.disconnect();

                // JSON 데이터 출력
                System.out.println(response.toString());
                // Gson 객체 생성       
                Gson gson = new Gson();         
                // Json 문자열 -> Student 객체        
               cctvDto = gson.fromJson(response.toString(), CctvUticDto.class);
                System.out.println("cctvUticDto : " + cctvDto.toString());
        		System.out.println(cctvDto.getKIND() + " dfadsfasfas");
            } else {
                System.out.println("API 호출 실패: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return cctvDto;
    }

	public static void main(String[] args) {
//        String cctvid = "L933085";
        String cctvid = "L010194";

        try {
            // URL 생성
            StringBuilder urlBuilder = getURLCctvObject(cctvid);

            // URL에 연결
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 응답 데이터 읽기
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                conn.disconnect();

                // JSON 데이터 출력
                System.out.println(response.toString());
                // Gson 객체 생성       
                Gson gson = new Gson();         
                // Json 문자열 -> Student 객체        
                CctvUticDto cctvDto = gson.fromJson(response.toString(), CctvUticDto.class);
                System.out.println("cctvUticDto : " + cctvDto.toString());
        		System.out.println(cctvDto.getKIND() + " dfadsfasfas");
            } else {
                System.out.println("API 호출 실패: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


		// WTM에서 wgs84 변환 메소드
		public String[] useProj4j(String xWTMS,String yWTMS) {
			CRSFactory crsFactory = new CRSFactory();
			 // WGS84 CRS 생성
			 CoordinateReferenceSystem WGS84 = crsFactory.createFromParameters("WGS84",
			            "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs");
	        CoordinateReferenceSystem WTM = crsFactory.createFromParameters("WTM",
	        		"+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +units=m +no_defs");
			   // 좌표 변환
	        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
	        CoordinateTransform wtmToWgs84 = ctFactory.createTransform(WTM, WGS84);
	       
	        // WTM 좌표
	        double xWTM = Double.parseDouble(xWTMS);
	        double yWTM = Double.parseDouble(yWTMS);
			// `result` is an output parameter to `transform()`
			ProjCoordinate result = new ProjCoordinate();
			
			wtmToWgs84.transform(new ProjCoordinate(xWTM, yWTM), result);
			System.out.println("WTM 좌표(" + xWTM + ", " + yWTM + ")는 WGS84 좌표(" + result.x + ", " + result.y + ")로 변환됩니다.");
			String xAsString = String.valueOf(result.x);
			String yAsString = String.valueOf(result.y);
			String[] rWgs = {xAsString,yAsString} ;
			return rWgs;
		}
		
		//wgs84에서 WTM 변환 메소드
		public String[] useProj4j2(String xWGS84s,String yWGS84s) {
			CRSFactory crsFactory = new CRSFactory();
			 // WGS84 CRS 생성
			 CoordinateReferenceSystem WGS84 = crsFactory.createFromParameters("WGS84",
			            "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs");
	        CoordinateReferenceSystem WTM = crsFactory.createFromParameters("WTM",
	        		"+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +units=m +no_defs");
			   // 좌표 변환
	        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
	        CoordinateTransform Wgs84Towtm = ctFactory.createTransform(WGS84,WTM);
	       
	        // WGS84 좌표
	        double xWGS84 = Double.parseDouble(xWGS84s);
	        double yWGS84 = Double.parseDouble(yWGS84s);
			// `result` is an output parameter to `transform()`
			ProjCoordinate result = new ProjCoordinate();
			
			Wgs84Towtm.transform(new ProjCoordinate(xWGS84, yWGS84), result);
			System.out.println("WGS84 좌표(" + xWGS84 + ", " + yWGS84 + ")는 WTM 좌표(" + result.x + ", " + result.y + ")로 변환됩니다.");
			String xAsString = String.valueOf(result.x);
			String yAsString = String.valueOf(result.y);
			String[] rWgs = {xAsString,yAsString} ;
			return rWgs;
		}

		public static StringBuilder getURLCctvObject(String cctvid) throws IOException {
			StringBuilder urlBuilder = new StringBuilder("http://www.utic.go.kr/map/getCctvInfoById.do?cctvId=");
			// api 호출 내용 AccInfo
			urlBuilder.append(URLEncoder.encode(cctvid, "UTF-8")); /* cctvid */
			return urlBuilder;
		}
		public static StringBuilder getURLCctv(Map<String,String> maxMinXY) throws IOException {
			StringBuilder urlBuilder = new StringBuilder(cctvURL);
			// api 호출 내용 AccInfo
			urlBuilder.append(
					"?" + URLEncoder.encode("apiKey", "UTF-8") + "=" + URLEncoder.encode(itsKey, "UTF-8")); /* 공개키 */
			urlBuilder.append(
					"&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("all", "UTF-8")); /* 도로유형 */
			urlBuilder.append(
					"&" + URLEncoder.encode("cctvType", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* CCTV유형 */
			
			urlBuilder.append("&" + URLEncoder.encode("minX", "UTF-8") + "="
					+ URLEncoder.encode(maxMinXY.get("minX"), "UTF-8")); /* 최소경도영역 */
			
			urlBuilder.append("&" + URLEncoder.encode("maxX", "UTF-8") + "="
					+ URLEncoder.encode(maxMinXY.get("maxX"), "UTF-8")); /* 최대경도영역 */
			
			urlBuilder.append("&" + URLEncoder.encode("minY", "UTF-8") + "="
					+ URLEncoder.encode(maxMinXY.get("minY"), "UTF-8")); /* 최소위도영역 */
			
			urlBuilder.append("&" + URLEncoder.encode("maxY", "UTF-8") + "="
					+ URLEncoder.encode(maxMinXY.get("maxY"), "UTF-8")); /* 최대위도영역 */
			
			urlBuilder.append(
					"&" + URLEncoder.encode("getType", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8")); /* 출력타입 */
			
			return urlBuilder;
		}
		//String 값들은 double로 바꾸어 map으로 생성하는 함수
		public Map<String, Double> transMap(String maxY, String minY, String maxX, String minX) {
			Map<String, Double> map = new HashMap<String, Double>();
			Double rmaxY = Double.parseDouble(maxY);
			Double rminY = Double.parseDouble(minY);
			Double rmaxX = Double.parseDouble(maxX);
			Double rminX = Double.parseDouble(minX);
			map.put("maxY", rmaxY);
			map.put("minY", rminY);
			map.put("maxX", rmaxX);
			map.put("minX", rminX);
			return map;
		}
		//기준좌표와 특정거리 m 에 따른 max min X Y 좌표를 담는 Map을 반환하는 함수
		public Map<String, Double> getMaxMinXYMap(String targetxWGS84,String targetyWGS84) {
			double aroundDistance = 4000.0;
			
//		    double radius = 6378.137; // 지구 반지름(WGS84 기준, 단위: km)
			double radius = 6371; // 6371로 변경함 (구글맵과 동일) 지구 반지름(WGS84 기준, 단위: km)
			double toRadian = Math.PI / 180; // 각도에 이것을 곱하면 라디안으로 변환 [각도 *toRadian]
			
			//반환될 map
			Map<String, Double> maxMinXYMap = new HashMap<String, Double>();
			
			//현재 위도 좌표 (y좌표)
			double nowLatitude = Double.parseDouble(targetyWGS84);
			//현재 경도 좌표 (x 좌표)
			double nowLongitude = Double.parseDouble(targetxWGS84);
			  
			  //m당 y 좌표 이동 값
			  double mForLatitude =(1 /(radius* 1 * toRadian))/ 1000;
			  //m당 x 좌표 이동 값
			  double mForLongitude =(1 /(radius* 1 * toRadian * Math.cos(Math.toRadians(nowLatitude))))/ 1000;
			  System.out.println("위도 y값 곱셈 : " + mForLatitude);
			  System.out.println("경도 x값 곱셈 : " + mForLongitude);
			  System.out.println(aroundDistance+"m와 위도 y값 곱셈값 : " + mForLongitude);
			  System.out.println(aroundDistance+"m와 경도 x값 곱셈값: " + mForLongitude);
			  
			  //현재 위치 기준 검색 거리 좌표
			  double maxY = nowLatitude + (aroundDistance* mForLatitude);
			  double minY = nowLatitude - (aroundDistance* mForLatitude);
			  double maxX = nowLongitude + (aroundDistance* mForLongitude);
			  double minX = nowLongitude - (aroundDistance* mForLongitude);
			  
			  //소숫점6자리 까지 반올림하여 표시
			  double rmaxY = Math.round(maxY * 1000000.0) / 1000000.0;
			  double rminY = Math.round(minY * 1000000.0) / 1000000.0;
			  double rmaxX = Math.round(maxX * 1000000.0) / 1000000.0;
			  double rminX = Math.round(minX * 1000000.0) / 1000000.0;
			  
			  System.out.println(rmaxY);
			  System.out.println(rminY);
			  System.out.println(rmaxX);
			  System.out.println(rminX);
			  
//			  maxMinXYMap.put("maxY", Double.toString(rmaxY));
//			  maxMinXYMap.put("minY", Double.toString(rminY));
//			  maxMinXYMap.put("maxX", Double.toString(rmaxX));
//			  maxMinXYMap.put("minX", Double.toString(rminX));
			  
			  maxMinXYMap.put("maxY", rmaxY);
			  maxMinXYMap.put("minY", rminY);
			  maxMinXYMap.put("maxX", rmaxX);
			  maxMinXYMap.put("minX", rminX);
			  
			  
			return maxMinXYMap;

		}

		public List<AccInfoDto> useOnlyDocSeoul(StringBuilder urlbuilder) throws SAXException, IOException, ParserConfigurationException, TransformerException {
			
			URL url = new URL(urlbuilder.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "text/xml;charset=UTF-8");
			// conn.getResponseCode()실행과 동시에 conn.connect()가 이루어짐
			//호출하는 순간에는 HTTP 연결이 실제로 이루어짐. 아직 연결되어있지 않다면  이 메서드가 호출될 때 연결이 자동으로 이루어짐
			System.out.println("Response code: " + conn.getResponseCode());

			 InputStream is = null;
	         if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
	        	 is = conn.getInputStream();
	         }
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			//httpURLConnection으로 가져온 inputStream을 documnetbuilder로 parsing
			Document doc = builder.parse(is);
			//역할끝난 conn disconnect
			conn.disconnect();
			
			doc.getDocumentElement().normalize();
			System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
			

			// Document 객체를 문자열로 변환하여 출력
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));

//			//데이터 추출
//			NodeList nList = doc.getElementsByTagName("row");
//			NodeList totcountNode = doc.getElementsByTagName("list_total_count");
//			System.out.println("총데이터갯수"+totcountNode.item(0).getTextContent());
//
//			System.out.println("파싱할 리스트 수 : "+ nList.getLength()); 
//			System.out.println(((NodeList) nList.item(0)).getLength());
//			
//			 List<AccInfoDto> accDtoList = new ArrayList<AccInfoDto>();
//
//		        for (int i = 0; i < nList.getLength(); i++) {
//		            Node rowNode = nList.item(i);
//		            if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
//		                AccInfoDto accInfoDto = new AccInfoDto();
//
//		                NodeList childNodes = rowNode.getChildNodes();
//		                for (int j = 0; j < childNodes.getLength(); j++) {
//		                    Node childNode = childNodes.item(j);
//		                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
//		                        String nodeName = childNode.getNodeName();
//		                        String nodeValue = childNode.getTextContent();
//		                        // Set corresponding field in AccInfoDto
//		                        setAccInfoDtoField(accInfoDto, nodeName, nodeValue);
//		                    }
//		                }
//		                //proj4j로 변환
//		                String x = accInfoDto.getGrs80tm_x();
//		                String y = accInfoDto.getGrs80tm_y();
//		                String[] rWgs= useProj4j(x,y);
//		                accInfoDto.setGrs80tm_x(rWgs[0]);
//		                accInfoDto.setGrs80tm_y(rWgs[1]);
//		                
//		                // Add AccInfoDto object to the list
//		                accDtoList.add(accInfoDto);
//		            }
//		        }
//			System.out.println("dtoList 사이즈"+accDtoList.size());
			String xmlString = writer.toString();
			System.out.println("XML Content:\n" + xmlString);
			
//			return accDtoList;
			return null;

		}
		private static void setAccInfoDtoField(AccInfoDto accInfoDto, String nodeName, String nodeValue) {
			 if ("acc_id".equals(nodeName)) {
			        accInfoDto.setAcc_id(nodeValue);
			    } else if ("occr_date".equals(nodeName)) {
			        accInfoDto.setOccr_date(nodeValue);
			    } else if ("occr_time".equals(nodeName)) {
			        accInfoDto.setOccr_time(nodeValue);
			    } else if ("exp_clr_date".equals(nodeName)) {
			        accInfoDto.setExp_clr_date(nodeValue);
			    } else if ("exp_clr_time".equals(nodeName)) {
			        accInfoDto.setExp_clr_time(nodeValue);
			    } else if ("acc_type".equals(nodeName)) {
			    	//accmain 한글값
		            String fromMain = AccMainCodeE.fromCode(nodeValue).getDescription();
		            System.out.println("이넘에서 가져온 name : " + fromMain);
			        accInfoDto.setAcc_type(fromMain);
			    } else if ("acc_dtype".equals(nodeName)) {
			    	//accsub 한글값
		            String fromSub = AccSubCodeE.fromCode(nodeValue).getDescription();
		            System.out.println("이넘에서 가져온 fromSub : " + fromSub);
			        accInfoDto.setAcc_dtype(fromSub);
			    } else if ("link_id".equals(nodeName)) {
			        accInfoDto.setLink_id(nodeValue);
			    } else if ("grs80tm_x".equals(nodeName)) {
			        accInfoDto.setGrs80tm_x(nodeValue);
			    } else if ("grs80tm_y".equals(nodeName)) {
			        accInfoDto.setGrs80tm_y(nodeValue);
			    } else if ("acc_info".equals(nodeName)) {
			        accInfoDto.setAcc_info(nodeValue);
			    } else if ("acc_road_code".equals(nodeName)) {
			    	String convertedRoadCode = null;
			    	if(nodeValue.equals("009")) {
			    		convertedRoadCode = "전체통제";
			    	}else {
			    		convertedRoadCode = "부분통제";
			    	}
			        accInfoDto.setAcc_road_code(convertedRoadCode);
			    }
			}
}
