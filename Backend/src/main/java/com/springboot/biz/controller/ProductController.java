package com.springboot.biz.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.biz.dto.PageRequestDTO;
import com.springboot.biz.dto.PageResponseDTO;
import com.springboot.biz.dto.ProductDTO;
import com.springboot.biz.service.ProductService;
import com.springboot.biz.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {

	private final CustomFileUtil fileUtil;
	
	private final ProductService productService;
	
	//등록
	@PostMapping("/")
	public Map<String, Long> register(ProductDTO productDTO) {
		
		log.info("register : " + productDTO);
		
		List<MultipartFile> files = productDTO.getFiles();
		
		List<String> uploadFileNames = fileUtil.saveFiles(files);
		
		productDTO.setUploadFileNames(uploadFileNames);
		
		log.info(uploadFileNames);
		
		Long pno = productService.register(productDTO);
		
		return Map.of("result", pno);
		
	}
	
	//파일 보기
	@GetMapping("/view/{fileName}")
	public ResponseEntity<Resource> viewFileGET(@PathVariable(name="fileName") String fileName) {
		return fileUtil.getFile(fileName);
	}
	
	//목록 보기
	@GetMapping("/list")
	public ResponseEntity<PageResponseDTO<ProductDTO>> getProductList(PageRequestDTO pageRequestDTO) {
        log.info("list-----------------" + pageRequestDTO);

        PageResponseDTO<ProductDTO> responseDTO = productService.getList(pageRequestDTO);

        return ResponseEntity.ok(responseDTO);
    }
	
	//상세 보기
	@PostMapping("/{pno}")
	public ProductDTO read(@PathVariable(name="pno") Long pno) {
		log.info("-----------------상품 상세 조회 요청: pno={}", pno);
		return productService.get(pno);
	}
	
	//수정
	@PostMapping("/{pno}")
	public Map<String, String> modify(@PathVariable(name="pno") Long pno, ProductDTO productDTO) {
		
		productDTO.setPno(pno);
		
		ProductDTO oldProductDTO = productService.get(pno);
		
		List<String> oldFileNames = oldProductDTO.getUploadFileNames();
		List<MultipartFile> files = productDTO.getFiles();
		List<String> currentUploadFileNames = fileUtil.saveFiles(files);
		List<String> uploadFileNames = productDTO.getUploadFileNames();
		
		if(currentUploadFileNames != null && currentUploadFileNames.size() > 0) {
			uploadFileNames.addAll(currentUploadFileNames);
		}
		
		productService.modify(oldProductDTO);
		
		if(oldFileNames != null && oldFileNames.size() > 0) {
			
			List<String> removeFiles = oldFileNames
					.stream()
					.filter(fileName -> uploadFileNames.indexOf(fileName) == -1).collect(Collectors.toList());
			
			fileUtil.deleteFiles(removeFiles);
		}
		
		return Map.of("RESULT", "SUCCESS");
	}
	
	@DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable Long pno) {
        productService.remove(pno);
        return Map.of("RESULT", "SUCCESS-deleted");
    }
}
