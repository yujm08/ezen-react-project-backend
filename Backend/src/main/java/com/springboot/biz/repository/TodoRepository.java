package com.springboot.biz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.biz.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long>{

}
