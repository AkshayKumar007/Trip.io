package com.trip.Smart.Trip.Planner.exception

/**
 * Custom exception class for weather service related errors
 */
class WeatherServiceException : RuntimeException {
    val errorCode: ErrorCode
    val details: Map<String, Any>?

    constructor(
        message: String,
        errorCode: ErrorCode = ErrorCode.API_ERROR,
        cause: Throwable? = null,
        details: Map<String, Any>? = null
    ) : super(message, cause) {
        this.errorCode = errorCode
        this.details = details
    }

    constructor(
        message: String,
        errorCode: ErrorCode,
        cause: Throwable
    ) : this(message, errorCode, cause, null)

    constructor(
        message: String,
        details: Map<String, Any>
    ) : this(message, ErrorCode.API_ERROR, null, details)

    override fun toString(): String {
        return "WeatherServiceException(errorCode=$errorCode, message=$message, details=$details, cause=$cause)"
    }

    companion object {
        fun locationNotFound(location: String): WeatherServiceException {
            return WeatherServiceException(
                message = "Location not found: $location",
                errorCode = ErrorCode.LOCATION_NOT_FOUND,
                details = mapOf("location" to location)
            )
        }

        fun invalidRequest(message: String, details: Map<String, Any>? = null): WeatherServiceException {
            return WeatherServiceException(
                message = message,
                errorCode = ErrorCode.INVALID_REQUEST,
                details = details
            )
        }

        fun apiError(message: String, cause: Throwable? = null): WeatherServiceException {
            return WeatherServiceException(
                message = "Weather API error: $message",
                errorCode = ErrorCode.API_ERROR,
                cause = cause
            )
        }

        fun rateLimitExceeded(retryAfter: Int? = null): WeatherServiceException {
            val details = retryAfter?.let { mapOf("retryAfterSeconds" to it) } ?: emptyMap()
            return WeatherServiceException(
                message = "API rate limit exceeded${retryAfter?.let { ", please try again in $it seconds" } ?: ""}",
                errorCode = ErrorCode.API_RATE_LIMIT_EXCEEDED,
                details = details
            )
        }

        fun serviceUnavailable(message: String, cause: Throwable? = null): WeatherServiceException {
            return WeatherServiceException(
                message = "Weather service unavailable: $message",
                errorCode = ErrorCode.SERVICE_UNAVAILABLE,
                cause = cause
            )
        }

        fun configurationError(message: String): WeatherServiceException {
            return WeatherServiceException(
                message = "Configuration error: $message",
                errorCode = ErrorCode.CONFIGURATION_ERROR
            )
        }
    }
}
