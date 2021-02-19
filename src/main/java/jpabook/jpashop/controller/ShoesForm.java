package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShoesForm {

    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    private String brand;
    private String collaboration;

}
