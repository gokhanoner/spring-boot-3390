package com.oner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsUtils;
import org.springframework.stereotype.Component;

@Component
public class Listener {

	private static Logger logger = LoggerFactory.getLogger(Listener.class);

	@Autowired
	private DBService dbService;

	@JmsListener(destination = "test_125")
	public void listener125(Message message) {
		try {
			Notification notification = (Notification) ((ObjectMessage) message).getObject();
			logger.info("Received notification | Id: " + notification.getId() + " | Redelivery: " + getDeliveryNumber(message));
			dbService.saveToBD(notification, "125");
			checkPostprocessException(message, notification);
		} catch (JMSException e) {
			throw JmsUtils.convertJmsAccessException(e);
		}

	}

	@JmsListener(destination = "test_124", containerFactory = "jmsListenerContainerFactory_124")
	public void listener124(Message message) {
		try {
			Notification notification = (Notification) ((ObjectMessage) message).getObject();
			logger.info("Received notification | Id: " + notification.getId() + " | Redelivery: " + getDeliveryNumber(message));
			dbService.saveToBD(notification, "124");
			checkPostprocessException(message, notification);
		} catch (JMSException e) {
			throw JmsUtils.convertJmsAccessException(e);
		}

	}
	
	/**
	 * Execution failure after saving the message to the DB
	 * 
	 * @param message
	 * @param notification
	 * @throws JMSException
	 */
	private void checkPostprocessException(Message message, Notification notification) throws JMSException {
		if (getDeliveryNumber(message) < 2) {
			throw new RuntimeException("error after processing message");
		}
	}

	/**
	 * Returns how many times the message has been delivered
	 * 
	 * @param message
	 * @return
	 * @throws JMSException
	 */
	private int getDeliveryNumber(Message message) throws JMSException {
		return message.getIntProperty("JMSXDeliveryCount");
	}

}
