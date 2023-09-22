package com.winterfoodies.winterfoodies_project.dto.product;

import lombok.Getter;

@Getter
public enum ProductEnum { //메인 첫 화면에 등장하는 메뉴
    BUNGABBANG("붕어빵",1L),
    FISHCAKE("어묵",2L),
    GUNBAM("군밤",3L),
    HODDUK("호떡", 4L),
    EGGBREAD("계란빵", 5L),
    SWEETPOTATO("군고구마", 6L),
    DAKOAKI("다코야키", 7L),
    HODUSNACK("호두과자", 8L),
    FLOWERBREAD("국화빵", 9L);

    private final String name;
    private final Long value;

    private ProductEnum(String name, Long value) {
        this.name = name;
        this.value = value;
    }
}
