package com.trip.Smart.Trip.Planner.model.dto

import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class TripSetupDTO(
    val userId: String,
    
    @field:NotBlank(message = "Trip type cannot be blank")
    @field:Size(max = 100, message = "Trip type must be less than 100 characters")
    val tripType: String? = null,

    @field:NotBlank(message = "Destination cannot be blank")
    @field:Size(max = 255, message = "Destination must be less than 255 characters")
    val destination: String? = null,

    @field:FutureOrPresent(message = "Start date must be in the present or future")
    val startDate: LocalDate? = null,

    @field:FutureOrPresent(message = "End date must be in the present or future")
    val endDate: LocalDate? = null,

    @field:Min(value = 1, message = "Number of days must be at least 1")
    val numDays: Int? = null,

    @field:Min(value = 1, message = "Number of people must be at least 1")
    val numPeople: Int? = null,
    
    val notes: String? = null,
    
    @field:Min(value = 0, message = "Budget cannot be negative")
    val budget: Double? = null,
    
    @field:Size(min = 3, max = 3, message = "Currency code must be 3 characters")
    val currency: String? = "USD"
)

data class TripDTO(
    val id: Long,
    val userId: String,
    val tripType: String?,
    val destination: String?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val numDays: Int?,
    val numPeople: Int?,
    val notes: String?,
    val budget: Double? = null,
    val currency: String? = "USD",
    val createdAt: String, // Using String for simplicity in DTO
    val updatedAt: String  // Using String for simplicity in DTO
)

data class WeatherForecastDTO(
    val destination: String,
    val date: String, // e.g., "YYYY-MM-DD"
    val temperatureMin: Double,
    val temperatureMax: Double,
    val conditionDescription: String,
    val iconCode: String? = null,
    val precipitationChance: Double? = null // e.g., 0.75 for 75%
)

data class PackingItemDTO(
    val id: Long? = null,
    @field:NotBlank(message = "Item name cannot be blank")
    val itemName: String,
    val category: String? = null,
    @field:Min(value = 1, message = "Quantity must be at least 1")
    val quantity: Int = 1,
    val isOwned: Boolean = false,
    val isTrendySuggestion: Boolean = false,
    val notes: String? = null
)

data class WardrobeDTO(
    val tripId: Long,
    val items: List<PackingItemDTO>
)

data class ShopItemDTO(
    val itemName: String,
    val category: String?,
    val simulatedAffiliateLink: String
)

data class AccommodationDTO(
    val name: String,
    val type: String, // e.g., Hotel, Hostel, Airbnb
    val priceEstimate: String, // e.g., "$100-150/night"
    val rating: Double? = null, // e.g., 4.5
    val simulatedBookingLink: String
)

data class TravelOptionDTO(
    val modeOfTransport: String, // e.g., Flight, Train, Bus
    val providerName: String? = null,
    val durationEstimate: String, // e.g., "5h 30m"
    val priceEstimate: String, // e.g., "$200-300"
    val simulatedBookingLink: String
)

data class CostItemDTO(
    val id: Long? = null,
    @field:NotBlank(message = "Category name cannot be blank")
    val categoryName: String,
    val itemDescription: String? = null,
    @field:Min(value = 0, message = "Estimated cost cannot be negative")
    val estimatedCost: Double? = null,
    @field:Min(value = 0, message = "Actual cost cannot be negative")
    val actualCost: Double? = null,
    val notes: String? = null
)

data class BudgetDTO(
    val tripId: Long,
    val costItems: List<CostItemDTO>,
    val totalEstimatedCost: Double? = null,
    val totalActualCost: Double? = null
)

data class OptimizationSuggestionDTO(
    val suggestionText: String
)

data class ErrorResponseDTO(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String?,
    val path: String
)

data class TripSearchRequest(
    val query: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val destination: String? = null,
    val page: Int = 0,
    val size: Int = 20,
    val sortBy: String = "startDate",
    val sortDirection: String = "asc"
)

data class PagedResponse<T>(
    val content: List<T>,
    val currentPage: Int,
    val totalItems: Long,
    val totalPages: Int,
    val isFirst: Boolean,
    val isLast: Boolean,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)

