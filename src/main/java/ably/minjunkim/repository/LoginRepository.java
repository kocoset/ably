package ably.minjunkim.repository;

import ably.minjunkim.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LoginRepository {
    private final EntityManager em;
    public void save(Member member) {
        em.persist(member);
    }
    public List<Member> findMember(Member member) {
        return em.createQuery("select m from Member m where m.loginId = :loginId and m.password = :password", Member.class)
                .setParameter("loginId", member.getLoginId())
                .setParameter("password", member.getPassword())
                .getResultList();
    }
    public List<Member> findByLoginId(String loginId) {
        return em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();
    }
}
