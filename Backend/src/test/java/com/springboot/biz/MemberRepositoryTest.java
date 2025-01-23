
package com.springboot.biz;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springboot.biz.domain.Member;
import com.springboot.biz.domain.MemberRole;
import com.springboot.biz.repository.MemberRepository;

@SpringBootTest
public class MemberRepositoryTest {

	@Autowired
	private MemberRepository memeberRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
//	@Test
//	public void testInsertmember() {
//		
//		for(int i = 0 ; i < 10 ; i++) {
//			
//			Member member = Member.builder()
//					.email("user" + i + "@aaa.aaa")
//					.pw(passwordEncoder.encode("1111"))
//					.nickname("USER" + i)
//					.build();
//			
//			member.addRole(MemberRole.USER);
//			
//			if(i >= 5) {
//				member.addRole(MemberRole.MANAGER);
//			}
//			
//			if(i >= 8) {
//				member.addRole(MemberRole.ADMIN);
//			}
//			
//			memeberRepository.save(member);
//		}
//	}
	
	@Test
	public void testRead() {
		String email = "user1@aaa.aaa";
		Member member = memeberRepository.getWithRoles(email);
		System.out.println("내가 검색한 객체 : " + member);
	}
}
