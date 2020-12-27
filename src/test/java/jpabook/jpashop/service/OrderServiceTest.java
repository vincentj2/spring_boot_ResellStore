package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
            //given
        Member member =CreateMember();
        Item item = CreateBook(10);
        int orderCount =2;
            //when
        Long orderId = orderService.order(member.getId(),item.getId(),orderCount);

            //then
        Order getOrder = orderRepository.findOne(orderId);
        Assert.assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        Assert.assertEquals("주문한 상품 종료 수가 정확해야한다",1, getOrder.getOrderItems().size());
        Assert.assertEquals("주문 가격은 가격*수량이다",10000*2, getOrder.getTotalPrice());
        Assert.assertEquals("주문 수량만큼 재고가 줄어야한다",8,item.getStockQuantity());
    }
    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
            //given
        Member member = CreateMember();
        Item item = CreateBook(10);

        int orderCount =11;

            //when
        orderService.order(member.getId(), item.getId(), orderCount);

            //then
        fail("재고 수량 부족 예외가 발생해야 한다");
    }

    @Test
    public void 주문취소() throws Exception{
            //given
        Member member = CreateMember();
        Item item = CreateBook(10);
        int orderCount =2;

        Long orderId = orderService.order(member.getId(),item.getId(),orderCount);
            //when
        orderService.cancelOrder(orderId);

            //then
        Order getOrder = orderRepository.findOne(orderId);
        Assert.assertEquals("주문취소시 상태는 CANCEL 이다",OrderStatus.CANCEL,getOrder.getStatus());
        Assert.assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야한다",10,item.getStockQuantity());
    }


    private Member CreateMember(){
        Member member = new Member();
        member.setName("jeong");
        member.setAddress(new Address("서울","서초대로","123-123"));
        em.persist(member);
        return member;
    }

    private Book CreateBook(int count){
        Book book = new Book();
        book.setName("책1");
        book.setAuthor("jsw");
        book.setPrice(10000);
        book.setStockQuantity(count);
        em.persist(book);
        return book;
    }
}