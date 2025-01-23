package com.springboot.biz.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

	@Bean
	public ModelMapper getMapper() {
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
		//기본적으로 ModelMapper는 getter/setter 메서드를 사용해 데이터를 매핑.하지만 이 설정을 true 로 하면 직접 필드에 접근해서 값을 매핑할 수 있음
		.setFieldMatchingEnabled(true)
		//접근 수준을 private 필드까지 허용하여 매핑이 가능하도록
		.setFieldAccessLevel(AccessLevel.PRIVATE)
		//매핑 전략을 느슨하게 설정하여, 필드 이름이 완전히 일치하지 않아도 비슷하면 매핑되도록 함
		.setMatchingStrategy(MatchingStrategies.LOOSE);
		
		return modelMapper;
	}
}
