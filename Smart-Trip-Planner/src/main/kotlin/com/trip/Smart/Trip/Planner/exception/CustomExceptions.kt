package com.trip.Smart.Trip.Planner.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String) : RuntimeException(message)

class ExternalApiException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
