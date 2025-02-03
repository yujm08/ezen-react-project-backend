package com.springboot.biz.util;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JWTUtil {

	//문자열 생성시 필요한 암호키 생성(길이가 짧으면 안됨. 30자 이상의 문자열로 암호키를 지정. 특수기호 X, 숫자 or 영문자)
	private static String key = "1234567890123456789012345678901234567890";
	
	//JWT 생성 메서드
	public static String generateToken(Map<String, Object> valueMap, int min) {
		
		SecretKey key = null;
		
		try { 
			key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
			//hmacShaKeyFor 메서드: HMAC 알고리즘을 적용한 Key를 생성 
			//HMAC(Hash-based message authentication code) 해시함수를 이용한 암화화 인증 기술- 공개키 암호화 방식
			//암호화 방식- 비대칭키(공개키) 암호화 방식, 대칭키(비밀키) 
			} catch(Exception e) {
				throw new RuntimeException(e.getMessage());
		}
		
		//사용자가 인증 요청을 하면 서버는 요청이 들어온 아이디/비밀번호를 확인
		//사용자가 맞다면 Access Token, Refresh Token을 서버가 만들어서 사용자에게 보냄
		//사용자 요청시 헤더에 토큰값을 넣어서 같이 보냄
		//서버는 사용자가 보내온 토큰을 검증 Access Token 만료 시간을 확인
		//만약 만료시간이 지난 뒤, 사용자로부터 요청이 들어오면 서버는 Refresh Token을 이용해서 Access Token을 재발급
		
		String jwtStr = Jwts.builder()
				.setHeader(Map.of("typ", "JWT"))
				.setClaims(valueMap) //사용자 정보 저장
				.setIssuedAt(Date.from(ZonedDateTime.now().toInstant())) //JWT 발행 일자
				.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())) //JWT 만료 일자
				.signWith(key) //서명을 위한 key 객체
				.compact(); //JWT 생성 및 직렬화
		
		return jwtStr;
		
	}
	
	//토큰 검증 메서드
	public static Map<String, Object> validateToken(String token) {
		
		Map<String, Object> claim = null;
		
		try {
			SecretKey key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
			
			claim = Jwts.parserBuilder() //토큰의 유효성 검사
					.setSigningKey(key) //서명키로 설정
					.build() 
					.parseClaimsJws(token) //토큰의 유효성을 검사하는 메서드
					.getBody();
			
			//JWT 헤더 + 페이로드 + 서명
			
			//헤더: 암호화 알고리즘, 토큰의 정보
			//페이로드: 사용자 정보, 토큰의 만료시간 등
			//서명: 헤더와 페이로드를 특정 암호화 알고리즘으로 서명한 결과
			
		} catch (MalformedJwtException malformedJwtException) {
			throw new CustomJWTException("Malformed");
		} catch (ExpiredJwtException expiredJwtException) {
			throw new CustomJWTException("Expired");
		} catch (InvalidClaimException e) {
			throw new CustomJWTException("Invalid");
		} catch (JwtException e) {
			throw new CustomJWTException("JwtError");
		} catch (Exception e) {
			throw new CustomJWTException("Error");
		}
		return claim;
	}
}
