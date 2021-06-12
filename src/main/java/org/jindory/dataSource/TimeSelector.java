package org.jindory.dataSource;

import org.apache.ibatis.annotations.Select;

public interface TimeSelector {

	@Select("select sysdate from dual")
	String getTime();
	
}
