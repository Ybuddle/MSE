package com.tech.mse.api;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
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

import com.tech.mse.dto.AccInfoDto;
import com.tech.mse.forenum.AccMainCodeE;
import com.tech.mse.forenum.AccSubCodeE;

public class TransCoord {
	static String accURL = "http://openapi.seoul.go.kr:8088";
	static String seoulKey = "546f564c636b7962313131574a784546";
	static String accURLwithKeyXML = "http://openapi.seoul.go.kr:8088/"+seoulKey+"/xml";

	static String transURL = "https://dapi.kakao.com/v2/local/geo/transcoord";
	static String restKey = "8044e14fa2240bb8b13af47f524ae9aa";
	static String auth = "KakaoAK " + restKey;
	
	public static StringBuilder getURL(String xlong, String ylat) throws IOException {
//		curl -v -G GET "https://dapi.kakao.com/v2/local/geo/transcoord.json?x=160710.37729270622&y=-4388.879299157299&input_coord=WTM&output_coord=WGS84" \
//		  -H "Authorization: KakaoAK ${REST_API_KEY}"

		StringBuilder urlBuilder = new StringBuilder(transURL);
		// jason or xml으로 요청형식 선택
		urlBuilder.append("." + URLEncoder.encode("xml", "UTF-8"));
		// X 좌표값, 경위도인 경우 longitude(경도)
		urlBuilder.append("?x=" + URLEncoder.encode(xlong, "UTF-8"));
		// Y 좌표값, 경위도인 경우 latitude(위도)
		urlBuilder.append("&y=" + URLEncoder.encode(ylat, "UTF-8"));
		// input_coord = x, y 값의 좌표계 지원 좌표계: WGS84, WCONGNAMUL, CONGNAMUL, WTM, TM, KTM,
			// UTM, BESSEL, WKTM, WUTM
			// (기본값: WGS84)
		urlBuilder.append("&input_coord=" + URLEncoder.encode("WTM", "UTF-8"));
		// output_coord 변환할 좌표계
			// 지원 좌표계:WGS84, WCONGNAMUL, CONGNAMUL, WTM, TM, KTM, UTM, BESSEL, WKTM, WUTM
			// (기본값: WGS84)
		urlBuilder.append("&output_coord=" + URLEncoder.encode("WGS84", "UTF-8"));

		return urlBuilder;
	}
	
	public void useHttp(StringBuilder urlbuilder) {
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(urlbuilder.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", auth);
			// conn.getResponseCode()실행과 동시에 conn.connect()가 이루어짐
			//호출하는 순간에는 HTTP 연결이 실제로 이루어짐. 아직 연결되어있지 않다면  이 메서드가 호출될 때 연결이 자동으로 이루어짐
			System.out.println("Response code: " + conn.getResponseCode());

			 BufferedReader rd;
			 Charset charset = Charset.forName("UTF-8");
             if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                 rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),charset));
             } else {
                 rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
             }
             
             String line;
             
             while ((line = rd.readLine()) != null) {
                 sb.append(line);
                 sb.append(System.lineSeparator()); // 줄바꿈 코드 추가
             }
             rd.close();
			
			conn.disconnect();
			System.out.println("결과값 : " + sb);
			
        	//읽어들인 StringBuiler를 스트링으로 -> 다시 바이트로 바꾸어 inputStream으로 (documentbuilder 파싱형식인 inputStream으로 맞추기위함)
			InputStream is =  new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document doc = builder.parse(is);
			
			doc.getDocumentElement().normalize();
			System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
			System.out.println("doc type: " + doc.getBaseURI());
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void useOnlyDoc(StringBuilder urlbuilder) throws SAXException, IOException, ParserConfigurationException, TransformerException {
		StringBuilder sb = new StringBuilder();
		
		URL url = new URL(urlbuilder.toString());

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Authorization", auth);
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

		String xmlString = writer.toString();
		System.out.println("XML Content:\n" + xmlString);
		
		
		//x.y 값 출력
		// Get the NodeList containing <x> and <y> elements
		NodeList nodeListX = doc.getElementsByTagName("x");
		NodeList nodeListY = doc.getElementsByTagName("y");

		// Output the values of <x> and <y> elements
		if (nodeListX.getLength() > 0 && nodeListY.getLength() > 0) {
		    String xValue = nodeListX.item(0).getTextContent();
		    String yValue = nodeListY.item(0).getTextContent();
		    System.out.println("x: " + xValue);
		    System.out.println("y: " + yValue);
		} else {
		    System.out.println("No x or y element found.");
		}
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
	
//	public static void main(String[] args) {
//		StringBuilder urlbuilder = null;
//		TransCoord tc = new TransCoord();
//		try {
//			urlbuilder = getURL("203134.4292753824", "451319.7952425136");
//			System.out.println("URL : "+ urlbuilder.toString());
//			tc.useOnlyDoc(urlbuilder);
//			tc.useProj4j("203134.4292753824", "451319.7952425136");
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
	public static void main(String[] args) {
		StringBuilder urlbuilder = null;
		TransCoord tc = new TransCoord();
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
					/*
					 * //accmain 한글값 String acc_type = accInfoDto.getAcc_type(); String fromMain =
					 * AccMainCodeE.fromCode(acc_type).getDescription();
					 * System.out.println("이넘에서 가져온 name : " + fromMain);
					 * accInfoDto.setAcc_type(fromMain); //accsub 한글값 String acc_dtype =
					 * accInfoDto.getAcc_dtype(); String fromSub =
					 * AccSubCodeE.fromCode(acc_dtype).getDescription();
					 * System.out.println("이넘에서 가져온 fromSub : " + fromSub);
					 * accInfoDto.setAcc_type(fromSub);
					 */
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
