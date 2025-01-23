package com.springboot.biz.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.biz.domain.Todo;
import com.springboot.biz.dto.PageRequestDTO;
import com.springboot.biz.dto.PageResponseDTO;
import com.springboot.biz.dto.TodoDto;
import com.springboot.biz.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {

	private final TodoRepository todoRepository;
	private final ModelMapper modelMapper;

	// 글 등록
	public Long register(TodoDto todoDto) {
		// 클라이언트로부터 입력받은 todoDto를 Todo(Entity)에 매핑
		Todo todo = modelMapper.map(todoDto, Todo.class);
		Todo SavedTodo = todoRepository.save(todo);
		// 저장된 후 저장된 객체의 tno 값을 리턴받게 했다
		return SavedTodo.getTno();
	}

	// 상세보기
	public TodoDto get(Long tno) {
		Optional<Todo> result = todoRepository.findById(tno);
		// orElseThrow()는 Optional 클래스에서 제공하는 메서드로, 값이 존재하지 않을 때 예외를 발생시킨다
		Todo todo = result.orElseThrow();
		// 검색된 todo 엔티티 클래스를 Dto 클래스로 변환해서 클라이언트에 리턴
		TodoDto dto = modelMapper.map(todo, TodoDto.class);
		return dto;
	}

	// 글 수정
	public void modify(TodoDto todoDto) {
		Optional<Todo> result = todoRepository.findById(todoDto.getTno());

		Todo todo = result.orElseThrow();

		todo.setTitle(todoDto.getTitle());
		todo.setWriter(todoDto.getWriter());
		todo.setComplete(todoDto.isComplete());

		todoRepository.save(todo);
	}

	// 글 삭제
	public void remove(Long tno) {
		todoRepository.deleteById(tno);
	}

	// 글 목록 보기
	public PageResponseDTO<TodoDto> list(PageRequestDTO pageRequestDTO) {

		Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
				Sort.by("tno").descending());

		Page<Todo> result = todoRepository.findAll(pageable);

		List<TodoDto> dtoList = result.getContent().stream().map(todo -> modelMapper.map(todo, TodoDto.class))
				.collect(Collectors.toList());

		long totalCount = result.getTotalElements();

		PageResponseDTO<TodoDto> responseDTO = PageResponseDTO.<TodoDto>withAll().dtoList(dtoList)
				.pageRequestDTO(pageRequestDTO).totalCount(totalCount).build();
		return responseDTO;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
