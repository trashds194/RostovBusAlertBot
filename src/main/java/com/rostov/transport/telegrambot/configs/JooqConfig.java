package com.rostov.transport.telegrambot.configs;

import javax.sql.DataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@Configuration
public class JooqConfig {

    @Bean
    public DefaultDSLContext dsl(DataSourceConnectionProvider connectionProvider) {
        return new DefaultDSLContext(configuration(connectionProvider));
    }

    @Bean
    public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    private DefaultConfiguration configuration(DataSourceConnectionProvider connectionProvider) {
        var dsl = new DefaultConfiguration();
        dsl.set(connectionProvider);
        dsl.set(SQLDialect.POSTGRES);
        return dsl;
    }
}
