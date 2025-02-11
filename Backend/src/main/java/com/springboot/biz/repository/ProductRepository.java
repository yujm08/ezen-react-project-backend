package com.springboot.biz.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.biz.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	@EntityGraph(attributePaths = "imageList")
	@Query("SELECT p FROM Product p WHERE p.pno = :pno")
	Optional<Product> selectOne(@Param("pno") Long pno);
	
	@Modifying
	@Query("UPDATE Product p SET p.delflag = :flag WHERE p.pno = :pno")
	void updateToDelete(@Param("pno") Long pno, @Param("flag") boolean flag);
	
	@Query("SELECT p, pi FROM Product p LEFT JOIN p.imageList pi WHERE pi.ord = 0 AND p.delflag = false")
	Page<Object[]> selectList(Pageable pageable);
}
