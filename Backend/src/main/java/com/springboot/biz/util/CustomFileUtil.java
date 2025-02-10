package com.springboot.biz.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil { //파일 데이터의 입출력 담당
    
	//실제 파일이 저장될 디렉터리 경로를 사용자 정의 property 로 설정
    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostConstruct //의존성 주입을 먼저 실행하고 클래스 객체가 만들어질 때 사용
    //생성자가 호출되었을 때 빈은 초기화되지 않았을 수도 있음. 이때 어노테이션을 사용하면 의존성주입을 먼저 완료하고 메서드를 실행 -> 순서 잡아주는 용도
    //의존성 주입 전에 inti 메서드가 실행되면 문제가 있을 수 있기 때문에 순서를 정의하는 역할을 함. 여기서 에러가 나지 않을 때도 있지만 에러가 날 수 있기 때문에 사용
    //어플리케이션이 실행될 때 한 번만 실행
    public void init() {
    	
    	//지정한 경로에 파일 생성
        File temFolder = new File(uploadPath); 

        if(temFolder.exists() == false){
            temFolder.mkdir();
        }
        
        //파일의 절대경로를 String 으로 반환
        uploadPath = temFolder.getAbsolutePath();

        log.info("--------------------------------");
        log.info(uploadPath);
    }
    
    //파일이 여러 개 들어갈 수 있기 때문에 메개변수로 받을 때 List<MultipartFile> 로 받음
    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException{

        if(files == null || files.size() == 0){
            return List.of();
        }

        List<String> uploadNames = new ArrayList<>();

        for(MultipartFile multipartFile : files){
        	
        	//다른 값과 중복되지 않는 uniqu 한 값을 생성할 때 사용하는 난수 생성기(실제로는 중복된 값이 발생할 수 있음)
            String savedName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

            /* MultipartFile : 인터페이스
             * getName() : 넘어온 파라미터 이름
             * getOriginalFilename() : 업로드 파일명
             * getContentType : 파일의 ContentType
             * getSize() : 파일의 바이트 사이즈
             * getInputStream() : 파일의 내용을 읽기 위한 InputStream 변환
             * transferTo : 파일 저장
             */
            
            //저장할 파일 경로: uploadPath, 저장할 파일의 이름 : savedName
            Path savePath = Paths.get(uploadPath, savedName);

            try {
            	//multipartFile 입력 스트림으로 파일의 내용을 읽고 savePath에 복사
            	//업로드한 파일을 저장 경로에 복사해서 저장
                Files.copy(multipartFile.getInputStream(), savePath);
                
                String contentType = multipartFile.getContentType();
                
                if(contentType != null && contentType.startsWith("image")) {
                	
                	Path thumbnailPath = Paths.get(uploadPath, "s_" + savedName);
                	//썸네일을 지정할 이미지 파일 크기 및 크기가 조절된 썸네일 파일의 경로 지정 
                	Thumbnails.of(savePath.toFile())
                	.size(200, 200)
                	.toFile(thumbnailPath.toFile());
                }
                
                uploadNames.add(savedName);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return uploadNames;
     }
    
    //파일 조회
    //Resource 스프링 프레임워크에서 제공하는 추상화된 리소스 인터페이스, 다양한 유형의 리소스를 표현할 수 있다.
    public ResponseEntity<Resource> getFile(String fileName) {
    	Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
    	
    	/*isReadable() : 리소스를 읽을 수 있는지를 확인
    	 * isFile() : 리소스가 파일인지 확인
    	 * isOpen() : 리소스가 열려있는지 확인
    	 * getDescription() : 전체 경로 포함한 파일 이름 또는 실제 URL
    	 */
    	
    	if(!resource.isReadable()) {
    		//uploadPath + File.separator + "123.jpg" 이 경로의 파일을 가져오는 역할. File.separator는 /를 의미
    		resource = new FileSystemResource(uploadPath + File.separator + "123.jpg");
    	}
    	
    	//HttpHeaders 를 이욯해서 해당 파일의 MIME 유형을 추가
    	//heders 를 통해서 MIME 유형을 추가해 주지 않으면 클라이언트가 서버로 전송하는 데이터의 형식을 정학히 인식할 수 X
    	//-> 파일이나 이미지가 깨짐
    	HttpHeaders headers = new HttpHeaders();
    	
    	try {
    		headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
    	}catch (Exception e) {
    		//internalServerError: 응용프로그램이나 서버 내부에서 오류가 발생했을 때 발생하는 응답 코드를 만드는 메서드
    		return ResponseEntity.internalServerError().build();
    	}
    	return ResponseEntity.ok().headers(headers).body(resource);
    }
    
    public void deleteFiles(List<String> fileNames) {
    	if(fileNames == null || fileNames.size() == 0) {
    		return;
    	}
    	
    	fileNames.forEach(fileName -> {
    		
    		String thumbnailFileName = "s_" + fileNames;
    		Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
    		Path filePath = Paths.get(uploadPath, fileName);
    		
    		try {
    			Files.deleteIfExists(filePath);
    			Files.deleteIfExists(thumbnailPath);
    		}catch(IOException e) {
    			throw new RuntimeException(e.getMessage());
    		}
    		
    		
    	});
    }
}
