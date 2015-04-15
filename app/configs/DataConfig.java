package configs;

import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import play.Logger;
import play.Play;

import java.util.HashMap;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DataConfig {

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		String showSql = ConfigFactory.load().getString("showsql");
		vendorAdapter.setShowSql(showSql!=null?Boolean.valueOf(showSql):false);
		//vendorAdapter.setShowSql(false);
		vendorAdapter.setGenerateDdl(false);
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setPackagesToScan(new String[] { "amit.nmapscans.entities" });
		entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
		entityManagerFactory.setDataSource(dataSource());
		entityManagerFactory.setJpaProperties(additionalProperties());
		entityManagerFactory.afterPropertiesSet();
		return entityManagerFactory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory());
		return transactionManager;
	}

	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(Play.application().configuration().getString("db.default.driver"));
		dataSource.setUrl(Play.application().configuration().getString("db.default.url"));
		dataSource.setUsername(Play.application().configuration().getString("db.default.user"));
		dataSource.setPassword(Play.application().configuration().getString("db.default.pass"));
		return dataSource;
	}

	Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.cache.use_second_level_cache", 		"false");
		properties.setProperty("hibernate.cache.use.query_cache", 					"false");
/*
		properties.setProperty("hibernate.max_fetch_depth", 									"1");
		properties.setProperty("hibernate.show_sql", 													"false");
		properties.setProperty("hibernate.max_fetch_depth", 									"1");
		properties.setProperty("hibernate.cache.use_structured_entries", 			"true");
*/
		return properties;
	}
}