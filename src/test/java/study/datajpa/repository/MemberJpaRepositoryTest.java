package study.datajpa.repository;


import static org.assertj.core.api.Assertions.assertThat;

import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Member;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

    }
    
    @Test
    public void usernameTest() {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member1", 19));

        List<Member> members = memberJpaRepository.findByUsernameAndAgeGreaterThan("member1", 9);
        assertThat(members.get(0).getUsername()).isEqualTo("member1");
        assertThat(members.get(1).getUsername()).isEqualTo("member1");

    }
    @Test
    public void bulkUpdate() {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 19));
        memberJpaRepository.save(new Member("member3", 20));
        memberJpaRepository.save(new Member("member4", 21));
        memberJpaRepository.save(new Member("member5", 40));

        int resultCount = memberJpaRepository.bulkAgePlus(20);

        assertThat(resultCount).isEqualTo(3);
    }

}