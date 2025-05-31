package com.trip.Smart.Trip.Planner.controller

import com.trip.Smart.Trip.Planner.exception.ResourceNotFoundException
import com.trip.Smart.Trip.Planner.model.dto.*
import com.trip.Smart.Trip.Planner.service.WeatherService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

/**
 * REST controller for weather-related operations
 */
@RestController
@RequestMapping("/api/v1/weather", produces = [MediaType.APPLICATION_JSON_VALUE])
@Validated
class WeatherController(
    private val weatherService: WeatherService
) {
    private val logger = LoggerFactory.getLogger(WeatherController::class.java)

    @GetMapping("/forecast")
    fun getWeatherForecast(
        @RequestParam @NotBlank(message = "Destination is required") destination: String,
        @RequestParam @NotNull(message = "Start date is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @NotNull(message = "End date is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate,
        @RequestParam(defaultValue = "metric") units: String = "metric"
    ): ResponseEntity<WeatherForecastResponse> {
        logger.info("Fetching weather forecast for $destination from $startDate to $endDate")
        return try {
            val request = WeatherForecastRequest(
                location = destination,
                startDate = startDate,
                endDate = endDate,
                units = units
            )
            val forecast = weatherService.getWeatherForecast(request)
            ResponseEntity.ok(forecast)
        } catch (ex: IllegalArgumentException) {
            logger.error("Invalid request parameters", ex)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message, ex)
        } catch (ex: Exception) {
            logger.error("Error fetching weather forecast", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching weather forecast", ex)
        }
    }

    @GetMapping("/trip/{tripId}")
    fun getWeatherForTrip(
        @PathVariable tripId: Long
    ): ResponseEntity<WeatherForecastResponse> {
        logger.info("Fetching weather forecast for trip ID: $tripId")
        return try {
            val forecast = weatherService.getWeatherForecastForTrip(tripId)
            ResponseEntity.ok(forecast)
        } catch (ex: ResourceNotFoundException) {
            logger.error("Trip not found with ID: $tripId", ex)
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found with ID: $tripId", ex)
        } catch (ex: Exception) {
            logger.error("Error fetching weather for trip ID: $tripId", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching weather for trip", ex)
        }
    }

    @GetMapping("/current")
    fun getCurrentWeather(
        @RequestParam @NotBlank(message = "Destination is required") destination: String,
        @RequestParam(defaultValue = "metric") units: String = "metric"
    ): ResponseEntity<CurrentWeather> {
        logger.info("Fetching current weather for $destination")
        return try {
            val currentWeather = weatherService.getCurrentWeather(destination, units)
            ResponseEntity.ok(currentWeather)
        } catch (ex: IllegalArgumentException) {
            logger.error("Invalid request parameters", ex)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message, ex)
        } catch (ex: Exception) {
            logger.error("Error fetching current weather for $destination", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching current weather", ex)
        }
    }

    @GetMapping("/alerts")
    fun getWeatherAlerts(
        @RequestParam @NotBlank(message = "Location is required") location: String,
        @RequestParam @NotNull(message = "Start date is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @NotNull(message = "End date is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate
    ): ResponseEntity<List<WeatherAlert>> {
        logger.info("Fetching weather alerts for $location from $startDate to $endDate")
        return try {
            val alerts = weatherService.getWeatherAlerts(location, startDate, endDate)
            ResponseEntity.ok(alerts)
        } catch (ex: IllegalArgumentException) {
            logger.error("Invalid request parameters", ex)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message, ex)
        } catch (ex: Exception) {
            logger.error("Error fetching weather alerts for $location", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching weather alerts", ex)
        }
    }

    @GetMapping("/historical")
    fun getHistoricalWeather(
        @RequestParam @NotBlank(message = "Location is required") location: String,
        @RequestParam @NotNull(message = "Date is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
        @RequestParam(defaultValue = "metric") units: String = "metric"
    ): ResponseEntity<DailyForecast> {
        logger.info("Fetching historical weather for $location on $date")
        return try {
            val historicalWeather = weatherService.getHistoricalWeather(location, date, units)
                ?: throw ResourceNotFoundException("Historical weather data not available for the specified date")
            ResponseEntity.ok(historicalWeather)
        } catch (ex: ResourceNotFoundException) {
            logger.error("Historical weather data not found", ex)
            throw ResponseStatusException(HttpStatus.NOT_FOUND, ex.message, ex)
        } catch (ex: IllegalArgumentException) {
            logger.error("Invalid request parameters", ex)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message, ex)
        } catch (ex: Exception) {
            logger.error("Error fetching historical weather for $location", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching historical weather", ex)
        }
    }

    @GetMapping("/uv-index")
    fun getUVIndexForecast(
        @RequestParam @NotNull(message = "Latitude is required") lat: Double,
        @RequestParam @NotNull(message = "Longitude is required") lon: Double,
        @RequestParam(defaultValue = "7") days: Int = 7
    ): ResponseEntity<UVIndexForecastResponse> {
        logger.info("Fetching UV index forecast for coordinates ($lat, $lon) for $days days")
        return try {
            val forecast = weatherService.getUVIndexForecast(lat, lon, days)
            ResponseEntity.ok(forecast)
        } catch (ex: IllegalArgumentException) {
            logger.error("Invalid request parameters", ex)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message, ex)
        } catch (ex: Exception) {
            logger.error("Error fetching UV index forecast", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching UV index forecast", ex)
        }
    }

    @GetMapping("/air-quality")
    fun getAirQuality(
        @RequestParam @NotNull(message = "Latitude is required") lat: Double,
        @RequestParam @NotNull(message = "Longitude is required") lon: Double
    ): ResponseEntity<AirQualityResponse> {
        logger.info("Fetching air quality for coordinates ($lat, $lon)")
        return try {
            val airQuality = weatherService.getAirQuality(lat, lon)
            ResponseEntity.ok(airQuality)
        } catch (ex: IllegalArgumentException) {
            logger.error("Invalid request parameters", ex)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message, ex)
        } catch (ex: Exception) {
            logger.error("Error fetching air quality data", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching air quality data", ex)
        }
    }

    @GetMapping("/recommendations")
    fun getWeatherRecommendations(
        @RequestParam @NotBlank(message = "Location is required") location: String,
        @RequestParam @NotNull(message = "Start date is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @NotNull(message = "End date is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate,
        @RequestParam(required = false) tripType: String? = null
    ): ResponseEntity<List<WeatherRecommendation>> {
        logger.info("Generating weather recommendations for $location from $startDate to $endDate, type: ${tripType ?: "not specified"}")
        return try {
            val recommendations = weatherService.getWeatherRecommendations(location, startDate, endDate, tripType)
            ResponseEntity.ok(recommendations)
        } catch (ex: IllegalArgumentException) {
            logger.error("Invalid request parameters", ex)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message, ex)
        } catch (ex: Exception) {
            logger.error("Error generating weather recommendations", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating weather recommendations", ex)
        }
    }

    @GetMapping("/suitability/{activity}")
    fun checkActivitySuitability(
        @PathVariable activity: String,
        @RequestParam @NotBlank(message = "Location is required") location: String,
        @RequestParam(required = false) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
        date: LocalDate? = null
    ): ResponseEntity<ActivitySuitabilityResponse> {
        logger.info("Checking weather suitability for $activity in $location on ${date ?: "current date"}")
        return try {
            val suitability = weatherService.checkActivitySuitability(activity, location, date)
            ResponseEntity.ok(suitability)
        } catch (ex: IllegalArgumentException) {
            logger.error("Invalid request parameters", ex)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message, ex)
        } catch (ex: Exception) {
            logger.error("Error checking activity suitability", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error checking activity suitability", ex)
        }
    }

    @GetMapping("/suitability/check")
    fun isWeatherSuitableForActivity(
        @RequestParam @NotBlank(message = "Location is required") location: String,
        @RequestParam @NotNull(message = "Date is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
        @RequestParam @NotBlank(message = "Activity type is required") activityType: String
    ): ResponseEntity<Boolean> {
        logger.info("Checking if weather is suitable for '$activityType' in $location on $date")
        return try {
            val isSuitable = weatherService.isWeatherSuitableForActivity(location, date, activityType)
            ResponseEntity.ok(isSuitable)
        } catch (ex: IllegalArgumentException) {
            logger.error("Invalid request parameters", ex)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message, ex)
        } catch (ex: Exception) {
            logger.error("Error checking weather suitability", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error checking weather suitability", ex)
        }
    }
}

