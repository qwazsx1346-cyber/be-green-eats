package com.green.eats.common;

//abstract 추상 클래스

import net.datafaker.Faker;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import java.util.Locale;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //테스트하기 위해서 데이터 바꾸지 않겠다는뜻
@Rollback(false)
public abstract class Dummy {
    protected Faker koFaker = new Faker(Locale.KOREAN);
    protected Faker enFaker = new Faker(Locale.ENGLISH);
}
