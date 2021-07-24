package org.jindory.domain;

import java.sql.Date;

import lombok.Data;

@Data
public class PublicBikeMemberAuthVO{
	private String memberId;
	private String auth;
	private Date createDate;
	private Date updateDate;
}
