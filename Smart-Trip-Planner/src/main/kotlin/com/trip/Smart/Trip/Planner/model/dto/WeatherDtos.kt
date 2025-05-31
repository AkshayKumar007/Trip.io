package com.trip.Smart.Trip.Planner.model.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

/**
 * DTO for weather forecast request
 */
data class WeatherForecastRequest(
    @field:NotNull(message = "Destination cannot be null")
    val location: String,
    
    @field:NotNull(message = "Start date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val startDate: LocalDate,
    
    @field:NotNull(message = "End date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val endDate: LocalDate,
    
    val units: String = "metric"
)

/**
 * DTO for weather forecast response
 */
data class WeatherForecastResponse(
    val location: String,
    
    val country: String,
    
    val lat: Double,
    
    val lon: Double,
    
    val dailyForecasts: List<DailyForecast>,
    
    val current: CurrentWeather? = null
)

/**
 * DTO for daily weather forecast
 */
data class DailyForecast(

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val date: LocalDate,

    val temp: Temperature,
    

    val feelsLike: FeelsLike,
    
    val pressure: Int,
    
    val humidity: Int,
    
    val windSpeed: Double,
    
    val windDeg: Int,
    
    val weather: List<WeatherCondition>,
    
    val pop: Double,
    

    val uvi: Double,
    

    val clouds: Int,

    @Schema(description = "Visibility in meters")
    val visibility: Int?,
    
    @Schema(description = "Probability of rain (0-1)")
    val rain: Double?,
    
    @Schema(description = "Probability of snow (0-1)")
    val snow: Double?,
    
    @Schema(description = "Sunrise time in UTC")
    val sunrise: Long,
    
    @Schema(description = "Sunset time in UTC")
    val sunset: Long
)

/**
 * DTO for temperature data
 */
data class Temperature(
    @Schema(description = "Morning temperature")
    val morn: Double,
    
    @Schema(description = "Day temperature")
    val day: Double,
    
    @Schema(description = "Evening temperature")
    val eve: Double,
    
    @Schema(description = "Night temperature")
    val night: Double,
    
    @Schema(description = "Minimum daily temperature")
    val min: Double?,
    
    @Schema(description = "Maximum daily temperature")
    val max: Double?
)

/**
 * DTO for 'feels like' temperature data
 */
data class FeelsLike(
    @Schema(description = "Morning 'feels like' temperature")
    val morn: Double,
    
    @Schema(description = "Day 'feels like' temperature")
    val day: Double,
    
    @Schema(description = "Evening 'feels like' temperature")
    val eve: Double,
    
    @Schema(description = "Night 'feels like' temperature")
    val night: Double
)

/**
 * DTO for weather condition
 */
data class WeatherCondition(
    @Schema(description = "Weather condition ID")
    val id: Int,
    
    @Schema(description = "Group of weather parameters")
    val main: String,
    
    @Schema(description = "Weather condition within the group")
    val description: String,
    
    @Schema(description = "Weather icon ID")
    val icon: String
)

/**
 * DTO for current weather data
 */
data class CurrentWeather(
    val dt: Long,
    
    val sunrise: Long,
    
    val sunset: Long,
    
    val temp: Double,
    
    val feelsLike: Double,
    
    val pressure: Int,
    
    val humidity: Int,
    
    val dewPoint: Double,
    

    val uvi: Double,
    
    val clouds: Int,
    
    val visibility: Int,
    
    val windSpeed: Double,
    
    val windDeg: Int,
    
    val windGust: Double?,
    
    val weather: List<WeatherCondition>,
    
    val rain: Double?,
    

    val snow: Double?
)

/**
 * DTO for weather alert
 */
data class WeatherAlert(

    val senderName: String,
    

    val event: String,
    

    val start: Long,
    

    val end: Long,
    
    val description: String,
    
    val tags: List<String>
)
