# Weather Service API

This service provides weather-related functionality for the Smart Trip Planner application, including weather forecasts, current conditions, alerts, and recommendations.

## Features

- **Weather Forecasts**: Get detailed weather forecasts for any location and date range
- **Current Weather**: Retrieve current weather conditions for a location
- **Weather Alerts**: Get active weather alerts for a location
- **UV Index Forecast**: Check UV index forecasts for sun safety
- **Air Quality**: Get air quality index and pollutant information
- **Activity Recommendations**: Get weather-based recommendations for activities
- **Activity Suitability**: Check if weather is suitable for specific activities

## API Endpoints

### 1. Get Weather Forecast

Get a detailed weather forecast for a location and date range.

```http
GET /api/v1/weather/forecast?destination={location}&startDate={startDate}&endDate={endDate}&units={units}
```

**Parameters:**
- `destination` (required): City and country code (e.g., "London,UK" or "New York,US")
- `startDate` (required): Start date in YYYY-MM-DD format
- `endDate` (required): End date in YYYY-MM-DD format
- `units` (optional): Temperature unit (metric, imperial, standard) - defaults to "metric"

### 2. Get Weather for Trip

Get weather forecast for a specific trip by trip ID.

```http
GET /api/v1/weather/trip/{tripId}
```

**Parameters:**
- `tripId` (path): ID of the trip

### 3. Get Current Weather

Get current weather conditions for a location.

```http
GET /api/v1/weather/current?destination={location}&units={units}
```

**Parameters:**
- `destination` (required): City and country code
- `units` (optional): Temperature unit (metric, imperial, standard) - defaults to "metric"

### 4. Get Weather Alerts

Get active weather alerts for a location.

```http
GET /api/v1/weather/alerts?location={location}
```

**Parameters:**
- `location` (required): City and country code

### 5. Get UV Index Forecast

Get UV index forecast for a location.

```http
GET /api/v1/weather/uv-index?lat={latitude}&lon={longitude}&days={days}
```

**Parameters:**
- `lat` (required): Latitude of the location
- `lon` (required): Longitude of the location
- `days` (optional): Number of days to forecast (1-8) - defaults to 7

### 6. Get Air Quality

Get air quality information for a location.

```http
GET /api/v1/weather/air-quality?lat={latitude}&lon={longitude}
```

**Parameters:**
- `lat` (required): Latitude of the location
- `lon` (required): Longitude of the location

### 7. Get Weather Recommendations

Get weather-based recommendations for activities.

```http
GET /api/v1/weather/recommendations?location={location}&date={date}
```

**Parameters:**
- `location` (required): City and country code
- `date` (optional): Date for recommendations (YYYY-MM-DD) - defaults to current date

### 8. Check Activity Suitability

Check if weather is suitable for a specific activity.

```http
GET /api/v1/weather/suitability/{activity}?location={location}&date={date}
```

**Path Variables:**
- `activity`: Activity to check (e.g., "hiking", "beach", "sightseeing")

**Query Parameters:**
- `location` (required): City and country code
- `date` (optional): Date to check (YYYY-MM-DD) - defaults to current date

## Error Handling

The API returns appropriate HTTP status codes and error messages in the following format:

```json
{
  "timestamp": "2023-07-20T12:34:56.789Z",
  "status": 404,
  "error": "Not Found",
  "message": "Location not found: InvalidCity",
  "errorCode": "LOCATION_NOT_FOUND"
}
```

### Common Error Codes

- `400 BAD_REQUEST`: Invalid request parameters
- `401 UNAUTHORIZED`: Missing or invalid API key
- `404 NOT_FOUND`: Requested resource not found
- `429 TOO_MANY_REQUESTS`: API rate limit exceeded
- `500 INTERNAL_SERVER_ERROR`: Server error
- `503 SERVICE_UNAVAILABLE`: Service temporarily unavailable

## Rate Limiting

The service implements rate limiting to prevent abuse. The default rate limit is 60 requests per minute per API key.

## Caching

Responses are cached to improve performance and reduce API calls. The default cache TTL is 30 minutes.

## Mock Data Mode

To use mock data instead of making real API calls, set the following environment variable:

```
MOCK_WEATHER_DATA=true
```

## Setup

1. Clone the repository
2. Set your OpenWeatherMap API key in `application-weather.yml` or as an environment variable:
   ```
   export OPENWEATHERMAP_API_KEY=your_api_key_here
   ```
3. Build and run the application

## Dependencies

- Spring Boot 3.x
- Spring Web
- Spring Cache
- Jackson for JSON processing
- OpenWeatherMap API client
- Swagger/OpenAPI for API documentation

## API Documentation

Interactive API documentation is available at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Testing

Run the test suite with:

```bash
./mvnw test
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
