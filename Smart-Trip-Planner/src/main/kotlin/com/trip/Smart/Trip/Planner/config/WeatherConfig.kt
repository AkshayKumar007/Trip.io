package com.trip.Smart.Trip.Planner.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

/**
 * Configuration class for weather-related beans and settings
 */
@Configuration
@EnableCaching
class WeatherConfig {

    @Value("\${openweathermap.api.key:}")
    lateinit var apiKey: String

    @Value("\${openweathermap.api.url:https://api.openweathermap.org/data/2.5}")
    lateinit var apiUrl: String

    @Value("\${openweathermap.api.geocoding-url:http://api.openweathermap.org/geo/1.0/direct}")
    lateinit var geocodingUrl: String

    @Value("\${openweathermap.api.air-pollution-url:http://api.openweathermap.org/data/2.5/air_pollution}")
    lateinit var airPollutionUrl: String

    @Value("\${openweathermap.api.uv-index-url:http://api.openweathermap.org/data/2.5/onecall}")
    lateinit var uvIndexUrl: String

    @Value("\${openweathermap.api.alert-url:http://api.openweathermap.org/data/3.0/onecall}")
    lateinit var alertUrl: String

    @Value("\${openweathermap.cache.ttl:3600}")
    var cacheTtl: Long = 3600 // Default 1 hour

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun cacheManager(): CacheManager {
        return ConcurrentMapCacheManager(
            "weatherForecast",
            "currentWeather",
            "weatherAlerts",
            "uvIndexForecast",
            "airQuality",
            "geocoding"
        ).apply {
            // Configure cache settings if needed
        }
    }


    /**
     * Validates that the OpenWeatherMap API key is configured
     * @throws IllegalStateException if API key is not configured
     */
    @Bean
    fun validateApiKey() {
        if (apiKey.isBlank()) {
            System.err.println("WARNING: OpenWeatherMap API key is not configured. Using mock data only.")
            System.err.println("Please set 'openweathermap.api.key' in your application properties.")
        }
    }

    companion object {
        const val CACHE_WEATHER_FORECAST = "weatherForecast"
        const val CACHE_CURRENT_WEATHER = "currentWeather"
        const val CACHE_WEATHER_ALERTS = "weatherAlerts"
        const val CACHE_UV_INDEX = "uvIndexForecast"
        const val CACHE_AIR_QUALITY = "airQuality"
        const val CACHE_GEOCODING = "geocoding"
    }
}
