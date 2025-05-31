package com.trip.Smart.Trip.Planner.config

import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.sql.DataSource

@Configuration
class FlywayConfig(
    private val dataSource: DataSource,
    @Value("\${spring.flyway.enabled:true}") private val flywayEnabled: Boolean,
    @Value("\${spring.flyway.locations:classpath:db/migration}") private val locations: String,
    @Value("\${spring.flyway.baseline-on-migrate:false}") private val baselineOnMigrate: Boolean
) {
    private val logger = LoggerFactory.getLogger(FlywayConfig::class.java)

    @Bean(initMethod = "migrate")
    @Profile("!test")
    fun flyway(): Flyway {
        if (!flywayEnabled) {
            logger.info("Flyway migrations are disabled")
            return Flyway.configure().dataSource(dataSource).load()
        }

        logger.info("Running Flyway migrations...")
        
        return Flyway.configure()
            .dataSource(dataSource)
            .locations(*locations.split(","))
            .baselineOnMigrate(baselineOnMigrate)
            .baselineVersion("0")
            .outOfOrder(true)
            .validateOnMigrate(true)
            .load()
            .also { flyway ->
                val migrations = flyway.info().all()
                logger.info("Found ${migrations.size} migrations to apply")
                migrations.forEach { migration ->
                    logger.info("Migration: ${migration.version} - ${migration.description} (${migration.state})")
                }
            }
    }
}
