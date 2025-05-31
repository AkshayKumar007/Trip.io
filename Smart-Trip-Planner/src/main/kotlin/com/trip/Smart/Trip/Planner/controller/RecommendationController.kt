package com.trip.Smart.Trip.Planner.controller

import com.trip.Smart.Trip.Planner.exception.ResourceNotFoundException
import com.trip.Smart.Trip.Planner.model.dto.AccommodationDTO
import com.trip.Smart.Trip.Planner.model.dto.TravelOptionDTO
import com.trip.Smart.Trip.Planner.service.RecommendationService
import jakarta.validation.constraints.*
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * REST controller for managing travel recommendations.
 * Provides endpoints for getting accommodation and travel recommendations.
 */
@RestController
@RequestMapping("/api/v1/recommendations")
@Validated
class RecommendationController(
    private val recommendationService: RecommendationService
) {
    private val logger = LoggerFactory.getLogger(RecommendationController::class.java)
    private val dateFormatter = DateTimeFormatter.ISO_DATE

    /**
     * Get accommodation recommendations for a destination and date range.
     *
     * @param destination The destination city or location
     * @param checkInDate The check-in date (format: yyyy-MM-dd)
     * @param checkOutDate The check-out date (format: yyyy-MM-dd)
     * @param guests Number of guests (default: 1)
     * @return List of accommodation recommendations
     */
    @GetMapping("/accommodations")
    fun getAccommodationRecommendations(
        @RequestParam @NotBlank(message = "Destination is required") destination: String,
        @RequestParam @NotNull(message = "Check-in date is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) checkInDate: LocalDate,
        @RequestParam @NotNull(message = "Check-out date is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) checkOutDate: LocalDate,
        @RequestParam(defaultValue = "1") @Min(1, message = "Number of guests must be at least 1") guests: Int = 1
    ): ResponseEntity<List<AccommodationDTO>> {
        logger.info("Fetching accommodation recommendations for $destination from $checkInDate to $checkOutDate for $guests guests")
        
        if (checkOutDate.isBefore(checkInDate.plusDays(1))) {
            throw IllegalArgumentException("Check-out date must be after check-in date")
        }
        
        val recommendations = recommendationService.getAccommodationRecommendations(
            destination = destination,
            startDate = checkInDate.format(dateFormatter),
            endDate = checkOutDate.format(dateFormatter),
            numPeople = guests
        )
        return ResponseEntity.ok(recommendations)
    }

    /**
     * Get travel recommendations between two locations.
     *
     * @param origin The starting location (city/airport code)
     * @param destination The destination location (city/airport code)
     * @param travelDate The date of travel (format: yyyy-MM-dd)
     * @param passengers Number of passengers (default: 1)
     * @return List of travel options
     */
    @GetMapping("/travel")
    fun getTravelRecommendations(
        @RequestParam @NotBlank(message = "Origin is required") origin: String,
        @RequestParam @NotBlank(message = "Destination is required") destination: String,
        @RequestParam @NotNull(message = "Travel date is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) travelDate: LocalDate,
        @RequestParam(defaultValue = "1") @Min(1, message = "Number of passengers must be at least 1") 
        passengers: Int = 1
    ): ResponseEntity<List<TravelOptionDTO>> {
        logger.info("Fetching travel recommendations from $origin to $destination on $travelDate for $passengers passengers")
        
        if (travelDate.isBefore(LocalDate.now())) {
            throw IllegalArgumentException("Travel date must be in the future")
        }
        
        val recommendations = recommendationService.getTravelRecommendations(
            origin = origin,
            destination = destination,
            travelDate = travelDate.format(dateFormatter),
            numPeople = passengers
        )
        return ResponseEntity.ok(recommendations)
    }
    
    /**
     * Exception handler for validation errors.
     */
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: IllegalArgumentException): ResponseEntity<String> {
        logger.warn("Validation error: ${ex.message}")
        return ResponseEntity.badRequest().body(ex.message)
    }
    
    /**
     * Exception handler for resource not found errors.
     */
    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException): ResponseEntity<String> {
        logger.warn("Resource not found: ${ex.message}")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }
    
    /**
     * Generic exception handler.
     */
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGenericException(ex: Exception): ResponseEntity<String> {
        logger.error("An error occurred: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An unexpected error occurred: ${ex.message}")
    }
}

