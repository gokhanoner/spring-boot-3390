package com.oner;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("1.2.4")
//@ActiveProfiles("1.2.5")
public class JMSTransctionTest {

	@Autowired
	private Sender sender;
	@Autowired
	private DBService dbService;
	
	@Test
	public void testCorrectMessage() throws InterruptedException {
		Notification notification = new Notification(0, "notification to deliver correctly");
		sender.convertAndSendMessage("test", notification);
		Thread.sleep(6000);
		assertEquals(1, dbService.getNotificationCount(0));
	}
	
}
