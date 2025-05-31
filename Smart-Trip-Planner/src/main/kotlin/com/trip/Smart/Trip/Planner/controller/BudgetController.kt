package com.trip.Smart.Trip.Planner.controller

import com.trip.Smart.Trip.Planner.exception.ResourceNotFoundException
import com.trip.Smart.Trip.Planner.model.dto.BudgetDTO
import com.trip.Smart.Trip.Planner.model.dto.OptimizationSuggestionDTO
import com.trip.Smart.Trip.Planner.service.BudgetService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for managing trip budgets.
 * Provides endpoints for updating and retrieving budget information.
 */
@RestController
@RequestMapping("/api/v1/trips/{tripId}/budget")
class BudgetController(
    private val budgetService: BudgetService
) {
    private val logger = LoggerFactory.getLogger(BudgetController::class.java)

    /**
     * Updates the budget for a specific trip.
     *
     * @param tripId The ID of the trip to update the budget for
     * @param budgetDTO The budget data to update
     * @return The updated budget DTO
     * @throws ResourceNotFoundException if the trip is not found
     */
    @PutMapping
    fun updateBudget(
        @PathVariable tripId: Long,
        @Valid @RequestBody budgetDTO: BudgetDTO
    ): ResponseEntity<BudgetDTO> {
        logger.info("Updating budget for trip ID: $tripId")
        val updatedBudget = budgetService.updateBudget(tripId, budgetDTO)
        return ResponseEntity.ok(updatedBudget)
    }

    /**
     * Retrieves the budget details for a specific trip.
     *
     * @param tripId The ID of the trip to get the budget for
     * @return The budget DTO containing all cost items and totals
     * @throws ResourceNotFoundException if the trip is not found
     */
    @GetMapping
    fun getBudgetByTripId(@PathVariable tripId: Long): ResponseEntity<BudgetDTO> {
        logger.info("Fetching budget for trip ID: $tripId")
        val budget = budgetService.getBudgetByTripId(tripId)
        return ResponseEntity.ok(budget)
    }

    /**
     * Generates budget optimization suggestions for a trip.
     *
     * @param tripId The ID of the trip to generate suggestions for
     * @return A list of optimization suggestions
     */
    @GetMapping("/optimize")
    fun getBudgetOptimizationSuggestions(
        @PathVariable tripId: Long
    ): ResponseEntity<List<OptimizationSuggestionDTO>> {
        logger.info("Fetching budget optimization suggestions for trip ID: $tripId")
        val suggestions = budgetService.getBudgetOptimizationSuggestions(tripId)
        return ResponseEntity.ok(suggestions)
    }

    /**
     * Exception handler for ResourceNotFoundException.
     * Returns a 404 status code when a resource is not found.
     */
    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException): ResponseEntity<String> {
        logger.error("Resource not found: ${ex.message}")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    /**
     * Generic exception handler.
     * Returns a 500 status code for unhandled exceptions.
     */
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGenericException(ex: Exception): ResponseEntity<String> {
        logger.error("An error occurred: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An unexpected error occurred: ${ex.message}")
    }
}

