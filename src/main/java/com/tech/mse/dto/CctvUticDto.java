package com.tech.mse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CctvUticDto {
    private String CCTVID;
    private String CCTVNAME;
    private String CENTERNAME;
    private String KIND;
    private String MOVIE;
    private String STRMID;
    private String CCTVIP;
    private String ID;
    private String PASSWD;
    private Double XCOORD;
    private Double YCOORD;
    private String PORT;
    private String CH;
	@Override
	public String toString() {
		return "CctvUticDto [CCTVID=" + CCTVID + ", CCTVNAME=" + CCTVNAME + ", CENTERNAME=" + CENTERNAME + ", KIND="
				+ KIND + ", MOVIE=" + MOVIE + ", STRMID=" + STRMID + ", CCTVIP=" + CCTVIP + ", ID=" + ID + ", PASSWD="
				+ PASSWD + ", XCOORD=" + XCOORD + ", YCOORD=" + YCOORD + ", PORT=" + PORT + ", CH=" + CH + "]";
	}

	
}
