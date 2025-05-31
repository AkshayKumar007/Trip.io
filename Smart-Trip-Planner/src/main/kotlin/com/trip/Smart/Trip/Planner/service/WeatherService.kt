package com.trip.Smart.Trip.Planner.service

import com.trip.Smart.Trip.Planner.model.dto.CurrentWeather
import com.trip.Smart.Trip.Planner.model.dto.WeatherForecastRequest
import com.trip.Smart.Trip.Planner.model.dto.WeatherForecastResponse
import com.trip.Smart.Trip.Planner.model.dto.*
import java.time.LocalDate

/**
 * Service interface for weather-related operations
 */
interface WeatherService {
    
    /**
     * Get weather forecast for a specific location and date range
     * @param request Weather forecast request containing location and date range
     * @return Weather forecast response with daily forecasts
     */
    fun getWeatherForecast(request: WeatherForecastRequest): WeatherForecastResponse
    
    /**
     * Get current weather for a specific location
     * @param location City and country code (e.g., 'London,UK' or 'New York,US')
     * @param units Temperature unit (metric, imperial, standard)
     * @return Current weather data
     */
    fun getCurrentWeather(location: String, units: String = "metric"): CurrentWeather
    
    /**
     * Get weather alerts for a specific location
     * @param location City and country code
     * @param startDate Start date for alerts
     * @param endDate End date for alerts
     * @return List of weather alerts
     */
    fun getWeatherAlerts(location: String, startDate: LocalDate, endDate: LocalDate): List<WeatherAlert>
    
    /**
     * Get historical weather data for a specific location and date
     * @param location City and country code
     * @param date Date for historical data
     * @param units Temperature unit (metric, imperial, standard)
     * @return Historical weather data
     */
    fun getHistoricalWeather(location: String, date: LocalDate, units: String = "metric"): DailyForecast?
    
    /**
     * Get UV index forecast for a specific location
     * @param lat Latitude
     * @param lon Longitude
     * @param days Number of days to forecast (1-8)
     * @return UV index forecast response
     */
    fun getUVIndexForecast(lat: Double, lon: Double, days: Int = 7): UVIndexForecastResponse
    
    /**
     * Get air quality data for a specific location
     * @param lat Latitude
     * @param lon Longitude
     * @return Air quality response
     */
    fun getAirQuality(lat: Double, lon: Double): AirQualityResponse
    
    /**
     * Get weather recommendations for a trip
     * @param location Destination location
     * @param startDate Trip start date
     * @param endDate Trip end date
     * @param tripType Type of trip (e.g., beach, hiking, city)
     * @return List of weather recommendations
     */
    fun getWeatherRecommendations(
        location: String,
        startDate: LocalDate,
        endDate: LocalDate,
        tripType: String? = null
    ): List<WeatherRecommendation>
    
    /**
     * Check if weather is suitable for a specific activity
     * @param activity Activity type (e.g., "hiking", "beach")
     * @param location Location name
     * @param date Date to check (null for current date)
     * @return Activity suitability response
     */
    fun checkActivitySuitability(
        activity: String,
        location: String,
        date: LocalDate? = null
    ): ActivitySuitabilityResponse
    
    /**
     * Get weather forecast for a specific trip
     * @param tripId ID of the trip
     * @return Weather forecast for the trip dates and location
     */
    fun getWeatherForecastForTrip(tripId: Long): WeatherForecastResponse
    
    /**
     * Check if weather conditions are suitable for outdoor activities
     * @param location Location to check
     * @param date Date to check
     * @param activityType Type of activity (e.g., hiking, beach, sightseeing)
     * @return Boolean indicating if conditions are suitable
     */
    fun isWeatherSuitableForActivity(
        location: String,
        date: LocalDate,
        activityType: String
    ): Boolean
}

/**
 * Data class for UV index forecast
 */
data class UvIndexForecast(
    val date: LocalDate,
    val uvIndex: Double,
    val uvIndexMax: Double,
    val uvIndexMaxTime: String,
    val safeExposure: String
)

/**
 * Data class for UV index forecast response
 */
data class UVIndexForecastResponse(
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val dailyForecast: List<DailyUVIndexForecast>,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Data class for daily UV index forecast
 */
data class DailyUVIndexForecast(
    val date: LocalDate,
    val uvIndex: Double,
    val uvIndexMax: Double,
    val uvIndexMaxTime: String,
    val riskLevel: String,
    val protectionAdvice: String
)

/**
 * Data class for air quality data
 */
data class AirQualityData(
    val aqi: Int,
    val co: Double,
    val no: Double,
    val no2: Double,
    val o3: Double,
    val so2: Double,
    val pm2_5: Double,
    val pm10: Double,
    val nh3: Double,
    val lastUpdated: String
)

/**
 * Data class for weather recommendations
 */
data class WeatherRecommendation(
    val location: String,
    val dateRange: String,
    val temperature: String,
    val conditions: String,
    val clothing: List<String>,
    val itemsToPack: List<String>,
    val activities: List<String>,
    val warnings: List<String>
)

/**
 * Data class for activity suitability response
 */
data class ActivitySuitabilityResponse(
    val activity: String,
    val isSuitable: Boolean,
    val reason: String,
    val score: Int, // 0-100
    val recommendations: List<String> = emptyList()
)

/**
 * Data class for air quality response
 */
data class AirQualityResponse(
    val location: String,
    val aqi: Int,
    val category: String,
    val dominantPollutant: String,
    val healthImplications: String,
    val timestamp: Long = System.currentTimeMillis(),
    val pollutants: Map<String, Double>
)
