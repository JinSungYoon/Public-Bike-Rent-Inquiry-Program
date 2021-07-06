package org.jindory.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jindory.dataSource.BreakdownAttach;
import org.jindory.dataSource.BreakdownReport;
import org.jindory.domain.BreakdownAttachVO;
import org.jindory.domain.BreakdownReportVO;
import org.jindory.domain.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
@AllArgsConstructor
public class BreakdownReportServiceImpl implements BreakdownReportService {

	@Autowired
	private BreakdownReport breakdownReport;
	
	@Autowired
	private BreakdownAttach breakdownAttach;
	
	@Transactional
	@Override
	public Long register(BreakdownReportVO data) {
		Long newNum;
		newNum = breakdownReport.insertBreakdownReport(data);
		
		if(data.getAttachList() == null || data.getAttachList().size()<=0) {
			return 0L;
		}
		
		data.getAttachList().forEach(attach->{
			attach.setBnum(breakdownReport.getMaxNum());
			breakdownAttach.insertBreakdownAttach(attach);
		});
		
		return newNum;
	}

	@Override
	public BreakdownReportVO get(Long bnum) {
		BreakdownReportVO result = new BreakdownReportVO();
		result = breakdownReport.getBreakdownReport(bnum);
		
		return result;
	}

	@Transactional
	@Override
	public Long modify(BreakdownReportVO data) {
		Long returnNum;
		returnNum = breakdownReport.updateBreakdownReport(data);
		
		if(data.getAttachList() == null || data.getAttachList().size()<=0) {
			return 0L;
		}
		
		breakdownAttach.deleteAllAttach(data.getBnum());
		
		data.getAttachList().forEach(attach->{
			attach.setBnum(data.getBnum());
			breakdownAttach.insertBreakdownAttach(attach);
			log.info("Delete File : "+attach.getFileName());
		});
		
		return returnNum;
	}
	
	@Transactional
	@Override
	public Long delete(Long bnum) {
		Long returnNum;
		// bnum과 관련된 첨부파일을 먼저 삭제해야한다(ORA-02292: 무결성 제약조건(ARADMIN1.SYS_C0036932)이 위배되었습니다- 자식 레코드가 발견을 피하기 위해서 자식 레코드 먼저 삭제 후 원테이블 데이터 제거)
		breakdownAttach.deleteAllAttach(bnum);
		returnNum = breakdownReport.deleteBreakdownReport(bnum);
		
		return returnNum;
	}

	@Override
	public List<BreakdownReportVO> searchList(Criteria cri) {
		List<BreakdownReportVO> list = new ArrayList<BreakdownReportVO>();
		list = breakdownReport.searchBreakdownReport(cri);
		return list;
	}

	@Override
	public List<Map<String, Integer>> searchBreakdownCount(Criteria index) {
		
		List<Map<String,Integer>> list = new ArrayList<Map<String,Integer>>();
		list = breakdownReport.searchBreakdownCount(index);
		
		return list;
	}

	@Override
	public int getTotalCount(Criteria cri) {
		int count = breakdownReport.getTotalCount(cri);
		return count;
	}

	@Override
	public Long getMaxNum() {
		Long returnNum = breakdownReport.getMaxNum();
		return returnNum;
	}

	@Override
	public List<BreakdownAttachVO> getAttachList(Long bnum) {
		log.info("get Attach list by bnum" + bnum);
		return breakdownAttach.searchByBnum(bnum);
	}

}
