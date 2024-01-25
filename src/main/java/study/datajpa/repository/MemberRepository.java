package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.dto.MemberResponse;
import study.datajpa.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Modifying(clearAutomatically = true)  // 이게 있어야 executeUpdate()를 실행한다.
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    List<Member> findByUsername(String username);

    @Query("select m from Member m join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();


    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);


    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select new study.datajpa.dto.MemberResponse(m.username, m.age) from Member m")
    List<MemberResponse> findMemberResponseDTO();

    @Query("select new study.datajpa.dto.MemberResponse$MemberRequest(m.username) from Member m")
    List<MemberResponse.MemberRequest> findMemberRequest();

    @Query("select m from Member m where m.username in :names")
    List<Member> findMembers(@Param("names") Collection<String> names);

    List<Member> findMemberByUsername(String username);

    Page<Member> findPageableMembersByUsername(String username, Pageable pageable);

}
