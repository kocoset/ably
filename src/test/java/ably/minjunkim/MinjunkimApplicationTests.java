package ably.minjunkim;

import ably.minjunkim.domain.Member;
import ably.minjunkim.repository.LoginRepository;
import ably.minjunkim.service.LoginService;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

class MinjunkimApplicationTests {
	EntityManager em;

	@Test
	void contextLoads() {
		LoginService loginService = new LoginService(new LoginRepository(em));
		Member member = new Member();
		member.setLoginId("test");
		member.setPassword("1234");
		String hashPassword = loginService.generateHashPassword(member);
		// 로직과 온라인 hash 변환기 비교
		assertThat(hashPassword).isEqualTo("937E8D5FBB48BD4949536CD65B8D35C426B80D2F830C5C308E2CDEC422AE2244".toLowerCase());
	}

}
