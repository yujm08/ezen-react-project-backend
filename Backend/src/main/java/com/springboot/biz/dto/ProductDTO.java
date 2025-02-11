package com.springboot.biz.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

	//상품의 고유 번호 (ID)
	private Long pno;
	
	private String name;
	
	private int price;
	
	//상품 설명
	private String pdesc;
	
	//삭제 여부
	private boolean delflag;
	
	//업로드된 파일 데이터를 저장하는 리스트, 사용자가 새로운 파일을 업로드할 때 실제 파일 데이터를 포함
	@Builder.Default
	private List<MultipartFile> files = new ArrayList<>();
	
	//업로드가 완료된 파일의 이름만 문자열로 보관하는 필드. 실제 파일 X, 업로드된 파일 이름 O
	@Builder.Default
	private List<String> uploadFileNames = new ArrayList<>();
}
