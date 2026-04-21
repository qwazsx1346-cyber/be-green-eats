package com.green.eats.store.enumcode;

import com.green.eats.common.enumcode.AbstractEnumCodeConverter;
import com.green.eats.common.enumcode.EnumMapperType;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor //DI받기 위함이 아니다.
public enum EnumMenuCategory implements EnumMapperType {
    //enum이름("코드", "값")
      KOREAN_FOOD("01", "한식")
    , JAPANESE_FOOD("02", "일식")
    , CHINESE_FOOD("03", "중식")
    , INDIA_FOOD("04", "인도식")
    , GOOD_FOOD("05", "맛식")
    ;

    private final String code;
    private final String value;

    @Converter(autoApply = true) //이걸 주면 쓰기만하면 자동으로 converter가 됨
    public static class CodeConverter extends AbstractEnumCodeConverter<EnumMenuCategory> {
        public CodeConverter() {
            super(EnumMenuCategory.class, false); //false는 null허용하니마니 차이. null값 허용한다면 true주면 됨
        }
    }
}
