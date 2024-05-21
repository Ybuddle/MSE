package com.tech.mse.dao;

import java.util.List;
import java.util.Map;

import com.tech.mse.dto.RedCodeNameDto;

public interface CodeDao {
	public int insertReg(List<RedCodeNameDto> slist);
	public int insertAccMain(List<Map<String,String>> slist);
	public int insertAccSub(List<Map<String,String>> slist);
}
