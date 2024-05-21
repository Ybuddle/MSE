package com.tech.mse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Seoulacci {
    private static String API_KEY = "546f564c636b7962313131574a784546";
    private static String BASIC_URL = "http://openapi.seoul.go.kr:8088";

    private StringBuilder urlBuilder;

    /*시도 조회
    * 동물보호관리시스템의 유기동물조회 조회조건의 '시도'조건을 가져올 수 있다.
    * call back url:http://apis.data.go.kr/1543061/abandonmentPublicSrvc/sido
    * 최대사이즈 : [4000] byte
    * 평균응답시간 :  [500] ms
    * 초당 최대 트랙잭션 : [30] tps */
    public static ArrayList<String> findsido(){
        ArrayList<String> sidoList = new ArrayList<String>();
        try {

            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/sido"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + API_KEY); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("500", "UTF-8")); /*한 페이지 결과 수(1,000 이하)*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml(기본값) 또는 json*/
            System.out.println("urlBuilder: " +urlBuilder);
            URL url = new URL(urlBuilder.toString());
            //http://apis.data.go.kr/1543061/abandonmentPublicSrvc/sido?serviceKey=hm0qJL%2FrZFagiGGwrhjAQ6KMXI2DOUcHs5uVRpIGSk0k1yvSZiQDcIr%2BzGHVVbw1Cs0n2wAIZNlzXcaeJbukmQ%3D%3D&numOfRows=500&pageNo=1&_type=json
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
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
            }
            rd.close();
            conn.disconnect();
//            System.out.println("결과값 : " + sb);
//            System.out.println("결과값 : adsfasdfasdf");
//            System.out.println("결과값 :1221321312313 ");
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject header = (JSONObject)response.get("header");
            JSONObject body = (JSONObject)response.get("body");
            JSONObject items = (JSONObject)body.get("items");
            JSONArray item = (JSONArray)items.get("item");

            System.out.println("header" + "=========");
            System.out.println("reqNo : "+header.get("reqNo"));
            System.out.println("resultCode : "+header.get("resultCode"));
            System.out.println("resultMsg : "+header.get("resultMsg"));
            System.out.println("body" + "=========");
            for (int i = 0; i < item.size(); i++){
                System.out.println(i+"번째 : "+item.get(i));
                JSONObject sido =  (JSONObject) item.get(i);
                String orgCd = (sido.get("orgCd")).toString();

                sidoList.add(orgCd);
            }

            System.out.println("numOfRows : "+body.get("numOfRows"));
            System.out.println("pageNo : "+body.get("pageNo"));
            System.out.println("totalCount : "+body.get("totalCount"));

        }catch (Exception e){
            e.printStackTrace();
        }
        return sidoList;
    }



    /*시군구 조회
     * 동물보호관리시스템의 유기동물조회 조회조건의 '시군구'조건을 가져올 수 있다.
     * call back url:http://apis.data.go.kr/1543061/abandonmentPublicSrvc/sigungu
     * 최대사이즈 : [4000] byte
     * 평균응답시간 :  [500] ms
     * 초당 최대 트랙잭션 : [30] tps */

    public static void findsigungu(String uprCd){
        try {
            StringBuilder urlBuilder = new StringBuilder(BASIC_URL); /*URL*/
            urlBuilder.append("/" + URLEncoder.encode(API_KEY, "UTF-8")); /*Service Key*/
            urlBuilder.append("/" + URLEncoder.encode("xml","UTF-8")); /*시군구 상위코드(시도코드) (입력 시 데이터 O, 미입력 시 데이터 X)*/
            urlBuilder.append("/" + URLEncoder.encode("AccSubCode", "UTF-8") + "/" + URLEncoder.encode("1", "UTF-8")); /*xml(기본값) 또는 json*/
            urlBuilder.append("/" + URLEncoder.encode("5", "UTF-8")); 
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
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println("결과값 : " + sb);
//            System.out.println("결과값 : adsfasdfasdf");
//            System.out.println("결과값 :1221321312313 ");
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


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*보호소 조회
     * 동물보호관리시스템의 유기동물조회 조회조건의 '보호소'조건을 가져올 수 있다.
     * call back url:http://apis.data.go.kr/1543061/abandonmentPublicSrvc/shelter
     * 최대사이즈 : [4000] byte
     * 평균응답시간 :  [500] ms
     * 초당 최대 트랙잭션 : [30] tps */
    public static void shelter(String uprCd, String org_cd){
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/shelter"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + API_KEY); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("upr_cd","UTF-8") + "=" + URLEncoder.encode(uprCd, "UTF-8")); /*시도코드(입력 시 데이터 O, 미입력 시 데이터 X)*/
            urlBuilder.append("&" + URLEncoder.encode("org_cd","UTF-8") + "=" + URLEncoder.encode(org_cd, "UTF-8")); /*시군구코드(입력 시 데이터 O, 미입력 시 데이터 X)*/
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml(기본값) 또는 json*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
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
            }
            rd.close();
            conn.disconnect();
            System.out.println("결과값 : " + sb);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject header = (JSONObject)response.get("header");
            JSONObject body = (JSONObject)response.get("body");
            JSONObject items = (JSONObject)body.get("items");
            JSONArray item = (JSONArray)items.get("item");

            System.out.println("header" + "=========");
            System.out.println("reqNo : "+header.get("reqNo"));
            System.out.println("resultCode : "+header.get("resultCode"));
            System.out.println("resultMsg : "+header.get("resultMsg"));
            System.out.println("body" + "=========");
            for (int i = 0; i < item.size(); i++){
                System.out.println(i+"번째 : "+item.get(i));
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*품종 조회
     * 동물보호관리시스템의 유기동물조회 조회조건의 '품종'조건을 가져올 수 있다.
     * call back url:http://apis.data.go.kr/1543061/abandonmentPublicSrvc/kind
     * 최대사이즈 : [4000] byte
     * 평균응답시간 :  [500] ms
     * 초당 최대 트랙잭션 : [30] tps */

    public static void kind(String up_kind_cd){
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/kind"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + API_KEY); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("up_kind_cd","UTF-8") + "=" + URLEncoder.encode(up_kind_cd, "UTF-8")); /*축종코드 (개 : 417000, 고양이 : 422400, 기타 : 429900)*/
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml(기본값) 또는 json*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
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
            }
            rd.close();
            conn.disconnect();
            System.out.println("결과값 : " + sb);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject header = (JSONObject)response.get("header");
            JSONObject body = (JSONObject)response.get("body");
            JSONObject items = (JSONObject)body.get("items");
            JSONArray item = (JSONArray)items.get("item");

            System.out.println("header" + "=========");
            System.out.println("reqNo : "+header.get("reqNo"));
            System.out.println("resultCode : "+header.get("resultCode"));
            System.out.println("resultMsg : "+header.get("resultMsg"));
            System.out.println("body" + "=========");
            for (int i = 0; i < item.size(); i++){
                System.out.println(i+"번째 : "+item.get(i));
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*유기동물 조회
     * 동물보호관리시스템의 유기동물 정보를 조회한다
     * call back url:http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic
     * 최대사이즈 : [4000] byte
     * 평균응답시간 :  [500] ms
     * 초당 최대 트랙잭션 : [30] tps */


    public static void abandonmentPublic(String start,String end){
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + API_KEY); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("bgnde","UTF-8") + "=" + URLEncoder.encode(start, "UTF-8")); /*유기날짜(검색 시작일) (YYYYMMDD)*/
            urlBuilder.append("&" + URLEncoder.encode("endde","UTF-8") + "=" + URLEncoder.encode(end, "UTF-8")); /*유기날짜(검색 종료일) (YYYYMMDD)*/
//            urlBuilder.append("&" + URLEncoder.encode("upkind","UTF-8") + "=" + URLEncoder.encode(" ", "UTF-8")); /*축종코드 (개 : 417000, 고양이 : 422400, 기타 : 429900)*/
//            urlBuilder.append("&" + URLEncoder.encode("kind","UTF-8") + "=" + URLEncoder.encode(" ", "UTF-8")); /*품종코드 (품종 조회 OPEN API 참조)*/
//            urlBuilder.append("&" + URLEncoder.encode("upr_cd","UTF-8") + "=" + URLEncoder.encode(" ", "UTF-8")); /*시도코드 (시도 조회 OPEN API 참조)*/
//            urlBuilder.append("&" + URLEncoder.encode("org_cd","UTF-8") + "=" + URLEncoder.encode(" ", "UTF-8")); /*시군구코드 (시군구 조회 OPEN API 참조)*/
//            urlBuilder.append("&" + URLEncoder.encode("care_reg_no","UTF-8") + "=" + URLEncoder.encode(" ", "UTF-8")); /*보호소번호 (보호소 조회 OPEN API 참조)*/
//            urlBuilder.append("&" + URLEncoder.encode("state","UTF-8") + "=" + URLEncoder.encode(" ", "UTF-8")); /*상태(전체 : null(빈값), 공고중 : notice, 보호중 : protect)*/
//            urlBuilder.append("&" + URLEncoder.encode("neuter_yn","UTF-8") + "=" + URLEncoder.encode(" ", "UTF-8")); /*상태 (전체 : null(빈값), 예 : Y, 아니오 : N, 미상 : U)*/
//            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호 (기본값 : 1)*/
//            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("20", "UTF-8")); /*페이지당 보여줄 개수 (1,000 이하), 기본값 : 10*/

            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml(기본값) 또는 json*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
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
            }
            rd.close();
            conn.disconnect();
            System.out.println("결과값 : " + sb);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject header = (JSONObject)response.get("header");
            JSONObject body = (JSONObject)response.get("body");
            JSONObject items = (JSONObject)body.get("items");
            JSONArray item = (JSONArray)items.get("item");

            System.out.println("header" + "=========");
            System.out.println("reqNo : "+header.get("reqNo"));
            System.out.println("resultCode : "+header.get("resultCode"));
            System.out.println("resultMsg : "+header.get("resultMsg"));
            System.out.println("body" + "=========");
            for (int i = 0; i < item.size(); i++){
                System.out.println(i+"번째 : "+item.get(i));
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

