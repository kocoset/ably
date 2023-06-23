package ably.minjunkim.service;

import ably.minjunkim.domain.Member;
import ably.minjunkim.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.List;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final LoginRepository loginRepository;

    public List<Member> login(Member member) {
        generateHashPassword(member); // 패스워드 해시값 변환
        return loginRepository.findMember(member);
    }

    @Transactional
    public String save(Member member) { // 회원 저장
        validateDuplicateMember(member); //중복 회원 검증
        generateHashPassword(member); // 패스워드 해시값 변환
        loginRepository.save(member); // 회원 저장
        return member.getLoginId();
    }

    public void validateDuplicateMember(Member member) { // 중복 회원 체크 로직
        List<Member> findMembers = loginRepository.findByLoginId(member.getLoginId());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public String generateHashPassword(Member member) { // 패스워드 해시값 변환 로직
        String hashPassword = member.getLoginId() + member.getPassword();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(hashPassword.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : md.digest()) {
                builder.append(String.format("%02x", b));
            }
            member.setPassword(builder.toString());
            hashPassword = member.getPassword();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
        return hashPassword;
    }
}