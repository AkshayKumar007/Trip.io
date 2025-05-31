package com.trip.Smart.Trip.Planner.model.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "weather_forecasts")
data class WeatherForecastEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "trip_id", nullable = false)
    var tripId: Long = 0,

    @Column(name = "forecast_date", nullable = false)
    var forecastDate: LocalDate = LocalDate.now(),

    @Column(nullable = false)
    var condition: String = "",

    @Column(name = "temperature_high", nullable = false)
    var temperatureHigh: Double = 0.0,

    @Column(name = "temperature_low", nullable = false)
    var temperatureLow: Double = 0.0,

    @Column(nullable = true)
    var humidity: Int? = null,

    @Column(name = "wind_speed", nullable = true)
    var windSpeed: Double? = null,

    @Column(name = "precipitation_probability", nullable = true)
    var precipitationProbability: Int? = null,

    @Column(name = "uv_index", nullable = true)
    var uvIndex: Double? = null,

    @Column(name = "last_updated", nullable = false)
    var lastUpdated: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @Column(columnDefinition = "TEXT")
    var notes: String? = null
)