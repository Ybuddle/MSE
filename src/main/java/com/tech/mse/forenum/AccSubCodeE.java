package com.tech.mse.forenum;

import java.util.Arrays;

public enum AccSubCodeE {
    CODE_05B01("소형낙하물"),
    CODE_12B01("제보"),
    CODE_11B01("기타"),
    CODE_10B01("훈련"),
    CODE_02B01("차량고장"),
    CODE_06B01("버스사고"),
    CODE_04B01("시설물보수"),
    CODE_01B01("추돌사고"),
    CODE_09B01("폭우"),
    CODE_08B01("화재"),
    CODE_07B01("지하철사고"),
    CODE_03B01("보행사고"),
    CODE_04B02("청소작업"),
    CODE_01B03("전복사고"),
    CODE_10B02("집회/시위"),
    CODE_09B02("호우주의보"),
    CODE_05B02("대형낙하물"),
    CODE_04B03("차선도색"),
    CODE_10B03("행사"),
    CODE_09B03("호우경보"),
    CODE_01B04("차량화재"),
    CODE_01B05("전도사고"),
    CODE_09B04("태풍주의보"),
    CODE_04B04("도로보수"),
    CODE_09B05("태풍경보"),
    CODE_04B05("제설작업"),
    CODE_09B06("폭설"),
    CODE_04B06("포장공사"),
    CODE_09B07("대설주의보"),
    CODE_04B07("가로수정비"),
    CODE_09B08("대설경보"),
    CODE_09B09("폭염"),
    CODE_09B10("폭염주의보"),
    CODE_09B11("한파"),
    CODE_09B12("한파주의보"),
    CODE_09B13("우박"),
    CODE_09B14("노면미끄러움"),
    CODE_09B15("도로침하"),
    CODE_09B16("도로침수"),
    CODE_09B17("도로결빙"),
    CODE_09B18("노면패임"),
    CODE_13B01("단순정보"),
    CODE_09B19("강우통제");

    private final String description;

    AccSubCodeE(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // 이름을 생성하는 메서드
    public String getName() {
        return "CODE_" + name();
    }
    public static AccSubCodeE fromDescription(String description) {
        return Arrays.stream(AccSubCodeE.values())
                .filter(type -> type.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid description: " + description));
    }

    public static AccSubCodeE fromCode(String code) {
        return Arrays.stream(AccSubCodeE.values())
                .filter(type -> type.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
    }

    // 각 Enum 상수에서 "CODE_" 부분을 제외한 코드를 반환하는 메소드 추가
    public String getCode() {
        return this.name().replace("CODE_", "");
    }
}

