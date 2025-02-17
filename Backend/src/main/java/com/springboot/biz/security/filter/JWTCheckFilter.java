package com.springboot.biz.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.springboot.biz.dto.MemberDTO;
import com.springboot.biz.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter{

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{
		
		if(request.getMethod().equals("OPTIONS")) {
			return true;
		}
		
		String path = request.getRequestURI();
		
		log.info("check uri............" + path);
		
		if(path.equals("/api/member/login")) {
		    return true;
		}
		
		//api/member/ 경로의 호출은 체크 X
		if(path.startsWith("/api/member/")) {
			return true;
		}
		
		//이미지 조회 경로는 체크하지 않음
		if(path.startsWith("/api/products/view/")){
			return true;
		}
		
		//ture = 체크, false = 체크X
		return false;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("-------------------------JWTCheckFilter-------------------------");
		
		String authHeaderStr = request.getHeader("Authorization");
		
		try {
			String accessToken = authHeaderStr.substring(7);
			Map<String, Object> claims = JWTUtil.validateToken(accessToken);
			
			log.info("JWT claims: "+ claims);
			
			String email = (String) claims.get("email");
			String pw = (String) claims.get("pw");
			String nickname = (String) claims.get("nickname");
			Boolean socail = (Boolean) claims.get("socail");
			List<String> roleNames = (List<String>) claims.get("roleNames");
			
			MemberDTO memberDTO = new MemberDTO(email, pw, nickname, socail.booleanValue(), roleNames);
			
			log.info("----------------------------");
			log.info(memberDTO);
			log.info(memberDTO.getAuthorities());
			
			UsernamePasswordAuthenticationToken authenticationToken 
			= new UsernamePasswordAuthenticationToken(memberDTO, pw, memberDTO.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			
			filterChain.doFilter(request, response);
			
		}catch(Exception e) {
			
			log.error("JWT Check Error...............");
			log.error(e.getMessage());
			
			Gson gson = new Gson();
			String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));
			
			response.setContentType("application/json");
			PrintWriter printWriter = response.getWriter();
			printWriter.println(msg);
			printWriter.close();
		}
		
	}

}
