package org.jindory.service;

import java.util.ArrayList;
import java.util.List;

import org.jindory.domain.BreakdownReportVO;
import org.jindory.domain.Criteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
@Log4j

public class BreakdownReportServiceTests {

	@Autowired
	private BreakdownReportService breakdownReportService;
	
//	@Test
//	public void testGetBreakdownReportService() {
//		BreakdownReportVO data = new BreakdownReportVO(); 
//		log.info("========================== Test get report ==========================");
//		Long bnum = 50L;
//		data = breakdownReportService.get(bnum);
//		log.info(data);
//		log.info("========================== End get test ==========================");
//	}
	
	@Test
	public void testSearchList() {
		List<BreakdownReportVO> list = new ArrayList<BreakdownReportVO>();
		Criteria cri = new Criteria();
		cri.setAmount(10);
		cri.setPageNum(1);
		cri.setKeyword("바퀴");
		log.info("========================== Test Search report ==========================");
		list = breakdownReportService.searchList(cri);
		list.forEach(data -> log.info(data));
		log.info("========================== End Search report ==========================");
	}
	
//	@Test
//	@Transactional
//	public void testRegister() {
//	log.info("========================== Test Register report ==========================");
//	breakdownReportService.searchList().forEach(result -> log.info(result));
//	log.info("========================== Before Register report ==========================");
//	BreakdownReportVO data = new BreakdownReportVO();
//	data.setBikenum(815417L);
//	data.setBrokenparts("바퀴");
//	data.setBtitle("고장신고");
//	data.setContent("바퀴날이 이상해요");
//	data.setStationid("ST-417");
//	data.setWriter("따릉이 이용자");
//	breakdownReportService.register(data);
//	log.info("========================== After Register report ==========================");
//	breakdownReportService.searchList().forEach(result -> log.info(result));
//	log.info("========================== End Register report ==========================");
//	}
	
//	@Test
//	@Transactional
//	public void testDelete() {
//		Long bnum = 50L;
//		Long resultNum;
//		log.info("========================== Test Delete report ==========================");
//		breakdownReportService.searchList().forEach(result -> log.info(result));
//		log.info("========================== After Register report ==========================");
//		resultNum = breakdownReportService.delete(bnum);
//		log.info("========================== After Register report ==========================");
//		breakdownReportService.searchList().forEach(result -> log.info(result));
//		log.info("========================== End Register report ==========================");
//		log.info("Return value : "+resultNum);
//	}
	
//	@Test
//	public void testSearchBreakdownCount() {
//		Criteria index = new Criteria();
//		index.setPageNum(2);
//		index.setAmount(10);
//		breakdownReportService.searchBreakdownCount(index).forEach(data -> log.info(data));
//	}
	
	@Test
	public void testGetTotalCount() {
		Criteria data = new Criteria();
		
		log.info("Return : "+breakdownReportService.getTotalCount(data));
	}
}
