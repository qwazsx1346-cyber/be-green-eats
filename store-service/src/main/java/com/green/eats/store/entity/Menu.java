package com.green.eats.store.entity;

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
public class Menu {

    @Id
    @Tsid //서버에서 시간과 랜덤 기반으로 id생성 후 DB로 날림
    private Long id;

    @Column(nullable = false, length = 255) //사실상 얘는 설정값 넣을때 아니면 아에 빼도됨
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockQuantity; //재고수량

    public void removeStock(int quantity) {
      int restStock = this.stockQuantity - quantity;
      if (restStock < 0) {
        throw new RuntimeException("상품 '" + name + "'의 재고가 부족합니다. (현재: " + stockQuantity + ")");
      }
      this.stockQuantity = restStock;
    }
}
