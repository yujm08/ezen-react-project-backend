package com.springboot.biz;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.biz.domain.Todo;
import com.springboot.biz.domain.Todo.TodoBuilder;
import com.springboot.biz.dto.TodoDto;
import com.springboot.biz.repository.TodoRepository;
import com.springboot.biz.service.TodoService;

@SpringBootTest
class BackendApplicationTests {

	@Autowired
	private TodoRepository todoRepository;
	@Autowired
	private TodoService todoservice;

	@Test
	void contextLoads() {

		Long tno = 100L;

		TodoDto dto = todoservice.get(tno);
		System.out.println(dto.getTitle());
		System.out.println(dto.getWriter());

		/*
		 * for(int i = 1; i <=100; i++) { Todo todo = Todo.builder() .title("제목" + i)
		 * .writer("사용자") .dueTime(LocalDate.now()) .build(); todoRepository.save(todo);
		 * 
		 * }
		 */

	}

}
