package com.oner;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class JMSTransctionTest {

	@Autowired
	private Sender sender;
	@Autowired
	private DBService dbService;
	
	@Test
	public void test125Release() throws InterruptedException {
		Notification notification = new Notification(0, "notification to deliver correctly");
		sender.convertAndSendMessage("test_125", notification);
		Thread.sleep(2000);
		assertEquals(1, dbService.getNotificationCount(0, "125"));
	}

	@Test
	public void test124Release() throws InterruptedException {
		Notification notification = new Notification(0, "notification to deliver correctly");
		sender.convertAndSendMessage("test_124", notification);
		Thread.sleep(2000);
		assertEquals(1, dbService.getNotificationCount(0, "124"));
	}
}
