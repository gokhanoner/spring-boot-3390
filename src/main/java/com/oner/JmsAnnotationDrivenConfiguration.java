package com.oner;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJndi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerConfigUtils;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.transaction.jta.JtaTransactionManager;

@Profile("1.2.5")
@Configuration
@ConditionalOnClass(EnableJms.class)
class JmsAnnotationDrivenConfiguration {

	@Autowired(required = false)
	private DestinationResolver destinationResolver;

	@Autowired(required = false)
	private JtaTransactionManager transactionManager;

	@Autowired
	private JmsProperties properties;

	@Bean
	@ConditionalOnMissingBean(name = "jmsListenerContainerFactory")
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
			ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPubSubDomain(this.properties.isPubSubDomain());
		if (this.transactionManager != null) {
			factory.setTransactionManager(this.transactionManager);
		}
		else {
			factory.setSessionTransacted(true);
		}
		if (this.destinationResolver != null) {
			factory.setDestinationResolver(this.destinationResolver);
		}
		return factory;
	}

	@EnableJms
	@ConditionalOnMissingBean(name = JmsListenerConfigUtils.JMS_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
	protected static class EnableJmsConfiguration {
	}

	@ConditionalOnJndi
	protected static class JndiConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public DestinationResolver destinationResolver() {
			JndiDestinationResolver resolver = new JndiDestinationResolver();
			resolver.setFallbackToDynamicDestination(true);
			return resolver;
		}

	}

}