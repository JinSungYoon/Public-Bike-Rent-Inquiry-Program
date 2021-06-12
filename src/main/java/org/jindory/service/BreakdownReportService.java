package org.jindory.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jindory.domain.BreakdownReportVO;
import org.jindory.domain.Criteria;

public interface BreakdownReportService {
	
	public Long register(BreakdownReportVO data);
	public int  getTotalCount(Criteria cri);
	public BreakdownReportVO get(Long bnum);
	public Long modify(BreakdownReportVO data);
	public Long delete(Long bnum);
	public List<BreakdownReportVO> searchList(Criteria cri);
	public List<Map<String,Integer>> searchBreakdownCount(Criteria index);
	
}
