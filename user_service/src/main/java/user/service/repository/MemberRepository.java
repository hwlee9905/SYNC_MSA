package user.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user.service.entity.Member;
import user.service.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    @Query("SELECT m FROM Member m WHERE m.user.id = :userId AND m.projectId = :projectId")
    Optional<Member> findMemberByUserIdAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);
    
    List<Member> findByProjectId(Long projectId);
    
    @Query("SELECT m.projectId FROM Member m WHERE m.user.id = :userId")
    List<Long> findProjectIdsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT m.user FROM Member m WHERE m.projectId = :projectId")
    List<User> findMemberIdsByProjectId(@Param("projectId") Long projectId);
    
    @Query("SELECT m FROM Member m JOIN FETCH m.user u WHERE u.id IN :userIds")
    List<Member> findMembersByUserIds(List<Long> userIds);
    
    @Query("SELECT COUNT(m) FROM Member m WHERE m.projectId = :projectId")
	Long countByProjectId(@Param("projectId") Long projectId);

    Optional<Member> findMemberByUserId(Long userId);

}
