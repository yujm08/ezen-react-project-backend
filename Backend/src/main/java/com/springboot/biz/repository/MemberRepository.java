package com.springboot.biz.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.biz.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String>{

	@EntityGraph(attributePaths = {"memberRoleList"})
	@Query("SELECT m FROM Member m WHERE m.email = :email")
	Member getWithRoles(@Param("email") String email);
	
}
