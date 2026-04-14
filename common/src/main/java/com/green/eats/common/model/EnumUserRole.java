package com.green.eats.common.model;

import com.green.eats.common.enumcode.AbstractEnumCodeConverter;
import com.green.eats.common.enumcode.EnumMapperType;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnumUserRole implements EnumMapperType {
  USER("01", "일반유저"),
  ADMIN("02", "관리자"),
  MANAGER("03", "중간관리자") //순서는 중요하지 않지만 안에 기존 key와 value는 수정하면안됨. 수정하면 DB에서도 update문 써줘야함
  ;
  private final String code;
  private final String value;

  @Converter(autoApply = true)
  public static class CodeConverter extends AbstractEnumCodeConverter<EnumUserRole> {
    public CodeConverter() { super(EnumUserRole.class, false); }
  }
}