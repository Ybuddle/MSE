package com.tech.mse.api;

import java.io.IOException;
import java.io.InputStream;
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

import org.apache.ibatis.session.SqlSession;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.tech.mse.dao.CodeDao;
import com.tech.mse.dto.RedCodeNameDto;

public class AccSubCode {
	static String accURL = "http://openapi.seoul.go.kr:8088";
	static String seoulKey = "546f564c636b7962313131574a784546";
	static String accURLwithKeyXML = "http://openapi.seoul.go.kr:8088/"+seoulKey+"/xml";

	static String transURL = "https://dapi.kakao.com/v2/local/geo/transcoord";
	static String restKey = "8044e14fa2240bb8b13af47f524ae9aa";
	static String auth = "KakaoAK " + restKey;

	public static StringBuilder getURLAcc(String start, String end) throws IOException {
		//	서울시 돌발 유형 코드 정보
		//http://openapi.seoul.go.kr:8088/(인증키)/xml/AccMainCode/1/5/
		StringBuilder urlBuilder = new StringBuilder(accURLwithKeyXML);
		// api 호출 내용 AccInfo
		urlBuilder.append("/" + URLEncoder.encode("AccSubCode", "UTF-8"));
		// 페이징 시작번호
		urlBuilder.append("/" + URLEncoder.encode(start, "UTF-8"));
		// 페이징 끝번호
		urlBuilder.append("/" + URLEncoder.encode(end, "UTF-8"));

		return urlBuilder;
	}
public List<Map<String, String>> useOnlyDocSeoul(StringBuilder urlbuilder) throws SAXException, IOException, ParserConfigurationException, TransformerException {
		
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
		


		
		NodeList nList = doc.getElementsByTagName("row");
		NodeList totcountNode = doc.getElementsByTagName("list_total_count");
		System.out.println("총데이터갯수"+totcountNode.item(0).getTextContent());

		System.out.println("파싱할 리스트 수 : "+ nList.getLength()); 
		System.out.println(((NodeList) nList.item(0)).getLength());
		

		List<Map<String, String>> accSubList = new ArrayList<>();


	        for (int i = 0; i < nList.getLength(); i++) {
	            Node rowNode = nList.item(i);
	            if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
	            	//row 한개마다 map인스턴스
	            	Map<String, String> accMain = new HashMap<>();
	            	//row안의 childNodes 리스트 
	                NodeList childNodes = rowNode.getChildNodes();
	                for (int j = 0; j < childNodes.getLength(); j++) {
	                    Node childNode = childNodes.item(j);
	                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
	                        String nodeName = childNode.getNodeName();
	                        String nodeValue = childNode.getTextContent();
	                        accMain.put(nodeName,nodeValue);
	                    }
	                }
	                // Add object to the list
	                accSubList.add(accMain);
	            }
	        }
	        	        
		System.out.println("dtoList 사이즈"+accSubList.size());
		
		// Document 객체를 문자열로 변환하여 출력
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String xmlString = writer.toString();
		System.out.println("XML Content:\n" + xmlString);
		
		
		return accSubList;

	}
public void insertDB(List<Map<String, String>> rDtolist,SqlSession sqlSession) {
	if (sqlSession == null) {
	    throw new IllegalStateException("sqlSession is null. Failed to autowire sqlSession.");
	}
	CodeDao dao=sqlSession.getMapper(CodeDao.class);
////	dao.insertmember(rDtolist);
//	//500개씩 잘라서 넣기
//	
	List<Map<String, String>> slist=new ArrayList<Map<String, String>>();
      for (int i = 0; i < rDtolist.size(); i++) {
         slist.add(rDtolist.get(i));   
         System.out.println(i+" : "+(rDtolist.size()-1));
         if(i%500==0 || i==rDtolist.size()-1) {
            int a = dao.insertAccSub(slist);         
            slist.clear();
            System.out.println("인써트 갯수 : "+ a);
         }
      }
	
}
public static void main(String[] args) {
	StringBuilder urlbuilder = null;
	AccMainCode tc = new AccMainCode();
	try {
		urlbuilder = getURLAcc("1","50");
		System.out.println("URL : "+ urlbuilder.toString());
//		List<RedCodeNameDto> rDtolist = tc.useOnlyDocSeoul(urlbuilder);
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
}
