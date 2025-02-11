package com.springboot.biz.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable //값 타입 객체. JPA 엔티티 클래스에 내장 타입을 사용하기 위해 지정하는 어노테이션
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

	// 상품등록 핵심은 상품 자체. 파일들을 부수적인 데이터. 
	// -> 상품은 PK를 가지는 완번한 엔티티로 작성, 파일들은 엔티티에 속해 있는 데이터로 만듦
	
	private String fileName;
	
	//이미지에 붙이는 번호. 하나의 상품에 얼마의 이미지가 들어가 있는지
	private int ord;
	
	public void setOrd(int ord) {
		this.ord = ord;
	}
}
