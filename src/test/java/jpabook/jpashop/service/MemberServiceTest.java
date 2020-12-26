package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;
    @Test
    @Rollback(value = false)// rollback을 막아 실제 DB에 저장되는것까지 확인가능
    public void 회원가입() throws Exception{
            //given
        Member member = new Member();
        member.setName("Jeong");

            //when
        Long saveId = memberService.join(member);
            //then
        em.flush(); // Insert query 확인시 사용 -> test 실행시 spring은 rollback 시키지만 flush를 통해 Inset query 확인가능
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
            //given
        Member member1 = new Member();
        member1.setName("jeong");
        Member member2 = new Member();
        member2.setName("jeong");

            //when
        memberService.join(member1);
     //   try {
            memberService.join(member2); // 예외가 발생해야한다
     //   }catch(IllegalStateException e){
     //       return;
     //   }
            //then
        fail("예외가 발생해야 한다.");
    }
}