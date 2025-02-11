package com.springboot.biz;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.biz.dto.PageRequestDTO;
import com.springboot.biz.dto.PageResponseDTO;
import com.springboot.biz.dto.ProductDTO;
import com.springboot.biz.service.ProductService;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ProductServiceTest {

	@Autowired
	ProductService productService;
	
	@Test
	public void testList() {
		
//		PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
//		
//		PageResponseDTO<ProductDTO> result = productService.getList(pageRequestDTO);
//		
//		result.getDtoList().forEach(dto -> log.info(dto));
		
//		ProductDTO productDTO = ProductDTO.builder()
//				.pname("새로운 상품")
//				.pdesc("신규 추가 상품입니다")
//				.price(1000)
//				.build();
//		
//		productDTO.setUploadFileNames(
//				List.of(
//						UUID.randomUUID().toString() + "_" + "Test1.jpg",
//						UUID.randomUUID().toString() + "_" + "Test2.jpg"));
//		
//		productService.register(productDTO);
		
		Long pno = 11L;
		
		ProductDTO productDTO = productService.get(pno);
		
		log.info(productDTO);
		log.info(productDTO.getUploadFileNames());
		
	}
	
	
}
