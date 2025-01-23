package com.springboot.biz.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Data;

@Data
public class PageResponseDTO<E> {

	private List<E> dtoList;
	
	private List<Integer> pageNumList;
	
	private PageRequestDTO pageRequestDTO;
	
	private boolean prev, next;
	
	private int totalCount, prevPage, nextPage, totalPage, current;
	
	
	
	
	
	
	@Builder(builderMethodName = "withAll")
	public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount) {
		
		this.dtoList = dtoList;
		this.pageRequestDTO = pageRequestDTO;
		this.totalCount = (int) totalCount;
		
		int end = (int) ((Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10);
		
		int start = end - 9;
		
		int last = (int) (Math.ceil(totalCount/(double)pageRequestDTO.getSize()) );
		
		end = end > last ? last : end;
		
		System.out.println("Start: " + start + ", End: " + end + ", Last: " + last);
		
		this.prev = pageRequestDTO.getPage() > 1;
		
		this.next = pageRequestDTO.getPage() < last;
		
		this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect((Collectors.toList()));		
		
		if(prev) {
			this.prevPage = pageRequestDTO.getPage() - 1; // 현재 페이지에서 -1
		}
		
		if(next) {
			this.nextPage = pageRequestDTO.getPage() + 1; //현재 페이지에서 +1
		}
		
		this.totalPage = this.pageNumList.size();
		
		this.current = pageRequestDTO.getPage();
		
	}
}
















