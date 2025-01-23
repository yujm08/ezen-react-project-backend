package com.springboot.biz.security;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.biz.domain.Member;
import com.springboot.biz.dto.MemberDTO;
import com.springboot.biz.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("----------------------loadUserByUsername");
		
		Member member = memberRepository.getWithRoles(username);
		
		if(member == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
		}
		
		MemberDTO memberDTO = new MemberDTO(
				member.getEmail(), 
				member.getPw(), 
				member.getNickname(), 
				member.isSocial(),
				member.getMemberRoleList()
				.stream()
				.map(MemberRole -> MemberRole.name()).collect(Collectors.toList()));
		
		log.info("회원 정보 확인" + memberDTO);
		
		return memberDTO;
	}
	
}
