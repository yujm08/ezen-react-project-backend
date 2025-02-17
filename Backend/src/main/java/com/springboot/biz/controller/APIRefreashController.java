package com.springboot.biz.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.biz.util.CustomJWTException;
import com.springboot.biz.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreashController {

	@RequestMapping
	public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, String refreshToken) {
		
		if(refreshToken == null) {
			throw new CustomJWTException("NULL_REFREASH");
		}
		
		if(authHeader == null || authHeader.length() < 7) {
			throw new CustomJWTException("INVALID_STRING");
		}
		
		String accessToken = authHeader.substring(7);
		
		//Access 토큰이 만료되지 않았을 경우
		if(checkExpiredToken(accessToken) == false) {
			return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
		}
		
		//Refresh 검증
		Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
		log.info("refresh . . . claims: " + claims);
		String newAccessToken = JWTUtil.generateToken(claims, 10);
		String newRefreshToken = checkTime((Integer)claims.get("exp")) == true ? JWTUtil.generateToken(claims, 60*24) : refreshToken;
		return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
	}
	
	//시간이 1시간 미만으로 남았다면
	private boolean checkTime(Integer exp) {
		//JWT exp를 날짜로 변환
		Date expDate = new Date( exp * (1000));
		//현재 시간과의 차이 계산 - 밀리세컨드
		long gap = expDate.getTime() - System.currentTimeMillis();
		//분단위 계산
		long leftMin = gap / (1000 * 60);
		//1시간 이하로 남았는지 검사
		return leftMin < 60;
	}
	
	private boolean checkExpiredToken(String token) {
		try {
			JWTUtil.validateToken(token);
		}catch(Exception e) {
			if(e.getMessage().equals("Expired")) {
				return true;
			}
		}
		return false;
	}
}
