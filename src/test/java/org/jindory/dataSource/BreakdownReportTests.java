package org.jindory.dataSource;

import java.util.Map;

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
public class BreakdownReportTests {

	@Autowired
	private BreakdownReport BreakdownReports;
	
//	@Test
//	public void getBreakdownReport() {
//		Long bnum = 3L;
//		log.info("==================================================");
//		log.info(BreakdownReports.getBreakdownReport(bnum));
//		log.info("==================================================");
//	}
//	
//	@Test
//	public void searchBreakdownReport() {
//		
//		Criteria cri = new Criteria();
//		
//		cri.setAmount(10);
//		cri.setPageNum(1);
//		cri.setKeyword("바퀴");
//		log.info("==================================================");
//		BreakdownReports.searchBreakdownReport(cri).forEach(data -> log.info(data));
//		log.info("==================================================");
//	}
	
//	@Test
//	public void insertBreakdownReport() {
		
//		BreakdownReportVO data = new BreakdownReportVO();
//		data.setBtitle("두번째 고장신고");
//		data.setBikenum(81595L);
//		data.setBrokenparts("타이어");
//		data.setStationid("ST-18");
//		data.setContent("타이어에 바람이 빠졌어요");
//		data.setWriter("newbi");
//		BreakdownReports.insertBreakdownReport(data);
//		log.info(data);
//		log.info("===================== After Insert =============================");
//		BreakdownReports.searchBreakdownReport().forEach(result -> log.info(result));
//		log.info("================================================================");
//	}

//	@Test
//	public void insertCustomBnumBreakdownReport() {
//		Long loop=55L;
//		//for(Long loop=18L;loop<25L;loop++) {
//		BreakdownReportVO data = new BreakdownReportVO();
//		data.setBtitle(loop+"번째 고장신고");
//		data.setBikenum(81150L);
//		data.setBrokenparts("바퀴");
//		data.setStationid("ST-"+loop);
//		data.setContent("펑크가 난것 같아요");
//		data.setWriter("따릉이 이용자");
//		data.setBnum(loop);
//		BreakdownReports.insertCustomBnumBreakdownReport(data);
//		log.info("===================== After Insert =============================");
//		//BreakdownReports.searchBreakdownReport(50L).forEach(result -> log.info(result));
//		//BreakdownReports.searchBreakdownReport().forEach(result -> log.info(result));
//		log.info("================================================================");
//		//}
//	}
	
//	@Test
//	public void updateBreakdownReport() {
//		BreakdownReportVO data = new BreakdownReportVO();
//		data.setBtitle("다섯번째 고장신고");
//		data.setBikenum(81123L);
//		data.setBrokenparts("단말기");
//		data.setStationid("ST-76");
//		data.setContent("단말기 인식이 안 돼요");
//		data.setBnum(5L);
//		BreakdownReports.updateBreakdownReport(data);
//		//log.info(data);
//		log.info("===================== After Update =============================");
//		BreakdownReports.getBreakdownReport(4L).forEach(result -> log.info(result));
//		log.info("================================================================");
//	}

//	@Test
//	public void deleteBreakdowReport() {
//		log.info("===================== Before delete =============================");
//		BreakdownReports.searchBreakdownReport();
//		log.info("===================== Delete =============================");
//		BreakdownReports.deleteBreakdownReport(6L);
//		log.info("===================== After delete =============================");
//		BreakdownReports.searchBreakdownReport();
//		log.info("===================== End =============================");
//	}

//	@Test
//	public void testSearchBreakdownCount() {
//		Criteria index = new Criteria();
//		index.setPageNum(2);
//		index.setAmount(10);
//		BreakdownReports.searchBreakdownCount(index).forEach(data->log.info(data));
//	}
	
	@Test
	public void testGetTotalCount() {
		Criteria data = new Criteria();
		data.setKeyword("바퀴");
		log.info("Total count : "+BreakdownReports.getTotalCount(data));
	}
	
}

