package com.tech.mse.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.tech.mse.dto.AccInfoDto;
import com.tech.mse.forenum.AccMainCodeE;
import com.tech.mse.forenum.AccSubCodeE;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TransCoordAjax {
	static String accURL = "http://openapi.seoul.go.kr:8088";
	static String seoulKey = "546f564c636b7962313131574a784546";
	static String accURLwithKeyXML = "http://openapi.seoul.go.kr:8088/"+seoulKey+"/xml";

	static String transURL = "https://dapi.kakao.com/v2/local/geo/transcoord";
	static String restKey = "8044e14fa2240bb8b13af47f524ae9aa";
	static String auth = "KakaoAK " + restKey;
	
	// List<accDTO>를 JSON으로 직렬화하는 함수
	public String accDTOListToJson(List<AccInfoDto> accDTOList) {
	    // Gson 객체 생성
	    Gson gson = new Gson();
	    // List<accDTO>를 JSON 형식으로 직렬화하여 문자열로 변환
	    String json = gson.toJson(accDTOList);
	    return json;
	}
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
	public static StringBuilder getURLAcc(String start, String end) throws IOException {
		//http://openapi.seoul.go.kr:8088/(인증키)/xml/AccInfo/1/5/
		StringBuilder urlBuilder = new StringBuilder(accURLwithKeyXML);
		// api 호출 내용 AccInfo
		urlBuilder.append("/" + URLEncoder.encode("AccInfo", "UTF-8"));
		// 페이징 시작번호
		urlBuilder.append("/" + URLEncoder.encode(start, "UTF-8"));
		// 페이징 끝번호
		urlBuilder.append("/" + URLEncoder.encode(end, "UTF-8"));

		return urlBuilder;
	}
	public static void main(String[] args) {
		StringBuilder urlbuilder = null;
		TransCoordAjax tc = new TransCoordAjax();
		try {
			urlbuilder = getURLAcc("1","50");
			System.out.println("URL : "+ urlbuilder.toString());
			tc.useOnlyDocSeoul(urlbuilder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<AccInfoDto> useOnlyDocSeoul(StringBuilder urlbuilder) throws SAXException, IOException, ParserConfigurationException, TransformerException {
		
		URL url = new URL(urlbuilder.toString());

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("GET");
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
		//데이터 추출
		NodeList nList = doc.getElementsByTagName("row");
		NodeList totcountNode = doc.getElementsByTagName("list_total_count");
		System.out.println("총데이터갯수"+totcountNode.item(0).getTextContent());

		System.out.println("파싱할 리스트 수 : "+ nList.getLength()); 
		System.out.println(((NodeList) nList.item(0)).getLength());
		
		 List<AccInfoDto> accDtoList = new ArrayList<AccInfoDto>();

	        for (int i = 0; i < nList.getLength(); i++) {
	            Node rowNode = nList.item(i);
	            if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
	                AccInfoDto accInfoDto = new AccInfoDto();

	                NodeList childNodes = rowNode.getChildNodes();
	                for (int j = 0; j < childNodes.getLength(); j++) {
	                    Node childNode = childNodes.item(j);
	                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
	                        String nodeName = childNode.getNodeName();
	                        String nodeValue = childNode.getTextContent();
	                        // Set corresponding field in AccInfoDto
	                        setAccInfoDtoField(accInfoDto, nodeName, nodeValue);
	                    }
	                }
	                //proj4j로 변환
	                String x = accInfoDto.getGrs80tm_x();
	                String y = accInfoDto.getGrs80tm_y();
	                String[] rWgs= useProj4j(x,y);
	                accInfoDto.setGrs80tm_x(rWgs[0]);
	                accInfoDto.setGrs80tm_y(rWgs[1]);
	                
	                // Add AccInfoDto object to the list
	                accDtoList.add(accInfoDto);
	            }
	        }
		System.out.println("dtoList 사이즈"+accDtoList.size());
		String xmlString = writer.toString();
		System.out.println("XML Content:\n" + xmlString);
		
		return accDtoList;

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
