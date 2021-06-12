package org.jindory.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jindory.dataSource.BreakdownReport;
import org.jindory.domain.BreakdownReportVO;
import org.jindory.domain.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
@AllArgsConstructor
public class BreakdownReportServiceImpl implements BreakdownReportService {

	@Autowired
	private BreakdownReport breakdownReport;
	
	@Override
	public Long register(BreakdownReportVO data) {
		Long newNum;
		newNum = breakdownReport.insertBreakdownReport(data);
		return newNum;
	}

	@Override
	public BreakdownReportVO get(Long bnum) {
		BreakdownReportVO result = new BreakdownReportVO();
		result = breakdownReport.getBreakdownReport(bnum);
		return result;
	}

	@Override
	public Long modify(BreakdownReportVO data) {
		Long returnNum;
		returnNum = breakdownReport.updateBreakdownReport(data);
		return returnNum;
	}

	@Override
	public Long delete(Long bnum) {
		Long returnNum;
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

}
