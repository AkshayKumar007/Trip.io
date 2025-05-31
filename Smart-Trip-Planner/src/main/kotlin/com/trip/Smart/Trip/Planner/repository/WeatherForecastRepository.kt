package com.trip.Smart.Trip.Planner.repository

import com.trip.Smart.Trip.Planner.model.entity.WeatherForecastEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface WeatherForecastRepository : BaseRepository<WeatherForecastEntity, Long> {
    
    fun findByTripId(tripId: Long): List<WeatherForecastEntity>
    
    fun findByTripIdAndForecastDateBetween(
        tripId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<WeatherForecastEntity>
    
    @Query("""
        SELECT w FROM WeatherForecastEntity w 
        WHERE w.tripId = :tripId 
        AND w.forecastDate = :date
    """)
    fun findByTripIdAndDate(
        @Param("tripId") tripId: Long,
        @Param("date") date: LocalDate
    ): WeatherForecastEntity?
    
    @Query("""
        SELECT w FROM WeatherForecastEntity w 
        WHERE w.tripId = :tripId 
        AND w.forecastDate BETWEEN :startDate AND :endDate
        ORDER BY w.forecastDate ASC
    """)
    fun findWeatherForDateRange(
        @Param("tripId") tripId: Long,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<WeatherForecastEntity>
    
    @Query("""
        SELECT w FROM WeatherForecastEntity w 
        WHERE w.tripId = :tripId 
        AND w.forecastDate >= CURRENT_DATE
        ORDER BY w.forecastDate ASC
        LIMIT 1
    """)
    fun findNextDayForecast(@Param("tripId") tripId: Long): WeatherForecastEntity?
    
    @Query("""
        SELECT DISTINCT w.condition 
        FROM WeatherForecastEntity w 
        WHERE w.tripId = :tripId 
        AND w.forecastDate BETWEEN :startDate AND :endDate
    """)
    fun findWeatherConditionsForTrip(
        @Param("tripId") tripId: Long,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<String>
    
    fun deleteByTripId(tripId: Long)
    
    @Query("""
        DELETE FROM WeatherForecastEntity w 
        WHERE w.tripId = :tripId 
        AND w.forecastDate < :beforeDate
    """)
    fun deleteOldForecasts(
        @Param("tripId") tripId: Long,
        @Param("beforeDate") beforeDate: LocalDate
    )
}
