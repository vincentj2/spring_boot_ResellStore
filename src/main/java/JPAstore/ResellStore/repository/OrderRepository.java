package JPAstore.ResellStore.repository;

import JPAstore.ResellStore.domain.Member;
import JPAstore.ResellStore.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(JPAstore.ResellStore.domain.Order order){
        em.persist(order);
    }

    public JPAstore.ResellStore.domain.Order findOne(Long id){
        return em.find(JPAstore.ResellStore.domain.Order.class, id);
    }

    public List<JPAstore.ResellStore.domain.Order> findAll(OrderSearch orderSearch){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(JPAstore.ResellStore.domain.Order.class);
        Root<JPAstore.ResellStore.domain.Order> o = cq.from(JPAstore.ResellStore.domain.Order.class);
        Join<JPAstore.ResellStore.domain.Order, Member>  m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if(orderSearch.getOrderStatus() != null){
            Predicate status = cb.equal(o.get("status"),orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())){
            Predicate name = cb.like(m.<String>get("name"), "%" +orderSearch.getMemberName()+ "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<JPAstore.ResellStore.domain.Order> query = em.createQuery(cq).setMaxResults(1000);//최대 1000건
        return query.getResultList();
    }


    public List<JPAstore.ResellStore.domain.Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", JPAstore.ResellStore.domain.Order.class).getResultList();
    }

    public List<JPAstore.ResellStore.domain.Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d " +
                        "join fetch o.orderItems oi" +
                        " join fetch oi.item i" , JPAstore.ResellStore.domain.Order.class)
                .getResultList();
    }

    public List<JPAstore.ResellStore.domain.Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
