package com.green.eats.common.model;

//UserDto위에는 얘의 맴버필드타입 적어주기
//Dto : Data Transfer Object 레이어 간 데이터 전달 용도의 객체
//Vo : Value Object
public record UserDto(Long id, String name) {}
