package org.jindory.dataSource;

import java.util.List;
import java.util.Map;

import org.jindory.domain.BreakdownReportVO;
import org.jindory.domain.Criteria;

import lombok.Data;
import lombok.extern.log4j.Log4j;

public interface BreakdownReport {
	
	public BreakdownReportVO getBreakdownReport(Long bnum);
	
	public int getTotalCount(Criteria cri);
	
	public List<BreakdownReportVO> searchBreakdownReport(Criteria cri);
	
	public Long insertBreakdownReport(BreakdownReportVO data);
	
	public Long insertCustomBnumBreakdownReport(BreakdownReportVO data);
	
	public Long updateBreakdownReport(BreakdownReportVO data);
	
	public Long deleteBreakdownReport(Long bnum);
	
	public List<Map<String,Integer>> searchBreakdownCount(Criteria index);
	
}
