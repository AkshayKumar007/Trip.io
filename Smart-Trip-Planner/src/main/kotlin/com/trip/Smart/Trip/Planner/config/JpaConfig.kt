package com.trip.Smart.Trip.Planner.config

import com.trip.Smart.Trip.Planner.repository.BaseRepository
import com.trip.Smart.Trip.Planner.repository.BaseRepositoryImpl
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableJpaRepositories(
    basePackages = ["com.trip.Smart.Trip.Planner.repository"],
    repositoryBaseClass = BaseRepositoryImpl::class
)
@EnableTransactionManagement
class JpaConfig
