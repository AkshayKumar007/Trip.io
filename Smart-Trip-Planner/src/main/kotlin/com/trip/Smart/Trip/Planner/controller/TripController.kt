package com.trip.Smart.Trip.Planner.controller

import com.trip.Smart.Trip.Planner.model.dto.*
import com.trip.Smart.Trip.Planner.service.TripService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/trips", produces = [MediaType.APPLICATION_JSON_VALUE])
@Tag(name = "Trip Management", description = "APIs for managing trips")
class TripController(
    private val tripService: TripService
) {
    private val logger = LoggerFactory.getLogger(TripController::class.java)

    @PostMapping
    @Operation(summary = "Create a new trip")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Trip created successfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = TripDTO::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input"
            )
        ]
    )
    fun createTrip(
        @Valid @RequestBody tripSetupDTO: TripSetupDTO
    ): ResponseEntity<TripDTO> {
        logger.info("Received request to create a new trip to ${tripSetupDTO.destination}")
        val createdTrip = tripService.createTrip(tripSetupDTO)
        return ResponseEntity(createdTrip, HttpStatus.CREATED)
    }

    @GetMapping("/{tripId}")
    @Operation(summary = "Get trip by ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Trip found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = TripDTO::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Trip not found"
            )
        ]
    )
    fun getTripById(
        @Parameter(description = "ID of the trip to be obtained") 
        @PathVariable tripId: Long
    ): ResponseEntity<TripDTO> {
        logger.info("Received request to get trip by ID: $tripId")
        val trip = tripService.getTripById(tripId)
        return ResponseEntity.ok(trip)
    }

    @PutMapping("/{tripId}")
    @Operation(summary = "Update an existing trip")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Trip updated successfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = TripDTO::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Trip not found"
            )
        ]
    )
    fun updateTrip(
        @Parameter(description = "ID of the trip to be updated") 
        @PathVariable tripId: Long,
        
        @Valid @RequestBody tripSetupDTO: TripSetupDTO
    ): ResponseEntity<TripDTO> {
        logger.info("Received request to update trip with ID: $tripId")
        val updatedTrip = tripService.updateTrip(tripId, tripSetupDTO)
        return ResponseEntity.ok(updatedTrip)
    }

    @DeleteMapping("/{tripId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a trip by ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Trip deleted successfully"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Trip not found"
            )
        ]
    )
    fun deleteTrip(
        @Parameter(description = "ID of the trip to be deleted") 
        @PathVariable tripId: Long
    ) {
        logger.info("Received request to delete trip with ID: $tripId")
        tripService.deleteTrip(tripId)
    }

    @GetMapping
    @Operation(summary = "Get all trips with filtering and pagination")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "List of trips",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Page::class)
                    )
                ]
            )
        ]
    )
    fun getAllTrips(
        @Parameter(description = "Filter by destination (case-insensitive)")
        @RequestParam(required = false) destination: String?,
        
        @Parameter(description = "Filter by start date (format: yyyy-MM-dd)")
        @RequestParam(required = false) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
        startDate: LocalDate?,
        
        @Parameter(description = "Filter by end date (format: yyyy-MM-dd)")
        @RequestParam(required = false) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
        endDate: LocalDate?,
        
        @Parameter(description = "Page number (0-based)")
        @RequestParam(defaultValue = "0") page: Int,
        
        @Parameter(description = "Number of items per page")
        @RequestParam(defaultValue = "20") size: Int,
        
        @Parameter(description = "Sort by field (e.g., startDate,asc or destination,desc)")
        @RequestParam(defaultValue = "startDate,asc") sort: String
    ): ResponseEntity<Page<TripDTO>> {
        logger.info("""
            Received request to get all trips with filters: 
            destination=$destination, startDate=$startDate, endDate=$endDate, 
            page=$page, size=$size, sort=$sort
        """.trimIndent())
        
        val sortField = sort.split(",")[0]
        val sortDirection = if (sort.contains(",")) sort.split(",")[1] else "asc"
        val pageable: Pageable = PageRequest.of(
            page, 
            size, 
            Sort.by(Sort.Direction.fromString(sortDirection.uppercase()), sortField)
        )
        
        val trips = tripService.getAllTrips(
            destination = destination,
            startDate = startDate?.toString(),
            endDate = endDate?.toString(),
            pageable = pageable
        )
        
        return ResponseEntity.ok(trips)
    }

    @GetMapping("/search")
    @Operation(summary = "Search trips with various criteria")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "List of matching trips",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Page::class)
                    )
                ]
            )
        ]
    )
    fun searchTrips(
        @Parameter(description = "Search query (searches in destination and notes)")
        @RequestParam(required = false) query: String?,
        
        @Parameter(description = "Filter by start date (format: yyyy-MM-dd)")
        @RequestParam(required = false) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
        startDate: LocalDate?,
        
        @Parameter(description = "Filter by end date (format: yyyy-MM-dd)")
        @RequestParam(required = false) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
        endDate: LocalDate?,
        
        @Parameter(description = "Page number (0-based)")
        @RequestParam(defaultValue = "0") page: Int,
        
        @Parameter(description = "Number of items per page")
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<TripDTO>> {
        logger.info("""
            Received search request for trips: 
            query=$query, startDate=$startDate, endDate=$endDate, 
            page=$page, size=$size
        """.trimIndent())
        
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        
        val trips = tripService.searchTrips(
            query = query,
            startDate = startDate?.toString(),
            endDate = endDate?.toString(),
            pageable = pageable
        )
        
        return ResponseEntity.ok(trips)
    }
    
    @GetMapping("/user/{userId}/upcoming")
    @Operation(summary = "Get upcoming trips for a user")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "List of upcoming trips",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = TripDTO::class))
                    )
                ]
            )
        ]
    )
    fun getUpcomingTrips(
        @Parameter(description = "ID of the user") 
        @PathVariable userId: String,
        
        @Parameter(description = "Maximum number of upcoming trips to return") 
        @RequestParam(defaultValue = "5") limit: Int
    ): ResponseEntity<List<TripDTO>> {
        logger.info("Received request to get upcoming trips for user: $userId, limit: $limit")
        val trips = tripService.getUpcomingTrips(userId, limit)
        return ResponseEntity.ok(trips)
    }
    
    @GetMapping("/user/{userId}/current")
    @Operation(summary = "Get current trips for a user")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "List of current trips",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = TripDTO::class))
                    )
                ]
            )
        ]
    )
    fun getCurrentTrips(
        @Parameter(description = "ID of the user") 
        @PathVariable userId: String
    ): ResponseEntity<List<TripDTO>> {
        logger.info("Received request to get current trips for user: $userId")
        val trips = tripService.getCurrentTrips(userId)
        return ResponseEntity.ok(trips)
    }
    
    @GetMapping("/{tripId}/belongs-to/{userId}")
    @Operation(summary = "Check if a trip belongs to a user")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Check result",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Map::class)
                    )
                ]
            )
        ]
    )
    fun doesTripBelongToUser(
        @Parameter(description = "ID of the trip") 
        @PathVariable tripId: Long,
        
        @Parameter(description = "ID of the user") 
        @PathVariable userId: String
    ): ResponseEntity<Map<String, Boolean>> {
        logger.info("Checking if trip $tripId belongs to user $userId")
        val belongs = tripService.isTripBelongsToUser(tripId, userId)
        return ResponseEntity.ok(mapOf("belongs" to belongs))
    }
}

