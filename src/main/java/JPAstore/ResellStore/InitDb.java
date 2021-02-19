package JPAstore.ResellStore;

import JPAstore.ResellStore.domain.*;
import JPAstore.ResellStore.domain.item.Shoes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;

        public void dbInit1(){
            Member member = createMember("userA", "서울", "1","1111");
            em.persist(member);

            Shoes shoes1 = createShoes("나이키 오프화이트", 1000000, 2);
            em.persist(shoes1);
            Shoes shoes2 = createShoes("나이키 디올 조던", 3000000, 10);
            em.persist(shoes2);
            OrderItem orderItem1 = OrderItem.createOrderItem(shoes1, 1000000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(shoes2, 3000000, 2);
            Order order = Order.createOrder(member, createDelivery(member),
                    orderItem1, orderItem2);
            em.persist(order);

        }

        public void dbInit2(){
            Member member = createMember("userB", "부산", "2","2222");
            em.persist(member);

            Shoes shoes1 = createShoes("컨버스 런스타 하이크", 600000, 15);
            em.persist(shoes1);
            Shoes shoes2 = createShoes("나이키 사카이", 700000, 10);
            em.persist(shoes2);
            OrderItem orderItem1 = OrderItem.createOrderItem(shoes1, 600000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(shoes2, 700000, 4);
            Order order = Order.createOrder(member, createDelivery(member),
                    orderItem1, orderItem2);
            em.persist(order);

        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }
        private Shoes createShoes(String name, int price, int stockQuantity) {
            Shoes shoe = new Shoes();
            shoe.setName(name);
            shoe.setPrice(price);
            shoe.setStockQuantity(stockQuantity);
            return shoe;
        }
        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

    }
}
