package org.jindory.domain;

import java.sql.Date;

import lombok.Data;

@Data 
public class BreakdownReportVO {
	private Long bnum;
	private String btitle;
	private Long bikenum;
	private String stationid;
	private String brokenparts;
	private String content;
	private String writer;
	private Date createDate;
	private Date updateDate;
}
