package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //상속관계 맵핑이기 때문에 상속관계전략 지정 필요 (부모에 지정)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int StockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();


    //비지니스 로직 추가//
    //도메인 주도 개발 방식 , StockQuantity를 관리하는 쪽에 비지니스 로직을 추가하는것이 더 객체지향스럽다

    //stock 증가
    public void addStock(int quantity){
        this.StockQuantity += quantity;
    }

    //stock 감소
    public void removeStock(int quantity){
        int restStock = this.StockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.StockQuantity = restStock;
    }
}
