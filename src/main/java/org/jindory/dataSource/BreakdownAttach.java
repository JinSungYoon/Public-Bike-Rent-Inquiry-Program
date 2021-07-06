package org.jindory.dataSource;

import java.util.List;

import org.jindory.domain.BreakdownAttachVO;

public interface BreakdownAttach {
	
	public void insertBreakdownAttach(BreakdownAttachVO vo);
	
	public void deleteBreakdownAttach(String uuid);
	
	public List<BreakdownAttachVO> searchByBnum(Long bno);
	
	public boolean deleteAllAttach(Long bnum);
}
