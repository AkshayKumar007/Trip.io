package com.trip.Smart.Trip.Planner.service.impl

import com.trip.Smart.Trip.Planner.exception.ResourceNotFoundException
import com.trip.Smart.Trip.Planner.model.dto.BudgetDTO
import com.trip.Smart.Trip.Planner.model.dto.CostItemDTO
import com.trip.Smart.Trip.Planner.model.dto.OptimizationSuggestionDTO
import com.trip.Smart.Trip.Planner.model.entity.CostItemEntity
import com.trip.Smart.Trip.Planner.repository.CostItemRepository
import com.trip.Smart.Trip.Planner.repository.TripRepository
import com.trip.Smart.Trip.Planner.service.BudgetService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BudgetServiceImpl(
    private val costItemRepository: CostItemRepository,
    private val tripRepository: TripRepository
) : BudgetService {

    private val logger = LoggerFactory.getLogger(BudgetServiceImpl::class.java)

    companion object {
        private const val HIGH_COST_FOOD_THRESHOLD = 100.0
        private const val HIGH_COST_SHOPPING_THRESHOLD = 200.0
        private const val HIGH_TOTAL_BUDGET_THRESHOLD = 1000.0
        private const val MAX_SUGGESTIONS = 3
    }

    @Transactional
    override fun updateBudget(tripId: Long, budgetDTO: BudgetDTO): BudgetDTO {
        logger.info("Updating budget for trip ID: $tripId")
        val trip = tripRepository.findById(tripId)
            .orElseThrow { ResourceNotFoundException("Trip not found with ID: $tripId") }

        // Clear existing items
        val existingCostItems = costItemRepository.findByTripId(tripId)
        costItemRepository.deleteAll(existingCostItems)
        trip.costItems.clear()

        val newCostItems = budgetDTO.costItems.mapNotNull { dto ->
            try {
                CostItemEntity(
                    trip = trip,
                    categoryName = dto.categoryName,
                    itemDescription = dto.itemDescription,
                    estimatedCost = dto.estimatedCost?.toBigDecimal(),
                    actualCost = dto.actualCost?.toBigDecimal(),
                    notes = dto.notes
                )
            } catch (e: NumberFormatException) {
                logger.warn("Invalid cost format in budget update for trip $tripId: ${e.message}")
                null
            }
        }

        
        trip.costItems.addAll(newCostItems)
        costItemRepository.saveAll(newCostItems)
        
        logger.info("Budget updated for trip ID: $tripId with ${newCostItems.size} cost items")
        return getBudgetByTripId(tripId)
    }

    @Transactional(readOnly = true)
    override fun getBudgetByTripId(tripId: Long): BudgetDTO {
        logger.debug("Fetching budget for trip ID: $tripId")
        val trip = tripRepository.findById(tripId)
            .orElseThrow { ResourceNotFoundException("Trip not found with ID: $tripId") }

        val costItemDTOs = trip.costItems.map { entity ->
            CostItemDTO(
                id = entity.id,
                categoryName = entity.categoryName,
                itemDescription = entity.itemDescription,
                estimatedCost = entity.estimatedCost?.toDouble(),
                actualCost = entity.actualCost?.toDouble(),
                notes = entity.notes
            )
        }
        
        val (totalEstimated, totalActual) = calculateTotalCosts(costItemDTOs)
        return BudgetDTO(
            tripId = tripId,
            costItems = costItemDTOs,
            totalEstimatedCost = totalEstimated,
            totalActualCost = totalActual
        )
    }

    override fun calculateTotalCosts(costItems: List<CostItemDTO>): Pair<Double, Double> {
        val totalEstimated = costItems.sumOf { it.estimatedCost ?: 0.0 }
        val totalActual = costItems.sumOf { it.actualCost ?: 0.0 }
        return totalEstimated to totalActual
    }

    @Transactional(readOnly = true)
    override fun getBudgetOptimizationSuggestions(tripId: Long): List<OptimizationSuggestionDTO> {
        logger.info("Fetching budget optimization suggestions for trip ID: $tripId")
        val budget = getBudgetByTripId(tripId)

        val suggestions = mutableListOf<OptimizationSuggestionDTO>().apply {
            // General suggestions
            add(OptimizationSuggestionDTO("Consider booking flights and accommodations in advance for better deals."))
            add(OptimizationSuggestionDTO("Look for package deals that include flights and hotels."))

            // Category-specific suggestions
            addAll(getCategorySpecificSuggestions(budget))
            
            // Total budget suggestion
            budget.totalEstimatedCost?.let { total ->
                if (total > HIGH_TOTAL_BUDGET_THRESHOLD) {
                    add(OptimizationSuggestionDTO("Your total budget is on the higher side. Review 'Miscellaneous' or 'Activities' for potential savings."))
                }
            }
            
            // Default suggestion if no others apply
            if (size <= 2) { // Only the general suggestions were added
                add(OptimizationSuggestionDTO("Your budget looks well-planned! Consider setting aside a small emergency fund."))
            }
        }

        return suggestions.distinct().take(MAX_SUGGESTIONS)
    }
    
    private fun getCategorySpecificSuggestions(budget: BudgetDTO): List<OptimizationSuggestionDTO> {
        val specificSuggestions = mutableListOf<OptimizationSuggestionDTO>()
        
        budget.costItems.forEach { item ->
            when (item.categoryName?.lowercase()) {
                "food" -> {
                    if ((item.estimatedCost ?: 0.0) > HIGH_COST_FOOD_THRESHOLD) {
                        specificSuggestions.add(
                            OptimizationSuggestionDTO("Consider more affordable dining options for ${item.itemDescription ?: "food"}")
                        )
                    }
                }
                "shopping" -> {
                    if ((item.estimatedCost ?: 0.0) > HIGH_COST_SHOPPING_THRESHOLD) {
                        specificSuggestions.add(
                            OptimizationSuggestionDTO("Shop around for better deals on ${item.itemDescription ?: "items"}")
                        )
                    }
                }
            }
        }
        
        return specificSuggestions
    }
}

