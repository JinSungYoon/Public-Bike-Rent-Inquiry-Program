package org.jindory.domain;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class PublicBikeMemberVO {
	private String memberId;
	private String memberPw;
	private String memberName;
	private String memberGender;
	private String memberPhone;
	private String memberAddress;
	private String memberBerth;
	private String memberRole;
	private String memberActiveYn;
	private Date   createDate;
	private Date   updateDate;
	private List<PublicBikeMemberAuthVO> authList;
}
