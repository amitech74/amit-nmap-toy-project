package amit.nmapscans.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Component("SpringUtils")
public class SpringUtils implements ApplicationContextAware {

	public static final String NMapExecutor = "NMapExecutor";

	private static ApplicationContext applicationContext;

	private static final Logger logger = LoggerFactory.getLogger(SpringUtils.class);

	public static void publishEvent(ApplicationEvent event) {
		Assert.notNull(applicationContext, "ApplicationContext must not be null!");
		applicationContext.publishEvent(event);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtils.applicationContext = applicationContext;
	}

	public static <T> T getBean(final String beanRefName, final Class<T> beanClass) {
		T bean = null;

		Assert.notNull(applicationContext, "ApplicationContext must not be null!");

		try {
			BeanFactory factory = applicationContext;
			bean = factory.getBean(beanRefName, beanClass);
		} catch (NoSuchBeanDefinitionException ex) {
			logger.error("No bean found with name {}", beanRefName, ex);
		}

		return bean;
	}


}
