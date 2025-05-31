package com.trip.Smart.Trip.Planner.config

import com.zaxxer.hikari.HikariDataSource
import org.hibernate.cfg.AvailableSettings
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.trip.Smart.Trip.Planner.repository"],
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager"
)
class DatabaseConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    fun dataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.configuration")
    fun dataSource(
        @Qualifier("dataSourceProperties") dataSourceProperties: DataSourceProperties
    ): DataSource {
        return dataSourceProperties.initializeDataSourceBuilder()
            .type(HikariDataSource::class.java)
            .build()
    }

    @Bean
    @Primary
    fun entityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("dataSource") dataSource: DataSource
    ): LocalContainerEntityManagerFactoryBean {
        val properties = Properties().apply {
            this[AvailableSettings.HBM2DDL_AUTO] = "none"
            this[AvailableSettings.SHOW_SQL] = "false"
            this[AvailableSettings.FORMAT_SQL] = "true"
            this[AvailableSettings.USE_SQL_COMMENTS] = "true"
            this[AvailableSettings.GENERATE_STATISTICS] = "false"
            this[AvailableSettings.USE_SECOND_LEVEL_CACHE] = "true"
            this[AvailableSettings.USE_QUERY_CACHE] = "true"
            this[AvailableSettings.CACHE_REGION_FACTORY] = "org.hibernate.cache.jcache.JCacheRegionFactory"
            this[AvailableSettings.JAKARTA_SHARED_CACHE_MODE] = "ENABLE_SELECTIVE"
            this[AvailableSettings.STATEMENT_BATCH_SIZE] = "100"
            this[AvailableSettings.ORDER_INSERTS] = "true"
            this[AvailableSettings.ORDER_UPDATES] = "true"
            this[AvailableSettings.BATCH_VERSIONED_DATA] = "true"
        }

        return builder
            .dataSource(dataSource)
            .packages("com.trip.Smart.Trip.Planner.model.entity")
            .persistenceUnit("default")
            .properties(properties as Map<String?, *>?)
            .build()
    }

    @Bean
    @Primary
    fun transactionManager(
        @Qualifier("entityManagerFactory") entityManagerFactory: jakarta.persistence.EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}
