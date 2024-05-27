package com.tech.mse.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.ui.Model;

import com.tech.mse.api.CctvAjax;
import com.tech.mse.dao.CodeDao;
import com.tech.mse.dto.CctvDto;
import com.tech.mse.dto.CctvUticDto;

public class CctvService {
	
	private CctvAjax cctvajax = new CctvAjax();
	
	public String createCCtvObject(Model model) {
		Map<String, Object> modelMap = model.asMap();
		String lat = (String) modelMap.get("lat");
		String lng = (String) modelMap.get("lng");
		SqlSession sqlSession = (SqlSession) modelMap.get("sqlSession");
		System.out.println(lat +":"+ lng );
		
		//minmaxXY 갖는 map
		Map<String, Double> minMaxXYMap = null;
		try {
			minMaxXYMap = cctvajax.getMaxMinXYMap(lng, lat);
			System.out.println(minMaxXYMap.get("maxY"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//minmaxXY로 해당 범위 cctv 리스트가져오기
		List<CctvDto> cctvDtoList = null;
		CodeDao codeDao = sqlSession.getMapper(CodeDao.class);
		cctvDtoList = codeDao.findCctv(minMaxXYMap);
		System.out.println("검색된 cctvlist 길이 : " + cctvDtoList.size());
		

        // 현재 위치와 CCTV 위치 간의 거리를 계산하여 리스트에 저장
        for (CctvDto cctvDto : cctvDtoList) {
            double distance = cctvajax.getDistanceInKilometerByHaversine(lat, lng, cctvDto.getYCOORD(), cctvDto.getXCOORD());
            cctvDto.setDistance(distance);
        }

        // 거리를 기준으로 오름차순으로 정렬
        Collections.sort(cctvDtoList, Comparator.comparingDouble(CctvDto::getDistance));

        // 가장 가까운 3개의 CCTV를 선택
        List<CctvDto> closestCctvs = cctvDtoList.subList(0, Math.min(cctvDtoList.size(), 3));

        // 가장 가까운 3개의 CCTV 출력
        for (CctvDto cctvDto : closestCctvs) {
            System.out.println("CCTV ID: " + cctvDto.getCCTVID());
            System.out.println("CCTV Name: " + cctvDto.getCCTVNAME());
            System.out.println("CCTV Center Name: " + cctvDto.getCENTERNAME());
            System.out.println("CCTV Latitude: " + cctvDto.getYCOORD());
            System.out.println("CCTV Longitude: " + cctvDto.getXCOORD());
            System.out.println("Distance from current location: " + cctvDto.getDistance() + " km");
            
            CctvUticDto cctvUticDto = null;
			try {
				cctvUticDto = cctvajax.getCctvUticDto(cctvDto.getCCTVID());
				System.out.println("cctvUticDto전달 : " + cctvUticDto.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            cctvDto.setCCTVUTICDTO(cctvUticDto);
            
        }
		
		String jasonClosestCctvs = cctvajax.accDTOListToJson(closestCctvs);
        
		
		return jasonClosestCctvs;
	}
	public String createAllCCtvObject(Model model) {
		Map<String, Object> modelMap = model.asMap();
		SqlSession sqlSession = (SqlSession) modelMap.get("sqlSession");
		
		
		//cctv 리스트가져오기
		List<CctvDto> cctvDtoList = null;
		CodeDao codeDao = sqlSession.getMapper(CodeDao.class);
		cctvDtoList = codeDao.findAllCctv();
		System.out.println("검색된 cctvlist 길이 : " + cctvDtoList.size());
		
		
		// CCTV 출력
		for (CctvDto cctvDto : cctvDtoList) {
//			System.out.println("CCTV ID: " + cctvDto.getCCTVID());
//			System.out.println("CCTV Name: " + cctvDto.getCCTVNAME());
//			System.out.println("CCTV Center Name: " + cctvDto.getCENTERNAME());
//			System.out.println("CCTV Latitude: " + cctvDto.getYCOORD());
//			System.out.println("CCTV Longitude: " + cctvDto.getXCOORD());
//			
		
		
		
	}
		String jasonClosestCctvs = cctvajax.accDTOListToJson(cctvDtoList);
		return jasonClosestCctvs;
	}
	
}
