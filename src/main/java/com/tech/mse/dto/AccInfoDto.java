package com.tech.mse.dto;

import lombok.Data;

@Data
public class AccInfoDto {
	private String acc_id; //돌발 아이디
	private String occr_date; //발생 일자
	private String occr_time; //발생 시각
	private String exp_clr_date; //종료 예정 일자
	private String exp_clr_time; //종료 예정 시각
	private String acc_type; //돌발 유형 코드
	private String acc_dtype; //돌발 세부 유형 코드
	private String link_id; //링크 아이디
	private String grs80tm_x; //TM X 좌표
	private String grs80tm_y; //TM Y 좌표
	private String acc_info; //돌발 내용
	private String acc_road_code;
}
