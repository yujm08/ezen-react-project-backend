package com.springboot.biz.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.biz.dto.PageRequestDTO;
import com.springboot.biz.dto.PageResponseDTO;
import com.springboot.biz.dto.TodoDto;
import com.springboot.biz.service.TodoService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

	private final TodoService todoService;
	
	@GetMapping("/{tno}") //상세보기
	public TodoDto get(@PathVariable(name="tno") Long tno) {
		return todoService.get(tno);
	}
	
	@PostMapping("/") //글 등록
	public Map<String, Long> register(@RequestBody TodoDto todoDTO) {
		System.out.println("TodoDTO : " + todoDTO);
		Long tno = todoService.register(todoDTO);
		return Map.of("TNO", tno);
	}
	
	@GetMapping("/list") //글 목록 조회
	public PageResponseDTO<TodoDto> list(PageRequestDTO pageRequestDTO) {
		return todoService.list(pageRequestDTO);
	}
	
	@PutMapping("/{tno}")
	public Map<String, String> modify(@PathVariable(name="tno") Long tno, @RequestBody TodoDto todoDTO) {
		todoDTO.setTno(tno);
		
		System.out.println("수정 결과 : " + todoDTO);
		todoService.modify(todoDTO);
		return Map.of("result", "성공");
	}
	
	@DeleteMapping
	public Map<String, String> remove(@PathVariable(name="tno") Long tno) {
		System.out.println("삭제 : " + tno);
		todoService.remove(tno);
		return Map.of("결과", "성공");
	}
	
}
