package com.tech.mse.forenum;

import java.util.Arrays;
import java.util.Map;

import lombok.AllArgsConstructor;

public enum Reg_code {
	 JONGNO(100, "종로구"),
	    JUNG(101, "중구"),
	    YONGSAN(102, "용산구"),
	    SEONGDONG(103, "성동구"),
	    GWANGJIN(104, "광진구"),
	    DONGDAEMUN(105, "동대문구"),
	    JUNGNANG(106, "중랑구"),
	    SEONGBUK(107, "성북구"),
	    GANGBUK(108, "강북구"),
	    DOBONG(109, "도봉구"),
	    NOWON(110, "노원구"),
	    EUNPYEONG(111, "은평구"),
	    SEODAEMUN(112, "서대문구"),
	    MAPO(113, "마포구"),
	    YANGCHEON(114, "양천구"),
	    GANGSEO(115, "강서구"),
	    GURO(116, "구로구"),
	    GEUMCHEON(117, "금천구"),
	    YEONGDEUNGPO(118, "영등포구"),
	    DONGJAK(119, "동작구"),
	    GWANAK(120, "관악구"),
	    SEOCHO(121, "서초구"),
	    GANGNAM(122, "강남구"),
	    SONGPA(123, "송파구"),
	    GANGDONG(124, "강동구");

	    private final int code;
	    private final String name;

	    Reg_code(int code, String name) {
	        this.code = code;
	        this.name = name;
	    }

	    public int getCode() {
	        return code;
	    }

	    public String getName() {
	        return name;
	    }

		/*
		 * public static Reg_code fromCode(int code) { for (Reg_code district :
		 * Reg_code.values()) { if (district.getCode() == code) { return district; } }
		 * throw new IllegalArgumentException("Invalid district code: " + code); }
		 * 
		 * public static Reg_code fromName(String name) { for (Reg_code district :
		 * Reg_code.values()) { if (district.getName().equals(name)) { return district;
		 * } } throw new IllegalArgumentException("Invalid district name: " + name); }
		 */
	    public static Reg_code fromCode(int code) {
	        return Arrays.stream(Reg_code.values())
	                .filter(regCode -> regCode.getCode() == code)
	                .findFirst()
	                .orElseThrow(() -> new IllegalArgumentException("Invalid district code: " + code));
	    }

	    public static Reg_code fromName(String name) {
	        return Arrays.stream(Reg_code.values())
	                .filter(regCode -> regCode.getName().equals(name))
	                .findFirst()
	                .orElseThrow(() -> new IllegalArgumentException("Invalid district name: " + name));
	    }
}
