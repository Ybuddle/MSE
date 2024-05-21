package com.tech.mse.forenum;

import java.util.Arrays;

public enum AccMainCodeE {
    A01("교통사고"),
    A02("차량고장"),
    A03("보행사고"),
    A04("공사"),
    A05("낙하물"),
    A06("버스사고"),
    A07("지하철사고"),
    A08("화재"),
    A09("기상/재난"),
    A10("집회및행사"),
    A11("기타"),
    A12("제보"),
    A13("단순정보");

    private final String description;

    AccMainCodeE(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AccMainCodeE fromDescription(String description) {
        return Arrays.stream(AccMainCodeE.values())
                .filter(type -> type.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid description: " + description));
    }
    public static AccMainCodeE fromCode(String code) {
        return Arrays.stream(AccMainCodeE.values())
                .filter(type -> type.name().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
    }
}