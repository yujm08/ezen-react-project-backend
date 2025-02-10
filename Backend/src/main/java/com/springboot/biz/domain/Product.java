package com.springboot.biz.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="tbl_product")
@Getter
@ToString(exclude = "imageList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pno;
	
	private String pname;
	
	private int price;
	
	private String pdesc;
	
	private boolean delflag;
	
	@ElementCollection //값 타입 객체, 하나의 상품 데이터가 여러 개의 상품 이미지를 가질 수 있도록 컬렉션 타입으로 했다.
	//엔티티 테이블은 product, 내장 객체(테이블)가 2 개의 테이블을 처리할 때 한번에 모든 테이블을 같이 로딩해서 퍼리 : eager Loding
	//2 개의 테이블 중 필요한 타입을 먼저 조획 -> Lazy Loading
	@Builder.Default
	private List<ProductImage> imageList = new ArrayList<>();
	
	public void changePrice(int price) {
		this.price = price;
	}
	
	public void changeDesc(String desc) {
		this.pdesc = desc;
	}
	
	public void changeName(String name) {
		this.pdesc = name;
	}
	
	public void addImage(ProductImage image) {
		image.setOrd(this.imageList.size());
		imageList.add(image);
	}
	
	public void addImageString(String fileName) {
		ProductImage productImage = ProductImage.builder()
				.fileName(fileName)
				.build();
		addImage(productImage);
	}

	public void clearList() {
		this.imageList.clear();
	}
	
}
