package study.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import study.datajpa.dto.MemberResponse;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void bulkAgePlus() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        int resultCount = memberRepository.bulkAgePlus(20);
//        em.flush(); // 아직 변경되지 않은 사항들을 반영
//        em.clear();

        List<Member> result = memberRepository.findByUsername("member5");
        Member member = result.get(0);
        System.out.println("member = " + member);
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member = " + member.getTeam().getClass());
            System.out.println("member = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint(){
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");
        System.out.println("findMember = " + findMember);
        em.flush();
    }

    @Test
    public void lock() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        List<Member> result = memberRepository.findLockByUsername("member1");
    }



    @Test
    public void dtoSelectTest() {
        Member member = new Member("member", 10, null);
        Member member2 = new Member("member", 10, null);
        Member member3 = new Member("member", 10, null);
        memberRepository.save(member);
        memberRepository.save(member2);
        memberRepository.save(member3);
        List<MemberResponse> memberResponseDTO = memberRepository.findMemberResponseDTO();
        for (MemberResponse memberResultDTO : memberResponseDTO) {
            System.out.println("memberResultDTO = " + memberResultDTO);
        }
    }

    @Test
    public void findMembersTest() {
        List<String> names = Arrays.asList("member", "member1", "member2");
        Member member = new Member("member", 10, null);
        Member member2 = new Member("member1", 10, null);
        Member member3 = new Member("member3", 10, null);
        memberRepository.save(member);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> members = memberRepository.findMembers(names);

        for (Member member1 : members) {
            System.out.println("member = " + member1);
        }
    }

    @Test
    public void test() {
        Member member = new Member("member", 10, null);
        Member member1 = new Member("member", 10, null);
        memberRepository.save(member);
        memberRepository.save(member1);

        // 자동으로 플러시가 실행
        TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
        List<Member> resultList = query.getResultList();
        for (Member findMember : resultList) {
            System.out.println("findMember = " + findMember);
        }
    }

    @Test
    public void testPageable() {
        Member member = new Member("member", 10, null);
        Member member2 = new Member("member", 10, null);
        Member member3 = new Member("member", 10, null);
        Member member4 = new Member("member", 10, null);
        Member member5 = new Member("member", 10, null);
        memberRepository.save(member);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<Member> memberPage = memberRepository.findPageableMembersByUsername("member", pageRequest);
        Pageable pageable = memberPage.nextPageable();
        Page<Member> nextPage = memberRepository.findPageableMembersByUsername("member", pageable);

        System.out.println("first page");
        for (Member findMember : memberPage) {
            System.out.println(findMember);
        }
        System.out.println("second page");
        for (Member findMember : nextPage) {
            System.out.println(findMember);
        }
    }
    @Test
    public void test() {
        Member member = new Member("nenber", 19, null);
        memberRepository.save(member);
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<Member> memberPage = memberRepository.findPageableMembersByUsername("member", pageRequest);
        Pageable pageable = memberPage.nextPageable();
    }



}