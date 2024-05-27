package com.tech.mse.dto;

import lombok.Data;

@Data
public class CctvDto {
	private String CCTVID;
	private String CCTVNAME;
	private String CENTERNAME;
	private String XCOORD;
	private String YCOORD;
	private double distance;
	private CctvUticDto CCTVUTICDTO;
	   // 기본 생성자
    public CctvDto() {}
	public CctvDto(String cCTVID, String cCTVNAME, String cENTERNAME, String xCOORD, String yCOORD) {
		super();
		this.CCTVID = cCTVID;
		this.CCTVNAME = cCTVNAME;
		this.CENTERNAME = cENTERNAME;
//		this.XCOORD = Float.parseFloat(xCOORD);
//		this.YCOORD = Float.parseFloat(yCOORD);
		this.XCOORD = xCOORD;
		this.YCOORD = yCOORD;
	}
	
}
