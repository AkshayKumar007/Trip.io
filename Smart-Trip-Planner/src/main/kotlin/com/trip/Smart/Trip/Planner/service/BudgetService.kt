package com.trip.Smart.Trip.Planner.service

import com.trip.Smart.Trip.Planner.model.dto.BudgetDTO
import com.trip.Smart.Trip.Planner.model.dto.CostItemDTO
import com.trip.Smart.Trip.Planner.model.dto.OptimizationSuggestionDTO
import org.springframework.transaction.annotation.Transactional

/**
 * Service interface for managing trip budgets and cost items.
 * Provides functionality to update budgets, retrieve budget details,
 * calculate costs, and get optimization suggestions.
 */
interface BudgetService {
    
    /**
     * Updates the budget for a specific trip.
     *
     * @param tripId The ID of the trip to update the budget for
     * @param budgetDTO The budget data to update
     * @return The updated budget DTO
     * @throws ResourceNotFoundException if the trip is not found
     */
    @Transactional
    fun updateBudget(tripId: Long, budgetDTO: BudgetDTO): BudgetDTO
    
    /**
     * Retrieves the budget details for a specific trip.
     *
     * @param tripId The ID of the trip to get the budget for
     * @return The budget DTO containing all cost items and totals
     * @throws ResourceNotFoundException if the trip is not found
     */
    @Transactional(readOnly = true)
    fun getBudgetByTripId(tripId: Long): BudgetDTO
    
    /**
     * Calculates the total estimated and actual costs from a list of cost items.
     *
     * @param costItems The list of cost items to calculate totals from
     * @return A pair containing (totalEstimatedCost, totalActualCost)
     */
    fun calculateTotalCosts(costItems: List<CostItemDTO>): Pair<Double, Double>
    
    /**
     * Generates budget optimization suggestions for a trip.
     *
     * @param tripId The ID of the trip to generate suggestions for
     * @return A list of optimization suggestions
     */
    @Transactional(readOnly = true)
    fun getBudgetOptimizationSuggestions(tripId: Long): List<OptimizationSuggestionDTO>
}