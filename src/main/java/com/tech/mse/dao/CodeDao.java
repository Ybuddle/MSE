package com.tech.mse.dao;

import java.util.List;
import java.util.Map;

import com.tech.mse.dto.CctvDto;
import com.tech.mse.dto.RedCodeNameDto;

public interface CodeDao {
	public List<CctvDto> findAllCctv();
	public List<CctvDto> findCctv(Map<String, Double> minMaxXYMap);
	public List<CctvDto> findAllCctv2(Map<String, Double> minMaxXYMap);
	public int insertCctv(List<CctvDto> slist);
	public int insertReg(List<RedCodeNameDto> slist);
	public int insertAccMain(List<Map<String,String>> slist);
	public int insertAccSub(List<Map<String,String>> slist);
}
