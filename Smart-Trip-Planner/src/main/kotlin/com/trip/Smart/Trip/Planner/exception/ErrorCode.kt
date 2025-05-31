package com.trip.Smart.Trip.Planner.exception

/**
 * Enumeration of error codes for weather service exceptions
 */
enum class ErrorCode(val name: String) {
    // Client errors (4xx)
    INVALID_REQUEST("INVALID_REQUEST"),
    LOCATION_NOT_FOUND("LOCATION_NOT_FOUND"),
    VALIDATION_ERROR("VALIDATION_ERROR"),
    
    // Server errors (5xx)
    API_ERROR("API_ERROR"),
    API_RATE_LIMIT_EXCEEDED("API_RATE_LIMIT_EXCEEDED"),
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE"),
    CACHE_ERROR("CACHE_ERROR"),
    
    // Other errors
    UNKNOWN_ERROR("UNKNOWN_ERROR"),
    CONFIGURATION_ERROR("CONFIGURATION_ERROR")
}
