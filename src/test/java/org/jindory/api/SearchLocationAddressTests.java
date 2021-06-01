package org.jindory.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class SearchLocationAddressTests {
	
//	@Autowired
//	public SearchLocationAddress placeApi;
//	
	@Test
	public void testGetLocationAddressResultJson() throws Exception{
		
		String location = "증산역";
		
		SearchLocationAddress.getLocationAddressResultJson(location).forEach(data -> log.info(data));
		
		//forEach(board->log.info(board))
	}
}
