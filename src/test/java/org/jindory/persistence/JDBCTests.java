package org.jindory.persistence;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
@Log4j
public class JDBCTests {
	
	@Test
	public void testConnection()throws Exception {
		Class clz = Class.forName("oracle.jdbc.driver.OracleDriver");
		log.info(clz);
		
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",
				"publicdata_portal","publicdata_portal");
		
		log.info(con);
		con.close();
	}
}
