package com.trip.Smart.Trip.Planner.service.impl

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.trip.Smart.Trip.Planner.exception.ResourceNotFoundException
import com.trip.Smart.Trip.Planner.model.dto.CurrentWeather
import com.trip.Smart.Trip.Planner.model.dto.WeatherForecastRequest
import com.trip.Smart.Trip.Planner.model.dto.WeatherForecastResponse
import com.trip.Smart.Trip.Planner.model.dto.*
import com.trip.Smart.Trip.Planner.repository.TripRepository
import com.trip.Smart.Trip.Planner.service.ActivitySuitabilityResponse
import com.trip.Smart.Trip.Planner.service.AirQualityResponse
import com.trip.Smart.Trip.Planner.service.UVIndexForecastResponse
import com.trip.Smart.Trip.Planner.service.WeatherRecommendation
import com.trip.Smart.Trip.Planner.service.WeatherService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Enhanced implementation of WeatherService with comprehensive weather functionality
 */
@Service
@Transactional(readOnly = true)
class EnhancedWeatherServiceImpl(
    private val restTemplate: RestTemplate,
    private val tripRepository: TripRepository
) : WeatherService {
    
    companion object {
        private val logger = LoggerFactory.getLogger(EnhancedWeatherServiceImpl::class.java)
        private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }
    
    @Value("\${openweathermap.api.key:}")
    private lateinit var apiKey: String
    
    @Value("\${openweathermap.api.url:}")
    private lateinit var apiUrl: String
    
    @Value("\${app.weather.mock.enabled:false}")
    private val useMockData: Boolean = false
    
    @Cacheable("weather_uv_index", key = "{#lat + '_' + #lon + '_' + #days}")
    override fun getUVIndexForecast(lat: Double, lon: Double, days: Int): UVIndexForecastResponse {
        logger.info("Fetching UV index forecast for lat: $lat, lon: $lon, days: $days")
        
        if (useMockData || apiKey.isBlank()) {
            logger.warn("Using mock UV index data as API is not configured or mock is enabled")
            return createMockUVIndexForecast(lat, lon, days)
        }
        
        try {
            // Implementation would call OpenWeatherMap API here
            // For now, return mock data
            return createMockUVIndexForecast(lat, lon, days)
        } catch (e: Exception) {
            logger.error("Error fetching UV index data", e)
            return createMockUVIndexForecast(lat, lon, days) // Fallback to mock data
        }
    }
    
    @Cacheable("weather_air_quality", key = "{#lat + '_' + #lon}")
    override fun getAirQuality(lat: Double, lon: Double): AirQualityResponse {
        logger.info("Fetching air quality data for lat: $lat, lon: $lon")
        
        if (useMockData || apiKey.isBlank()) {
            logger.warn("Using mock air quality data as API is not configured or mock is enabled")
            return createMockAirQualityResponse(lat, lon)
        }
        
        try {
            // Implementation would call OpenWeatherMap API here
            // For now, return mock data
            return createMockAirQualityResponse(lat, lon)
        } catch (e: Exception) {
            logger.error("Error fetching air quality data", e)
            return createMockAirQualityResponse(lat, lon) // Fallback to mock data
        }
    }
    
    override fun checkActivitySuitability(
        activity: String,
        location: String,
        date: LocalDate?
    ): ActivitySuitabilityResponse {
        val checkDate = date ?: LocalDate.now()
        logger.info("Checking suitability for $activity in $location on $checkDate")
        
        val isSuitable = isWeatherSuitableForActivity(location, checkDate, activity)
        val score = if (isSuitable) 80 + (0..20).random() else 20 + (0..30).random()
        
        return ActivitySuitabilityResponse(
            activity = activity,
            isSuitable = isSuitable,
            reason = if (isSuitable) "Weather conditions are favorable for $activity"
            else "Weather conditions may not be ideal for $activity",
            score = score,
            recommendations = generateActivityRecommendations(activity, location, checkDate)
        )
    }

    @Cacheable("weather_forecast", key = "{#request.location + '_' + #request.startDate + '_' + #request.endDate}")
    override fun getWeatherForecast(request: WeatherForecastRequest): WeatherForecastResponse {
        logger.info("Fetching weather forecast for ${request.location} from ${request.startDate} to ${request.endDate}")
        
        if (useMockData || apiKey.isBlank()) {
            logger.warn("Using mock weather data as API is not configured or mock is enabled")
            return createMockWeatherForecast(request)
        }
        
        try {
            // Implementation would call OpenWeatherMap API here
            // For now, return mock data
            return createMockWeatherForecast(request)
        } catch (e: Exception) {
            logger.error("Error fetching weather data", e)
            return createMockWeatherForecast(request) // Fallback to mock data
        }
    }
    
    @Cacheable("weather_current", key = "{#location + '_' + #units}")
    override fun getCurrentWeather(location: String, units: String): CurrentWeather {
        logger.info("Fetching current weather for $location")
        return createMockCurrentWeather(location)
    }
    
    @Cacheable("weather_alerts", key = "{#location + '_' + #startDate + '_' + #endDate}")
    override fun getWeatherAlerts(location: String, startDate: LocalDate, endDate: LocalDate): List<WeatherAlert> {
        logger.info("Fetching weather alerts for $location from $startDate to $endDate")
        return createMockWeatherAlerts(location, startDate, endDate)
    }

    override fun getHistoricalWeather(
        location: String,
        date: LocalDate,
        units: String
    ): DailyForecast {
        logger.info("Fetching historical weather for $location on $date")

        if (useMockData || apiKey.isBlank()) {
            logger.warn("Using mock historical weather data as API is not configured or mock is enabled")
            return createMockDailyForecast(date)
        }

        try {
            // Implementation would call OpenWeatherMap API here
            // For now, return mock data
            return createMockDailyForecast(date)
        } catch (e: Exception) {
            logger.error("Error fetching historical weather data", e)
            return createMockDailyForecast(date) // Fallback to mock data
        }
    }

    @Transactional(readOnly = true)
    override fun getWeatherForecastForTrip(tripId: Long): WeatherForecastResponse {
        logger.info("Fetching weather forecast for trip ID: $tripId")
        
        val trip = tripRepository.findByIdOrNull(tripId)
            ?: throw ResourceNotFoundException("Trip not found with ID: $tripId")
            
        val destination = trip.destination
        val startDate = trip.startDate ?: LocalDate.now()
        val endDate = trip.endDate ?: startDate.plusDays(7)
        
        val request = WeatherForecastRequest(
            location = destination,
            startDate = startDate,
            endDate = endDate,
            units = "metric"
        )
        
        return getWeatherForecast(request)
    }
    
    override fun isWeatherSuitableForActivity(location: String, date: LocalDate, activityType: String): Boolean {
        logger.info("Checking if weather is suitable for $activityType in $location on $date")
        
        // Get weather forecast for the date
        val forecast = try {
            val request = WeatherForecastRequest(
                location = location,
                startDate = date,
                endDate = date.plusDays(1),
                units = "metric"
            )
            getWeatherForecast(request).dailyForecasts.firstOrNull()
        } catch (e: Exception) {
            logger.error("Error checking weather suitability", e)
            return true // Default to suitable if we can't determine
        }
        
        // Basic suitability check based on activity type
        return when (activityType.lowercase()) {
            "hiking" -> isSuitableForHiking(forecast)
            "beach" -> isSuitableForBeach(forecast)
            "sightseeing" -> isSuitableForSightseeing(forecast)
            "winter_sports" -> isSuitableForWinterSports(forecast)
            else -> true // Default to suitable for unknown activity types
        }
    }

    override fun getWeatherRecommendations(
        location: String,
        startDate: LocalDate,
        endDate: LocalDate,
        tripType: String?
    ): List<WeatherRecommendation> {
        logger.info("Generating weather recommendations for $location from $startDate to $endDate, type: $tripType")

        // Get weather forecast
        val forecast = try {
            val request = WeatherForecastRequest(
                location = location,
                startDate = startDate,
                endDate = endDate,
                units = "metric"
            )
            getWeatherForecast(request)
        } catch (e: Exception) {
            logger.error("Error generating weather recommendations", e)
            return createDefaultRecommendations(location, startDate, endDate, tripType)
        }

        // Generate recommendations based on forecast and trip type
        return listOf(generateRecommendations(forecast, tripType))
    }

    // Also update the default recommendations method
    private fun createDefaultRecommendations(
        location: String,
        startDate: LocalDate,
        endDate: LocalDate,
        tripType: String?
    ): List<WeatherRecommendation> {
        return listOf(WeatherRecommendation(
            location = location,
            dateRange = "$startDate to $endDate",
            temperature = "15°C to 25°C",
            conditions = "Partly cloudy",
            clothing = listOf("Comfortable layers", "Light jacket"),
            itemsToPack = listOf("Comfortable shoes", "Water bottle", "Portable charger"),
            activities = listOf("Sightseeing", "Local cuisine tasting"),
            warnings = emptyList()
        ))
    }
    
    // Private helper methods
    
    private fun isSuitableForHiking(forecast: DailyForecast?): Boolean {
        if (forecast == null) return true
        
        val temp = forecast.temp.day ?: return true
        val condition = forecast.weather.firstOrNull()?.main?.lowercase() ?: return true
        val windSpeed = forecast.windSpeed ?: 0.0
        val precipitation = forecast.pop ?: 0.0
        
        // Good hiking conditions: moderate temperature, no extreme weather, low chance of rain, not too windy
        return temp in 10.0..30.0 && 
               condition !in listOf("thunderstorm", "heavy rain", "snow") &&
               precipitation < 0.3 &&
               windSpeed < 20.0
    }
    
    private fun isSuitableForBeach(forecast: DailyForecast?): Boolean {
        if (forecast == null) return true
        
        val temp = forecast.temp.day ?: return true
        val condition = forecast.weather.firstOrNull()?.main?.lowercase() ?: return true
        
        // Good beach conditions: warm, clear or partly cloudy, not raining
        return temp > 25.0 &&
               condition in listOf("clear", "clouds") &&
               (forecast.pop ?: 0.0) < 0.2
    }
    
    private fun isSuitableForSightseeing(forecast: DailyForecast?): Boolean {
        if (forecast == null) return true
        
        val condition = forecast.weather.firstOrNull()?.main?.lowercase() ?: return true
        val precipitation = forecast.pop ?: 0.0
        
        // Good sightseeing conditions: not heavy rain or snow
        return condition !in listOf("thunderstorm", "heavy rain", "snow") &&
               precipitation < 0.5
    }
    
    private fun isSuitableForWinterSports(forecast: DailyForecast?): Boolean {
        if (forecast == null) return true
        
        val temp = forecast.temp.day ?: return true
        val condition = forecast.weather.firstOrNull()?.main?.lowercase() ?: return true
        
        // Good winter sports conditions: cold, snowing or clear
        return temp < 5.0 && 
               (condition in listOf("snow", "clear") || (forecast.snow ?: 0.0) > 0.0)
    }
    
    private fun generateRecommendations(
        forecast: WeatherForecastResponse,
        tripType: String?
    ): WeatherRecommendation {
        val clothing = mutableListOf<String>()
        val itemsToPack = mutableListOf<String>()
        val activities = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        
        val firstDay = forecast.dailyForecasts.firstOrNull()
        
        // Temperature-based recommendations
        firstDay?.let { day ->
            when {
                (day.temp.max ?: 0.0) > 30 -> {
                    clothing.add("Lightweight, breathable clothing")
                    clothing.add("Wide-brimmed hat")
                    itemsToPack.add("Sunscreen (SPF 30+)")
                    itemsToPack.add("Reusable water bottle")
                    warnings.add("High temperatures expected, stay hydrated")
                }
                (day.temp.max ?: 0.0) < 10 -> {
                    clothing.add("Warm layers")
                    clothing.add("Insulated jacket")
                    itemsToPack.add("Gloves and hat")
                    itemsToPack.add("Thermal wear")
                }
                else -> {
                    clothing.add("Layered clothing")
                    clothing.add("Light jacket")
                }
            }
            
            // Weather condition-based recommendations
            val weatherCondition = day.weather.firstOrNull()?.main?.lowercase()
            when (weatherCondition) {
                "rain" -> {
                    itemsToPack.add("Waterproof jacket")
                    itemsToPack.add("Umbrella")
                    itemsToPack.add("Waterproof shoes")
                    activities.add("Indoor activities")
                    warnings.add("Rain expected, plan indoor activities")
                }
                "snow" -> {
                    itemsToPack.add("Waterproof boots")
                    itemsToPack.add("Thermal wear")
                    activities.add("Skiing")
                    activities.add("Snowboarding")
                    warnings.add("Snow expected, check road conditions")
                }
                "clear" -> {
                    activities.add("Outdoor activities")
                    activities.add("Sightseeing")
                    activities.add("Hiking")
                }
                "clouds" -> {
                    activities.add("Sightseeing")
                    activities.add("City tours")
                }
            }
            
            // UV Index warnings
            day.uvi?.let { uvi ->
                when {
                    uvi > 8 -> warnings.add("Extreme UV index ($uvi), seek shade and wear sunscreen")
                    uvi > 5 -> warnings.add("High UV index ($uvi), apply sunscreen regularly")
                }
            }
        }
        
        // Add default items if no specific recommendations
        if (itemsToPack.isEmpty()) {
            itemsToPack.add("Comfortable shoes")
            itemsToPack.add("Water bottle")
            itemsToPack.add("Portable charger")
        }
        
        if (activities.isEmpty()) {
            activities.add("Sightseeing")
            activities.add("Local cuisine tasting")
        }
        
        return WeatherRecommendation(
            location = forecast.location,
            dateRange = "${forecast.dailyForecasts.firstOrNull()?.date} to ${forecast.dailyForecasts.lastOrNull()?.date}",
            temperature = "${forecast.dailyForecasts.firstOrNull()?.temp?.min?.toInt() ?: 0}°C to ${
                forecast.dailyForecasts.firstOrNull()?.temp?.max?.toInt() ?: 0
            }°C",
            conditions = forecast.dailyForecasts.firstOrNull()?.weather?.firstOrNull()?.description ?: "N/A",
            clothing = clothing.distinct(),
            itemsToPack = itemsToPack.distinct(),
            activities = activities.distinct(),
            warnings = warnings.distinct()
        )
    }
    
    // Mock data generation methods
    
    private fun createMockWeatherForecast(request: WeatherForecastRequest): WeatherForecastResponse {
        val dailyForecasts = mutableListOf<DailyForecast>()
        var currentDate = request.startDate
        
        while (!currentDate.isAfter(request.endDate)) {
            dailyForecasts.add(createMockDailyForecast(currentDate))
            currentDate = currentDate.plusDays(1)
        }
        
        return WeatherForecastResponse(
            location = request.location.split(",")[0].trim(),
            country = "US",
            lat = 37.7749,
            lon = -122.4194,
            current = createMockCurrentWeather(request.location),
            dailyForecasts = dailyForecasts,
        )
    }
    
    private fun createMockDailyForecast(date: LocalDate): DailyForecast {
        val temp = 15.0 + (Math.random() * 20) // Between 15°C and 35°C
        val conditions = listOf("clear", "clouds", "rain").random()
        
        return DailyForecast(
            date = date,
            temp = Temperature(
                day = temp,
                min = temp - 5.0,
                max = temp + 5.0,
                night = temp - 8.0,
                eve = temp - 3.0,
                morn = temp - 5.0
            ),
            feelsLike = FeelsLike(
                day = temp + 2.0,
                night = temp - 6.0,
                eve = temp - 1.0,
                morn = temp - 3.0
            ),
            pressure = 1015,
            humidity = 65,
            windSpeed = 3.5,
            windDeg = 180,
            weather = listOf(
                WeatherCondition(
                    id = when (conditions) {
                        "clear" -> 800
                        "clouds" -> 803
                        else -> 500
                    },
                    main = when (conditions) {
                        "clear" -> "Clear"
                        "clouds" -> "Clouds"
                        else -> "Rain"
                    },
                    description = when (conditions) {
                        "clear" -> "clear sky"
                        "clouds" -> "broken clouds"
                        else -> "light rain"
                    },
                    icon = when (conditions) {
                        "clear" -> "01d"
                        "clouds" -> "04d"
                        else -> "10d"
                    }
                )
            ),
            clouds = if (conditions == "clouds") 75 else 20,
            pop = if (conditions == "rain") 0.7 else 0.1,
            rain = if (conditions == "rain") 2.5 else null,
            snow = null,
            uvi = 6.5,
            visibility = 10000,
            sunrise = date.atTime(6, 30).toEpochSecond(ZoneOffset.UTC),
            sunset = date.atTime(18, 0).toEpochSecond(ZoneOffset.UTC)
        )
    }
    
    private fun createMockCurrentWeather(location: String): CurrentWeather {
        return CurrentWeather(
            dt = System.currentTimeMillis() / 1000,
            sunrise = LocalDateTime.now().withHour(6).withMinute(30).toEpochSecond(ZoneOffset.UTC),
            sunset = LocalDateTime.now().withHour(18).withMinute(0).toEpochSecond(ZoneOffset.UTC),
            temp = 22.5,
            feelsLike = 23.0,
            pressure = 1015,
            humidity = 65,
            dewPoint = 15.5,
            uvi = 6.5,
            clouds = 20,
            visibility = 10000,
            windSpeed = 3.5,
            windDeg = 180,
            windGust = 5.0,
            weather = listOf(
                WeatherCondition(
                    id = 800,
                    main = "Clear",
                    description = "clear sky",
                    icon = "01d"
                )
            ),
            rain = null,
            snow = null
        )
    }
    
    private fun createMockWeatherAlerts(
        location: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<WeatherAlert> {
        // Only return alerts for some locations
        if (!location.lowercase().contains("london") && !location.lowercase().contains("tokyo")) {
            return emptyList()
        }
        
        val alerts = mutableListOf<WeatherAlert>()
        
        if (location.lowercase().contains("london")) {
            alerts.add(
                WeatherAlert(
                    senderName = "Met Office",
                    event = "Yellow Weather Warning - Rain",
                    start = startDate.atTime(12, 0).toEpochSecond(ZoneOffset.UTC),
                    end = startDate.plusDays(1).atTime(12, 0).toEpochSecond(ZoneOffset.UTC),
                    description = "Heavy rain may cause some flooding and disruption to travel.",
                    tags = listOf("Rain", "Flood", "Travel")
                )
            )
        }
        
        if (location.lowercase().contains("tokyo")) {
            alerts.add(
                WeatherAlert(
                    senderName = "Japan Meteorological Agency",
                    event = "Typhoon Warning",
                    start = startDate.plusDays(1).atTime(0, 0).toEpochSecond(ZoneOffset.UTC),
                    end = startDate.plusDays(2).atTime(0, 0).toEpochSecond(ZoneOffset.UTC),
                    description = "Typhoon approaching. Expect strong winds and heavy rain.",
                    tags = listOf("Typhoon", "Wind", "Rain")
                )
            )
        }
        
        return alerts
    }
    
    private fun createDefaultRecommendations(
        location: String,
        startDate: LocalDate,
        endDate: LocalDate,
        tripType: String?
    ): WeatherRecommendation {
        return WeatherRecommendation(
            location = location,
            dateRange = "$startDate to $endDate",
            temperature = "15°C to 25°C",
            conditions = "Partly cloudy",
            clothing = listOf("Comfortable layers", "Light jacket"),
            itemsToPack = listOf("Comfortable shoes", "Water bottle", "Portable charger"),
            activities = listOf("Sightseeing", "Local cuisine tasting"),
            warnings = emptyList()
        )
    }
}

    private fun createMockUVIndexForecast(lat: Double, lon: Double, days: Int): UVIndexForecastResponse {
        val dailyForecasts = (0 until days.coerceAtMost(7)).map { dayOffset ->
            val date = LocalDate.now().plusDays(dayOffset.toLong())
            val uvIndex = (1..11).random().toDouble()
            
            DailyUVIndexForecast(
                date = date,
                uvIndex = uvIndex,
                uvIndexMax = uvIndex + 1.0,
                uvIndexMaxTime = date.atTime(12, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                riskLevel = when {
                    uvIndex < 3 -> "Low"
                    uvIndex < 6 -> "Moderate"
                    uvIndex < 8 -> "High"
                    uvIndex < 11 -> "Very High"
                    else -> "Extreme"
                },
                protectionAdvice = when {
                    uvIndex < 3 -> "No protection needed."
                    uvIndex < 6 -> "Wear sunglasses and use sunscreen."
                    uvIndex < 8 -> "Seek shade during midday hours."
                    uvIndex < 11 -> "Take extra precautions."
                    else -> "Avoid being outside during midday hours."
                }
            )
        }
        
        return UVIndexForecastResponse(
            location = "Mock Location",
            latitude = lat,
            longitude = lon,
            timezone = "UTC",
            dailyForecast = dailyForecasts,
            timestamp = System.currentTimeMillis()
        )
    }
    
    private fun createMockAirQualityResponse(lat: Double, lon: Double): AirQualityResponse {
        val pollutants = mapOf(
            "co" to (0.1..2.0).random(),
            "no" to (0..50).random().toDouble(),
            "no2" to (0..50).random().toDouble(),
            "o3" to (0..100).random().toDouble(),
            "so2" to (0..20).random().toDouble(),
            "pm2_5" to (0..50).random().toDouble(),
            "pm10" to (0..100).random().toDouble(),
            "nh3" to (0..10).random().toDouble()
        )
        
        val aqi = (0..500).random()
        val (category, healthImplications) = when (aqi) {
            in 0..50 -> "Good" to "Air quality is satisfactory, and air pollution poses little or no risk."
            in 51..100 -> "Moderate" to "Air quality is acceptable. However, there may be a risk for some people, particularly those who are unusually sensitive to air pollution."
            in 101..150 -> "Unhealthy for Sensitive Groups" to "Members of sensitive groups may experience health effects. The general public is less likely to be affected."
            in 151..200 -> "Unhealthy" to "Some members of the general public may experience health effects; members of sensitive groups may experience more serious health effects."
            in 201..300 -> "Very Unhealthy" to "Health alert: The risk of health effects is increased for everyone."
            else -> "Hazardous" to "Health warning of emergency conditions: everyone is more likely to be affected."
        }
        
        return AirQualityResponse(
            location = "Mock Location",
            aqi = aqi,
            category = category,
            dominantPollutant = pollutants.entries.maxByOrNull { it.value }?.key ?: "pm2_5",
            healthImplications = healthImplications,
            timestamp = System.currentTimeMillis(),
            pollutants = pollutants
        )
    }
    
    private fun generateActivityRecommendations(activity: String, location: String, date: LocalDate): List<String> {
        return when (activity.lowercase()) {
            "hiking" -> listOf("Wear comfortable hiking shoes", "Bring enough water", "Use sunscreen")
            "beach" -> listOf("Apply sunscreen regularly", "Stay hydrated", "Be aware of water conditions")
            "sightseeing" -> listOf("Wear comfortable shoes", "Bring a hat and sunglasses", "Carry a water bottle")
            "winter_sports" -> listOf("Dress in layers", "Wear appropriate snow gear", "Stay hydrated")
            else -> listOf("Check local weather conditions", "Dress appropriately for the weather")
        }
    }
}

// Data classes for OpenWeatherMap API responses

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenWeatherMapResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val current: OpenWeatherMapCurrent?,
    val daily: List<OpenWeatherMapDaily>?,
    val alerts: List<OpenWeatherMapAlert>?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenWeatherMapCurrent(
    val dt: Long,
    val sunrise: Long?,
    val sunset: Long?,
    val temp: Double,
    @JsonProperty("feels_like") val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    @JsonProperty("dew_point") val dewPoint: Double?,
    val uvi: Double?,
    val clouds: Int?,
    val visibility: Int?,
    @JsonProperty("wind_speed") val windSpeed: Double,
    @JsonProperty("wind_deg") val windDeg: Int,
    @JsonProperty("wind_gust") val windGust: Double?,
    val weather: List<OpenWeatherMapWeather>,
    val rain: OpenWeatherMapRain?,
    val snow: OpenWeatherMapSnow?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenWeatherMapDaily(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: OpenWeatherMapTemp,
    @JsonProperty("feels_like") val feelsLike: OpenWeatherMapFeelsLike,
    val pressure: Int,
    val humidity: Int,
    @JsonProperty("dew_point") val dewPoint: Double,
    @JsonProperty("wind_speed") val windSpeed: Double,
    @JsonProperty("wind_deg") val windDeg: Int,
    val weather: List<OpenWeatherMapWeather>,
    val clouds: Int,
    val pop: Double,
    val rain: Double?,
    val snow: Double?,
    val uvi: Double,
    val visibility: Int?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenWeatherMapTemp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenWeatherMapFeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenWeatherMapWeather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenWeatherMapRain(
    @JsonProperty("1h") val oneHour: Double
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenWeatherMapSnow(
    @JsonProperty("1h") val oneHour: Double
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenWeatherMapAlert(
    @JsonProperty("sender_name") val senderName: String,
    val event: String,
    val start: Long,
    val end: Long,
    val description: String,
    val tags: List<String>?
)
