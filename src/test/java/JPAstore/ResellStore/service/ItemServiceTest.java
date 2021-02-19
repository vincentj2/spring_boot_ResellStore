package JPAstore.ResellStore.service;

import JPAstore.ResellStore.domain.item.Shoes;
import JPAstore.ResellStore.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;
    @Autowired EntityManager em;
    @Test
    public void 상품등록() throws Exception{
            //given
        Shoes book = new Shoes();
        book.setBrand("나이키");
        book.setCollaboration("디올");
        book.setName("나이키 디올 조던");
            //when
        itemService.saveItem(book);
            //then
        em.flush();
    }

}