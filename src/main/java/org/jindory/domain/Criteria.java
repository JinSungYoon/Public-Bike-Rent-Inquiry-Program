package org.jindory.domain;

import lombok.Data;

@Data
public class Criteria {
	private int pageNum;
	private int amount;
	private String keyword;
	
	public Criteria() {
		this(1,10,"");
	}
	
	public Criteria(int pageNum,int amount,String keyword) {
		this.pageNum = pageNum;
		this.amount = amount;
		this.keyword = keyword;
	}
	
}
