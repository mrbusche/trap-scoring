package trap.dao;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
class EmbeddedMariaDbConfig {

    @Value("${app.mariaDB4j.databaseName}")
    String databaseName;
    @Value("${spring.datasource.username}")
    String datasourceUsername;
    @Value("${spring.datasource.password}")
    String datasourcePassword;
    @Value("${spring.datasource.driver-class-name}")
    String datasourceDriver;

    @Bean
    static MariaDB4jSpringService mariaDB4jSpringService() {
        return new MariaDB4jSpringService();
    }

    @Bean
    DataSource dataSource(MariaDB4jSpringService mariaDB4jSpringService) throws ManagedProcessException {
        //Create our database with default root user and no password
        mariaDB4jSpringService.getDB().createDB(databaseName);

        DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();

        return DataSourceBuilder.create().username(datasourceUsername).password(datasourcePassword).url(config.getURL(databaseName) + "?allowLoadLocalInfile=true").driverClassName(datasourceDriver).build();
    }
}
