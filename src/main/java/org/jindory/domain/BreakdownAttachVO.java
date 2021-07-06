package org.jindory.domain;

import lombok.Data;

@Data
public class BreakdownAttachVO {
	private String uuid;
	private String uploadPath;
	private String fileName;
	private boolean fileType;
	private Long bnum;
}
