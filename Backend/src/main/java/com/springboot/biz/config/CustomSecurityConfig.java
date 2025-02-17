package com.springboot.biz.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.springboot.biz.security.filter.JWTCheckFilter;
import com.springboot.biz.security.handler.APILoginFailHandler;
import com.springboot.biz.security.handler.APILoginSuccessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2 //로그 출력
@RequiredArgsConstructor
public class CustomSecurityConfig {

	@Bean
	public SecurityFilterChain filter(HttpSecurity http) throws Exception{
		System.out.println("------------시큐리티 적용------------");
		log.info("***************security 적용***************");
		
		http.cors(httpSecurityCorsConfigurer -> {
			httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
		});
		
		http.sessionManagement(SessionConfig -> SessionConfig.sessionCreationPolicy(SessionCreationPolicy.NEVER));
		
		http.csrf(config -> config.disable());
		
		http.formLogin(config -> {
			config.loginPage("/api/member/login");
			config.successHandler(new APILoginSuccessHandler());
			config.failureHandler(new APILoginFailHandler());
		});
		http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class); //JWT 체크
		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration confuguration = new CorsConfiguration();
		
		//confuguration.setAllowedOriginPatterns(Arrays.asList("*")); //어떤 도메인(origin)에서 요청을 허용할지 지정
		confuguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));//Access-Control-Allow-Origin을 *로 설정하면서 Access-Control-Allow-Credentials: true를 사용하면 브라우저 보안 정책상 에러
		//쿠키를 주고받으려면 Access-Control-Allow-Origin에 정확한 도메인(예: http://localhost:3000)을 넣어야 한다.
		confuguration.setAllowedMethods(Arrays.asList("HEAD", "GET", "PUT", "DELETE", "POST")); //모든 Method에 대해 cors 허용
		confuguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); //클라이언트가 요청할 때, HTTP 헤더 중 허용할 헤더를 지정
		confuguration.setAllowCredentials(true); //쿠키나 인증 정보를 포함한 요청을 허용
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", confuguration);
		
		return source;
	}
	
	
	@Bean
	public PasswordEncoder passWordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
