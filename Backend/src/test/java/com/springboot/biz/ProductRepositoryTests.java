package com.springboot.biz;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.springboot.biz.dto.ProductDTO;
import com.springboot.biz.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository productRepository;
	
	@Test
	public void testInsert() {
		
//		Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());
//		
//		Page<Object[]> result = productRepository.selectList(pageable);
//		
//		result.getContent().forEach(err -> log.info(Arrays.toString(err)));
		
//		Long pno = 9L;
//		
//		Product product = productRepository.selectOne(pno).get();
//		
//		product.changeName("9번 상품");
//		product.changeDesc("9번 상품 설명입니다");
//		product.changePrice(5000);
//		
//		product.clearList();
//		
//		product.addImageString(UUID.randomUUID().toString() + "_" + "NEWIMAGE1.jpg");
//		product.addImageString(UUID.randomUUID().toString() + "_" + "NEWIMAGE2.jpg");
//		product.addImageString(UUID.randomUUID().toString() + "_" + "NEWIMAGE3.jpg");
//		
//		productRepository.save(product);
		
//		Long pno = 2L;
//		
//		productRepository.updateToDelete(pno, true);
		
//		for(int i = 0 ; i < 10 ; i++) {
//			Product product = Product.builder()
//					.pname("상품"+ i)
//					.price(100*i)
//					.pdesc("상품 설명" + i)
//					.build();
//			
//			product.addImageString(UUID.randomUUID().toString() + "_" + "IMAGE1.jpg");
//			product.addImageString(UUID.randomUUID().toString() + "_" + "IMAGE2.jpg");
//			
//			productRepository.save(product);
//			
//			log.info("------------------------"); 
//		}
		
//		Long pno = 1L;
//		
//		Optional<Product> result = productRepository.selectOne(pno);
//		
//		Product product = result.orElseThrow();
//		
//		log.info(product);
//		log.info(product.getImageList());
		
		
		
	}
}
