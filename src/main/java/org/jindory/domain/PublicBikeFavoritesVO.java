package org.jindory.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PublicBikeFavoritesVO {
	String memberId;
	String stationId;
	String stationName;
	Integer noticeBikeNum;
	Integer noticeScope;
	String noticeTime1;
	String noticeTime2;
	String noticeTime3;
	String effectiveDate;
	String activeYn;
	Date createDate;
	Date updateDate;
}
