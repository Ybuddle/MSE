package com.tech.mse.dto;

import lombok.Data;

@Data
public class AccInfoDto {
	String acc_id; //돌발 아이디
	String occr_date; //발생 일자
	String occr_time; //발생 시각
	String exp_clr_date; //종료 예정 일자
	String exp_clr_time; //종료 예정 시각
	String acc_type; //돌발 유형 코드
	String acc_dtype; //돌발 세부 유형 코드
	String link_id; //링크 아이디
	String grs80tm_x; //TM X 좌표
	String grs80tm_y; //TM Y 좌표
	String acc_info; //돌발 내용
	String acc_road_code;
}
