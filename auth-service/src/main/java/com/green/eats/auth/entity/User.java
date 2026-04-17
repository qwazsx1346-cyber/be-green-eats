package com.green.eats.auth.entity;

import com.green.eats.common.entity.CreatedUpdatedAt;
import com.green.eats.common.model.EnumUserRole;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends CreatedUpdatedAt {
    @Id
    @Tsid //서버에서 시간과 랜덤 기반으로 id생성 후 DB로 날림
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String address;

    @Column(nullable = false, length = 2)
    private EnumUserRole enumUserRole;

    @Column(nullable = false)
    private Boolean isDel; //wrapper Type, 대문자는 레퍼런스 타입, 소문자는 프라머티브 타입, 자바는 둘 다 지원가능,c#은 대문자만 가능

}
