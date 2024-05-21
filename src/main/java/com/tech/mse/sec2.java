package com.tech.mse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class sec2 {
	static String mykey = "bUvDmypNTc0P8VUwR7ME2GzCOmQUu%2FLpU6x0R4Ch5YMEy%2BNM7OnG1%2BRTLgA9JshVcFbISk%2F16UZ6gDyo0HWF4g%3D%3D";
	@Autowired
	private static SqlSession sqlSession;
    private static String API_KEY = "546f564c636b7962313131574a784546";
    private static String BASIC_URL = "http://openapi.seoul.go.kr:8088";

    
    public static StringBuilder SpotInfo() {
    	StringBuilder urlBuilder = null;
    	
    	try {
      	  urlBuilder = new StringBuilder(BASIC_URL); /*URL*/
            urlBuilder.append("/" + URLEncoder.encode(API_KEY, "UTF-8")); /*Service Key*/
            urlBuilder.append("/" + URLEncoder.encode("xml","UTF-8")); /*시군구 상위코드(시도코드) (입력 시 데이터 O, 미입력 시 데이터 X)*/
            urlBuilder.append("/" + URLEncoder.encode("SpotInfo", "UTF-8") + "/" + URLEncoder.encode("1", "UTF-8")); /*xml(기본값) 또는 json*/
            urlBuilder.append("/" + URLEncoder.encode("43", "UTF-8")); 
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb2 = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb2.append(line);
                sb2.append(System.lineSeparator()); // 줄바꿈 코드 추가
            }
            rd.close();
            conn.disconnect();
            System.out.println("결과값 : " + sb2);
          //읽어들인 StringBuiler를 스트링으로 -> 다시 바이트로 바꾸어 inputStream으로 (documentbuilder 파싱형식인 inputStream으로 맞추기위함)
			InputStream is =  new ByteArrayInputStream(sb2.toString().getBytes("UTF-8"));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document doc = builder.parse(is);
			
			doc.getDocumentElement().normalize();
//			System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
			//item 노드 가져와서 노드리스트
			NodeList nList = doc.getElementsByTagName("row");
			System.out.println("파싱할 리스트 수 : "+ nList.getLength()); 
			System.out.println(((NodeList) nList.item(0)).getLength());
    	}catch (UnsupportedEncodingException e) {
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
    	
		return urlBuilder;
    	
    }
    
	public static void main(String[] args) throws IOException {
			
//			StringBuilder sb1 = SpotInfo();
			
	        try {
	        	  StringBuilder urlBuilder = new StringBuilder(BASIC_URL); /*URL*/
	              urlBuilder.append("/" + URLEncoder.encode(API_KEY, "UTF-8")); /*Service Key*/
	              urlBuilder.append("/" + URLEncoder.encode("xml","UTF-8")); /*시군구 상위코드(시도코드) (입력 시 데이터 O, 미입력 시 데이터 X)*/
	              urlBuilder.append("/" + URLEncoder.encode("AccSubCode", "UTF-8") + "/" + URLEncoder.encode("1", "UTF-8")); /*xml(기본값) 또는 json*/
	              urlBuilder.append("/" + URLEncoder.encode("43", "UTF-8")); 
	              URL url = new URL(urlBuilder.toString());
	              HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	              conn.setRequestMethod("GET");
//	              conn.setRequestProperty("Content-type", "application/json");
	              System.out.println("Response code: " + conn.getResponseCode());
	              BufferedReader rd;
	              if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
	                  rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	              } else {
	                  rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	              }
	              StringBuilder sb = new StringBuilder();
	              String line;
	              while ((line = rd.readLine()) != null) {
	                  sb.append(line);
	                  sb.append(System.lineSeparator()); // 줄바꿈 코드 추가
	              }
	              rd.close();
	              conn.disconnect();
	              System.out.println("결과값 : " + sb);
//	              System.out.println("결과값 : adsfasdfasdf");
//	              System.out.println("결과값 :1221321312313 ");
	  			/*
	  			 * JSONParser jsonParser = new JSONParser(); JSONObject jsonObject =
	  			 * (JSONObject) jsonParser.parse(sb.toString()); JSONObject response =
	  			 * (JSONObject) jsonObject.get("response"); JSONObject header =
	  			 * (JSONObject)response.get("header"); JSONObject body =
	  			 * (JSONObject)response.get("body"); JSONObject items =
	  			 * (JSONObject)body.get("items"); JSONArray item = (JSONArray)items.get("item");
	  			 * 
	  			 * System.out.println("header" + "=========");
	  			 * System.out.println("reqNo : "+header.get("reqNo"));
	  			 * System.out.println("resultCode : "+header.get("resultCode"));
	  			 * System.out.println("resultMsg : "+header.get("resultMsg"));
	  			 * System.out.println("body" + "========="); for (int i = 0; i < item.size();
	  			 * i++){ System.out.println(i+"번째 : "+item.get(i)); }
	  			 */


	        	//읽어들인 StringBuiler를 스트링으로 -> 다시 바이트로 바꾸어 inputStream으로 (documentbuilder 파싱형식인 inputStream으로 맞추기위함)
				InputStream is =  new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				
				Document doc = builder.parse(is);
				
				doc.getDocumentElement().normalize();
				System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
				//item 노드 가져와서 노드리스트
				NodeList nList = doc.getElementsByTagName("row");
				System.out.println("파싱할 리스트 수 : "+ nList.getLength()); 
				System.out.println(((NodeList) nList.item(0)).getLength());
				System.out.println("row의 첫번째 자식 노드네임"+((NodeList) nList.item(0)).item(0).getNodeName());
				System.out.println("row의 두번째 자식 노드네임"+((NodeList) nList.item(0)).item(1).getNodeName());
//				System.out.println((Element) nList.item(0));
//				List<CategoryCodeDto> dtoList = new ArrayList<CategoryCodeDto>();
//				
				for (int i = 0; i < nList.getLength(); i++) {
					Element itemEle = (Element) nList.item(i);
					
//					CategoryCodeDto dto = new CategoryCodeDto();
					
					String cdnm = itemEle.getElementsByTagName("acc_dtype").item(0).getTextContent();
					String commcd = itemEle.getElementsByTagName("acc_dtype_nm").item(0).getTextContent();
					System.out.println(cdnm);
					System.out.println(commcd);
					
//					dto.setCdnm(cdnm);
//					dto.setCommcd(commcd);
//					
//					dtoList.add(dto);
				}
//				
////				CategoryCodeDao dao = sqlSession.getMapper(CategoryCodeDao.class);
////				
////				for (int i = 0; i < dtoList.size(); i++) {
////					String cdnm = dtoList.get(i).getCdnm();
////					String commcd = dtoList.get(i).getCommcd();
////					
////					dao.CategoryCodeWrite(cdnm, commcd);
////				}
////				
//				
//				Element ele = (Element)nList.item(0);
//				System.out.println("첫번째노드리스트객체: "+ele.getElementsByTagName("cdNm").item(0).getTextContent());
//				System.out.println("-----");
//				System.out.println(nList.item(0).getChildNodes().getLength());
//				
//				NodeList cList = nList.item(0).getChildNodes();
//				for (int i = 0; i < cList.getLength(); i++) {
//					System.out.println("자식노드이름: "+ cList.item(i).getNodeName());
//					System.out.println("자식노드 값: "+ cList.item(i).getTextContent());
//					
//				}
//				
//				//child 노드의 값
////				System.out.println(nList.item(0).getFirstChild().getTextContent());
//				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
////	        Element element = doc.getDocumentElement();
//	        
//	        
//	        
//	        

	    }
}
